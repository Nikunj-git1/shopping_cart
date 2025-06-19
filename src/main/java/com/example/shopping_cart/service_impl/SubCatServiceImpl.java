package com.example.shopping_cart.service_impl;

import com.example.shopping_cart.entity.SubCatEntity;
import com.example.shopping_cart.repository.SubCatRepository;
import com.example.shopping_cart.request_dto.SubCatDTO;
import com.example.shopping_cart.request_dto.SubCatDTOResponse;
import com.example.shopping_cart.request_dto.SubCatDTOUpdate;
import com.example.shopping_cart.service.SubCatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.shopping_cart.util.CustomizeDateFormat.formatTimestamp;

@Service

public class SubCatServiceImpl implements SubCatService {

    @Autowired
    private SubCatRepository subCatRepository;

    @Autowired
    private UserLoginServiceImpl userLoginServiceImpl;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    @CachePut(value = "subCatCache", key = "#result.subCatId")
    public SubCatDTOResponse create(SubCatDTO subCatDTO, User user) {
        subCatRepository.findBySubCatNameIgnoreCase(subCatDTO.getSubCatName())
                .ifPresent(subCatEntity -> {
                    throw new DataIntegrityViolationException(
                            "This sub category '" + subCatDTO.getSubCatName() + "' is  already exists.");
                });

        Integer createdBy = userLoginServiceImpl.getAdminId(user);

        SubCatEntity subCatEntity = new SubCatEntity();
        subCatEntity.setCatId(subCatDTO.getCatId());
        subCatEntity.setSubCatName(subCatDTO.getSubCatName());
        subCatEntity.setStatus(subCatDTO.getStatus());
        subCatEntity.setCreatedBy(createdBy);
        subCatEntity = subCatRepository.save(subCatEntity);

        return mapToResponse(subCatEntity);
    }


    @Override
    @Cacheable(
            value = "subCatListCache",
            key = "#status == null || #status.isEmpty() || #status.equalsIgnoreCase('All') ? 'ALL' : #status",
            unless = "#result == null || #result.isEmpty()"
    )
    public List<SubCatDTOResponse> getList(String status) {
        boolean isAllStatus = status == null || status.isEmpty() || status.equalsIgnoreCase("All");

        List<SubCatEntity> subCatEntityList = isAllStatus ?
                subCatRepository.findAll() : subCatRepository.findByStatus(status);


        return subCatEntityList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    @CacheEvict(value = "subCatListCache", allEntries = true)
    public SubCatDTOResponse update(SubCatDTOUpdate subCatDTOUpdate, User user) {
        SubCatEntity subCatEntity = subCatRepository.findById(subCatDTOUpdate.getSubCatId())
                .orElseThrow(() -> new NoSuchElementException(
                        "This sub category id '" + subCatDTOUpdate.getCatId() + "' is not found."));

        if (subCatRepository.existsBySubCatNameIgnoreCaseAndSubCatIdNot(
                subCatDTOUpdate.getSubCatName(),
                subCatDTOUpdate.getSubCatId())) {
            throw new DataIntegrityViolationException(
                    "This category name '" + subCatDTOUpdate.getSubCatName() + "' already exists.");
        }

        Integer updatedBy = userLoginServiceImpl.getAdminId(user);

        modelMapper.map(subCatDTOUpdate, subCatEntity);
        subCatEntity.setUpdatedBy(updatedBy);
        subCatEntity.setUpdatedAt(new Date());
        subCatEntity = subCatRepository.save(subCatEntity);

        return mapToResponse(subCatEntity);
    }


    @Override
    @CacheEvict(value = "subCatListCache", allEntries = true)
    public SubCatDTOResponse updateStatus(Integer subCatId, String status, User user) {
        SubCatEntity subCatEntity = subCatRepository.findById(subCatId)
                .orElseThrow(() -> new NoSuchElementException(
                        "This sub category id '" + subCatId + "' is not found."));

        Integer updatedBy = userLoginServiceImpl.getAdminId(user);

        subCatEntity.setStatus(status);
        subCatEntity.setUpdatedAt(new Date());
        subCatEntity.setUpdatedBy(updatedBy);
        subCatEntity = subCatRepository.save(subCatEntity);

        return mapToResponse(subCatEntity);
    }


    @Override
    public List<SubCatDTOResponse> getListCatId(Integer catId) {
        List<SubCatEntity> subCatEntityList = subCatRepository.findByCatId(catId);

        if (subCatEntityList.isEmpty()) {
            throw new NoSuchElementException("This category id '" + catId + "' is not found.");
        }

        return subCatEntityList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    private SubCatDTOResponse mapToResponse(SubCatEntity subCatEntity) {
        SubCatDTOResponse response = modelMapper.map(subCatEntity, SubCatDTOResponse.class);
        response.setCreatedAt(formatTimestamp(subCatEntity.getCreatedAt()));
        response.setUpdatedAt(formatTimestamp(subCatEntity.getUpdatedAt()));
        return response;
    }
}