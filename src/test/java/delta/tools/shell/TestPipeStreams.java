package delta.tools.shell;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.common.utils.io.StreamTools;

/**
 * Test piped streams.
 * @author DAM
 */
@DisplayName("Pipe streams test")
class TestPipeStreams
{
  private static final Logger LOGGER=LoggerFactory.getLogger(TestPipeStreams.class);

  private Thread _producer;
  private Thread _consumer;
  private BuffersQueue _buffer;

  private static final int PIPE_SIZE=1024;
  private static final int CONSUMER_BUFFER_SIZE=17;
  private static final int PRODUCER_BUFFER_SIZE=36;
  private static final int BYTES_TO_EXCHANGE=10000;

  /**
   * Test a producer/consumer scenario.
   */
  @Test
  void testPipeBuffer()
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
            LOGGER.debug("Reading...");
            int read=is.read(myBuffer,0,CONSUMER_BUFFER_SIZE);
            bytesRead+=read;
            LOGGER.debug("Read "+read+" bytes...");
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
            LOGGER.debug("Writing...");
            os.write(myBuffer,0,PRODUCER_BUFFER_SIZE);
            bytesWritten+=PRODUCER_BUFFER_SIZE;
            LOGGER.debug("Wrote "+PRODUCER_BUFFER_SIZE+" bytes...");
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
