package org.bahmni.module.hwcinventory.service.impl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bahmni.module.hwcinventory.contract.CreatePatientRequest;
import org.bahmni.module.hwcinventory.contract.LoginRequest;
import org.bahmni.module.hwcinventory.mapper.EsanjeevaniPatientMapper;
import org.bahmni.module.hwcinventory.service.EsanjeevaniService;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

public class EsanjeevaniServiceImpl implements EsanjeevaniService {

    @Autowired
    PatientService patientService;
    @Override
    public String getEsanjeevaniWebDomain() throws Exception {

        String referenceId=authenticateReference();

        System.out.println("Response from referenceId generateReferenceIdForSSO data: " + Context.getAdministrationService().getGlobalProperty("esanjeevani.login.url")+referenceId);

        return Context.getAdministrationService().getGlobalProperty("esanjeevani.login.url")+referenceId;
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
    public String extractReferenceId(String response) {
        try {
            Map<String, Object> jsonResponse = new ObjectMapper().readValue(response, Map.class);
            Map<String, Object> modelObject = (Map<String, Object>) jsonResponse.get("model");
            return (String) modelObject.get("referenceId");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String makeProviderLoginRequest() throws Exception {

        LoginRequest loginRequest = new LoginRequest(getUserName(), getPassword(),getSalt(),getSource());

        String endpoint = Context.getAdministrationService().getGlobalProperty("esanjeevani.api.baseUrl")+"/aus/api/ThirdPartyAuth/providerLogin";

        String response = makeHttpRequest(endpoint, new ObjectMapper().writeValueAsString(loginRequest), null);

        System.out.println("Response from provider login: " + response);

        String accessToken = extractAccessToken(response);

        return accessToken;
    }

    public String createPatientRegistration(String patientUuid) throws Exception {

        String token = makeProviderLoginRequest();
        System.out.println("Token: " + token);

        Patient patient =patientService.getPatientByUuid(patientUuid);
        EsanjeevaniPatientMapper esanjeevaniPatientMapper=new EsanjeevaniPatientMapper();
        CreatePatientRequest createPatientRequest = esanjeevaniPatientMapper.getPatientRequest(patient);

        String endpoint = Context.getAdministrationService().getGlobalProperty("esanjeevani.api.baseUrl")+"/ps/api/v1/Patient";
        String requestBody = Context.getAdministrationService().getGlobalProperty("esanjeevani.patientRegistrationRequestBody");

        String response = makeHttpRequest(endpoint, new ObjectMapper().writeValueAsString(createPatientRequest), token);

        System.out.println("Response from patient registration: " + response);

        return response;
    }

    public String authenticateReference() throws Exception {
        LoginRequest loginRequest = new LoginRequest(getUserName(), getPassword(),getSalt(),getSource());

        String endpoint = Context.getAdministrationService().getGlobalProperty("esanjeevani.api.baseUrl")+"/aus/api/ThirdPartyAuth/authenticateReference";

        String response = makeHttpRequest(endpoint, new ObjectMapper().writeValueAsString(loginRequest), null);

        System.out.println("Response from generateReferenceIdForSSO: " + response);

        String referenceId = extractReferenceId(response);

        System.out.println("Response from generateReferenceIdForSSO: " + referenceId);

        return referenceId;
    }

    private String getSource() {
        return "dummy";
    }

    private String getSalt() {
        return "dummy";
    }

    private String getPassword() {
        return "dummy";
    }

    private String getUserName() {
        return "dummy";
    }
}
