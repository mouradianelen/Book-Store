package com.example.bookstore.dto;

import com.example.bookstore.entity.UserEntity;
import com.example.bookstore.entity.UserRole;
import com.sun.istack.NotNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class UserDto {
    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    private String password;
    @Email
    @NotNull
    @NotEmpty
    private String email;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDTO = (UserDto) o;
        return Objects.equals(password, userDTO.password) && Objects.equals(email, userDTO.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, email);
    }

    public static UserDto mapUserEntityToUserDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setEmail(userEntity.getEmail());
        userDto.setName(userEntity.getName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setUsername(userEntity.getUsername());

        return userDto;
    }

    public static List<UserDto> mapUserEntityToUserDto(List<UserEntity> userEntities) {
        return userEntities.stream().map(UserDto::mapUserEntityToUserDto).collect(Collectors.toList());
    }
    public static List<UserEntity> mapUserDtoToUserEntity(List<UserDto> userDtos){
        return userDtos.stream().map(UserDto::mapUserDtoToUserEntity).collect(Collectors.toList());
    }

    public static UserEntity mapUserDtoToUserEntity(UserDto userDto) {
        UserEntity user = new UserEntity();
        user.setName(userDto.getName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        return user;
    }

}
