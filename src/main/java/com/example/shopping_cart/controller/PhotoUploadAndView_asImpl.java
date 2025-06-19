package com.example.shopping_cart.controller;

import com.example.shopping_cart.entity.ItemEntity;
import com.example.shopping_cart.repository.ItemRepository;
import com.example.shopping_cart.request_dto.ItemDTOResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.example.shopping_cart.util.CustomizeDateFormat.formatTimestamp;
import static com.example.shopping_cart.util.PhotoUploadHelper.cleanFileName;

@Slf4j
@RestController
@RequestMapping("/file")


public class PhotoUploadAndView_asImpl {

    @Value("${shopping_cart.file_upload_path}")
    private String fileUploadPath;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ModelMapper modelMapper;


    @PostMapping("/item")
    public ItemDTOResponse uploadItemPhoto(@RequestParam MultipartFile file,
                                           @RequestParam Integer itemId) throws IOException {

        Optional<ItemEntity> optionalProductEntity = itemRepository.findById(itemId);
        if (optionalProductEntity.isEmpty()) {
            throw new DataIntegrityViolationException("item not found");
        }

        String fileName = cleanFileName(file.getOriginalFilename());
        Path path = Paths.get(fileUploadPath, fileName);
        file.transferTo(path);

        ItemEntity itemEntity = optionalProductEntity.get();
        itemEntity.setPhoto(fileName);

        itemEntity = itemRepository.save(itemEntity);

        ItemDTOResponse itemDTOResponse = modelMapper.map(itemEntity, ItemDTOResponse.class);
        String frExpDate = formatTimestamp(itemEntity.getExpDate());
        itemDTOResponse.setExpDate(frExpDate);
        String frCreatedAt = formatTimestamp(itemEntity.getCreatedAt());
        itemDTOResponse.setCreatedAt(frCreatedAt);
        String frUpdatedAt = formatTimestamp(itemEntity.getUpdatedAt());
        itemDTOResponse.setUpdatedAt(frUpdatedAt);

        return itemDTOResponse;
    }

    @GetMapping(value = "/item2/{fileName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public Object viewItemPhoto(@PathVariable String fileName) throws IOException {

        Path path = Paths.get(fileUploadPath, fileName);

        if (!(path.toFile().isFile() && path.toFile().exists())) {
            throw new FileNotFoundException("Not found");
        }

        return Files.readAllBytes(path);
    }

    //For popup (web)
    @GetMapping(value = "/item3/{fileName}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public Object viewItemPhoto2(@PathVariable String fileName) throws IOException {
        Path path = Paths.get(fileUploadPath, fileName);

        if (!(path.toFile().isFile() && path.toFile().exists())) {
            throw new FileNotFoundException("Not found");
        }

        return Files.readAllBytes(path);
    }
}