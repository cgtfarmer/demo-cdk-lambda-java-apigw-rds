package com.cgtfarmer.demo.repository;

import com.cgtfarmer.demo.accessor.EnvironmentAccessor;
import com.cgtfarmer.demo.config.JdbcConfiguration;
import com.cgtfarmer.demo.dto.UserEntity;
import com.cgtfarmer.demo.exception.JdbcConfigCreationException;
import com.cgtfarmer.demo.factory.JdbcConfigFactory;
import com.cgtfarmer.demo.factory.JdbcConnectionFactory;
import com.cgtfarmer.demo.factory.LocalJdbcConfigFactory;
import com.cgtfarmer.demo.mapper.UserEntityMapper;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
class UserRepositoryTest {

  private final UserEntityRepository userRepository;

  public UserRepositoryTest() throws JdbcConfigCreationException {
    EnvironmentAccessor environmentAccessor = new EnvironmentAccessor();

    JdbcConfigFactory jdbcConfigFactory = new LocalJdbcConfigFactory(environmentAccessor);

    JdbcConfiguration config = jdbcConfigFactory.create();

    JdbcConnectionFactory connectionFactory = new JdbcConnectionFactory(config);

    UserEntityMapper userEntityMapper = new UserEntityMapper();

    this.userRepository = new UserEntityRepository(connectionFactory, userEntityMapper);
  }

  @Test
  void findAll() {
    List<UserEntity> userEntities = this.userRepository.findAll();

    System.out.println(userEntities);
  }

  @Test
  void findById() {
    UserEntity userEntity = userRepository.findById(1);

    System.out.println(userEntity);
  }

  @Test
  void create() {
    UserEntity userEntity = UserEntity.builder()
        .first_name("John")
        .last_name("Doe")
        .age(32)
        .weight(183.5F)
        .build();

    UserEntity newUserEntity = userRepository.create(userEntity);

    System.out.println(newUserEntity);
  }

  @Test
  void update() {
    UserEntity userEntity = UserEntity.builder()
        .id(1)
        .first_name("Johnny")
        .last_name("Doe")
        .age(32)
        .weight(183.5F)
        .build();

    UserEntity newUserEntity = userRepository.update(userEntity);

    System.out.println(newUserEntity);
  }

  @Test
  void destroy() {
    userRepository.destroy(1);
  }
}
