package com.example.shopping_cart.service;

import com.example.shopping_cart.request_dto.ItemDTO;
import com.example.shopping_cart.request_dto.ItemDTOResponse;
import com.example.shopping_cart.request_dto.ItemDTOUpdate;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ItemService {

    Object create(ItemDTO itemDTO, User user) throws IOException;

    List<Map<String, Object>> getList(String status);

    ItemDTOResponse update(ItemDTOUpdate itemDTOUpdate, User user);

    ItemDTOResponse updateStatus(Integer itemId, String status, User user);

    boolean delete(Integer itemId);

    List<ItemDTOResponse> getListPageWise(Integer pageNo, Integer pageSize, String sortBy, String sorDir);

}