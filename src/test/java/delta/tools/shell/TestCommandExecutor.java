package delta.tools.shell;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;
import delta.common.utils.environment.FileSystem;
import delta.common.utils.files.TextFileWriter;
import delta.tools.shell.commands.CatCommand;
import delta.tools.shell.commands.SortCommand;
import delta.tools.shell.streams.StreamsFactory;

public class TestCommandExecutor extends TestCase
{
  /**
   * Constructor.
   */
  public TestCommandExecutor()
  {
    super("Command executor test");
  }

  public void testCatCommand()
  {
    // Setup :
    File inFile=getTestFile();
    writeTestFile();

    // Implements cat < f1 > /dev/null
    CatCommand command=new CatCommand();
    ShellCommand executor=new ShellCommand(command);
    StreamsFactory factory=new StreamsFactory();
    InputStream in=factory.getFileInputStream(inFile);
    executor.redirectStdin(in);
    OutputStream out=factory.getDevNullOutputStream();
    executor.redirectStdout(out);
    executor.execute();

    // Cleanup
    inFile.delete();
  }

  public void testPipeCommand()
  {
    // Setup :
    File inFile=getTestFile();
    File outFile=getOutputFile();
    writeTestFile();

    // Implements cat < f1 | sort > f2
    CatCommand catCommand=new CatCommand();
    SortCommand sortCommand=new SortCommand();
    PipeCommand pipeCommand=new PipeCommand(catCommand,sortCommand,StreamRedirectionType.STDOUT);
    ShellCommand command=new ShellCommand(pipeCommand);
    StreamsFactory factory=new StreamsFactory();
    InputStream in=factory.getFileInputStream(inFile);
    command.redirectStdin(in);
    OutputStream out=factory.getFileOutputStream(outFile);
    command.redirectStdout(out);
    command.execute();

    // Cleanup
    inFile.delete();
    outFile.delete();
  }

  private File getTestFile()
  {
    File tmpDir=FileSystem.getTmpDir();
    File testFile=new File(tmpDir,"test.txt");
    return testFile;
  }

  private File getOutputFile()
  {
    File tmpDir=FileSystem.getTmpDir();
    File outputFile=new File(tmpDir,"output.txt");
    return outputFile;
  }

  private void writeTestFile()
  {
    File file=getTestFile();
    TextFileWriter writer=new TextFileWriter(file);
    if (writer.start())
    {
      for(int i=0;i<10;i++)
      {
        writer.writeNextLine("ligne "+i);
      }
      writer.terminate();
    }
  }
}
