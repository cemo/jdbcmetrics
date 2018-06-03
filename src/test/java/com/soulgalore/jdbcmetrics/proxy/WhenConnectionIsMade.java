package com.soulgalore.jdbcmetrics.proxy;


import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WhenConnectionIsMade extends AbstractDriverTest {

  @Before
  public void setup() throws SQLException {}

  @Test
  public void connectionShouldBeProxy() throws SQLException {
    Connection connection = driver.connect(URL_JDBC_METRICS, null);
    assertThat(connection).isNotNull();
    assertThat(connection).isInstanceOf(Proxy.class);
    assertThat(Proxy.getInvocationHandler(connection)).isInstanceOf(ConnectionInvocationHandler.class);
  }

  @Test
  public void connectionShouldNull() throws SQLException {
    Connection connection = driver.connect(URL_UNKNOWN, null);
    assertThat(connection).isNull();
  }

}
