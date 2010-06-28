package delta.tools.shell;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Environment of a shell command.
 * @author DAM
 */
public class CommandEnvironment
{
  /**
   * Standard input stream (stdin).
   */
  private InputStream _stdin;
  /**
   * Standard output stream (stdout).
   */
  private OutputStream _stdout;
  /**
   * Standard error output stream (stderr).
   */
  private OutputStream _stderr;

  /**
   * Full constructor.
   * @param in Standard input.
   * @param out Standard ouput.
   * @param err Standard error output.
   */
  public CommandEnvironment(InputStream in, OutputStream out, OutputStream err)
  {
    _stdin=in;
    _stdout=out;
    _stderr=err;
  }

  /**
   * Get the standard input stream.
   * @return the standard input stream.
   */
  public InputStream getInputStream()
  {
    return _stdin;
  }

  /**
   * Get the standard output stream.
   * @return the standard output stream.
   */
  public OutputStream getOutputStream()
  {
    return _stdout;
  }

  /**
   * Get the standard error output stream.
   * @return the standard error output stream.
   */
  public OutputStream getErrorStream()
  {
    return _stderr;
  }
}
