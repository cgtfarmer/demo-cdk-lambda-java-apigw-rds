package com.cgtfarmer.demo.factory;

import com.cgtfarmer.demo.accessor.EnvironmentAccessor;
import com.cgtfarmer.demo.accessor.SecretAccessor;
import com.cgtfarmer.demo.config.JdbcConfiguration;
import com.cgtfarmer.demo.dto.DbSecret;
import com.cgtfarmer.demo.exception.JdbcConfigCreationException;
import java.io.IOException;

public class DeployedJdbcConfigFactory implements JdbcConfigFactory {

  private final EnvironmentAccessor environmentAccessor;

  private final SecretAccessor secretAccessor;

  public DeployedJdbcConfigFactory(
      EnvironmentAccessor environmentAccessor,
      SecretAccessor secretAccessor
  ) {
    this.environmentAccessor = environmentAccessor;
    this.secretAccessor = secretAccessor;
  }

  public JdbcConfiguration create() throws JdbcConfigCreationException {
    String awsSessionToken = this.environmentAccessor.get("AWS_SESSION_TOKEN");
    String credsSecretId = this.environmentAccessor.get("DB_CREDS_SECRET_ID");

    DbSecret dbSecret = null;
    try {
      dbSecret = this.secretAccessor.getDbSecret(awsSessionToken, credsSecretId);
    } catch (InterruptedException | IOException e) {
      throw new JdbcConfigCreationException(e);
    }

    String jdbcUrl = this.environmentAccessor.get("DB_JDBC_URL");

    return JdbcConfiguration.builder()
        .url(jdbcUrl)
        .username(dbSecret.getUsername())
        .password(dbSecret.getPassword())
        .build();
  }
}
