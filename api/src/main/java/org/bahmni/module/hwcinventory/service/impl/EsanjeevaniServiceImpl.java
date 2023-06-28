package org.bahmni.module.hwcinventory.service.impl;

import org.bahmni.module.hwcinventory.service.EsanjeevaniService;

public class EsanjeevaniServiceImpl implements EsanjeevaniService {

        private final static String ESANJEEVANI_SERVER_URL_KEY = "esanjeevani.url.webDomain";

        private final static String DEFAULT_ESANJEEVANI_SERVER_URL = "https://uat.esanjeevani.in/#/";

        @Override
        public String getEsanjeevaniWebDomain() {
            String esanjeevaniServerUrl = org.openmrs.api.context.Context.getAdministrationService().getGlobalProperty(ESANJEEVANI_SERVER_URL_KEY);
            if ((esanjeevaniServerUrl == null) || "".equals(esanjeevaniServerUrl)) {
                esanjeevaniServerUrl = DEFAULT_ESANJEEVANI_SERVER_URL;
            }
            return esanjeevaniServerUrl;
        }
}
