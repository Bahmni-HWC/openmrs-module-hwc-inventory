package org.bahmni.module.hwcinventory.mapper;

import org.bahmni.module.hwcinventory.contract.CreatePatientRequest;
import org.bahmni.module.hwcinventory.contract.EsanjeevaniContactDetails;
import org.bahmni.module.hwcinventory.contract.EsanjeevaniPatientAddress;
import org.openmrs.Patient;
import org.openmrs.PersonAddress;
import org.openmrs.api.context.Context;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EsanjeevaniPatientMapper {

    public CreatePatientRequest getPatientRequest(Patient patient){
        CreatePatientRequest createPatientRequest = new CreatePatientRequest();
        createPatientRequest.setAge(patient.getAge());
        createPatientRequest.setBirthdate(getDateString(patient.getBirthdate()));
        createPatientRequest.setDisplayName(patient.getPersonName().getFullName());
        createPatientRequest.setFirstName(patient.getGivenName());
        createPatientRequest.setMiddleName(patient.getMiddleName());
        createPatientRequest.setLastName(patient.getFamilyName());
        createPatientRequest.setGenderDisplay(getGenderFullText(patient));
        createPatientRequest.setGenderCode(mapGenderDisplayToCode(patient.getGender()));
        createPatientRequest.setLstPatientAddress(mapPatientAddress(patient));
        createPatientRequest.setLstPatientContactDetail(mapPatientContactDetails(patient));
        createPatientRequest.setIsBlock(false);
        createPatientRequest.setSource(Context.getAdministrationService().getGlobalProperty("esanjeevani.source"));
        patient.getIdentifiers().forEach(identifier -> {
            if (identifier.getIdentifierType().getName().equals("ABHA Number")) {
                createPatientRequest.setAbhaNumber(identifier.getIdentifier());
            }
            if (identifier.getIdentifierType().getName().equals("ABHA Address")) {
                createPatientRequest.setAbhaAddress(identifier.getIdentifier());
            }
        });
        return createPatientRequest;
    }

    private String getDateString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    private String getGenderFullText(Patient patient){
        String gender= patient.getGender();
        if (gender.equals("M")) return "Male";
        if (gender.equals("F")) return  "Female";
        return "Other";
    }

    private List<EsanjeevaniPatientAddress> mapPatientAddress(Patient patient){
        PersonAddress personAddress = patient.getPersonAddress();
        EsanjeevaniPatientAddress esanjeevaniPatientAddress = new EsanjeevaniPatientAddress();
        esanjeevaniPatientAddress.setAddressLine1(personAddress.getAddress1());
        esanjeevaniPatientAddress.setAddressType("Physical");
        esanjeevaniPatientAddress.setAddressUse("Work");
        esanjeevaniPatientAddress.setPostalCode(personAddress.getPostalCode());
        esanjeevaniPatientAddress.setCityCode(0);
        esanjeevaniPatientAddress.setCityDisplay(personAddress.getCityVillage());
        esanjeevaniPatientAddress.setBlockCode(5604);
        esanjeevaniPatientAddress.setBlockDisplay(personAddress.getAddress4());
        esanjeevaniPatientAddress.setDistrictCode(526);
        esanjeevaniPatientAddress.setDistrictDisplay(personAddress.getCountyDistrict());
        esanjeevaniPatientAddress.setStateCode(29);
        esanjeevaniPatientAddress.setStateDisplay(personAddress.getStateProvince());
        esanjeevaniPatientAddress.setCountryCode("1");
        esanjeevaniPatientAddress.setCountryDisplay("India");
        return Arrays.asList(esanjeevaniPatientAddress);
    }

    private  List<EsanjeevaniContactDetails> mapPatientContactDetails(Patient patient){
        EsanjeevaniContactDetails esanjeevaniContactDetails = new EsanjeevaniContactDetails();
        esanjeevaniContactDetails.setContactPointStatus(true);
        esanjeevaniContactDetails.setContactPointType("Phone");
        esanjeevaniContactDetails.setContactPointUse("Work");
        esanjeevaniContactDetails.setContactPointValue(patient.getAttribute("phoneNumber").getValue());
        return Arrays.asList(esanjeevaniContactDetails);
    }

    private int mapGenderDisplayToCode(String genderdisplay){
        if(genderdisplay.equals("M")){
            return 1;
        }else if(genderdisplay.equals("F")){
            return 2;
        }else{
            return 3;
        }
    }

}
