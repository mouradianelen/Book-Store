package com.example.bookstore.dto;

import com.example.bookstore.entity.UserEntity;
import com.opencsv.bean.CsvBindByPosition;

import java.util.List;
import java.util.stream.Collectors;

public class UserCSVDto {

    @CsvBindByPosition(position = 0)
    private long userId;
    @CsvBindByPosition(position = 1)
    private String location;
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
        if(age.startsWith("NULL"))
            this.setAge("18");
        else{
        this.age = age.substring(0,2);
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public static UserCSVDto mapUserEntityToUserDto(UserEntity userEntity) {
        UserCSVDto userCSVDto = new UserCSVDto();
        userCSVDto.setUserId(userEntity.getUserId());
        userCSVDto.setAge(String.valueOf(userEntity.getAge()));
        userCSVDto.setLocation(userEntity.getLocation());

        return userCSVDto;
    }

    public static List<UserCSVDto> mapUserEntityToUserDto(List<UserEntity> userEntities) {
        return userEntities.stream().map(UserCSVDto::mapUserEntityToUserDto).collect(Collectors.toList());
    }
    public static List<UserEntity> mapUserDtoToUserEntity(List<UserCSVDto> userCSVDtos){
        return userCSVDtos.stream().map(UserCSVDto::mapUserDtoToUserEntity).collect(Collectors.toList());
    }

    public static UserEntity mapUserDtoToUserEntity(UserCSVDto userCSVDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userCSVDto.getId());
        userEntity.setAge(Integer.parseInt(userCSVDto.getAge()));
        userEntity.setLocation(userCSVDto.getLocation());

        return userEntity;
    }
}
