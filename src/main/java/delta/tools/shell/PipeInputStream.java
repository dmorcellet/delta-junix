package delta.tools.shell;

import java.io.IOException;
import java.io.InputStream;

/**
 * Input stream used in a pipe.
 * @author DAM
 */
public class PipeInputStream extends InputStream
{
  private BuffersQueue _buffersQueue;
  /**
   * The internal buffer array where the data is stored. When necessary, it may
   * be replaced by another array of a different size.
   */
  private byte[] _localBuffer;

  /**
   * The current position in the buffer. This is the index of the next character
   * to be read from the <code>buf</code> array.
   * <p>
   * This value is always in the range <code>0</code> through
   * <code>count</code>. If it is less than <code>count</code>, then
   * <code>buf[pos]</code> is the next byte to be supplied as input; if it is
   * equal to <code>count</code>, then the next <code>read</code> or
   * <code>skip</code> operation will require more bytes to be read from the
   * underlying buffers queue.
   */
  private int _position;

  /**
   * Constructor.
   * @param buffers Input buffers.
   */
  public PipeInputStream(BuffersQueue buffers)
  {
    _buffersQueue=buffers;
  }

  private byte[] getBuffer() throws IOException
  {
    if (_buffersQueue==null)
    {
      throw new IOException("Stream closed");
    }
    if (_position==-1)
    {
      return null;
    }
    if ((_localBuffer==null) || (_localBuffer.length==_position))
    {
      _localBuffer=_buffersQueue.getBuffer();
      _position=0;
      if (_localBuffer.length==0)
      {
        _position=-1;
        _localBuffer=null;
      }
    }
    return _localBuffer;
  }

  @Override
  public synchronized int read() throws IOException
  {
    byte[] buffer=getBuffer();
    if (buffer==null)
    {
      return -1;
    }
    int ret=buffer[_position]&0xFF;
    _position++;
    return ret;
  }

  @Override
  public synchronized int read(byte[] b, int off, int len) throws IOException
  {
    int nbRead=0;
    while (nbRead<len)
    {
      byte[] buffer=getBuffer();
      if (buffer==null)
      {
        // EOF reached
        if (nbRead>0)
        {
          return nbRead;
        }
        return -1;
      }
      int nbAvailable=buffer.length-_position;
      int nbToRead;
      if (nbRead+nbAvailable>=len)
      {
        nbToRead=len-nbRead;
      }
      else
      {
        nbToRead=nbAvailable;
      }
      System.arraycopy(buffer,_position,b,off+nbRead,nbToRead);
      nbRead+=nbToRead;
      _position+=nbToRead;
    }
    return nbRead;
  }

  @Override
  public synchronized int available() throws IOException
  {
    int ret=0;
    if (_localBuffer!=null)
    {
      ret+=_localBuffer.length-_position;
    }
    if (_buffersQueue!=null)
    {
      ret=_buffersQueue.getAvailableBytes();
    }
    return ret;
  }

  /**
   * Close the stream.
   */
  @Override
  public void close()
  {
    _buffersQueue=null;
    _localBuffer=null;
    _position=-1;
  }
}
