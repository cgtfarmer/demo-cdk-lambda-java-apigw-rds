package com.cgtfarmer.demo.config;

import com.cgtfarmer.demo.accessor.EnvironmentAccessor;
import com.cgtfarmer.demo.accessor.LambdaParameterSecretClient;
import com.cgtfarmer.demo.accessor.SecretAccessor;
import com.cgtfarmer.demo.controller.UserController;
import com.cgtfarmer.demo.factory.JdbcConnectionFactory;
import com.cgtfarmer.demo.mapper.UserMapper;
import com.cgtfarmer.demo.repository.UserRepository;
import com.cgtfarmer.demo.service.UserService;
import com.cgtfarmer.demo.factory.JdbcConfigFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.http.HttpClient;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DependencyGraph {

  private static DependencyGraph singleton;

  public static DependencyGraph getInstance()
      throws InterruptedException, IOException, SQLException {

    if (!Objects.isNull(singleton)) return singleton;

    EnvironmentAccessor environmentAccessor = new EnvironmentAccessor();

    HttpClient httpClient = HttpClient.newHttpClient();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.registerModule(new JavaTimeModule());

    LambdaParameterSecretClient lambdaParameterSecretClient =
        new LambdaParameterSecretClient(httpClient, objectMapper);

    SecretAccessor secretAccessor = new SecretAccessor(
        objectMapper,
        lambdaParameterSecretClient
    );

    JdbcConfiguration jdbcConfiguration = new JdbcConfigFactory(
        environmentAccessor,
        secretAccessor
    ).create();

    Connection connection = new JdbcConnectionFactory().create(jdbcConfiguration);

    UserRepository userRepository = new UserRepository(connection);

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
