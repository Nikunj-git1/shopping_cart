package com.example.shopping_cart.controller;

import com.example.shopping_cart.comman_response_dto.CommonResponse;
import com.example.shopping_cart.comman_response_dto.ResGenerator;
import com.example.shopping_cart.request_dto.SubCatDTO;
import com.example.shopping_cart.request_dto.SubCatDTOUpdate;
import com.example.shopping_cart.service.SubCatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/sub-cat")
@Tag(name = "Sub-category API", description = "For CRUD operation of sub-categories.")

@Validated
public class SubCatController {

    @Autowired
    SubCatService subCatService;


    @PostMapping("/create")
    public ResponseEntity<CommonResponse> create(@RequestBody @Valid SubCatDTO subCatDTO,
                                                 @AuthenticationPrincipal User user) {

        return ResGenerator.create("Sub-category create successfully", subCatService.create(subCatDTO, user));
    }


    @GetMapping("/get-list")
    public ResponseEntity<CommonResponse> getList(@RequestParam(required = false)
                                                  @Pattern(
                                                          regexp = "^(Active|Inactive|[Aa][Ll][Ll])?$",
                                                          message = "Status must be either 'Active', 'Inactive', " +
                                                                  "'All' (any case), or empty"
                                                  )
                                                  String status) {

        return ResGenerator.success("List of sub-category", subCatService.getList(status));
    }


    @PutMapping("/update")
    public ResponseEntity<CommonResponse> update(@RequestBody @Valid SubCatDTOUpdate subCatDTOUpdate,
                                                 @AuthenticationPrincipal User user) {

        return ResGenerator.success("Sub-category update successfully", subCatService.update(subCatDTOUpdate, user));
    }


    @PatchMapping("/update-status/{subCatId}/{status}")
    public ResponseEntity<CommonResponse> updateStatus(@PathVariable(required = true)
                                                       @NotNull(message = "Sub category ID must not be null")
                                                       @Positive(message = "Sub category ID must be a positive number")
                                                       Integer subCatId,

                                                       @PathVariable(required = true)
                                                       @NotBlank(message = "Status is required")
                                                       @Pattern(regexp = "^(Active|Inactive)$",
                                                               message = "Status must be either Active or Inactive")
                                                       String status,

                                                       @AuthenticationPrincipal User user) {

        return ResGenerator.success("Sub-category update successfully",
                subCatService.updateStatus(subCatId, status, user));
    }


    @GetMapping("/get-list-cat-id")
    public ResponseEntity<CommonResponse> getListCatId(@RequestParam (required = true)
                                                       @NotNull(message = "Category ID must not be null")
                                                       Integer catId) {

        return ResGenerator.success("Sub-category list of your category id", subCatService.getListCatId(catId));
    }
}