package com.example.shopping_cart.service_impl;

import com.example.shopping_cart.advice.MyCustomException;
import com.example.shopping_cart.entity.AdminEntity;
import com.example.shopping_cart.repository.AdminRepository;
import com.example.shopping_cart.request_dto.AdminDTO;
import com.example.shopping_cart.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void signup(AdminDTO adminDTO) {
        adminRepository.findByAdminName(adminDTO.getAdminName())
                .ifPresent(adminEntity -> {
                    throw new MyCustomException(
                            "This admin name '" + adminDTO.getAdminName() + "' is  already exists.");
                });

        AdminEntity adminEntity = modelMapper.map(adminDTO, AdminEntity.class);
        adminEntity.setPswd(passwordEncoder.encode(adminDTO.getPswd()));
        adminRepository.save(adminEntity);
    }
}