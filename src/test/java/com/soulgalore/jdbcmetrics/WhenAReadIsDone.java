package com.soulgalore.jdbcmetrics;


import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WhenAReadIsDone {

  private ReadAndWrites rw;

  @Before
  public void setUp() throws Exception {

    rw = new ReadAndWrites();
  }

  @Test
  public void theNumberOfReadsShouldBeIncreased() {

    rw.incReads(1);
    assertThat(rw.getReads()).isEqualTo(1);
    rw.incReads(2);
    rw.incReads(3);
    assertThat(rw.getReads()).isEqualTo(3);
    assertThat(rw.getTotalReadTime()).isEqualTo(6);
    rw.clear();
    assertThat(rw.getReads()).isEqualTo(0);

  }

}
