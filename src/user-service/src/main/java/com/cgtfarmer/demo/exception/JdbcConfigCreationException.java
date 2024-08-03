package com.cgtfarmer.demo.exception;

public class JdbcConfigCreationException extends Exception {

  public JdbcConfigCreationException(Throwable cause) {
    super("Failed to create JDBC configuration", cause);
  }
}
