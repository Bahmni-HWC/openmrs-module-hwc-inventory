package org.bahmni.module.hwcinventory.service;

import org.bahmni.module.hwcinventory.contract.EAushadhaResponse;
import org.bahmni.module.hwcinventory.contract.EAushadhaStockRecieptRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface EaushadhaService {

    List<EAushadhaResponse> fetchStockDetails(String outwardId) throws Exception;

}
