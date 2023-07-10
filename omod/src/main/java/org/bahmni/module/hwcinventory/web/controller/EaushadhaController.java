package org.bahmni.module.hwcinventory.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.core.Logger;
import org.bahmni.module.hwcinventory.contract.EAushadhaRequest;
import org.bahmni.module.hwcinventory.contract.EAushadhaResponse;
import org.bahmni.module.hwcinventory.contract.EAushadhaStockRecieptRequest;
import org.bahmni.module.hwcinventory.service.EsanjeevaniService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            //String loginResponse = "end point hit" + stockRecieptRequest.getMonth() + stockRecieptRequest.getYear()  + accessToken;
            //String eAushadhaUrl = "https://dlc.kar.nic.in/e-services/api/WHOutwarddata";
            String eAushadhaUrl = "https://dlc.kar.nic.in/e-services/api/InstituteOutward";


            EAushadhaRequest externalRequestData = new EAushadhaRequest(stockRecieptRequest.getOuid());

            // Set the access token in the request headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            // Create the request entity with headers and body
            HttpEntity<EAushadhaRequest> requestEntity = new HttpEntity<>(externalRequestData, headers);

            ResponseEntity<String> eAushadharesponse = restTemplate.exchange(eAushadhaUrl, HttpMethod.POST, requestEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            //String responseBody = eAushadharesponse.getBody();
            String escapedResponse = StringEscapeUtils.unescapeJava(eAushadharesponse.getBody());
            List<EAushadhaResponse> outwardDetails = objectMapper.readValue(escapedResponse.substring(1,escapedResponse.length()-1), new TypeReference<List<EAushadhaResponse>>() {});
            //log.error("outwardDetails[0]"+ outwardDetails.get(0));
            for (EAushadhaResponse response : outwardDetails) {
                String drugName = response.getDrug_name();
                int batchQuantity = response.getQuantity();

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
                    response.setActual_quantity(multiplicationResult);

                }
            }

            return new ResponseEntity<>(outwardDetails, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error while logging in to eAushadha", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


}
