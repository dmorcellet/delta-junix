package delta.tools.shell;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import delta.tools.shell.utils.JUnixLoggers;

/**
 * Queue of byte buffers.
 * @author DAM
 */
public class BuffersQueue
{
  private static final Logger _logger=JUnixLoggers.getJUnixLogger();

  private static final int DEFAULT_BUFFER_SIZE=1024;

  private int _maxBufferSize;
  private LinkedBlockingQueue<byte[]> _queue;
  private int _availableBytes;

  /**
   * Default constructor.
   * Uses a default buffer size.
   */
  public BuffersQueue()
  {
    this(DEFAULT_BUFFER_SIZE);
  }

  /**
   * Full constructor.
   * @param bufferSize Size of managed buffers.
   */
  public BuffersQueue(int bufferSize)
  {
    _queue=new LinkedBlockingQueue<byte[]>();
    _maxBufferSize=bufferSize;
    _availableBytes=0;
  }

  /**
   * Get the size of managed buffer sizes.
   * @return the size of managed buffer sizes.
   */
  public int getMaxBufferSize()
  {
    return _maxBufferSize;
  }

  /**
   * Put some bytes in the queue.
   * @param buffer Buffer where bytes must be taken from.
   * @param offset Offset of bytes to fetch in this buffer.
   * @param length Number of bytes to retrieve.
   */
  public void putBuffer(byte[] buffer, int offset, int length)
  {
    if (_logger.isDebugEnabled())
    {
      _logger.debug("Received buffer : offset="+offset+", lenght="+length);
    }
    try
    {
      byte[] newBuffer=new byte[length];
      System.arraycopy(buffer,offset,newBuffer,0,length);
      _queue.put(newBuffer);
      _availableBytes+=length;
    }
    catch(InterruptedException iException)
    {
      _logger.error("",iException);
    }
  }

  /**
   * Get the number of available bytes in the entire queue.
   * @return the number of available bytes in the entire queue.
   */
  public int getAvailableBytes()
  {
    return _availableBytes;
  }

  /**
   * Get a new buffer from the queue.
   * @return A new buffer.
   */
  public byte[] getBuffer()
  {
    byte[] ret=null;
    try
    {
      ret=_queue.take();
      _availableBytes-=ret.length;
    }
    catch(InterruptedException iException)
    {
      _logger.error("",iException);
    }
    return ret;
  }
}
