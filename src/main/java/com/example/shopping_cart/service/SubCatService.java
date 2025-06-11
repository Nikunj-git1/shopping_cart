package com.example.shopping_cart.service;

import com.example.shopping_cart.request_dto.SubCatDTO;
import com.example.shopping_cart.request_dto.SubCatDTOResponse;
import com.example.shopping_cart.request_dto.SubCatDTOUpdate;

import java.util.List;

public interface SubCatService {

    SubCatDTOResponse create(SubCatDTO subCatDTO, Integer createdBy);

    List<SubCatDTOResponse> getList(String status);

    List<SubCatDTOResponse> getListCatId(int CatId);

    SubCatDTOResponse update(SubCatDTOUpdate subCatDTOUpdate);

    SubCatDTOResponse updateStatus(int subCatId, String status);

}