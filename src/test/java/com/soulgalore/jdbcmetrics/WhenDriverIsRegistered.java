package com.soulgalore.jdbcmetrics;


import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.soulgalore.jdbcmetrics.Driver;
import com.soulgalore.jdbcmetrics.proxy.AbstractDriverTest;

import static org.assertj.core.api.Assertions.assertThat;

public class WhenDriverIsRegistered extends AbstractDriverTest {

  @Test
  public void driverShouldBeInDriverManager() {
    List<java.sql.Driver> driversInManager = Collections.list(DriverManager.getDrivers());
    boolean foundJDBCMetricsDriver = false;
    for (java.sql.Driver driver : driversInManager) {
      if (driver instanceof Driver) {
        foundJDBCMetricsDriver = true;
        break;
      }
    }
    assertThat(foundJDBCMetricsDriver).isTrue();
    assertThat(underlayingDriver).isIn(driversInManager);
  }

  @Test
  public void driverShouldBeLocatedByKnownUrl() throws SQLException {
    java.sql.Driver d = DriverManager.getDriver(URL_JDBC_METRICS);
    assertThat(d).isNotNull();
    assertThat(d).isInstanceOf(Driver.class);
  }

  @Test(expected = SQLException.class)
  public void driverShouldNotBeLocatedByUnknownUrl() throws SQLException {
    DriverManager.getDriver(URL_UNKNOWN);
    // Should throw, no suitable driver
  }

  @Test
  public void knownUrlShouldBeCleaned() {
    assertThat(driver.cleanUrl(URL_JDBC_METRICS)).isEqualTo(URL_KNOWN_DRIVER);
  }

  @Test
  public void knownUrlWithDriverShouldBeCleaned() {
    assertThat(driver.cleanUrl(URL_JDBC_METRICS_SPECIFIED_DRIVER)).isEqualTo(URL_KNOWN_DRIVER);
  }

  @Test
  public void unknownUrlShouldNotBeCleaned() {
    assertThat(driver.cleanUrl(URL_UNKNOWN)).isEqualTo(URL_UNKNOWN);
  }

  @Test
  public void underlayingDriverShouldExistInDriverManager() throws SQLException {
    assertThat(driver.getDriverFromDriverManager(driver.cleanUrl(URL_KNOWN_DRIVER))).isSameAs(underlayingDriver);
  }

  @Test
  public void underlayingDriverShouldExist() throws SQLException {
    assertThat(driver.getDriver(URL_JDBC_METRICS, URL_KNOWN_DRIVER)).isSameAs(underlayingDriver);
  }

  @Test
  public void specifiedDriverClassNameShouldBeParsedFromUrl() {
    assertThat(driver.getSpecifiedDriverClassName(URL_JDBC_METRICS_SPECIFIED_DRIVER))
        .isEqualTo("org.postgresql.Driver");
  }

  @Test
  public void specifiedDriverClassNameShouldNotBeFoundInUrl() {
    assertThat(driver.getSpecifiedDriverClassName(URL_JDBC_METRICS)).isNull();
  }

  @Test(expected = RuntimeException.class)
  public void driverByNonExistingClassNameShouldThrow() {
    driver.getDriverByClassName("non.existing.Class");
  }

  @Test(expected = RuntimeException.class)
  public void driverByClassNameNotImplementingDriverShouldThrow() {
    driver.getDriverByClassName(String.class.getName());
  }

  @Test
  public void driverByClassNameShouldReturn() {
    java.sql.Driver d = driver.getDriverByClassName(Driver.class.getName());
    assertThat(d).isNotNull();
    assertThat(d.getClass()).isEqualTo(Driver.class);
  }

  @Test
  public void shouldAcceptUrl() throws SQLException {
    assertThat(driver.acceptsURL(URL_JDBC_METRICS)).isTrue();
    assertThat(driver.acceptsURL(URL_JDBC_METRICS_SPECIFIED_DRIVER)).isTrue();
  }

  @Test
  public void shouldNotAcceptUrl() throws SQLException {
    assertThat(driver.acceptsURL(URL_KNOWN_DRIVER)).isFalse();
    assertThat(driver.acceptsURL(URL_UNKNOWN)).isFalse();
  }

}
