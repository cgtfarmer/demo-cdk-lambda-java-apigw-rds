package com.cgtfarmer.demo.service;

import com.cgtfarmer.demo.dto.UserEntity;
import com.cgtfarmer.demo.mapper.UserMapper;
import com.cgtfarmer.demo.model.User;
import com.cgtfarmer.demo.repository.UserEntityRepository;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {

  private final UserEntityRepository userRepository;

  private final UserMapper userMapper;

  public UserService(UserEntityRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public List<User> findAll() {
    System.out.println("[UserService#findAll]");

    List<UserEntity> results = this.userRepository.findAll();

    List<User> users = results.stream()
        .map(e -> this.userMapper.fromUserEntity(e))
        .collect(Collectors.toList());

    return users;
  }

  public User findById(int id) {
    System.out.println("[UserService#findById] " + id);

    UserEntity result = this.userRepository.findById(id);

    User user = this.userMapper.fromUserEntity(result);

    return user;
  }

  public User create(User user) {
    System.out.println("[UserService#create] " + user);

    UserEntity userEntity = this.userMapper.toUserEntity(user);

    UserEntity result = this.userRepository.create(userEntity);

    User formattedResult = this.userMapper.fromUserEntity(result);

    return formattedResult;
  }

  public User update(User user) {
    System.out.println("[UserService#update] " + user);

    UserEntity userEntity = this.userMapper.toUserEntity(user);

    UserEntity result = this.userRepository.update(userEntity);

    User formattedResult = this.userMapper.fromUserEntity(result);

    return formattedResult;
  }

  public void destroy(int id) {
    System.out.println("[UserService#destroy] " + id);

    this.userRepository.destroy(id);
  }
}
