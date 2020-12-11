package com.steelerose.crm.service.impl;
import java.util.*;
import java.util.stream.Collectors;

import com.steelerose.crm.model.Client;
import com.steelerose.crm.model.Role;
import com.steelerose.crm.model.User;
import com.steelerose.crm.repository.UserRepository;
import com.steelerose.crm.service.UserService;
import com.steelerose.crm.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(UserRegistrationDto registration) {
        User user = new User();
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        return userRepository.save(user);
    }

    @Override
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateRoles(Long id, String type) {
        Optional<User> optional = userRepository.findById(id);
        User user;
        if (optional.isPresent()) {
            user = optional.get();
        } else {
            throw new RuntimeException(" Client not found for id :: " + id);
        }
        if (type.equals("makeAdmin")) {
            Collection<Role> roles = user.getRoles();
            roles.add(new Role("ADMIN"));
            user=  userRepository.save(user);
        } else if (type.equals("removeAdmin")) {
            Collection<Role> roles = user.getRoles();
            Role role1 = null;
            for (Role role: roles) {
                if (role.getName().equals("ADMIN")) {
                    role1 = role;
                }
            }
            roles.remove(role1);
            user= userRepository.save(user);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
              //  getAuthorities("ROLE_USER"));
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

    private Collection < ? extends GrantedAuthority > mapRolesToAuthorities(Collection < Role > roles) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;

        /*return roles.stream()
                .map(role - > new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());*/
    }
}