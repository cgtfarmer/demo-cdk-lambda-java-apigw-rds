package com.cgtfarmer.demo.config;

import com.cgtfarmer.demo.accessor.EnvironmentAccessor;
import com.cgtfarmer.demo.accessor.LambdaParameterSecretClient;
import com.cgtfarmer.demo.accessor.SecretAccessor;
import com.cgtfarmer.demo.controller.UserController;
import com.cgtfarmer.demo.exception.JdbcConfigCreationException;
import com.cgtfarmer.demo.factory.JdbcConnectionFactory;
import com.cgtfarmer.demo.factory.LocalJdbcConfigFactory;
import com.cgtfarmer.demo.mapper.UserEntityMapper;
import com.cgtfarmer.demo.mapper.UserMapper;
import com.cgtfarmer.demo.repository.UserEntityRepository;
import com.cgtfarmer.demo.service.UserService;
import com.cgtfarmer.demo.factory.DeployedJdbcConfigFactory;
import com.cgtfarmer.demo.factory.JdbcConfigFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.http.HttpClient;
import java.sql.SQLException;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DependencyGraph {

  private static DependencyGraph singleton;

  public static DependencyGraph getInstance()
      throws InterruptedException, IOException, JdbcConfigCreationException, SQLException {

    if (!Objects.isNull(singleton)) return singleton;

    EnvironmentAccessor environmentAccessor = new EnvironmentAccessor();

    HttpClient httpClient = HttpClient.newHttpClient();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.registerModule(new JavaTimeModule());

    LambdaParameterSecretClient lambdaParameterSecretClient =
        new LambdaParameterSecretClient(httpClient, objectMapper);

    String environment = environmentAccessor.get("ENV");

    JdbcConfigFactory jdbcConfigFactory = null;
    if ("local".equalsIgnoreCase(environment)) {
      jdbcConfigFactory = new LocalJdbcConfigFactory(environmentAccessor);
    } else {
      SecretAccessor secretAccessor = new SecretAccessor(objectMapper, lambdaParameterSecretClient);

      jdbcConfigFactory = new DeployedJdbcConfigFactory(environmentAccessor, secretAccessor);
    }

    JdbcConfiguration jdbcConfiguration = jdbcConfigFactory.create();

    JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactory(jdbcConfiguration);

    UserEntityMapper userEntityMapper = new UserEntityMapper();

    UserEntityRepository userRepository = new UserEntityRepository(jdbcConnectionFactory, userEntityMapper);

    UserMapper userMapper = new UserMapper(objectMapper);

    UserService userService = new UserService(userRepository, userMapper);

    UserController userController = new UserController(userService, userMapper);

    singleton = DependencyGraph.builder()
        .userController(userController)
        .build();

    return singleton;
  }

  private UserController userController;
}
