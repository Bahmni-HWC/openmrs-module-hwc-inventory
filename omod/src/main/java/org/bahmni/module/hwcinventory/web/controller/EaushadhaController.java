package org.bahmni.module.hwcinventory.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bahmni.module.hwcinventory.service.EsanjeevaniService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/eaushadha")
public class EaushadhaController {

    @RequestMapping(value = "/stockReceipt", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> stockReceipt(@RequestBody String month, @RequestBody String year) {

        try {
            String accessToken = "S3Ntc2NsOktzbXNjbCMxMjM=";
            String loginResponse = "end point hit" + month + year;

       /*     if (esanjeevaniService.isSuccessResponse(loginResponse)) {

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
            }*/

                return new ResponseEntity<String>(loginResponse, HttpStatus.OK);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
