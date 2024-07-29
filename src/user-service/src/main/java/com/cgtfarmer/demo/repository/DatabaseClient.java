package com.cgtfarmer.demo.repository;

public interface DatabaseClient<T> {

  void initConnection();

  executeStatement(sql: string): Promise<any[]>;

  executeStatementWithParams(sql: string, params: string[]): Promise<any[]>;

  endConnection(): void;
}
