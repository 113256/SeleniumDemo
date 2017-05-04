package Tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SystemCommandExecutor {
	private List<String> commandInformation;
	private String workingDirectory = null;
	private ThreadedStreamHandler inputStreamHandler;
	private ThreadedStreamHandler errorStreamHandler;

	public SystemCommandExecutor(final String commandLine, String workingDirectory){
		if (commandLine != null && commandLine.length() > 0){
			List<String> commandList = new ArrayList<String>();
			commandList.add("cmd.exe");
			commandList.add("/c");
			commandList.add(commandLine);
			this.commandInformation = commandList;
			this.workingDirectory = workingDirectory;
		}else{
			throw new NullPointerException("The commandInformation is required.");
		}
	}
	
	public SystemCommandExecutor(final List<String> commandInformation, String workingDirectory) {
		if (commandInformation == null)
			throw new NullPointerException("The commandInformation is required.");
		this.workingDirectory = workingDirectory;
		this.commandInformation = commandInformation;
	}

	public int executeCommand() throws IOException, InterruptedException {
		int exitValue = -99;

		try {
			ProcessBuilder pb = new ProcessBuilder(commandInformation);
			if (workingDirectory != null){
				pb.directory(new File(workingDirectory));
			}
			Process process = pb.start();

			OutputStream stdOutput = process.getOutputStream();

			InputStream inputStream = process.getInputStream();
			InputStream errorStream = process.getErrorStream();

			inputStreamHandler = new ThreadedStreamHandler(inputStream,stdOutput);
			errorStreamHandler = new ThreadedStreamHandler(errorStream);

			inputStreamHandler.start();
			errorStreamHandler.start();

			exitValue = process.waitFor();

			inputStreamHandler.interrupt();
			errorStreamHandler.interrupt();
			inputStreamHandler.join();
			errorStreamHandler.join();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw e;
		} finally {
			return exitValue;
		}
	}

	public StringBuilder getStandardOutputFromCommand() {
		return inputStreamHandler.getOutputBuffer();
	}

	public StringBuilder getStandardErrorFromCommand() {
		return errorStreamHandler.getOutputBuffer();
	}

}
