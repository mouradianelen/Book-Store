package com.example.bookstore.dto;

import com.example.bookstore.entity.UserEntity;
import com.opencsv.bean.CsvBindByPosition;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class UserDto {
    @NotNull
    @CsvBindByPosition(position = 0)
    private long userId;
    @NotNull
    @CsvBindByPosition(position = 1)
    private String location;
    @NotNull
    @CsvBindByPosition(position = 2)
    private String age;
    public long getId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAge() {
        return age;
    }


    public void setAge(String age){
        if(age.equals("NULL"))
            this.setAge("18");
        else{
        this.age = age;
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public static UserDto mapUserEntityToUserDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setUserId(userEntity.getUserId());
        userDto.setAge(String.valueOf(userEntity.getAge()));
        userDto.setLocation(userEntity.getLocation());

        return userDto;
    }

    public static List<UserDto> mapUserEntityToUserDto(List<UserEntity> userEntities) {
        return userEntities.stream().map(UserDto::mapUserEntityToUserDto).collect(Collectors.toList());
    }
    public static List<UserEntity> mapUserDtoToUserEntity(List<UserDto> userDtos){
        return userDtos.stream().map(UserDto::mapUserDtoToUserEntity).collect(Collectors.toList());
    }

    public static UserEntity mapUserDtoToUserEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userDto.getId());
        userEntity.setAge(Integer.parseInt(userDto.getAge()));
        userEntity.setLocation(userDto.getLocation());

        return userEntity;
    }
}
