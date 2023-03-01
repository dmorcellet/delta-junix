package delta.tools.shell.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import delta.tools.shell.CommandEnvironment;
import delta.tools.shell.InternalCommand;

/**
 * Implementation of the Unix cat command as a Java internal command.
 * @author DAM
 */
public class CatCommand extends InternalCommand
{
  private static final int DEFAULT_BUFFER_SIZE=1024;
  private static final Logger LOGGER=Logger.getLogger(CatCommand.class);

  /**
   * Execute the command.
   * @param env Command environment.
   */
  @Override
  public void execute(CommandEnvironment env)
  {
    InputStream is=env.getInputStream();
    OutputStream os=env.getOutputStream();

    int exitCode;
    if (is!=null)
    {
      byte[] buffer=new byte[DEFAULT_BUFFER_SIZE];
      try
      {
        while (true)
        {
          int readBytes=is.read(buffer);
          if (readBytes==-1)
          {
            exitCode=0;
            break;
          }
          os.write(buffer,0,readBytes);
        }
      }
      catch(IOException ioe)
      {
        exitCode=-1;
      }
    }
    else
    {
      exitCode=-1;
    }
    if (LOGGER.isInfoEnabled())
    {
      LOGGER.info("Exit code="+exitCode);
    }
  }
}
