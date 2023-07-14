package org.bahmni.module.hwcinventory.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bahmni.module.hwcinventory.contract.EAushadhaRequest;
import org.bahmni.module.hwcinventory.contract.EAushadhaResponse;
import org.bahmni.module.hwcinventory.service.EaushadhaService;
import org.openmrs.api.context.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EaushadhaServiceImpl implements EaushadhaService {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public List<EAushadhaResponse> fetchStockDetails(String outwardId) throws Exception {
        List<EAushadhaResponse> outwardDetails = null;
        try {

            String accessToken = System.getenv("EAUSHADHA_ACCESS_TOKEN");

            String eAushadhaUrl = Context.getAdministrationService().getGlobalProperty("eaushadha.api.baseUrl")+"/api/InstituteOutward";


            EAushadhaRequest externalRequestData = new EAushadhaRequest(outwardId);

            String response = makeHttpRequest(eAushadhaUrl, new ObjectMapper().writeValueAsString(externalRequestData), accessToken);


            ObjectMapper objectMapper = new ObjectMapper();

            String escapedResponse = StringEscapeUtils.unescapeJava(response);
            outwardDetails = objectMapper.readValue(escapedResponse.substring(1, escapedResponse.length() - 1), new TypeReference<List<EAushadhaResponse>>() {
            });

            for (EAushadhaResponse outwardDetail : outwardDetails) {
                String drugName = outwardDetail.getDrug_name();
                int batchQuantity = outwardDetail.getQuantity();

                Pattern pattern = Pattern.compile("(\\d+x[\\dx]+)");
                Matcher matcher = pattern.matcher(drugName);

                if (matcher.find()) {
                    String quantityExpression = matcher.group(0);
                    String[] quantityParts = quantityExpression.split("x");
                    int multiplicationResult = 1;

                    for (String quantityPart : quantityParts) {
                        int value = Integer.parseInt(quantityPart);
                        multiplicationResult *= value;
                    }

                    multiplicationResult *= batchQuantity;


                    //assign this multiplication result to actual quantity field in eaushadha response
                    outwardDetail.setActual_quantity(multiplicationResult);

                }
            }
        } catch (Exception e) {
            log.error("Error while logging in to eAushadha", e);
            new Exception("Error while fetching stock details from eAushadha");
        }
        return outwardDetails;
    }

    public String makeHttpRequest(String endpoint, String requestBody, String token) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        if (token != null) {
            connection.setRequestProperty("Authorization", "Bearer " + token);
        }
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(requestBody.getBytes());
        }

        int responseCode = connection.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                return bufferedReader.lines().collect(Collectors.joining());
            }
        } else {
            throw new Exception("Error response received with code: " + responseCode + ", Message: " + connection.getResponseMessage());
        }
    }
}
