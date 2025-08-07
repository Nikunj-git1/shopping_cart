package com.example.shopping_cart.controller;

import com.example.shopping_cart.comman_response_dto.CommonResponse;
import com.example.shopping_cart.comman_response_dto.ResGenerator;
import com.example.shopping_cart.request_dto.ItemDTO;
import com.example.shopping_cart.request_dto.ItemDTOUpdate;
import com.example.shopping_cart.service.ItemService;
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

import java.io.IOException;


@RestController
@RequestMapping("/admin/item")
@Tag(name = "Item API", description = "For CRUD operation of items.")

@Validated
public class ItemController {

    @Autowired
    ItemService itemService;

//    add line

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> create(@ModelAttribute @Valid ItemDTO itemDTO,
                                                 @AuthenticationPrincipal User user) throws IOException {

        return ResGenerator.create("item created successfully", itemService.create(itemDTO, user));
    }


    @GetMapping("/get-list")
    public ResponseEntity<CommonResponse> getList(@RequestParam(required = false)
                                                  @Pattern(
                                                          regexp = "^(Active|Inactive|[Aa][Ll][Ll])?$",
                                                          message = "Status must be either 'Active', 'Inactive', " +
                                                                  "'All' (any case), or empty"
                                                  )
                                                  String status) {

        return ResGenerator.success("item list based on your status", itemService.getList(status));
    }


    @PutMapping("/update")
    public ResponseEntity<CommonResponse> update(@RequestBody ItemDTOUpdate itemDTOUpdate,
                                                 @AuthenticationPrincipal User user) {

        return ResGenerator.success("item updated successfully", itemService.update(itemDTOUpdate, user));
    }


    @PatchMapping("/update-status/{itemId}/{status}")
    public ResponseEntity<CommonResponse> updateStatus(@PathVariable(required = true)
                                                       @NotNull(message = "Item ID is required")
                                                       @Positive(message = "Item ID must be a positive number")
                                                       Integer itemId,

                                                       @PathVariable(required = false)
                                                       @Pattern(
                                                               regexp = "^(Active|Inactive|[Aa][Ll][Ll])?$",
                                                               message = "Status must be either 'Active', 'Inactive', " +
                                                                       "'All' (any case), or empty"
                                                       )
                                                       String status,

                                                       @AuthenticationPrincipal User user) {

        return ResGenerator.success(
                "item updated successfully", itemService.updateStatus(itemId, status, user));
    }


    @DeleteMapping("/delete")
    public ResponseEntity<CommonResponse> delete(@RequestParam(required = true)
                                                 @NotNull(message = "Item ID is required")
                                                 @Positive(message = "Item ID must be a positive number")
                                                 Integer itemId) {

        return ResGenerator.success("item delete successfully", itemService.delete(itemId));
    }


    @GetMapping("/get-list-page")
    public ResponseEntity<CommonResponse> getListWithPagination(@RequestParam(defaultValue = "1", required = false)
                                                                @Positive(message = "Page no must be a positive number")
                                                                Integer pageNo,

                                                                @RequestParam(defaultValue = "3", required = false)
                                                                @Positive(message = "No of record per page must be a positive number")
                                                                Integer pageSize,

                                                                @RequestParam(defaultValue = "price", required = false)
                                                                @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "SortBy must be alphanumeric with underscores only")
                                                                String sortBy,

                                                                @RequestParam(defaultValue = "DESC", required = false)
                                                                @Pattern(regexp = "^(?i)(asc|desc)$", message = "SortDir must be 'asc' or 'desc'")
                                                                String sortDir) {

        return ResGenerator.success("item list based on your request", itemService.getListPageWise(pageNo, pageSize, sortBy, sortDir));
    }
}