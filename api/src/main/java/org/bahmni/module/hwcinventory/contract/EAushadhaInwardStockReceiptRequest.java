package org.bahmni.module.hwcinventory.contract;

public class EAushadhaInwardStockReceiptRequest {
    private String inwardDate;
    private String instituteId;

    public EAushadhaInwardStockReceiptRequest() {
    }

    public String getInwardDate() {
        return inwardDate;
    }

    public void setInwardDate(String inwardDate) {
        this.inwardDate = inwardDate;
    }

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }
}
