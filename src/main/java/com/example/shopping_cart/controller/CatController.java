package com.example.shopping_cart.controller;

import com.example.shopping_cart.comman_response_dto.CommonResponse;
import com.example.shopping_cart.comman_response_dto.ResGenerator;
import com.example.shopping_cart.repository.AdminRepository;
import com.example.shopping_cart.request_dto.CatDTO;
import com.example.shopping_cart.request_dto.CatDTOUpdate;
import com.example.shopping_cart.service.CatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/admin/cat")
@Tag(name = "Category API", description = "For CRUD operation of categories.")
// Change for GIT


@Validated
public class CatController {
    @Autowired
    CatService catService;

    @Autowired
    AdminRepository adminRepository;


    @PostMapping("/create")
    public ResponseEntity<CommonResponse> create(@Valid @RequestBody CatDTO catDTO, @AuthenticationPrincipal User user) {

        return ResGenerator.create("Category create successfully", catService.create(catDTO, user));
    }

    @PostMapping("/create-by-import")
    public ResponseEntity<CommonResponse> createByImport(@RequestParam("excelFile") MultipartFile excelFile,
                                                         @AuthenticationPrincipal User user) throws Exception {
        Map<String, Object> result = catService.createByImportExcel(excelFile, user);

        return ResGenerator.create("Excel file imported successfully", result);
    }

    @GetMapping("/generate-pdf")
    public ResponseEntity<CommonResponse> savePdfToLocal() {
        catService.exportCategoryPdf();

        return ResGenerator.success(
                "PDF saved to local file system at D:/java_practice/shopping_cart/pdf-generated",
                null);
    }

    @GetMapping("/get-list")
    public ResponseEntity<CommonResponse> getList(@Valid
                                                  @RequestParam(required = false)
                                                  @Pattern(regexp = "^(Active|Inactive)?$",
                                                          message = "Status must be either Active, Inactive or Blank")
                                                  String status) {

        return ResGenerator.success("List of category", catService.getList(status));
    }

    @PutMapping("/update")
    public ResponseEntity<CommonResponse> update(@Valid @RequestBody CatDTOUpdate catDTOUpdate) {

        return ResGenerator.success("Category updated successfully", catService.update(catDTOUpdate));
    }

    @PatchMapping("/update-status/{catId}/{status}")
    public ResponseEntity<CommonResponse> updateStatus(@PathVariable("catId")
                                                       @NotNull(message = "Category ID must not be null")
                                                       @Positive(message = "Category ID must be a positive number")
                                                       Integer catId,

                                                       @PathVariable("status")
                                                       @NotBlank(message = "Status is required")
                                                       @Pattern(regexp = "^(Active|Inactive)$",
                                                               message = "Status must be either Active or Inactive")
                                                       String status) {

        return ResGenerator.success("Category status update successfully", catService.updateStatus(catId, status));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CommonResponse> delete(@NotNull(message = "Category ID must not be null")
                                                 @RequestParam int catId) {

        return new ResponseEntity<>(new CommonResponse(
                true,"This category has been deleted", catService.delete(catId)), HttpStatus.OK);
    }
}