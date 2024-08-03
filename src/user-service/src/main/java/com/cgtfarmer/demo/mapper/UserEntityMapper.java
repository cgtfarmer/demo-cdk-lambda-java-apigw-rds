package com.cgtfarmer.demo.mapper;

import com.cgtfarmer.demo.dto.UserEntity;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserEntityMapper {

  public UserEntity fromResultSet(ResultSet resultSet) throws SQLException {
    return UserEntity.builder()
        .id(resultSet.getInt("id"))
        .age(resultSet.getInt("age"))
        .first_name(resultSet.getString("first_name"))
        .last_name(resultSet.getString("last_name"))
        .weight(resultSet.getFloat("weight"))
        .smoker(resultSet.getBoolean("smoker"))
        .build();
  }
}
