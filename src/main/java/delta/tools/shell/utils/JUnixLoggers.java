package delta.tools.shell.utils;

import org.apache.log4j.Logger;

import delta.common.utils.traces.LoggersRegistry;

/**
 * Management class for all JUnix loggers.
 * @author DAM
 */
public abstract class JUnixLoggers
{
  /**
   * Name of the "JUNIX" logger.
   */
  public static final String JUNIX="JUNIX";

  private static final Logger _junixLogger=LoggersRegistry.getLogger(JUNIX);

  /**
   * Get the logger used for JUnix (JUNIX).
   * @return the logger used for JUnix.
   */
  public static Logger getJUnixLogger()
  {
    return _junixLogger;
  }
}
