package com.example.shopping_cart.service;

import com.example.shopping_cart.request_dto.CustDTO;
import com.example.shopping_cart.request_dto.CustDTOResponse;
import com.example.shopping_cart.request_dto.CustDTOUpdate;

import java.util.List;


public interface CustService {

    CustDTOResponse signup (CustDTO custDTO);

    List<CustDTOResponse> getList ();

    CustDTOResponse update (CustDTOUpdate custDTOUpdate);

    boolean delete (Integer custId);
}