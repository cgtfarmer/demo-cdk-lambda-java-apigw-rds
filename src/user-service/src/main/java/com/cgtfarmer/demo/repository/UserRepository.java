package com.cgtfarmer.demo.repository;

import com.cgtfarmer.demo.config.JdbcConfiguration;
import com.cgtfarmer.demo.entity.UserEntity;
import com.cgtfarmer.demo.factory.JdbcConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;

public class UserRepository {

  private final JdbcConnectionFactory jdbcConnectionFactory;
  private final JdbcConfiguration jdbcConfiguration;

  public UserRepository(
    JdbcConnectionFactory jdbcConnectionFactory,
    JdbcConfiguration jdbcConfiguration
  ) {
    this.jdbcConnectionFactory = jdbcConnectionFactory;
    this.jdbcConfiguration = jdbcConfiguration;
  }

  public List<UserEntity> findAll() {
    System.out.println("[UserRepository#findAll]");

    String sql = """
      SELECT *
      FROM users
    """;

    try (
      Connection connection = this.jdbcConnectionFactory.create(this.jdbcConfiguration);
      Statement statement = connection.createStatement()
    ) {
      ResultSet rs = statement.executeQuery(sql);

      List<UserEntity> users = new ArrayList<>();

      while(rs.next()) {
        UserEntity user = UserEntity.builder()
          .id(rs.getInt("id"))
          .age(rs.getInt("age"))
          .first_name(rs.getString("first_name"))
          .last_name(rs.getString("last_name"))
          .weight(rs.getFloat("weight"))
          .smoker(rs.getBoolean("smoker"))
          .build();
        users.add(user);
      }

      return users;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public UserEntity findById(int id) {
    System.out.println("[UserRepository#findById] " + id);

    String sql = """
      SELECT *
      FROM users
      WHERE id = ?
    """;

    try (
      Connection connection = this.jdbcConnectionFactory.create(this.jdbcConfiguration);
      PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();

      if (rs != null && rs.next()) {
        return UserEntity.builder()
          .id(rs.getInt("id"))
          .age(rs.getInt("age"))
          .first_name(rs.getString("first_name"))
          .last_name(rs.getString("last_name"))
          .weight(rs.getFloat("weight"))
          .smoker(rs.getBoolean("smoker"))
          .build();
      }

      throw new RuntimeException("User not found(id=" + id + ")");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public UserEntity create(UserEntity user) {
    System.out.println("[UserRepository#create] " + user);

    String sql = """
      INSERT INTO users (first_name, last_name, age, weight, smoker)
      VALUES (?, ?, ?, ?, ?)
    """;
//      RETURNING id
//    """;

    try (
      Connection connection = this.jdbcConnectionFactory.create(this.jdbcConfiguration);
      PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setString(1, user.getFirst_name());
      statement.setString(2, user.getLast_name());
      statement.setInt(3, user.getAge());
      statement.setFloat(4, user.getWeight());
      statement.setBoolean(5, user.isSmoker());
      statement.executeUpdate();

      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public UserEntity update(UserEntity user) {
    System.out.println("[UserRepository#update] " + user);

    String sql = """
      UPDATE users
      SET first_name = ?,
          last_name = ?,
          age = ?,
          weight = ?,
          smoker = ?
      WHERE id = ?
    """;

    try (
      Connection connection = this.jdbcConnectionFactory.create(this.jdbcConfiguration);
      PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setString(1, user.getFirst_name());
      statement.setString(2, user.getLast_name());
      statement.setInt(3, user.getAge());
      statement.setFloat(4, user.getWeight());
      statement.setBoolean(5, user.isSmoker());
      statement.setInt(6, user.getId());
      ResultSet rs = statement.executeQuery();

      if (rs != null && rs.next()) {
        return UserEntity.builder()
          .id(rs.getInt("id"))
          .age(rs.getInt("age"))
          .first_name(rs.getString("first_name"))
          .last_name(rs.getString("last_name"))
          .weight(rs.getFloat("weight"))
          .smoker(rs.getBoolean("smoker"))
          .build();
      }

      throw new RuntimeException("User not found(id=" + user.getId() + ")");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void destroy(int id) {
    System.out.println("[UserRepository#destroy] " + id);

    String sql = """
      DELETE FROM users
      WHERE id = ?
    """;

    try (
      Connection connection = this.jdbcConnectionFactory.create(this.jdbcConfiguration);
      PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setInt(1, id);
      int result = statement.executeUpdate();

      if (result > 0) {
        return;
      }

      throw new RuntimeException("User not found(id=" + id + ")");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
