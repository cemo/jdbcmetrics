package com.soulgalore.jdbcmetrics;


import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WhenAWriteIsDone {

  private ReadAndWrites rw;

  @Before
  public void setUp() throws Exception {

    rw = new ReadAndWrites();
  }

  @Test
  public void theNumberOfWritesShouldBeIncreased() {

    rw.incWrites(5);
    assertThat(rw.getWrites()).isEqualTo(1);
    assertThat(rw.getWrites()).isEqualTo(1);
    rw.incWrites(4);
    rw.incWrites(2);
    assertThat( rw.getWrites()).isEqualTo(3);
    assertThat(rw.getTotalWriteTime()).isEqualTo(11);
    rw.clear();
    assertThat(rw.getWrites()).isEqualTo(0);

  }

}
