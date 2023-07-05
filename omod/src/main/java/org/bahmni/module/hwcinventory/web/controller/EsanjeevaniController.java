package org.bahmni.module.hwcinventory.web.controller;

import org.bahmni.module.hwcinventory.service.EsanjeevaniService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/esanjeevani")
public class EsanjeevaniController extends BaseRestController {

    @Autowired
    private EsanjeevaniService esanjeevaniService;

    @RequestMapping(value = "/launch", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> launchEsanjeevani(@RequestParam(value = "patientUuid", required = true) String patientUuid) {

        try {
            String accessToken;
            String loginResponse = esanjeevaniService.getLoginResponse();
            if (esanjeevaniService.isSuccessResponse(loginResponse)) {
                accessToken = esanjeevaniService.extractAccessToken(loginResponse);
                String registerPatientResponse = esanjeevaniService.registerPatient(patientUuid, accessToken);
                if (esanjeevaniService.isSuccessResponse(registerPatientResponse) || esanjeevaniService.isSameProfileResponse(registerPatientResponse)) {
                    String ssoLoginResponse = esanjeevaniService.performSSOLogin();
                    if (esanjeevaniService.isSuccessResponse(ssoLoginResponse)) {
                        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header("Location", esanjeevaniService.getSSOUrl(ssoLoginResponse)).build();
                    } else {
                        return new ResponseEntity<String>(ssoLoginResponse, HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<String>(registerPatientResponse, HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<String>(loginResponse, HttpStatus.OK);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
