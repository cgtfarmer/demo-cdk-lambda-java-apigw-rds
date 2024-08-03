package com.cgtfarmer.demo.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

public class UserMapperTest {

  private final UserMapper userMapper;

  public UserMapperTest() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.registerModule(new JavaTimeModule());

    this.userMapper = new UserMapper(objectMapper);
  }

  @Test
  void fromString() throws Exception {
    this.userMapper.fromString("{\"firstName\":\"John\"}");
  }
}
