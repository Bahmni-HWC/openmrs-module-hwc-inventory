package org.bahmni.module.hwcinventory.contract;

public class EAushadhaInwardRequest {
    private String InwardDate ;
    private String InstituteId;

    public EAushadhaInwardRequest(String InwardDate, String InstituteId) {
        this.InwardDate = InwardDate;
        this.InstituteId = InstituteId;
    }

    public String getInwardDate() {
        return InwardDate;
    }

    public void setInwardDate(String inwardDate) {
        InwardDate = inwardDate;
    }

    public String getInstituteId() {
        return InstituteId;
    }

    public void setInstituteId(String instituteId) {
        InstituteId = instituteId;
    }

    public EAushadhaInwardRequest() {
    }
}
