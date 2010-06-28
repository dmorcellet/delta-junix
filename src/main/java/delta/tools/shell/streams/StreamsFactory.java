package delta.tools.shell.streams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import delta.tools.shell.utils.JUnixLoggers;

/**
 * @author DAM
 */
public class StreamsFactory
{
  private static final Logger _logger=JUnixLoggers.getJUnixLogger();

  public InputStream getFileInputStream(File path)
  {
    FileInputStream ret=null;
    try
    {
      ret=new FileInputStream(path);
    }
    catch(FileNotFoundException fnfE)
    {
      _logger.error(fnfE);
    }
    return ret;
  }

  public OutputStream getDevNullOutputStream()
  {
    OutputStream ret=new DevNullOutputStream();
    return ret;
  }

  public OutputStream getFileOutputStream(File path)
  {
    FileOutputStream ret=null;
    try
    {
      ret=new FileOutputStream(path);
    }
    catch(FileNotFoundException fnfE)
    {
      _logger.error(fnfE);
    }
    return ret;
  }

  public void release(InputStream is)
  {
    // Nothing to do ?
  }
}
