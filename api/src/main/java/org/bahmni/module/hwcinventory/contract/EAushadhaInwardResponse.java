package org.bahmni.module.hwcinventory.contract;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EAushadhaInwardResponse {
    @JsonAlias("Sl_No")
    private int Sl_No;
    @JsonAlias("inwardno")
    private String inwardno;
    @JsonAlias("instituteid")
    private String instituteid;
    @JsonAlias("Institute_name")
    private String Institute_name;
    @JsonAlias("InstituteType")
    private String InstituteType;
    @JsonAlias("Receipt_Date")
    private String Receipt_Date;
    @JsonAlias("Batch_number")
    private String Batch_number;
    @JsonAlias("Mfg_date")
    private String Mfg_date;
    @JsonAlias("Exp_date")
    private String Exp_date;
    @JsonAlias("Quantity_In_Pack")
    private int Quantity_In_Pack;
    @JsonAlias("UnitPack")
    private String UnitPack;
    @JsonAlias("Quantity_In_Units")
    private int Quantity_In_Units;
    @JsonAlias("Available_quantity")
    private int Available_quantity;
    @JsonAlias("Warehouse_name")
    private String Warehouse_name;
    @JsonAlias("Drug_id")
    private String Drug_id;
    @JsonAlias("Drug_name")
    private String Drug_name;
    @JsonAlias("StandardQuality")
    private String StandardQuality;

}
