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
}