package org.bahmni.module.hwcinventory.service.impl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bahmni.module.hwcinventory.service.EsanjeevaniService;
import org.openmrs.api.context.Context;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;


public class EsanjeevaniServiceImpl implements EsanjeevaniService {

    private final static String ESANJEEVANI_SERVER_URL_KEY = "esanjeevani.url.webDomain";

    private final static String DEFAULT_ESANJEEVANI_SERVER_URL = "https://uat.esanjeevani.in/#/";

    @Override
    public String getEsanjeevaniWebDomain() {
        String esanjeevaniServerUrl = org.openmrs.api.context.Context.getAdministrationService().getGlobalProperty(ESANJEEVANI_SERVER_URL_KEY);
        if ((esanjeevaniServerUrl == null) || "".equals(esanjeevaniServerUrl)) {
            esanjeevaniServerUrl = DEFAULT_ESANJEEVANI_SERVER_URL;
        }
        return esanjeevaniServerUrl;
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

    public String extractAccessToken(String response) {
        try {
            Map<String, Object> jsonResponse = new ObjectMapper().readValue(response, Map.class);
            Map<String, Object> modelObject = (Map<String, Object>) jsonResponse.get("model");
            return (String) modelObject.get("access_token");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String makeProviderLoginRequest() throws Exception {
        String endpoint = Context.getAdministrationService().getGlobalProperty("esanjeevani.providerEndpoint");
        String requestBody = Context.getAdministrationService().getGlobalProperty("esanjeevani.requestBody");

        String response = makeHttpRequest(endpoint, requestBody, null);

        System.out.println("Response from provider login: " + response);

        String accessToken = extractAccessToken(response);

        return accessToken;
    }

    public String createPatientRegistration() throws Exception {
        String token = makeProviderLoginRequest();
        System.out.println("Token: " + token);

        String endpoint = Context.getAdministrationService().getGlobalProperty("esanjeevani.patientRegistrationEndpoint");
        String requestBody = Context.getAdministrationService().getGlobalProperty("esanjeevani.patientRegistrationRequestBody");

        String response = makeHttpRequest(endpoint, requestBody, token);

        System.out.println("Response from patient registration: " + response);

        return response;
    }

    public String generateReferenceIdForSSO() throws Exception {
        String endpoint = Context.getAdministrationService().getGlobalProperty("esanjeevani.referenceIdEndpoint");
        String requestBody = Context.getAdministrationService().getGlobalProperty("esanjeevani.requestBody");

        String response = makeHttpRequest(endpoint, requestBody, null);

        System.out.println("Response from generateReferenceIdForSSO: " + response);

        return response;
    }

}
