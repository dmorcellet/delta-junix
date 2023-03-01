package delta.tools.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.utils.io.StreamTools;
import delta.tools.shell.CommandEnvironment;
import delta.tools.shell.InternalCommand;

/**
 * Implementation of the Unix sort command as a Java internal command.
 * @author DAM
 */
public class SortCommand extends InternalCommand
{
  private static final Logger LOGGER=Logger.getLogger(SortCommand.class);

  /**
   * Execute the command.
   * @param env Command environment.
   */
  @Override
  public void execute(CommandEnvironment env)
  {
    InputStream is=env.getInputStream();
    OutputStream os=env.getOutputStream();

    InputStreamReader isr=null;
    BufferedReader br=null;
    PrintWriter pw=null;
    int exitCode;
    try
    {
      List<String> lines=new ArrayList<String>();
      isr=new InputStreamReader(is);
      br=new BufferedReader(isr);
      String line;
      while (true)
      {
        line=br.readLine();
        if (line==null)
        {
          exitCode=0;
          break;
        }
        lines.add(line);
      }
      Collections.sort(lines);
      pw=new PrintWriter(os);
      for(Iterator<String> it=lines.iterator();it.hasNext();)
      {
        line=it.next();
        pw.println(line);
      }
    }
    catch(IOException ioe)
    {
      exitCode=-1;
    }
    finally
    {
      StreamTools.close(br); br=null;
      StreamTools.close(isr); isr=null;
      StreamTools.close(pw); pw=null;
    }
    if (LOGGER.isInfoEnabled())
    {
      LOGGER.info("Exit code="+exitCode);
    }
  }
}
