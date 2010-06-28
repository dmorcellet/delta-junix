package delta.tools.shell;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import delta.common.utils.io.StreamTools;
import delta.tools.shell.utils.JUnixLoggers;

public class TestPipeStreams extends TestCase
{
  private static final Logger _logger=JUnixLoggers.getJUnixLogger();

  private Thread _producer;
  private Thread _consumer;
  private BuffersQueue _buffer;

  private static final int PIPE_SIZE=1024;
  private static final int CONSUMER_BUFFER_SIZE=17;
  private static final int PRODUCER_BUFFER_SIZE=36;
  private static final int BYTES_TO_EXCHANGE=10000;

  /**
   * Constructor.
   */
  public TestPipeStreams()
  {
    super("Pipe streams test");
  }

  public void testPipeBuffer()
  {
    _buffer=new BuffersQueue(PIPE_SIZE);
    startConsumer();
    startProducer();
    try
    {
      _consumer.join();
      _producer.join();
    }
    catch(InterruptedException ie)
    {
      ie.printStackTrace();
    }
  }

  private void startConsumer()
  {
    Runnable r=new Runnable()
    {
      public void run()
      {
        PipeInputStream is=null;
        long bytesRead=0;
        try
        {
          is=new PipeInputStream(_buffer);
          byte[] myBuffer=new byte[CONSUMER_BUFFER_SIZE];
          while(bytesRead<BYTES_TO_EXCHANGE)
          {
            _logger.debug("Reading...");
            int read=is.read(myBuffer,0,CONSUMER_BUFFER_SIZE);
            bytesRead+=read;
            _logger.debug("Read "+read+" bytes...");
          }
        }
        catch(IOException ioe)
        {
          ioe.printStackTrace();
        }
        finally
        {
          StreamTools.close(is);
        }
      }
    };
    _consumer=new Thread(r);
    _consumer.setName("Consumer");
    _consumer.start();
  }

  private void startProducer()
  {
    Runnable r=new Runnable()
    {
      public void run()
      {
        PipeOutputStream os=null;
        long bytesWritten=0;
        try
        {
          os=new PipeOutputStream(_buffer);
          byte[] myBuffer=new byte[PRODUCER_BUFFER_SIZE];
          while(bytesWritten<BYTES_TO_EXCHANGE)
          {
            _logger.debug("Writing...");
            os.write(myBuffer,0,PRODUCER_BUFFER_SIZE);
            bytesWritten+=PRODUCER_BUFFER_SIZE;
            _logger.debug("Wrote "+PRODUCER_BUFFER_SIZE+" bytes...");
          }
          os.close();
        }
        catch(IOException ioe)
        {
          ioe.printStackTrace();
        }
        finally
        {
          StreamTools.close(os);
        }
      }
    };
    _producer=new Thread(r);
    _producer.setName("Producer");
    _producer.start();
  }
}
