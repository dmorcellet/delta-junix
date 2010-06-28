package delta.tools.shell;

/**
 * Stream redirection type.
 * This class provides constant instances of herself for all known redirection types.
 * @author DAM
 */
public final class StreamRedirectionType
{
  private String _name;
  private boolean _redirectsStdout;
  private boolean _redirectsStderr;

  /**
   * No redirection (no stdout and no stderr redirection).
   */
  public static final StreamRedirectionType NONE=new StreamRedirectionType("NONE",false,false);

  /**
   * Standard output redirection only.
   */
  public static final StreamRedirectionType STDOUT=new StreamRedirectionType("STDOUT",true,false);

  /**
   * Standard error output redirection only.
   */
  public static final StreamRedirectionType STDERR=new StreamRedirectionType("STDERR",false,true);

  /**
   * Redirects both output streams.
   */
  public static final StreamRedirectionType BOTH=new StreamRedirectionType("BOTH",true,true);

  /**
   * Private constructor.
   * This constructor is private to ensure that the only instances of this class
   * are those provided here as constants.
   * @param name Name of this redirection.
   * @param stdout Redirect standard output.
   * @param stderr Redirect standard error output.
   */
  private StreamRedirectionType(String name, boolean stdout, boolean stderr)
  {
    _name=name;
    _redirectsStdout=stdout;
    _redirectsStderr=stderr;
  }

  /**
   * Indicates if standard output is redirected or not.
   * @return <code>true</code> if it is redirected, <code>false</code> otherwise.
   */
  public boolean redirectsStdOut()
  {
    return _redirectsStdout;
  }

  /**
   * Indicates if standard error output is redirected or not.
   * @return <code>true</code> if it is redirected, <code>false</code> otherwise.
   */
  public boolean redirectsStdErr()
  {
    return _redirectsStderr;
  }

  /**
   * Get a readable string that represents this object.
   * @return a readable string that represents this object.
   */
  @Override
  public String toString()
  {
    return _name;
  }
}
