package com.cgtfarmer.demo.mapper;

import com.cgtfarmer.demo.entity.UserEntity;
import com.cgtfarmer.demo.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class UserMapper {

  private final ObjectMapper objectMapper;

  public UserMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public User fromString(String userString) throws JsonProcessingException {
    return this.objectMapper.readValue(userString, User.class);
  }

  public User fromUserEntity(UserEntity userEntity) {
    return User.builder()
        .id(userEntity.getId())
        .firstName(userEntity.getFirst_name())
        .lastName(userEntity.getLast_name())
        .age(userEntity.getAge())
        .weight(userEntity.getWeight())
        .smoker(userEntity.isSmoker())
        .build();
  }

  public String toString(User user) throws JsonProcessingException {
    return this.objectMapper.writeValueAsString(user);
  }

  public String toString(List<User> users) throws JsonProcessingException {
    return this.objectMapper.writeValueAsString(users);
  }

  public UserEntity toUserEntity(User user) {
    return UserEntity.builder()
        .id(user.getId())
        .first_name(user.getFirstName())
        .last_name(user.getLastName())
        .age(user.getAge())
        .weight(user.getWeight())
        .smoker(user.isSmoker())
        .build();
  }
}
