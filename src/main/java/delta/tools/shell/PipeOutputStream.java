package delta.tools.shell;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Output stream that writes data to a pipe.
 * Data is buffered into an internal buffer before it is sent to the pipe.
 * @author DAM
 */
public class PipeOutputStream extends OutputStream
{
  private BuffersQueue _buffers;
  /**
   * The internal buffer where data is stored.
   */
  private byte[] _internalBuffer;
  /**
   * Number of valid bytes in the internal buffer.
   */
  private int _validBytes;

  /**
   * Constructor.
   * @param buffers Pipe to write to.
   */
  public PipeOutputStream(BuffersQueue buffers)
  {
    _buffers=buffers;
    int bufferSize=buffers.getMaxBufferSize();
    _internalBuffer=new byte[bufferSize];
  }

  /**
   * Flush the internal buffer.
   */
  private void flushBuffer()
  {
    if (_validBytes>0)
    {
      _buffers.putBuffer(_internalBuffer,0,_validBytes);
      _validBytes=0;
    }
  }

  /**
   * Close the stream.
   */
  @Override
  public void close()
  {
    flushBuffer();
    byte[] eofBuffer=new byte[0];
    _buffers.putBuffer(eofBuffer,0,0);
  }

  /**
   * Flushes the contents of the internal buffer to the pipe.
   */
  @Override
  public void flush() throws IOException
  {
    flushBuffer();
  }

  /**
   * Writes some data to the pipe.
   * @param buffer Data to write.
   * @param offset Offset of data to write.
   * @param length Number of bytes to write.
   */
  @Override
  public void write(byte[] buffer, int offset, int length) throws IOException
  {
    int currentLength=length;
    int currentOffset=offset;
    while (currentLength>0)
    {
      if (currentLength<_internalBuffer.length-_validBytes)
      {
        // Buffering only
        System.arraycopy(buffer,currentOffset,_internalBuffer,_validBytes,currentLength);
        _validBytes+=currentLength;
        currentLength=0;
      }
      else
      {
        int writeLength;
        if (_validBytes==0)
        {
          writeLength=_internalBuffer.length;
          _buffers.putBuffer(buffer,currentOffset,_internalBuffer.length);
        }
        else
        {
          writeLength= _internalBuffer.length-_validBytes;
          System.arraycopy(buffer,currentOffset,_internalBuffer,_validBytes,writeLength);
          _validBytes+=writeLength;
          flushBuffer();
        }
        currentLength-=writeLength;
        currentOffset+=writeLength;
      }
    }
  }

  /**
   * Writes some data to the pipe.
   * @param buffer Data to write.
   */
  @Override
  public void write(byte[] buffer) throws IOException
  {
    write(buffer,0,buffer.length);
  }

  /**
   * Writes a byte to the pipe.
   * @param byteToWrite Byte to write.
   */
  @Override
  public void write(int byteToWrite) throws IOException
  {
    if (_validBytes>=_internalBuffer.length)
    {
      flushBuffer();
    }
    _internalBuffer[_validBytes++]=(byte)byteToWrite;
  }
}
