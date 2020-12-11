package com.steelerose.crm.service;

import com.steelerose.crm.model.User;
import com.steelerose.crm.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User save(UserRegistrationDto registration);

    List<User> findUsers();

    User updateRoles(Long id, String makeAdmin);
}
