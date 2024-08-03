package com.cgtfarmer.demo.service;

import com.cgtfarmer.demo.accessor.EnvironmentAccessor;
import com.cgtfarmer.demo.config.JdbcConfiguration;
import com.cgtfarmer.demo.exception.JdbcConfigCreationException;
import com.cgtfarmer.demo.factory.JdbcConfigFactory;
import com.cgtfarmer.demo.factory.JdbcConnectionFactory;
import com.cgtfarmer.demo.factory.LocalJdbcConfigFactory;
import com.cgtfarmer.demo.mapper.UserEntityMapper;
import com.cgtfarmer.demo.mapper.UserMapper;
import com.cgtfarmer.demo.model.User;
import com.cgtfarmer.demo.repository.UserEntityRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
class UserServiceTest {

  private final UserService userService;

  public UserServiceTest() throws JdbcConfigCreationException {
    EnvironmentAccessor environmentAccessor = new EnvironmentAccessor();

    JdbcConfigFactory jdbcConfigFactory = new LocalJdbcConfigFactory(environmentAccessor);

    JdbcConfiguration config = jdbcConfigFactory.create();

    JdbcConnectionFactory connectionFactory = new JdbcConnectionFactory(config);

    UserEntityMapper userEntityMapper = new UserEntityMapper();

    UserEntityRepository userRepository = new UserEntityRepository(connectionFactory, userEntityMapper);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.registerModule(new JavaTimeModule());

    UserMapper userMapper = new UserMapper(objectMapper);

    this.userService = new UserService(userRepository, userMapper);
  }

  @Test
  void findAll() {
    List<User> users = this.userService.findAll();

    System.out.println(users);
  }

  @Test
  void findById() {
    User user = this.userService.findById(1);

    System.out.println(user);
  }

  @Test
  void create() {
    User user = User.builder()
        .firstName("John")
        .lastName("Doe")
        .age(32)
        .weight(183.5F)
        .smoker(false)
        .build();

    User newUser = this.userService.create(user);

    System.out.println(newUser);
  }

  @Test
  void update() {
    User user = User.builder()
        .id(1)
        .firstName("Johnny")
        .lastName("Doe")
        .age(32)
        .weight(183.5F)
        .smoker(true)
        .build();

    User newUser = this.userService.update(user);

    System.out.println(newUser);
  }

  @Test
  void destroy() {
    this.userService.destroy(1);
  }
}
