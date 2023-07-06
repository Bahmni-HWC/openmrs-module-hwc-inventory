package org.bahmni.module.hwcinventory.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.pro.packaged.S;
import org.apache.velocity.runtime.log.Log;
import org.bahmni.module.hwcinventory.contract.CreatePatientRequest;
import org.bahmni.module.hwcinventory.contract.LoginRequest;
import org.bahmni.module.hwcinventory.mapper.EsanjeevaniPatientMapper;
import org.bahmni.module.hwcinventory.service.EsanjeevaniService;
import org.bahmni.module.hwcinventory.util.PasswordUtil;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class EsanjeevaniServiceImpl implements EsanjeevaniService {

    @Autowired
    PatientService patientService;
    private Log log;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EsanjeevaniServiceImpl.class.getName());

    @Override
    public String getSSOUrl(String ssoLoginResponse) throws Exception {
        String referenceId = extractReferenceId(ssoLoginResponse);
        return Context.getAdministrationService().getGlobalProperty("esanjeevani.login.url") + referenceId;
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
                String response = bufferedReader.lines().collect(Collectors.joining());
                logResponse(endpoint,response);
                return response;
            }
        } else {
            throw new Exception("Error response received with code: " + responseCode + ", Message: " + connection.getResponseMessage());
        }
    }

    public boolean isSuccessResponse(String response) throws Exception {
        Map<String, Object> jsonResponse = new ObjectMapper().readValue(response, Map.class);
        if (jsonResponse.get("msgCode") != null && (int) jsonResponse.get("msgCode") == 1) {
            return true;
        }
        return false;
    }

    public boolean isSameProfileResponse(String response) throws Exception {
        Map<String, Object> jsonResponse = new ObjectMapper().readValue(response, Map.class);
        if (jsonResponse.get("msgCode") != null && (int) jsonResponse.get("msgCode") == 78) {
            return true;
        }
        return false;
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

    public String getLoginResponse(String username, String password) throws Exception {
        String salt = getSalt();
        LoginRequest loginRequest = new LoginRequest(username, PasswordUtil.getEncryptedPassword(password, salt), salt, getSource());
        String endpoint = Context.getAdministrationService().getGlobalProperty("esanjeevani.api.baseUrl") + "/aus/api/ThirdPartyAuth/providerLogin";
        String response = makeHttpRequest(endpoint, new ObjectMapper().writeValueAsString(loginRequest), null);
        return response;
    }

    public String registerPatient(String patientUuid, String accessToken) throws Exception {
        Patient patient = patientService.getPatientByUuid(patientUuid);
        EsanjeevaniPatientMapper esanjeevaniPatientMapper = new EsanjeevaniPatientMapper();
        CreatePatientRequest createPatientRequest = esanjeevaniPatientMapper.getPatientRequest(patient);
        String endpoint = Context.getAdministrationService().getGlobalProperty("esanjeevani.api.baseUrl") + "/ps/api/v1/Patient";
        String response = makeHttpRequest(endpoint, new ObjectMapper().writeValueAsString(createPatientRequest), accessToken);
        return response;
    }

    public String performSSOLogin(String username, String password) throws Exception {
        String salt = getSalt();
        LoginRequest loginRequest = new LoginRequest(username, PasswordUtil.getEncryptedPassword(password, salt), salt, getSource());
        String endpoint = Context.getAdministrationService().getGlobalProperty("esanjeevani.api.baseUrl") + "/aus/api/ThirdPartyAuth/authenticateReference";
        String response = makeHttpRequest(endpoint, new ObjectMapper().writeValueAsString(loginRequest), null);
        return response;
    }

    private void logResponse(String url, String response) throws Exception {
        if (Context.getAdministrationService().getGlobalProperty("esanjeevani.debug").equals("true")) {
            logger.log(Level.INFO, "Response for: "+ url);
            logger.log(Level.INFO, response);
        }
    }

    private String getSource() {
        return Context.getAdministrationService().getGlobalProperty("esanjeevani.source");
    }

    private String getSalt() {
        int randomNumber = (int) (Math.random() * 900000) + 100000;
        return Integer.toString(randomNumber);
    }
}
