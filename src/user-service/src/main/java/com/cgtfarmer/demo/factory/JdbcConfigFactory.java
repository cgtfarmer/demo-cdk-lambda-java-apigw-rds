package com.cgtfarmer.demo.factory;

import com.cgtfarmer.demo.config.JdbcConfiguration;
import com.cgtfarmer.demo.exception.JdbcConfigCreationException;

public interface JdbcConfigFactory {

  JdbcConfiguration create() throws JdbcConfigCreationException;
}
