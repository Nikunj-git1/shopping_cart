package com.example.shopping_cart.controller;

import com.example.shopping_cart.request_dto.SubCatDTO;
import com.example.shopping_cart.comman_response_dto.CommonResponse;
import com.example.shopping_cart.comman_response_dto.ResGenerator;
import com.example.shopping_cart.request_dto.SubCatDTOUpdate;
import com.example.shopping_cart.service.SubCatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j

@RestController
@RequestMapping("/admin/sub-cat")
@Tag(name = "Sub-category API", description = "For CRUD operation of sub-categories.")

@Validated
public class SubCatController {

    @Autowired
    SubCatService subCatService;


    @PostMapping("/create")
    public ResponseEntity<CommonResponse> create(@Valid
                                                 @RequestBody SubCatDTO subCatDTO,
                                                 @AuthenticationPrincipal User user) {

        return ResGenerator.create(
                "Sub-category create successfully", subCatService.create(subCatDTO, user));
    }

    @GetMapping("/get-list")
    public ResponseEntity<CommonResponse> getList(@RequestParam(required = false) String status) {

        return ResGenerator.success("List of sub-category", subCatService.getList(status));
    }

    @PutMapping("/update")
    public ResponseEntity<CommonResponse> update(@RequestBody SubCatDTOUpdate subCatDTOUpdate) {

        return ResGenerator.success("Sub-category update successfully", subCatService.update(subCatDTOUpdate));
    }

    @PatchMapping("/update-status/{subCatId}/{status}")
    public ResponseEntity<CommonResponse> updateStatus(@PathVariable int subCatId,
                                                       @PathVariable String status) {

        return ResGenerator.success(
                "Sub-category update successfully", subCatService.updateStatus(subCatId, status));
    }

    @GetMapping("/get-list-cat-id")
    public ResponseEntity<CommonResponse> getListSubCatId(@RequestParam int catId) {

        return ResGenerator.success("Sub-category list of your id", subCatService.getListCatId(catId));
    }
}