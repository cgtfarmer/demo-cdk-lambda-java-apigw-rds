package com.cgtfarmer.demo.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JdbcConfiguration {

  private String url;

  private String username;

  private String password;
}
