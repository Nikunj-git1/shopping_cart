package com.example.shopping_cart.controller;

import com.example.shopping_cart.comman_response_dto.CommonResponse;
import com.example.shopping_cart.comman_response_dto.ResGenerator;
import com.example.shopping_cart.request_dto.ItemDTOUpdate;
import com.example.shopping_cart.service.ItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/admin/item")
@Tag(name = "Item API", description = "For CRUD operation of items.")


public class ItemController {

    @Autowired
    ItemService itemService;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> create(@RequestParam Integer subCatId, @RequestParam String itemName,
                                                 @RequestParam  Integer price, @RequestParam Integer stockQty,
                                                 @RequestParam String expDate, @RequestParam String status,
                                                 @RequestParam MultipartFile photo) throws IOException {

        return ResGenerator.create("item created successfully",itemService.create(
                subCatId, itemName, price, stockQty, expDate, status, photo));
    }

    @GetMapping("/get-list")
    public ResponseEntity<CommonResponse> getList(@RequestParam(required = false) String status) {

        return ResGenerator.success("item list based on your status", itemService.getList(status));
    }

    @PutMapping("/update")
    public ResponseEntity<CommonResponse> update(@RequestBody ItemDTOUpdate itemDTOUpdate) {

        return ResGenerator.success("item updated successfully", itemService.update(itemDTOUpdate));
    }

    @PatchMapping("/update-status/{itemId}/{status}")
    public ResponseEntity<CommonResponse> updateStatus(@PathVariable Integer itemId,
                                                       @PathVariable String status) {

        return ResGenerator.success("item updated successfully", itemService.updateStatus(itemId, status));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CommonResponse> delete(@RequestParam Integer itemId) {

        return ResGenerator.success("item delete successfully", itemService.delete(itemId));
    }
}