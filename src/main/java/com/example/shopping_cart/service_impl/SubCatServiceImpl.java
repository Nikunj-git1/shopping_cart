package com.example.shopping_cart.service_impl;

import com.example.shopping_cart.entity.SubCatEntity;
import com.example.shopping_cart.repository.SubCatRepository;
import com.example.shopping_cart.request_dto.SubCatDTO;
import com.example.shopping_cart.request_dto.SubCatDTOResponse;
import com.example.shopping_cart.request_dto.SubCatDTOUpdate;
import com.example.shopping_cart.service.SubCatService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service

public class SubCatServiceImpl implements SubCatService {

    @Autowired
    private SubCatRepository subCatRepository;

    @Autowired
    private ModelMapper modelMapper;


    public SubCatDTOResponse create(SubCatDTO subCatDTO, Integer createdBy) {

        Optional<SubCatEntity> subCatEntityOptional = subCatRepository.findBysubCatName(subCatDTO.getSubCatName());

        if (!subCatEntityOptional.isEmpty()) {

        }

        SubCatEntity subCatEntity = modelMapper.map(subCatDTO, SubCatEntity.class);
       subCatEntity.setCreatedBy(createdBy);
        subCatEntity = subCatRepository.save(subCatEntity);

        return modelMapper.map(subCatEntity, SubCatDTOResponse.class);
    }

    public List<SubCatDTOResponse> getList(String status) {

        boolean isAllStatus = status == null || status.isEmpty() || status.equalsIgnoreCase("All");

        List<SubCatEntity> subCatEntityList = isAllStatus ?
                subCatRepository.findAll() : subCatRepository.findByStatus(status);

        List<SubCatDTOResponse> subCatDTOResponseList = new ArrayList<>();
        for (SubCatEntity subCatEntity : subCatEntityList) {
            subCatDTOResponseList.add(modelMapper.map(subCatEntity, SubCatDTOResponse.class));
        }

        return subCatDTOResponseList;
    }

    public SubCatDTOResponse update(SubCatDTOUpdate subCatDTOUpdate) {

        Optional<SubCatEntity> optionalSubCatEntity = subCatRepository.findById(subCatDTOUpdate.getSubCatId());
        if (optionalSubCatEntity.isEmpty()) {
        }

        boolean isDuplicate = subCatRepository.existsBySubCatNameIgnoreCaseAndSubCatIdNot(
                subCatDTOUpdate.getSubCatName(),
                subCatDTOUpdate.getSubCatId());
        if (isDuplicate) {

        }

        SubCatEntity subCatEntity = optionalSubCatEntity.get();
        modelMapper.map(subCatDTOUpdate, subCatEntity);
        subCatEntity = subCatRepository.save(subCatEntity);

        return modelMapper.map(subCatEntity, SubCatDTOResponse.class);
    }

    public SubCatDTOResponse updateStatus(int subCatId, String status) {

        Optional<SubCatEntity> optionalSubCateEntity = subCatRepository.findById(subCatId);

        if (optionalSubCateEntity.isEmpty()) {
        }

        SubCatEntity subCatEntity = optionalSubCateEntity.get();
        subCatEntity.setStatus(status);
        subCatEntity = subCatRepository.save(subCatEntity);

        return modelMapper.map(subCatEntity, SubCatDTOResponse.class);
    }

    public List<SubCatDTOResponse> getListCatId(int catId) {

        List<SubCatEntity> subCatEntityList = subCatRepository.findByCatId(catId);

        if (subCatEntityList.isEmpty()) {
        }

        List<SubCatDTOResponse> subCategoryDTOResponseList = new ArrayList<>();
        for (SubCatEntity subCatEntity : subCatEntityList) {
            subCategoryDTOResponseList.add(modelMapper.map(subCatEntity, SubCatDTOResponse.class));
        }
        return subCategoryDTOResponseList;
    }
}