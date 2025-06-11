package com.example.shopping_cart.service_impl;

import com.example.shopping_cart.entity.SubCatEntity;
import com.example.shopping_cart.repository.AdminRepository;
import com.example.shopping_cart.repository.SubCatRepository;
import com.example.shopping_cart.request_dto.SubCatDTO;
import com.example.shopping_cart.request_dto.SubCatDTOResponse;
import com.example.shopping_cart.request_dto.SubCatDTOUpdate;
import com.example.shopping_cart.service.SubCatService;
import com.example.shopping_cart.util.CustomizeDateFormat;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service

public class SubCatServiceImpl implements SubCatService {

    @Autowired
    private SubCatRepository subCatRepository;

    @Autowired
    private UserLoginServiceImpl userLoginService;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    @CacheEvict(value = "catListCache", allEntries = true)
    public SubCatDTOResponse create(SubCatDTO subCatDTO, User user) {
        subCatRepository.findBySubCatNameIgnoreCase(subCatDTO.getSubCatName())
                .ifPresent(existing -> {
                    throw new DataIntegrityViolationException(
                            "This sub category '" + subCatDTO.getSubCatName() + "' already exists.");
                });

        Integer createdBy = userLoginService.getAdminId(user);

        SubCatEntity subCatEntity = new SubCatEntity();
        subCatEntity.setCatId(subCatDTO.getCatId());
        subCatEntity.setSubCatName(subCatDTO.getSubCatName());
        subCatEntity.setStatus(subCatDTO.getStatus());
        subCatEntity.setCreatedBy(createdBy);

        subCatEntity = subCatRepository.save(subCatEntity);

        SubCatDTOResponse subCatDTOResponse = modelMapper.map(subCatEntity, SubCatDTOResponse.class);
        String formattedDate = CustomizeDateFormat.formatTimestamp(subCatEntity.getCreatedAt());
        subCatDTOResponse.setCreatedAt(formattedDate);

        return subCatDTOResponse;
    }

    @Override
    @Cacheable(value = "catListCache",
            key = "#status == null || #status.isEmpty() || #status.equalsIgnoreCase('All') ? 'ALL' : #status",
            unless = "#result == null || #result.isEmpty()")
    public List<SubCatDTOResponse> getList(String status) {
        boolean isAllStatus = status == null || status.isEmpty() || status.equalsIgnoreCase("All");

        List<SubCatEntity> subCatEntityList = isAllStatus
                ? subCatRepository.findAll() : subCatRepository.findByStatus(status);

        return subCatEntityList.stream()
                .map(subCatEntity -> modelMapper
                        .map(subCatEntity, SubCatDTOResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "catListCache", allEntries = true)
    public SubCatDTOResponse update(SubCatDTOUpdate subCatDTOUpdate) {
        SubCatEntity subCatEntity = subCatRepository.findById(subCatDTOUpdate.getSubCatId())
                .orElseThrow(() -> new NoSuchElementException(
                        "This sub category '" + subCatDTOUpdate.getCatId() + "' is not found."));

        if (subCatRepository.existsBySubCatNameIgnoreCaseAndSubCatIdNot(
                subCatDTOUpdate.getSubCatName(), subCatDTOUpdate.getSubCatId())) {
            throw new DataIntegrityViolationException(
                    "This sub category name '" + subCatDTOUpdate.getSubCatName() + "' is already exists.");
        }

        modelMapper.map(subCatDTOUpdate, subCatEntity);
        subCatEntity = subCatRepository.save(subCatEntity);

        return modelMapper.map(subCatEntity, SubCatDTOResponse.class);
    }

    @Override
    @CacheEvict(value = "catListCache", allEntries = true)
    public SubCatDTOResponse updateStatus(int subCatId, String status) {
        SubCatEntity subCatEntity = subCatRepository.findById(subCatId)
                .orElseThrow(() -> new NoSuchElementException(
                        "This sub category id '" + subCatId + "' is not found."));

        subCatEntity.setStatus(status);
        subCatEntity = subCatRepository.save(subCatEntity);

        return modelMapper.map(subCatEntity, SubCatDTOResponse.class);
    }

    @Override
    @Cacheable(value = "sub-catListCache")
    public List<SubCatDTOResponse> getListCatId(int catId) {
        List<SubCatEntity> subCatEntityList = subCatRepository.findByCatId(catId);

        if (subCatEntityList.isEmpty()) {
            throw new NoSuchElementException("This category id '" + catId + "' is not found.");
        }

        List<SubCatDTOResponse> subCatDTOResponseList = new ArrayList<>();
        for (SubCatEntity subCatEntity : subCatEntityList) {
           SubCatDTOResponse subCatDTOResponse = modelMapper.map(subCatEntity, SubCatDTOResponse.class);
            subCatDTOResponseList.add(subCatDTOResponse);
        }
        return subCatDTOResponseList;
    }
}