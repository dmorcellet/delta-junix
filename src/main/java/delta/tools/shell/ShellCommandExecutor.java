package delta.tools.shell;

/**
 * @author DAM
 */
public class ShellCommandExecutor implements Runnable
{
  private ShellCommand _command;
  public ShellCommandExecutor(ShellCommand command)
  {
    _command=command;
  }

  public void run()
  {
    _command.execute();
  }
}
