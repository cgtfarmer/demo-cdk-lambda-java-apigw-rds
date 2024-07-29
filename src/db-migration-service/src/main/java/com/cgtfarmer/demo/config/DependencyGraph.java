package com.cgtfarmer.demo.config;

import com.cgtfarmer.demo.accessor.EnvironmentAccessor;
import com.cgtfarmer.demo.accessor.LambdaParameterSecretClient;
import com.cgtfarmer.demo.accessor.SecretAccessor;
import com.cgtfarmer.demo.factory.LiquibaseClientFactory;
import com.cgtfarmer.demo.factory.LiquibaseConfigFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.http.HttpClient;
import java.sql.SQLException;
import java.util.Objects;
import liquibase.Liquibase;
import liquibase.exception.DatabaseException;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DependencyGraph {

  private static DependencyGraph singleton;

  public static DependencyGraph getInstance()
      throws DatabaseException, InterruptedException, IOException, SQLException {

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

    LiquibaseConfiguration liquibaseConfiguration = new LiquibaseConfigFactory(
        environmentAccessor,
        secretAccessor
    ).create();

    Liquibase liquibaseClient = new LiquibaseClientFactory().create(liquibaseConfiguration);

    singleton = DependencyGraph.builder()
        .liquibaseClient(liquibaseClient)
        .build();

    return singleton;
  }

  private Liquibase liquibaseClient;
}
