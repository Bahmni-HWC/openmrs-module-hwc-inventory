package org.bahmni.module.hwcinventory.contract;

public class EAushadhaRequest {
    private String WHOutwardmonth;
    private String WHOutwardyear;

    public String getWHOutwardmonth() {
        return WHOutwardmonth;
    }

    public EAushadhaRequest(String WHOutwardmonth, String WHOutwardyear) {
        this.WHOutwardmonth = WHOutwardmonth;
        this.WHOutwardyear = WHOutwardyear;
    }

    public void setWHOutwardmonth(String WHOutwardmonth) {
        this.WHOutwardmonth = WHOutwardmonth;
    }

    public String getWHOutwardyear() {
        return WHOutwardyear;
    }

    public void setWHOutwardyear(String WHOutwardyear) {
        this.WHOutwardyear = WHOutwardyear;
    }

    public EAushadhaRequest() {
    }
}
