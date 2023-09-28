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
    private int sl_No;
    @JsonAlias("inwardno")
    private String inwardNo;
    @JsonAlias("instituteid")
    private String instituteId;
    @JsonAlias("Institute_name")
    private String institute_Name;
    @JsonAlias("InstituteType")
    private String instituteType;
    @JsonAlias("Receipt_Date")
    private String receipt_Date;
    @JsonAlias("Batch_number")
    private String batch_Number;
    @JsonAlias("Mfg_date")
    private String mfg_Date;
    @JsonAlias("Exp_date")
    private String exp_Date;
    @JsonAlias("Quantity_In_Pack")
    private int quantity_In_Pack;
    @JsonAlias("UnitPack")
    private String unitPack;
    @JsonAlias("Quantity_In_Units")
    private int quantity_In_Units;
    @JsonAlias("Available_quantity")
    private int available_Quantity;
    @JsonAlias("Warehouse_name")
    private String warehouse_Name;
    @JsonAlias("Drug_id")
    private String drug_Id;
    @JsonAlias("Drug_name")
    private String drug_Name;
    @JsonAlias("StandardQuality")
    private String standardQuality;

}
