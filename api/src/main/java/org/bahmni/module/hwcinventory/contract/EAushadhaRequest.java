package org.bahmni.module.hwcinventory.contract;

public class EAushadhaRequest {

    private String ouid;



    public EAushadhaRequest(String ouid) {
        this.ouid = ouid;

    }

    public String getOuid() {
        return ouid;
    }

    public void setOuid(String ouid) {
        this.ouid = ouid;
    }


    public EAushadhaRequest() {
    }
}
