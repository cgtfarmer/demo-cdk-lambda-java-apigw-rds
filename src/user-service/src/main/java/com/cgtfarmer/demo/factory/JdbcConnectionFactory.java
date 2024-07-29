package com.cgtfarmer.demo.factory;

import com.cgtfarmer.demo.config.JdbcConfiguration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionFactory {

  public Connection create(JdbcConfiguration jdbcConfiguration)
      throws SQLException {

    Connection connection = DriverManager.getConnection(
        jdbcConfiguration.getUrl(),
        jdbcConfiguration.getUsername(),
        jdbcConfiguration.getPassword()
    );

    return connection;
  }
}
