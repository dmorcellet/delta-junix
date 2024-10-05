package delta.tools.shell.streams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory methods to build streams.
 * @author DAM
 */
public class StreamsFactory
{
  private static final Logger LOGGER=LoggerFactory.getLogger(StreamsFactory.class);

  /**
   * Build a file input stream.
   * @param path File to read from.
   * @return A new stream or <code>null</code> if file does not exist.
   */
  public InputStream getFileInputStream(File path)
  {
    FileInputStream ret=null;
    try
    {
      ret=new FileInputStream(path);
    }
    catch(FileNotFoundException fnfE)
    {
      LOGGER.error("",fnfE);
    }
    return ret;
  }

  /**
   * Build a '/dev/null' output stream.
   * @return A new output stream.
   */
  public OutputStream getDevNullOutputStream()
  {
    OutputStream ret=new DevNullOutputStream();
    return ret;
  }

  /**
   * Build a file output stream.
   * @param path File to write to.
   * @return A new output stream.
   */
  public OutputStream getFileOutputStream(File path)
  {
    FileOutputStream ret=null;
    try
    {
      ret=new FileOutputStream(path);
    }
    catch(FileNotFoundException fnfE)
    {
      LOGGER.error("",fnfE);
    }
    return ret;
  }

  /**
   * Release the specified stream.
   * @param is
   */
  public void release(InputStream is)
  {
    // Nothing to do?
  }
}
