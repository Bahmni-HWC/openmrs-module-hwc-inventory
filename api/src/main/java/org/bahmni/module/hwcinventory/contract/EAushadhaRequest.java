package org.bahmni.module.hwcinventory.contract;

public class EAushadhaRequest {

    private String ouid;
 /*   private String WHOutwardmonth;
    private String WHOutwardyear;*/


    /*public String getWHOutwardmonth() {
        return WHOutwardmonth;
    }*/

    public EAushadhaRequest(String ouid) {
        this.ouid = ouid;

    }

    public String getOuid() {
        return ouid;
    }

    public void setOuid(String ouid) {
        this.ouid = ouid;
    }
    /*
    public void setWHOutwardmonth(String WHOutwardmonth) {
        this.WHOutwardmonth = WHOutwardmonth;
    }

    public String getWHOutwardyear() {
        return WHOutwardyear;
    }

    public void setWHOutwardyear(String WHOutwardyear) {
        this.WHOutwardyear = WHOutwardyear;
    }*/

    public EAushadhaRequest() {
    }
}
