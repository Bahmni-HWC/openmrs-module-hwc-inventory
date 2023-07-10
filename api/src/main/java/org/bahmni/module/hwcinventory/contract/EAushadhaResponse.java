package org.bahmni.module.hwcinventory.contract;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EAushadhaResponse {
    @JsonAlias("Sl_No")
    private int Sl_No;
    @JsonAlias("ouid")
    private String ouid;
    @JsonAlias("instid")
    private String instid;
    @JsonAlias("Institute_name")
    private String Institute_name;
    @JsonAlias("OutwardDate")
    private String OutwardDate;
    @JsonAlias("Batch_number")
    private String Batch_number;
    @JsonAlias("Mfg_date")
    private String Mfg_date;
    @JsonAlias("Exp_date")
    private String Exp_date;
    @JsonAlias("Quantity")
    private int Quantity;
    @JsonAlias("Drug_id")
    private String Drug_id;
    @JsonAlias("Drug_name")
    private String Drug_name;
    private int Actual_quantity;




    /*
    @JsonAlias("Supplier_name")
    private String Supplier_name;

    @JsonAlias("InstituteType")
    private String InstituteType;

    @JsonAlias("Rack_number")
    private String Rack_number;



    @JsonAlias("Unit_value")
    private int Unit_value;
    @JsonAlias("IndentNo")
    private String IndentNo;


    @JsonAlias("Warehouse_name")
    private String Warehouse_name;
    */



}
