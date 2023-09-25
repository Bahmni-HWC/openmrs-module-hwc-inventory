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
import java.util.*;

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
        isCompletePatientAddress(personAddress);
        Map<String, Integer> lgdCodeForPersonAddress = getLGDCOdeForPersonAddress(personAddress);
        EsanjeevaniPatientAddress esanjeevaniPatientAddress = new EsanjeevaniPatientAddress();
        esanjeevaniPatientAddress.setAddressLine1(personAddress.getCityVillage());
        esanjeevaniPatientAddress.setAddressType("Physical");
        esanjeevaniPatientAddress.setAddressUse("Work");
        esanjeevaniPatientAddress.setPostalCode(personAddress.getPostalCode());
        esanjeevaniPatientAddress.setCityCode(lgdCodeForPersonAddress.get(personAddress.getAddress4()));
        esanjeevaniPatientAddress.setCityDisplay(personAddress.getAddress4());
        esanjeevaniPatientAddress.setDistrictCode(lgdCodeForPersonAddress.get(personAddress.getCountyDistrict()));
        esanjeevaniPatientAddress.setDistrictDisplay(personAddress.getCountyDistrict());
        esanjeevaniPatientAddress.setStateCode(lgdCodeForPersonAddress.get(personAddress.getStateProvince()));
        esanjeevaniPatientAddress.setStateDisplay(personAddress.getStateProvince());
        esanjeevaniPatientAddress.setCountryCode("1");
        esanjeevaniPatientAddress.setCountryDisplay("India");
        return Arrays.asList(esanjeevaniPatientAddress);
    }

    private static void isCompletePatientAddress(PersonAddress personAddress) {
        if(personAddress.getAddress4()== null || personAddress.getCountyDistrict() == null || personAddress.getStateProvince() == null){
            throw new NullPointerException("State or District or Sub-district cannot be null");
        }
    }

    private Map<String,Integer> getLGDCOdeForPersonAddress(PersonAddress personAddress) throws LGDCodeNotFoundException {
        Map<String,Integer> lgdCodeMap = new HashMap<>();
        AddressHierarchyService addressHierarchyService = Context.getService(AddressHierarchyService.class);
        AddressHierarchyLevel addressHierarchyLevel = addressHierarchyService.getAddressHierarchyLevelByAddressField(AddressField.ADDRESS_4);
        List<AddressHierarchyEntry> matchingSubDistrictEntries = addressHierarchyService.getAddressHierarchyEntriesByLevelAndName(addressHierarchyLevel,personAddress.getAddress4());

        for (AddressHierarchyEntry entry : matchingSubDistrictEntries) {
            AddressHierarchyEntry districtEntry = entry.getParent();
            AddressHierarchyEntry stateEntry = entry.getParent().getParent();

            if (districtEntry.getName().equalsIgnoreCase(personAddress.getCountyDistrict()) && stateEntry.getName().equalsIgnoreCase(personAddress.getStateProvince())) {
                lgdCodeMap.put(personAddress.getStateProvince(), Integer.parseInt(stateEntry.getUserGeneratedId()));
                lgdCodeMap.put(personAddress.getCountyDistrict(), Integer.parseInt(districtEntry.getUserGeneratedId()));
                lgdCodeMap.put(personAddress.getAddress4(), Integer.parseInt(entry.getUserGeneratedId()));
                return lgdCodeMap;
            }
        }

        throw new LGDCodeNotFoundException("LGD Code not found for " + personAddress.getAddress4() + ", " + personAddress.getCountyDistrict() + ", " + personAddress.getStateProvince());
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
