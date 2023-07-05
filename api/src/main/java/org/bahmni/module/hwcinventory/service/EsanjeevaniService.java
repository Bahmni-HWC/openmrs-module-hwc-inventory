package org.bahmni.module.hwcinventory.service;

import org.springframework.stereotype.Service;


@Service
public interface EsanjeevaniService {

    String getSSOUrl(String ssoLoginResponse) throws Exception;

    String getLoginResponse() throws Exception;

    String registerPatient(String patientUuid, String accessToken) throws Exception;

    boolean isSameProfileResponse(String response) throws Exception;

    boolean isSuccessResponse(String response) throws Exception;

    String extractAccessToken(String response) throws Exception;

    String performSSOLogin() throws Exception;


}
