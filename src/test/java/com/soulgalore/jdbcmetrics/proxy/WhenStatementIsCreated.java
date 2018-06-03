package com.soulgalore.jdbcmetrics.proxy;


import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class WhenStatementIsCreated extends AbstractDriverTest {

  private Connection connection;

  @Before
  public void setup() throws SQLException {
    connection = driver.connect(URL_JDBC_METRICS, null);
    assertThat(connection).isNotNull();
  }

  @Test
  public void statementShouldBeProxy() throws SQLException {
    Statement statement = connection.createStatement();
    assertThat(statement).isNotNull();
    assertThat(statement).isInstanceOf(Proxy.class);
    assertThat(Proxy.getInvocationHandler(statement)).isInstanceOf(StatementInvocationHandler.class);
  }

  @Test
  public void callableStatementShouldBeProxy() throws SQLException {
    CallableStatement statement = connection.prepareCall("SELECT 1");
    assertThat(statement).isNotNull();
    assertThat(statement).isInstanceOf(Proxy.class);
    assertThat(Proxy.getInvocationHandler(statement)).isInstanceOf(StatementInvocationHandler.class);
  }

  @Test
  public void preparedStatementShouldBeProxy() throws SQLException {
    PreparedStatement statement = connection.prepareStatement("SELECT 1");
    assertThat(statement).isNotNull();
    assertThat(statement).isInstanceOf(Proxy.class);
    assertThat(Proxy.getInvocationHandler(statement)).isInstanceOf(StatementInvocationHandler.class);
  }

}
