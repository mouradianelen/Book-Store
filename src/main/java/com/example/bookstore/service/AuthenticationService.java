package com.example.bookstore.service;

import com.example.bookstore.dto.UserDto;
import com.example.bookstore.entity.UserEntity;
import com.example.bookstore.entity.UserRole;
import com.example.bookstore.exceptions.EmailAlreadyExistsException;
import com.example.bookstore.exceptions.UserNotFoundException;
import com.example.bookstore.exceptions.UsernameAlreadyExistsException;
import com.example.bookstore.repository.RoleRepository;
import com.example.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public UserDto registerNewUserAccount(UserDto userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new EmailAlreadyExistsException(userDTO.getEmail());
        }
        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new UsernameAlreadyExistsException(userDTO.getUsername());
        }

        UserEntity user = UserDto.mapUserDtoToUserEntity(userDTO);

        UserRole role = roleRepository.findByName("ROLE_USER");
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEnabled(true);
        user.setRole(role);
        roleRepository.save(role);
        userRepository.save(user);

        return UserDto.mapUserEntityToUserDto(user);
    }
    @Transactional
    public UserDto addNewAdmin(String username){
        UserEntity user = userRepository.findByUsername(username);
        if(user==null)
            throw new UserNotFoundException(username);
        UserRole role = roleRepository.findByName("ROLE_ADMIN");
        user.getRoles().add(role);
        return UserDto.mapUserEntityToUserDto(user);
    }
}
