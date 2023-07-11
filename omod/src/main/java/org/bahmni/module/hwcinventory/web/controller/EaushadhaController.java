package org.bahmni.module.hwcinventory.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bahmni.module.hwcinventory.contract.EAushadhaResponse;
import org.bahmni.module.hwcinventory.contract.EAushadhaStockRecieptRequest;
import org.bahmni.module.hwcinventory.service.EaushadhaService;
import org.openmrs.Privilege;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/eaushadha")
public class EaushadhaController {

    @Autowired
    private EaushadhaService eaushadhaService;
    private Log log = LogFactory.getLog(this.getClass());
    private RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/stock-receipt", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> stockReceipt(@RequestBody EAushadhaStockRecieptRequest stockRecieptRequest) {
        Privilege privilege = Context.getUserService().getPrivilege("app:inventory");
        if (Context.getUserContext().getAuthenticatedUser().getPrivileges().contains(Context.getUserService().getPrivilege("app:inventory"))) {

            List<EAushadhaResponse> outwardDetails = null;
            try {
                outwardDetails = eaushadhaService.fetchStockDetails(stockRecieptRequest.getOuid());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(outwardDetails, HttpStatus.OK);


        } else {
            return new ResponseEntity<>("User not having enough privileges", HttpStatus.FORBIDDEN);
        }
    }

}
