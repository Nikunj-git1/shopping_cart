package com.example.shopping_cart.service_impl;

import com.example.shopping_cart.entity.CustEntity;
import com.example.shopping_cart.repository.CustRepository;
import com.example.shopping_cart.request_dto.CustDTO;
import com.example.shopping_cart.request_dto.CustDTOResponse;
import com.example.shopping_cart.request_dto.CustDTOUpdate;
import com.example.shopping_cart.service.CustService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.example.shopping_cart.util.CustomizeDateFormat.formatTimestamp;

@Service
public class CustServiceImpl implements CustService {

    @Autowired
    private CustRepository custRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @CacheEvict(value = "custListCache", allEntries = true)
    public CustDTOResponse signup(CustDTO custDTO) {
        custRepository.findByAadhaarNo(custDTO.getAadhaarNo())
                .ifPresent(custEntity -> {
                    throw new DataIntegrityViolationException(
                            "This aadhaar no '" + custDTO.getAadhaarNo() + "' already exists.");
                });

        CustEntity custEntity = modelMapper.map(custDTO, CustEntity.class);
        custEntity.setPswd(passwordEncoder.encode(custDTO.getPswd()));
        custEntity = custRepository.save(custEntity);

        return mapEntityToResponse(custEntity);
    }


    @Override
    @Cacheable(value = "custListCache")
    public List<CustDTOResponse> getList() {
        List<CustEntity> custEntityList = custRepository.findAll();

        return custEntityList.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }


    @Override
    @CacheEvict(value = "custListCache", allEntries = true)
    public CustDTOResponse update(CustDTOUpdate custDTOUpdate) {
        CustEntity custEntity = custRepository.findById(custDTOUpdate.getCustId())
                .orElseThrow(() -> new NoSuchElementException(
                        "This customer id '" + custDTOUpdate.getCustId() + "' is not found."));

        if (custRepository.existsByAadhaarNoIgnoreCaseAndCustIdNot(
                custDTOUpdate.getAadhaarNo(),
                custDTOUpdate.getCustId())) {
            throw new DataIntegrityViolationException(
                    "This aadhaar no '" + custDTOUpdate.getAadhaarNo() + "' already exists.");
        }

        modelMapper.map(custDTOUpdate, custEntity);
        custEntity.setPswd(passwordEncoder.encode(custDTOUpdate.getPswd()));
        custEntity.setUpdatedAt(new Date());
        custEntity = custRepository.save(custEntity);

        return mapEntityToResponse(custEntity);
    }


    @Override
    @CacheEvict(value = "custListCache", allEntries = true)
    public boolean delete(Integer custId) {
        CustEntity custEntity = custRepository.findById(custId)
                .orElseThrow(() -> new NoSuchElementException(
                        "This customer id '" + custId + "' is not found."));

        custRepository.deleteById(custId);
        return true;
    }


    /**
     * Helper method to map entity to response DTO with formatted dates.
     */
    private CustDTOResponse mapEntityToResponse(CustEntity custEntity) {
        CustDTOResponse response = modelMapper.map(custEntity, CustDTOResponse.class);
        response.setCreatedAt(formatTimestamp(custEntity.getCreatedAt()));
        response.setUpdatedAt(formatTimestamp(custEntity.getUpdatedAt()));
        return response;
    }
}