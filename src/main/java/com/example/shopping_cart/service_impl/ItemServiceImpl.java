package com.example.shopping_cart.service_impl;

import com.example.shopping_cart.entity.ItemEntity;
import com.example.shopping_cart.repository.ItemRepository;
import com.example.shopping_cart.request_dto.ItemDTOResponse;
import com.example.shopping_cart.request_dto.ItemDTOUpdate;
import com.example.shopping_cart.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.shopping_cart.controller.FileUpload.cleanFileName;

@Slf4j
@Service

public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Value("${shopping_cart.file_upload_path}")
    private String fileUploadPath;

    @Autowired
    private NamedParameterJdbcTemplate npJdbcTemplate;

    private ItemDTOResponse getModelMapperItemDTORespo(ItemEntity itemEntity) {
        return modelMapper.map(itemRepository.save(itemEntity), ItemDTOResponse.class);
    }

    public ItemDTOResponse create(Integer subCatId, String itemName, Integer stockQty, Integer price,
                                  String expDate, String status, MultipartFile photo) throws IOException {

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date sqlDate;
        try {
            Date parsed = formatter.parse(expDate);
log.info("look date conver {}", parsed);
            sqlDate = new Date(parsed.getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format, expected dd-MM-yyyy", e);
        }

        Optional<ItemEntity> optionalItemEntity = itemRepository.findByItemName(itemName);

        if (!optionalItemEntity.isEmpty()) {
        }

        String fileName = cleanFileName(photo.getOriginalFilename());
        Path path = Paths.get(fileUploadPath, fileName);
        photo.transferTo(path);


        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setSubCatId(subCatId);
        itemEntity.setItemName(itemName);
        itemEntity.setPhoto(fileName);
        itemEntity.setPrice(price);
        itemEntity.setExpDate(sqlDate);
        itemEntity.setStatus(status);

        itemEntity = itemRepository.save(itemEntity);

        return getModelMapperItemDTORespo(itemEntity);
    }

    public List<Map<String, Object>> getList(String status) {

        String sql = """
                    SELECT c.catName category,
                    sc.subCatName sub_category, itemId,i.itemName, stockQty, price, expDate, i.status, photo
                    FROM item i
                    LEFT JOIN sub_category sc ON sc.subCatId = i.subCatId
                    LEFT JOIN category c ON c.catId = sc.catId
                """;
        if (!(status == null || status.equalsIgnoreCase("all"))) {
            sql += " WHERE p.status = :status";
        }

        return npJdbcTemplate.queryForList(sql, Map.of("status", status));

    }

    public ItemDTOResponse update(ItemDTOUpdate itemDTOUpdate) {

        Optional<ItemEntity> optionalProductEntity = itemRepository.findById(itemDTOUpdate.getItemId());
        if (optionalProductEntity.isEmpty()) {

        }

        boolean isDuplicate = itemRepository.existsByItemNameIgnoreCaseAndItemIdNot(
               itemDTOUpdate.getItemName(),
                itemDTOUpdate.getItemId());
        if (isDuplicate) {

        }

        ItemEntity itemEntity = optionalProductEntity.get();
        modelMapper.map(itemDTOUpdate, itemEntity);
        itemEntity = itemRepository.save(itemEntity);

        return getModelMapperItemDTORespo(itemEntity);
    }

    public ItemDTOResponse updateStatus(Integer itemId, String status) {

        Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemId);

        if (optionalItemEntity.isEmpty()) {

        }

        ItemEntity itemEntity = optionalItemEntity.get();
        itemEntity.setStatus(status);

        return getModelMapperItemDTORespo(itemEntity);

    }

    public boolean delete(Integer itemId) {

        Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemId);

        if (optionalItemEntity.isEmpty()) {
        }

        itemRepository.deleteById(itemId);
        return true;
    }
}