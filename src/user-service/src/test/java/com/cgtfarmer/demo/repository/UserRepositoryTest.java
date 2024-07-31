package com.cgtfarmer.demo.repository;

import com.cgtfarmer.demo.config.JdbcConfiguration;
import com.cgtfarmer.demo.entity.UserEntity;
import com.cgtfarmer.demo.factory.JdbcConnectionFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class UserRepositoryTest {
  JdbcConfiguration config = JdbcConfiguration.builder()
    .url("jdbc:postgresql://postgres:5432/postgres")
    .username("postgres")
    .password("super")
    .build();
  UserRepository userRepository = new UserRepository(new JdbcConnectionFactory(), config);
  @Test
  void findAll() {
    System.out.println(this.userRepository.findAll());
  }

  @Nested
  class FindById {
    @Test
    void whenResourceIsFound_ReturnsUserEntity() {
      UserEntity expected = UserEntity.builder()
        .id(1).first_name("Ryan").last_name("Test").age(24).weight(180.0F).build();
      UserEntity actual = userRepository.findById(1);
      assertEquals(expected, actual);
    }

    @Test
    void whenResourceIsNotFound_ReturnsException() {
      assertThrows(
        RuntimeException.class,
        () -> userRepository.findById(1000)
      );
    }
  }

  @Nested
  class Create {
    @Test
    void returnsCreatedUserEntity() {
      UserEntity expected = UserEntity.builder()
        .id(2).first_name("Christian").last_name("Test").age(29).weight(180.5F).build();
      UserEntity actual = userRepository.create(expected);
      assertEquals(expected, actual);
    }
  }

  @Nested
  class Update {

    @Test
    void whenResourceIsFound_ReturnsUpdatedUserEntity() {
      UserEntity expected = UserEntity.builder()
        .id(2).first_name("Ryan").last_name("Test").age(24).weight(180.5F).build();
      UserEntity actual = userRepository.update(expected);
      assertEquals(expected, actual);
    }

    @Test
    void whenResourceIsNotFound_ReturnsException() {
      UserEntity expected = UserEntity.builder()
        .id(1000).first_name("Christian").last_name("Test").age(29).weight(180.5F).build();
      assertThrows(
        RuntimeException.class,
        () -> userRepository.update(expected)
      );
    }
  }

  @Nested
  class Destroy {
    @Test
    void whenResourceIsFound_DeletesUserEntity() {
      assertDoesNotThrow(()-> userRepository.destroy(2));
    }

    @Test
    void whenResourceIsNotFound_ReturnsException() {
      assertThrows(
        RuntimeException.class,
        () -> userRepository.destroy(1000)
      );
    }
  }
}
