package com.fmning.service.manager;

import java.time.Instant;
import java.util.Map;

public interface AboutManager
{
  public final static String SDK_VERSION_KEY = "version";
  public final static String SDK_VERSION = "Core SDK Ver 1.0";

  public static final String RELEASE_DATE_KEY = "releaseDate";
  public static final String RELEASE_DATE = "Aug, 2017";

  public static final String START_TIME_KEY = "startTime";
  public static final String START_TIME = Instant.now().toString();

  Map<String, Object> getInfo( );
}
