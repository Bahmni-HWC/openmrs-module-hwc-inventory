package org.bahmni.module.hwcinventory.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bahmni.module.hwcinventory.contract.EAushadhaInwardRequest;
import org.bahmni.module.hwcinventory.contract.EAushadhaInwardResponse;
import org.bahmni.module.hwcinventory.contract.EAushadhaRequest;
import org.bahmni.module.hwcinventory.contract.EAushadhaResponse;
import org.bahmni.module.hwcinventory.service.EaushadhaService;
import org.openmrs.api.context.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EaushadhaServiceImpl implements EaushadhaService {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public List<EAushadhaResponse> fetchStockDetails(String outwardId) throws Exception {
        List<EAushadhaResponse> eAushadhaResponses;
        try {

            String accessToken = System.getenv("EAUSHADHA_ACCESS_TOKEN");

            String eAushadhaUrl = Context.getAdministrationService().getGlobalProperty("eaushadha.api.baseUrl") + "/api/InstituteOutward";


            EAushadhaRequest externalRequestData = new EAushadhaRequest(outwardId);

            String response = makeHttpRequest(eAushadhaUrl, new ObjectMapper().writeValueAsString(externalRequestData), accessToken);


            ObjectMapper objectMapper = new ObjectMapper();

            eAushadhaResponses = objectMapper.readValue(response, new TypeReference<List<EAushadhaResponse>>() {
            });

        } catch (Exception e) {
            log.error("Error while fetching stock details from eAushadha", e);
            throw new Exception("Error while fetching stock details from eAushadha");
        }
        return eAushadhaResponses;
    }



    public List<EAushadhaInwardResponse> fetchInwardStockDetails(String inwardDate, String instituteId) throws Exception {
        List<EAushadhaInwardResponse> eAushadhaInwardResponses;
        try {

            String accessToken = System.getenv("EAUSHADHA_ACCESS_TOKEN");

            String eAushadhaUrl = Context.getAdministrationService().getGlobalProperty("eaushadha.api.baseUrl") + "/api/DWInstituteInward";


            EAushadhaInwardRequest externalRequestData = new EAushadhaInwardRequest(inwardDate, instituteId);

            String response = makeHttpRequest(eAushadhaUrl, new ObjectMapper().writeValueAsString(externalRequestData), accessToken);


            ObjectMapper objectMapper = new ObjectMapper();

            eAushadhaInwardResponses = objectMapper.readValue(response, new TypeReference<List<EAushadhaInwardResponse>>() {
            });

        } catch (Exception e) {
            log.error("Error while fetching Inward stock details from eAushadha", e);
            throw new Exception("Error while fetching Inward stock details from eAushadha");
        }
        return eAushadhaInwardResponses;
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
