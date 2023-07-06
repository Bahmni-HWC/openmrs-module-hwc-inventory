package org.bahmni.module.hwcinventory.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.core.Logger;
import org.bahmni.module.hwcinventory.contract.EAushadhaRequest;
import org.bahmni.module.hwcinventory.contract.EAushadhaStockRecieptRequest;
import org.bahmni.module.hwcinventory.service.EsanjeevaniService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/eaushadha")
public class EaushadhaController {
    private Log log = LogFactory.getLog(this.getClass());
    private RestTemplate restTemplate = new RestTemplate();
    @RequestMapping(value = "/stock-receipt", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> stockReceipt(@RequestBody EAushadhaStockRecieptRequest stockRecieptRequest) {

        try {
            String accessToken = "S3Ntc2NsOktzbXNjbCMxMjM=";
            String loginResponse = "end point hit" + stockRecieptRequest.getMonth() + stockRecieptRequest.getYear()  + accessToken;
            String eAushadhaUrl = "https://dlc.kar.nic.in/e-services/api/WHOutwarddata";
            // Prepare the request URL and body for the external API
           // String externalApiUrl = "https://api.example.com/endpoint";

            EAushadhaRequest externalRequestData = new EAushadhaRequest(stockRecieptRequest.getMonth(), stockRecieptRequest.getYear());

            // Set the access token in the request headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            // Create the request entity with headers and body
            HttpEntity<EAushadhaRequest> requestEntity = new HttpEntity<>(externalRequestData, headers);

            // Make the HTTP POST request to the external API
            ResponseEntity<Object> eAushadharesponse = restTemplate.exchange(eAushadhaUrl, HttpMethod.POST, requestEntity, Object.class);

               // return new ResponseEntity<>(loginResponse, HttpStatus.OK);
            return new ResponseEntity<>(eAushadharesponse.getBody(), HttpStatus.OK);

        } catch (Exception e) {
          log.error("Error while logging in to eAushadha", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


}
