//package com.example.shopping_cart.service_impl;
//
//import com.example.shopping_cart.entity.CustomerEntity;
//import com.example.shopping_cart.repository.CustomerRepository;
//import com.example.shopping_cart.repository.UserRepository;
//import com.example.shopping_cart.request_dto.SignupDTO;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//@Transactional(readOnly = true)
//public class UserSignupServiceImpl {
//
//  private final UserRepository userRepository;
//  private final PasswordEncoder passwordEncoder;
//
//  public UserSignupServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//    this.userRepository = userRepository;
//    this.passwordEncoder = passwordEncoder;
//  }
//
//  @Transactional
//  public void signup(SignupDTO signupDTO) {
//    String aadhaarNo = signupDTO.aadhaarNo();
//    Optional<User> existingUser = userRepository.findByAadharNo(aadhaarNo);
//    if (existingUser.isPresent()) {
//      throw new RuntimeException(String.format("User with the aadhaarNo '%s' already exists.", aadhaarNo));
//    }
//
//    String hashedPassword = passwordEncoder.encode(signupDTO.pswd());
//    User user = new User(signupDTO.name(), aadhaarNo, hashedPassword);
//    userRepository.add(user);
//  }
//}