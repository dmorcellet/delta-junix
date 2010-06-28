package delta.tools.shell.streams;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An output stream that ignores all data written to it.
 * @author DAM
 */
public class DevNullOutputStream extends OutputStream
{
  /**
   * Does nothing (passed data is ignored).
   * @param buffer Data to write.
   * @param offset Offset of data to write.
   * @param length Number of bytes to write.
   */
  @Override
  public void write(byte[] buffer, int offset, int length)
  {
    // Ignore !!
  }

  /**
   * Does nothing (passed data is ignored).
   * @param byteToWrite Byte to write.
   */
  @Override
  public void write(int byteToWrite) throws IOException
  {
    // Ignore !!
  }
}
