package org.bahmni.module.hwcinventory.contract;

public class EAushadhaStockRecieptRequest {
    private String month;
    private String year;

    public EAushadhaStockRecieptRequest(String month, String year) {
        this.month = month;
        this.year = year;
    }

    public EAushadhaStockRecieptRequest() {
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
