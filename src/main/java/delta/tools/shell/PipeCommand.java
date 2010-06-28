package delta.tools.shell;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A command that executes two other commands whose input and output streams
 * are piped.
 * @author DAM
 */
public class PipeCommand extends InternalCommand
{
  private ShellCommandImpl _c1;
  private ShellCommandImpl _c2;
  private StreamRedirectionType _redirection;

  /**
   * Constructor.
   * @param c1 First command.
   * @param c2 Second command.
   * @param redirection Redirection type to use in the pipe.
   */
  public PipeCommand(ShellCommandImpl c1, ShellCommandImpl c2, StreamRedirectionType redirection)
  {
    _c1=c1;
    _c2=c2;
    if (redirection==StreamRedirectionType.NONE)
    {
      throw new IllegalArgumentException("StreamRedirectionType cannot be"+StreamRedirectionType.NONE);
    }
    _redirection=redirection;
  }

  /**
   * Executes this command.
   * @param env Command's environment.
   */
  @Override
  public void execute(CommandEnvironment env)
  {
    // Build pipe
    BuffersQueue queue=new BuffersQueue();

    // Compute in/out/err streams for both commands
    InputStream stdin1=env.getInputStream();
    OutputStream stdout1=env.getOutputStream();
    OutputStream stderr1=env.getErrorStream();

    OutputStream stdout2=env.getOutputStream();
    OutputStream stderr2=env.getErrorStream();

    PipeOutputStream os=new PipeOutputStream(queue);
    boolean redirectStdOut=_redirection.redirectsStdOut();
    if (redirectStdOut)
    {
      stdout1=os;
    }
    boolean redirectStdErr=_redirection.redirectsStdErr();
    if (redirectStdErr)
    {
      stderr1=os;
    }
    PipeInputStream is=new PipeInputStream(queue);
    InputStream stdin2=is;

    // Prepare commands
    ShellCommand c1=new ShellCommand(_c1,stdin1,stdout1,stderr1);
    ShellCommandExecutor exec1=new ShellCommandExecutor(c1);
    Thread t1=new Thread(exec1,"c1");
    ShellCommand c2=new ShellCommand(_c2,stdin2,stdout2,stderr2);
    ShellCommandExecutor exec2=new ShellCommandExecutor(c2);
    Thread t2=new Thread(exec2,"c2");

    // Execute both commands in separate threads...
    t1.start();
    t2.start();
    // ... and join them !
    joinThread(t1);
    joinThread(t2);
  }

  private void joinThread(Thread t)
  {
    try
    {
      t.join();
    }
    catch(InterruptedException ie)
    {
      ie.printStackTrace();
    }
  }
}
