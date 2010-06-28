package delta.tools.shell;

import java.io.InputStream;
import java.io.OutputStream;

import delta.common.utils.io.StreamTools;

/**
 * @author DAM
 */
public class ShellCommand
{
  private ShellCommandImpl _commandImpl;
  private InputStream _in;
  private OutputStream _out;
  private OutputStream _err;

  /**
   * Constructor.
   * @param command Managed command.
   */
  public ShellCommand(ShellCommandImpl command)
  {
    _commandImpl=command;
    _in=System.in;
    _out=System.out;
    _err=System.err;
  }

  /**
   * Constructor.
   * @param command Managed command.
   */
  public ShellCommand(ShellCommandImpl command, InputStream in, OutputStream out, OutputStream err)
  {
    _commandImpl=command;
    _in=in;
    _out=out;
    _err=err;
  }

  public void redirectStdin(InputStream is)
  {
    _in=is;
  }

  public void redirectStdout(OutputStream os)
  {
    _out=os;
  }

  public void redirectStderr(OutputStream os)
  {
    _err=os;
  }

  /**
   * Command execution.
   */
  public void execute()
  {
    CommandEnvironment env=new CommandEnvironment(_in,_out,_err);
    _commandImpl.execute(env);
    StreamTools.close(_out);
    StreamTools.close(_err);
  }
}
