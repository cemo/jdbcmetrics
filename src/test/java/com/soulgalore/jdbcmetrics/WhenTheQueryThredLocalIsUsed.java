package com.soulgalore.jdbcmetrics;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WhenTheQueryThredLocalIsUsed {

  @Test
  public void theNumberOfReadsShouldBeUpdated() {
    QueryThreadLocal.init();
    QueryThreadLocal.addRead(1223);
    try {
      assertThat(QueryThreadLocal.getNrOfQueries().getReads()).isEqualTo(1);
    } finally {
      QueryThreadLocal.removeNrOfQueries();
    }
  }

  @Test
  public void theNumberOfWritesShouldBeUpdated() {
    QueryThreadLocal.init();
    QueryThreadLocal.addWrite(2322);
    try {
      assertThat(QueryThreadLocal.getNrOfQueries().getWrites()).isEqualTo(1);
    } finally {
      QueryThreadLocal.removeNrOfQueries();
    }
  }
}
