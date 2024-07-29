package com.cgtfarmer.demo.repository;

import com.cgtfarmer.demo.entity.UserEntity;
import java.sql.Connection;
import java.util.List;

public class UserRepository {

  private final Connection connection;

  public UserRepository(Connection connection) {
    this.connection = connection;
  }

  public List<UserEntity> findAll() {
    System.out.println("[UserRepository#findAll]");

    String sql = """
      SELECT *
      FROM users
    """;

    // const results: UserEntity[] = await this.dbClient.executeStatement(sql);

    return null;
  }

  public UserEntity findById(int id) {
    System.out.println("[UserRepository#findById] " + id);

    String sql = """
      SELECT *
      FROM users
      WHERE id = $1
    """;

    // const values = [String(id)];

    // const results: UserEntity[] = await this.dbClient.executeStatementWithParams(sql, values);

    return null;
  }

  public UserEntity create(UserEntity user) {
    System.out.println("[UserRepository#create] " + user);

    String sql = """
      INSERT INTO users (first_name, last_name, age, weight, smoker)
      VALUES ($1, $2, $3, $4, $5)
      RETURNING id
    """;

    // const values = [
    //   user.first_name,
    //   user.last_name,
    //   String(user.age),
    //   String(user.weight),
    //   String(user.smoker),
    // ];

    // const results: UserEntity[] = await this.dbClient.executeStatementWithParams(sql, values);

    // user.id = results[0].id;

    return null;
  }

  public UserEntity update(UserEntity user) {
    System.out.println("[UserRepository#update] " + user);

    String sql = """
      UPDATE users
      SET first_name = $1,
          last_name = $2,
          age = $3,
          weight = $4,
          smoker = $5
      WHERE id = $6
    """;

    // const values = [
    //   user.first_name,
    //   user.last_name,
    //   String(user.age),
    //   String(user.weight),
    //   String(user.smoker),
    //   String(user.id)
    // ];

    // await this.dbClient.executeStatementWithParams(sql, values);

    return null;
  }

  public void destroy(int id) {
    System.out.println("[UserRepository#destroy] " + id);

    String sql = """
      DELETE FROM users
      WHERE id = $1
    """;

    // const values = [String(id)];

    // await this.dbClient.executeStatementWithParams(sql, values);
  }
}
