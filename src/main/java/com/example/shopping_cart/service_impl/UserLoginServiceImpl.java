package com.example.shopping_cart.service_impl;


import com.example.shopping_cart.advice.MyCustomException;
import com.example.shopping_cart.entity.AdminEntity;
import com.example.shopping_cart.entity.CustEntity;
import com.example.shopping_cart.repository.AdminRepository;
import com.example.shopping_cart.repository.CustRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserLoginServiceImpl implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CustRepository custRepository;


    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
log.info("loadUserByUsername --------{}");
        Set<GrantedAuthority> set = new HashSet<>();

        Optional<AdminEntity> optionalAdminEntity = adminRepository.findByAdminName(name);

        if (optionalAdminEntity.isPresent()) {

            AdminEntity adminEntity = optionalAdminEntity.get();
            GrantedAuthority authority = new SimpleGrantedAuthority(adminEntity.getAdminRole());
            set.add(authority);

            return new User(name, adminEntity.getPswd(), set);
        }

        Optional<CustEntity> optionalCustEntity = custRepository.findByCustName(name);

        if (optionalCustEntity.isPresent()) {

        return new User(name, optionalCustEntity.get().getPswd(), set);
        }

        throw new MyCustomException("Account not found");

    }
}