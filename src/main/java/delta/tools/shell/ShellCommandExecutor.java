package delta.tools.shell;

/**
 * Executor for shell commands.
 * @author DAM
 */
public class ShellCommandExecutor implements Runnable
{
  private ShellCommand _command;

  /**
   * Constructor.
   * @param command Shell command to execute.
   */
  public ShellCommandExecutor(ShellCommand command)
  {
    _command=command;
  }

  public void run()
  {
    _command.execute();
  }
}
