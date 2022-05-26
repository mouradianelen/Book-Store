package com.example.bookstore.service;

import com.example.bookstore.dto.UserDto;
import com.example.bookstore.entity.UserEntity;
import com.example.bookstore.entity.UserRole;
import com.example.bookstore.exceptions.EmailAlreadyExistsException;
import com.example.bookstore.repository.RoleRepository;
import com.example.bookstore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto registerNewUserAccount(UserDto userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new EmailAlreadyExistsException(userDTO.getEmail());
        }

        UserEntity user = UserDto.mapUserDtoToUserEntity(userDTO);

        UserRole role = new UserRole();
        role.setName("ROLE_USER");
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEnabled(true);
        user.setRole(role);
        roleRepository.save(role);
        userRepository.save(user);

        return UserDto.mapUserEntityToUserDto(user);
    }
}
