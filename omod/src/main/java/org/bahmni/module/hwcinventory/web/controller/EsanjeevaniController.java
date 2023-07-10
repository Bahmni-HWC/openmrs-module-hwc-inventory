package org.bahmni.module.hwcinventory.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bahmni.module.hwcinventory.contract.LaunchRequest;
import org.bahmni.module.hwcinventory.exception.LGDCodeNotFoundException;
import org.bahmni.module.hwcinventory.service.EsanjeevaniService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/esanjeevani")
public class EsanjeevaniController extends BaseRestController {

    @Autowired
    private EsanjeevaniService esanjeevaniService;

    @RequestMapping(value = "/launch", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> launchEsanjeevani(@RequestBody LaunchRequest launchRequest) {

        try {
            String loginResponse = esanjeevaniService.getLoginResponse(launchRequest.getUsername(),launchRequest.getPassword());
            if (esanjeevaniService.isSuccessResponse(loginResponse)) {
                String accessToken = esanjeevaniService.extractAccessToken(loginResponse);
                String registerPatientResponse = esanjeevaniService.registerPatient(launchRequest.getPatientUuid(), accessToken);
                if (esanjeevaniService.isSuccessResponse(registerPatientResponse) || esanjeevaniService.isSameProfileResponse(registerPatientResponse)) {
                    String ssoLoginResponse = esanjeevaniService.performSSOLogin(launchRequest.getUsername(),launchRequest.getPassword());
                    if (esanjeevaniService.isSuccessResponse(ssoLoginResponse)) {
                        return new ResponseEntity<>(esanjeevaniService.getSSOUrl(ssoLoginResponse),HttpStatus.OK);
                    } else {
                        return new ResponseEntity<String>(getResponseMessage(ssoLoginResponse), HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<String>(getResponseMessage(registerPatientResponse), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<String>(getResponseMessage(loginResponse), HttpStatus.BAD_REQUEST);
            }

        }
        catch (LGDCodeNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    private String getResponseMessage(String response) throws JsonProcessingException {
        Map<String, Object> jsonResponse = new ObjectMapper().readValue(response, Map.class);
        return jsonResponse.get("message").toString();
    }
}
