package com.cgtfarmer.demo.repository;

import com.cgtfarmer.demo.dto.UserEntity;
import com.cgtfarmer.demo.factory.JdbcConnectionFactory;
import com.cgtfarmer.demo.mapper.UserEntityMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserEntityRepository {

  private final JdbcConnectionFactory jdbcConnectionFactory;

  private final UserEntityMapper userEntityMapper;

  public UserEntityRepository(
      JdbcConnectionFactory jdbcConnectionFactory,
      UserEntityMapper userEntityMapper
  ) {
    this.jdbcConnectionFactory = jdbcConnectionFactory;
    this.userEntityMapper = userEntityMapper;
  }

  public List<UserEntity> findAll() {
    System.out.println("[UserRepository#findAll]");

    String sql = """
      SELECT *
      FROM users
    """;

    try (
      Connection connection = this.jdbcConnectionFactory.create();
      Statement statement = connection.createStatement()
    ) {
      ResultSet rs = statement.executeQuery(sql);

      List<UserEntity> userEntities = new ArrayList<>();

      while(rs.next()) {
        UserEntity userEntity = this.userEntityMapper.fromResultSet(rs);

        userEntities.add(userEntity);
      }

      return userEntities;
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
      Connection connection = this.jdbcConnectionFactory.create();
      PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();

      UserEntity userEntity = null;
      if (rs != null && rs.next()) {
        userEntity = this.userEntityMapper.fromResultSet(rs);
      }

      return userEntity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public UserEntity create(UserEntity userEntity) {
    System.out.println("[UserRepository#create] " + userEntity);

    String sql = """
      INSERT INTO users (first_name, last_name, age, weight, smoker)
      VALUES (?, ?, ?, ?, ?)
    """;

    try (
      Connection connection = this.jdbcConnectionFactory.create();
      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
    ) {
      statement.setString(1, userEntity.getFirst_name());
      statement.setString(2, userEntity.getLast_name());
      statement.setInt(3, userEntity.getAge());
      statement.setFloat(4, userEntity.getWeight());
      statement.setBoolean(5, userEntity.isSmoker());

      statement.executeUpdate();

      ResultSet rs = statement.getGeneratedKeys();
      if (rs != null && rs.next()) {
        userEntity.setId(rs.getInt(1));
      }

      return userEntity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public UserEntity update(UserEntity userEntity) {
    System.out.println("[UserRepository#update] " + userEntity);

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
      Connection connection = this.jdbcConnectionFactory.create();
      PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setString(1, userEntity.getFirst_name());
      statement.setString(2, userEntity.getLast_name());
      statement.setInt(3, userEntity.getAge());
      statement.setFloat(4, userEntity.getWeight());
      statement.setBoolean(5, userEntity.isSmoker());
      statement.setInt(6, userEntity.getId());

      statement.executeUpdate();

      return userEntity;
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
      Connection connection = this.jdbcConnectionFactory.create();
      PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setInt(1, id);

      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
