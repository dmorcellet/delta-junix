package delta.tools.shell;

import java.io.IOException;
import java.util.Random;

import junit.framework.TestCase;
import delta.common.utils.io.StreamTools;

public class TestPipeOutputStream extends TestCase
{
  /**
   * Constructor.
   */
  public TestPipeOutputStream()
  {
    super("Pipe output stream test");
  }

  public void testPipeOutputStream()
  {
    PipeOutputStream pos=null;
    try
    {
      BuffersQueue buffers=new BuffersQueue(1000);
      pos=new PipeOutputStream(buffers);
      final int NB=10;
      final int MAX_SIZE=2000;
      Random r=new Random(System.currentTimeMillis());
      byte[] b=new byte[MAX_SIZE];
      for(int i=0;i<NB;i++)
      {
        int nb=r.nextInt(MAX_SIZE);
        pos.write(b,0,nb);
      }
    }
    catch(IOException ioe)
    {
      ioe.printStackTrace();
    }
    finally
    {
      StreamTools.close(pos);
    }
  }
}
