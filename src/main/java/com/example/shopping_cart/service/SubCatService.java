package com.example.shopping_cart.service;

import com.example.shopping_cart.request_dto.SubCatDTO;
import com.example.shopping_cart.request_dto.SubCatDTOResponse;
import com.example.shopping_cart.request_dto.SubCatDTOUpdate;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface SubCatService {

    SubCatDTOResponse create(SubCatDTO subCatDTO, User user);

    List<SubCatDTOResponse> getList(String status);

    List<SubCatDTOResponse> getListCatId(Integer CatId);

    SubCatDTOResponse update(SubCatDTOUpdate subCatDTOUpdate, User user);

    SubCatDTOResponse updateStatus(Integer subCatId, String status, User user);
}