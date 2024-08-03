package com.cgtfarmer.demo.factory;

import com.cgtfarmer.demo.accessor.EnvironmentAccessor;
import com.cgtfarmer.demo.config.JdbcConfiguration;
import com.cgtfarmer.demo.exception.JdbcConfigCreationException;

public class LocalJdbcConfigFactory implements JdbcConfigFactory {

  private final EnvironmentAccessor environmentAccessor;

  public LocalJdbcConfigFactory(EnvironmentAccessor environmentAccessor) {
    this.environmentAccessor = environmentAccessor;
  }

  public JdbcConfiguration create() throws JdbcConfigCreationException {
    String jdbcUrl = this.environmentAccessor.get("DB_JDBC_URL");
    String username = this.environmentAccessor.get("DB_USERNAME");
    String password = this.environmentAccessor.get("DB_PASSWORD");

    return JdbcConfiguration.builder()
        .url(jdbcUrl)
        .username(username)
        .password(password)
        .build();
  }
}
