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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/esanjeevani")
public class EsanjeevaniController extends BaseRestController{

    @Autowired
    private EsanjeevaniService esanjeevaniService;

    @RequestMapping(value = "/launch" ,method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Void> launchEsanjeevani() {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header("Location", esanjeevaniService.getEsanjeevaniWebDomain()).build();
    }
}
