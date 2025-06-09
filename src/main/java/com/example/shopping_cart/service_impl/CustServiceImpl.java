package com.example.shopping_cart.service_impl;

import com.example.shopping_cart.entity.CustEntity;
import com.example.shopping_cart.repository.CustRepository;
import com.example.shopping_cart.request_dto.CustDTO;
import com.example.shopping_cart.request_dto.CustDTOResponse;
import com.example.shopping_cart.request_dto.CustDTOUpdate;
import com.example.shopping_cart.service.CustService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service

public class CustServiceImpl implements CustService {

    @Autowired
    CustRepository custRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @CacheEvict(value = "custListCache", allEntries = true)
    public CustDTOResponse signup(CustDTO custDTO) {
        Optional<CustEntity> optionalCustEntity = custRepository.findByAadhaarNo(custDTO.getAadhaarNo());

        if (optionalCustEntity.isPresent()) {
            throw new DataIntegrityViolationException(
                    "This aadhaar no'" + custDTO.getAadhaarNo() + "' is already exists.");
        }

        CustEntity custEntity = modelMapper.map(custDTO, CustEntity.class);
        custEntity.setPswd(passwordEncoder.encode(custDTO.getPswd()));

        return modelMapper.map(custRepository.save(custEntity), CustDTOResponse.class);
    }

    @Override
    @Cacheable(value = "custListCache")
    public List<CustDTOResponse> getList() {
        List<CustEntity> custEntityList = custRepository.findAll();
        List<CustDTOResponse> custDTOResponseList = new ArrayList<>();

        for (CustEntity custEntity : custEntityList) {
            CustDTOResponse dto = new CustDTOResponse();

            dto.setCustId(custEntity.getCustId());
            dto.setCustName(custEntity.getCustName());
            dto.setAadhaarNo(custEntity.getAadhaarNo());
            dto.setAddress(custEntity.getAddress());
            dto.setCreatedAt(custEntity.getCreatedAt().toString());
            custDTOResponseList.add(dto);
        }

        return custDTOResponseList;
    }

    @Override
    @CacheEvict(value = "custListCache", allEntries = true)
    public CustDTOResponse update(CustDTOUpdate custDTOUpdate) {

        Optional<CustEntity> optionalCustEntity = custRepository.findById(custDTOUpdate.getCustId());
        if (optionalCustEntity.isEmpty()) {
            throw new NoSuchElementException(
                    "This customer id '" + custDTOUpdate.getCustId() + "' is not found.");
        }

        Optional<CustEntity> duplicateAadhaarNo = custRepository.findByAadhaarNo(custDTOUpdate.getAadhaarNo());
        if (duplicateAadhaarNo.isPresent() &&
                !duplicateAadhaarNo.get().getCustId().equals(custDTOUpdate.getCustId())) {
            throw new DataIntegrityViolationException(
                    "This aadhaar no'" + custDTOUpdate.getAadhaarNo() + "' is already exists.");
        }

        CustEntity custEntity = optionalCustEntity.get();
        custEntity.setCustName(custDTOUpdate.getCustName());
        custEntity.setAadhaarNo(custDTOUpdate.getAadhaarNo());
        custEntity.setAddress(custDTOUpdate.getAddress());

        custEntity = custRepository.save(custEntity);

        CustDTOResponse dto = new CustDTOResponse();
        dto.setCustId(custEntity.getCustId());
        dto.setCustName(custEntity.getCustName());
        dto.setAadhaarNo(custEntity.getAadhaarNo());
        dto.setAddress(custEntity.getAddress());
        dto.setCreatedAt(custEntity.getCreatedAt().toString());

        return dto;
    }

    @Override
    @CacheEvict(value = "custListCache", allEntries = true)
    public boolean delete(Integer custId) {

        Optional<CustEntity> optionalCustEntity = custRepository.findById(custId);
        if (optionalCustEntity.isEmpty()) {
            throw new NoSuchElementException("This customer id '" + custId + "' is not found.");

        }
        custRepository.deleteById(custId);
        return true;
    }
}