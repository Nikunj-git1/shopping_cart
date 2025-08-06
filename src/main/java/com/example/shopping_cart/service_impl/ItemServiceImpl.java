package com.example.shopping_cart.service_impl;

import com.example.shopping_cart.entity.CatEntity;
import com.example.shopping_cart.entity.ItemEntity;
import com.example.shopping_cart.repository.ItemRepository;
import com.example.shopping_cart.request_dto.CatDTOResponse;
import com.example.shopping_cart.request_dto.ItemDTO;
import com.example.shopping_cart.request_dto.ItemDTOResponse;
import com.example.shopping_cart.request_dto.ItemDTOUpdate;
import com.example.shopping_cart.service.ItemService;
import org.eclipse.angus.mail.imap.protocol.Item;
import org.modelmapper.ModelMapper;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.shopping_cart.util.CustomizeDateFormat.formatTimestamp;
import static com.example.shopping_cart.util.PhotoUploadHelper.cleanFileName;

@Service

public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserLoginServiceImpl userLoginServiceImpl;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${shopping_cart.file_upload_path}")
    private String fileUploadPath;

    @Autowired
    private NamedParameterJdbcTemplate npJdbcTemplate;


    @Override
    @CachePut(value = "itemListCache", key = "#result.itemId")
    public ItemDTOResponse create(ItemDTO itemDTO, User user) throws IOException {
        itemRepository.findByItemNameIgnoreCase(itemDTO.getItemName())
                .ifPresent(existing -> {
                    throw new DataIntegrityViolationException(
                            "This item '" + itemDTO.getItemName() + "' is  already exists.");
                });

        String fileName = cleanFileName(itemDTO.getPhoto().getOriginalFilename());
        Path path = Paths.get(fileUploadPath, fileName);
        itemDTO.getPhoto().transferTo(path);

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date sqlDate;
        try {
            Date parsed = formatter.parse(itemDTO.getExpDate());
            sqlDate = new Date(parsed.getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format, expected dd-MM-yyyy", e);
        }

        Integer createBy = userLoginServiceImpl.getAdminId(user);

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setSubCatId(itemDTO.getSubCatId());
        itemEntity.setItemName(itemDTO.getItemName());
        itemEntity.setPrice(itemDTO.getPrice());
        itemEntity.setStockQty(itemDTO.getStockQty());
        itemEntity.setExpDate(sqlDate);
        itemEntity.setStatus(itemDTO.getStatus());
        itemEntity.setPhoto(fileName);
        itemEntity.setCreatedBy(createBy);

        itemEntity = itemRepository.save(itemEntity);

        return mapToResponse(itemEntity);
    }


    @Override
    @Cacheable(
            value = "itemListCache",
            key = "#status == null || #status.isEmpty() || #status.equalsIgnoreCase('All') ? 'ALL' : #status",
            unless = "#result == null || #result.isEmpty()"
    )
    public List<Map<String, Object>> getList(String status) {
        String sql = """
                  SELECT c.cat_name,
                  sc.sub_cat_name, item_id,item_name, stock_qty, price, exp_date, i.status, photo
                  FROM item i
                  LEFT JOIN sub_category sc ON sc.sub_cat_id = i.sub_cat_id
                  LEFT JOIN category c ON c.cat_id = sc.cat_id
                """;


        boolean isAllStatus = (status == null || status.isEmpty() || status.equalsIgnoreCase("all"));

        if (!isAllStatus) {
            sql += " WHERE i.status = :status";
            return npJdbcTemplate.queryForList(sql, Map.of("status", status));
        } else {
            return npJdbcTemplate.queryForList(sql, Map.of());
        }
    }


    @Override
    @CacheEvict(value = "itemListCache", allEntries = true)
    public ItemDTOResponse update(ItemDTOUpdate itemDTOUpdate, User user) {
        ItemEntity itemEntity = itemRepository.findById(itemDTOUpdate.getItemId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Item ID '" + itemDTOUpdate.getItemId() + "' not found."));

        if (itemRepository.existsByItemNameIgnoreCaseAndItemIdNot(
                itemDTOUpdate.getItemName(),
                itemDTOUpdate.getItemId())) {
            throw new DataIntegrityViolationException(
                    "Item name '" + itemDTOUpdate.getItemName() + "' already exists.");
        }

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date sqlDate;
        try {
            Date parsed = formatter.parse(itemDTOUpdate.getExpDate());
            sqlDate = new Date(parsed.getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format, expected dd-MM-yyyy", e);
        }

        Integer updatedBy = userLoginServiceImpl.getAdminId(user);

        modelMapper.map(itemDTOUpdate, itemEntity);
        itemEntity.setExpDate(sqlDate
        );
        itemEntity.setUpdatedAt(new Date());
        itemEntity.setUpdatedBy(updatedBy);
        itemEntity = itemRepository.save(itemEntity);

        return mapToResponse(itemEntity);
    }


    @Override
    @CacheEvict(value = "itemListCache", allEntries = true)
    public ItemDTOResponse updateStatus(Integer itemId, String status, User user) {
        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item ID '" + itemId + "' not found."));

        Integer updatedBy = userLoginServiceImpl.getAdminId(user);

        itemEntity.setStatus(status);
        itemEntity.setUpdatedAt(new Date());
        itemEntity.setUpdatedBy(updatedBy);
        itemEntity = itemRepository.save(itemEntity);

        return mapToResponse(itemEntity);
    }


    @Override
    @CacheEvict(value = "itemListCache", allEntries = true)
    public boolean delete(Integer itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NoSuchElementException("This item id '" + itemId + "' is not found.");
        }

        itemRepository.deleteById(itemId);

        return true;
    }


    @Override
//    @Cacheable(value = )
    public List<ItemDTOResponse> getListPageWise(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        PageRequest itemDTOResPagebl = PageRequest.of(pageNo, pageSize, Sort.Direction.valueOf(sortDir), sortBy);

//            if need to Multiple Sorting
//        Sort sort = Sort.by(Sort.Order.asc("price"), Sort.Order.asc("stockQty"));
        Page<ItemEntity> itemsPage = itemRepository.findAll(itemDTOResPagebl);

        List<ItemDTOResponse> dtoList = itemsPage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();
        return dtoList;
    }

    private ItemDTOResponse mapToResponse(ItemEntity itemEntity) {
        ItemDTOResponse response = modelMapper.map(itemEntity, ItemDTOResponse.class);
        response.setCreatedAt(formatTimestamp(itemEntity.getCreatedAt()));
        response.setUpdatedAt(formatTimestamp(itemEntity.getUpdatedAt()));
        response.setExpDate(formatTimestamp(itemEntity.getExpDate()));

        return response;
    }


    private Date getSqlDateForamt(ItemDTO itemDTO) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date sqlDate;
        try {
            Date parsed = formatter.parse(itemDTO.getExpDate());
            sqlDate = new Date(parsed.getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format, expected dd-MM-yyyy", e);
        }

        return sqlDate;
    }
}