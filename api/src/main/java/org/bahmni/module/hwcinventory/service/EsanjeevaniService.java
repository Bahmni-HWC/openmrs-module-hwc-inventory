package org.bahmni.module.hwcinventory.service;

import org.springframework.stereotype.Service;


@Service
public interface EsanjeevaniService {

    String getEsanjeevaniWebDomain();

    String makeProviderLoginRequest() throws Exception;

    String createPatientRegistration() throws Exception;

    String generateReferenceIdForSSO() throws Exception;

}
