package com.example.shopping_cart.service_impl;

import com.example.shopping_cart.advice.MyCustomException;
import com.example.shopping_cart.entity.AdminEntity;
import com.example.shopping_cart.repository.AdminRepository;
import com.example.shopping_cart.request_dto.AdminDTO;
import com.example.shopping_cart.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

@Slf4j

public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void signup(AdminDTO adminDTO) {

        Optional<AdminEntity> existingName = adminRepository.findByAdminName(adminDTO.getAdminName());
        if (existingName.isPresent()) {
            throw new MyCustomException(
                    "This admin name '" + adminDTO.getAdminName() + "' is  already exists.");
        }

        AdminEntity adminEntity = modelMapper.map(adminDTO, AdminEntity.class);
        adminEntity.setPswd(passwordEncoder.encode(adminDTO.getPswd()));
        adminRepository.save(adminEntity);
    }
}