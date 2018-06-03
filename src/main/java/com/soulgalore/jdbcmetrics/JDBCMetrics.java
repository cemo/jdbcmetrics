/******************************************************
 * JDBCMetrics
 * 
 * 
 * Copyright (C) 2013 by Magnus Lundberg (http://magnuslundberg.com) & Peter Hedenskog
 * (http://peterhedenskog.com)
 * 
 ****************************************************** 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 ******************************************************* 
 */
package com.soulgalore.jdbcmetrics;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class responsible for holding all the Yammer Metrics. The default MetricRegistry is used by
 * default, if you want a new to be created add a System property
 * <em>com.soulgalore.jdbcmetrics.MetricRegistry</em> with the value <em>new</em>.
 * 
 */
public class JDBCMetrics {

  public static final String REGISTRY_PROPERTY_NAME = "com.soulgalore.jdbcmetrics.MetricRegistry";

  private static final String GROUP = "jdbc";
  private static final String GROUP_POOL = "connectionpool";
  private static final String TYPE_READ = "read";
  private static final String TYPE_WRITE = "write";
  private static final String TYPE_READ_OR_WRITE = "readorwrite";

  private final MeterRegistry registry = Metrics.globalRegistry;

  private final DistributionSummary readCountsPerRequest;
  private final DistributionSummary writeCountsPerRequest;

  private final Timer writeTimer;
  private final Timer readTimer;
  private final Timer writeTimerPerRequest;
  private final Timer readTimerPerRequest;
  private final Timer connectionPoolTimer;

  private static final JDBCMetrics INSTANCE = new JDBCMetrics();


  private JDBCMetrics() {

    String propertyValue = System.getProperty(REGISTRY_PROPERTY_NAME);
    if (propertyValue == null) {
      InputStream propertiesStream = getClass().getResourceAsStream("/jdbcmetrics.properties");
      if (propertiesStream != null) {
        try {
          Properties properties = new Properties();
          properties.load(propertiesStream);

          propertyValue = properties.getProperty("metricRegistry.name");
        } catch (Exception e) {
          throw new RuntimeException(e);
        } finally {
          try {
            propertiesStream.close();
          } catch (IOException e) {
            // ignore
          }
        }
      }
    }

    //if (propertyValue != null)
    //  registry = SharedMetricRegistries.getOrCreate(propertyValue);
    //else
    //  registry = new SimpleMeterRegistry();

    readCountsPerRequest = registry.summary("read-counts-per-request", GROUP, TYPE_READ);

    writeCountsPerRequest = registry.summary("write-counts-per-request", GROUP, TYPE_WRITE);

    writeTimer = registry.timer("write-time", GROUP, TYPE_WRITE );

    readTimer = registry.timer("read-time", GROUP, TYPE_READ);

    writeTimerPerRequest = registry.timer("write-time-per-request", GROUP, TYPE_WRITE);

    readTimerPerRequest = registry.timer("read-time-per-request", GROUP, TYPE_READ);

    connectionPoolTimer = registry.timer("wait-for-connection", GROUP_POOL, TYPE_READ_OR_WRITE);
  }

  /**
   * Get the instance.
   * 
   * @return the singleton instance.
   */
  public static JDBCMetrics getInstance() {
    return INSTANCE;
  }


  public DistributionSummary getReadCountsPerRequest() {
    return readCountsPerRequest;
  }

  public DistributionSummary getWriteCountsPerRequest() {
    return writeCountsPerRequest;
  }

  public Timer getWriteTimer() {
    return writeTimer;
  }

  public Timer getReadTimer() {
    return readTimer;
  }

  public Timer getWaitForConnectionInPool() {
    return connectionPoolTimer;
  }

  public MeterRegistry getRegistry() {
    return registry;
  }

  public Timer getWriteTimerPerRequest() {
    return writeTimerPerRequest;
  }

  public Timer getReadTimerPerRequest() {
    return readTimerPerRequest;
  }
}
