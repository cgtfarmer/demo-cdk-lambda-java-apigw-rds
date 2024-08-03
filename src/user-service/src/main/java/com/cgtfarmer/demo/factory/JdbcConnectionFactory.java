package com.cgtfarmer.demo.factory;

import com.cgtfarmer.demo.config.JdbcConfiguration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionFactory {

  private final JdbcConfiguration configuration;

  public JdbcConnectionFactory(JdbcConfiguration configuration) {
    this.configuration = configuration;
  }

  public Connection create() throws SQLException {
    return DriverManager.getConnection(
        configuration.getUrl(),
        configuration.getUsername(),
        configuration.getPassword()
    );
  }
}
