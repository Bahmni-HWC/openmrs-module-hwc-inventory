package org.bahmni.module.hwcinventory.mapper;

import org.bahmni.module.hwcinventory.contract.CreatePatientRequest;
import org.bahmni.module.hwcinventory.contract.EsanjeevaniContactDetails;
import org.bahmni.module.hwcinventory.contract.EsanjeevaniPatientAddress;
import org.bahmni.module.hwcinventory.exception.LGDCodeNotFoundException;
import org.openmrs.Patient;
import org.openmrs.PersonAddress;
import org.openmrs.api.context.Context;
import org.openmrs.module.addresshierarchy.AddressField;
import org.openmrs.module.addresshierarchy.AddressHierarchyEntry;
import org.openmrs.module.addresshierarchy.AddressHierarchyLevel;
import org.openmrs.module.addresshierarchy.service.AddressHierarchyService;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EsanjeevaniPatientMapper {

    public CreatePatientRequest getPatientRequest(Patient patient) throws Exception {
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

    private List<EsanjeevaniPatientAddress> mapPatientAddress(Patient patient) throws Exception {
        PersonAddress personAddress = patient.getPersonAddress();
        EsanjeevaniPatientAddress esanjeevaniPatientAddress = new EsanjeevaniPatientAddress();
        esanjeevaniPatientAddress.setAddressLine1(personAddress.getAddress1());
        esanjeevaniPatientAddress.setAddressType("Physical");
        esanjeevaniPatientAddress.setAddressUse("Work");
        esanjeevaniPatientAddress.setPostalCode(personAddress.getPostalCode());
        esanjeevaniPatientAddress.setCityCode(getLGDCode(personAddress.getCityVillage(), AddressField.CITY_VILLAGE));
        esanjeevaniPatientAddress.setCityDisplay(personAddress.getCityVillage());
        esanjeevaniPatientAddress.setBlockCode(getLGDCode(personAddress.getAddress4(), AddressField.ADDRESS_4));
        esanjeevaniPatientAddress.setBlockDisplay(personAddress.getAddress4());
        esanjeevaniPatientAddress.setDistrictCode(getLGDCode(personAddress.getCountyDistrict(), AddressField.COUNTY_DISTRICT));
        esanjeevaniPatientAddress.setDistrictDisplay(personAddress.getCountyDistrict());
        esanjeevaniPatientAddress.setStateCode(getLGDCode(personAddress.getStateProvince(), AddressField.STATE_PROVINCE));
        esanjeevaniPatientAddress.setStateDisplay(personAddress.getStateProvince());
        esanjeevaniPatientAddress.setCountryCode("1");
        esanjeevaniPatientAddress.setCountryDisplay("India");
        return Arrays.asList(esanjeevaniPatientAddress);
    }
    private Integer getLGDCode(String addressFieldValue, AddressField addressField) throws Exception {
        AddressHierarchyService addressHierarchyService = Context.getService(AddressHierarchyService.class);
        AddressHierarchyLevel addressHierarchyLevel = addressHierarchyService.getAddressHierarchyLevelByAddressField(addressField);
        List<AddressHierarchyEntry> addressHierarchyEntries = addressHierarchyService.getAddressHierarchyEntriesByLevel(addressHierarchyLevel);
        Integer lgdCode = null;
        for (AddressHierarchyEntry addressHierarchyEntry : addressHierarchyEntries) {
            if (addressHierarchyEntry.getName().equals(addressFieldValue)) {
                lgdCode = Integer.parseInt(addressHierarchyEntry.getUserGeneratedId());
                break;
            }
        }

        if (lgdCode == null) {
            throw new LGDCodeNotFoundException("LGD code not found for Patient Address");
        }
        return lgdCode;
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
