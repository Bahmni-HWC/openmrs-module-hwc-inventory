package org.bahmni.module.hwcinventory.service;

import org.springframework.stereotype.Service;


@Service
public interface EsanjeevaniService {

    String getEsanjeevaniWebDomain() throws Exception;

    String makeProviderLoginRequest() throws Exception;

    String createPatientRegistration() throws Exception;

    String authenticateReference() throws Exception;

}
