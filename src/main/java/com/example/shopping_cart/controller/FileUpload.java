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

import static com.example.shopping_cart.util.PhotoUploadHelper.cleanFileName;

@Slf4j
@RestController
@RequestMapping("/file")


public class FileUpload {

    @Value("${shopping_cart.file_upload_path}")
    private String fileUploadPath;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ModelMapper modelMapper;


    @PostMapping("/item")
    public ItemDTOResponse uploadProductFile(@RequestParam MultipartFile file,
                                             @RequestParam int id) throws IOException {

        Optional<ItemEntity> optionalProductEntity = itemRepository.findById(id);
        if (optionalProductEntity.isEmpty()) {
            throw new DataIntegrityViolationException("item not found");
        }

        String fileName = cleanFileName(file.getOriginalFilename());
        Path path = Paths.get(fileUploadPath, fileName);
        log.info("getName {}", file.getName());
        log.info("getOriginalFilename {}", file.getOriginalFilename());
        file.transferTo(path);

        ItemEntity itemEntity = optionalProductEntity.get();
        itemEntity.setPhoto(fileName);
        return modelMapper.map(itemRepository.save(itemEntity), ItemDTOResponse.class);

        //String status = fileProcessingService.uploadFile(fileName, file);

    }


    @GetMapping(value = "/item/{fileName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public Object uploadProductFile(@PathVariable String fileName) throws IOException {

        Path path = Paths.get(fileUploadPath, fileName);

        if (!(path.toFile().isFile() && path.toFile().exists())) {
            throw new FileNotFoundException("Not found");
        }

        return Files.readAllBytes(path);
    }

    //For popup (web)
    @GetMapping(value = "/product2/{fileName}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public Object uploadProductFile2(@PathVariable String fileName) throws IOException {

        Path path = Paths.get(fileUploadPath, fileName);

        if (!(path.toFile().isFile() && path.toFile().exists())) {
            throw new FileNotFoundException("Not found");
        }

        return Files.readAllBytes(path);
    }

}