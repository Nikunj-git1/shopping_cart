package com.example.shopping_cart.service;

import com.example.shopping_cart.request_dto.CatDTO;
import com.example.shopping_cart.request_dto.CatDTOResponse;
import com.example.shopping_cart.request_dto.CatDTOUpdate;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


public interface CatService {

    CatDTOResponse create(CatDTO catDTO, User user);

    Map<String, Object> createByImportExcel(MultipartFile excelFile, User user);

    void exportCategoryPdf ();

    List<CatDTOResponse> getList(String status);

    CatDTOResponse update(CatDTOUpdate catDTOUpdate);

    CatDTOResponse updateStatus(int catId, String status);

    boolean delete(int catId);

}
