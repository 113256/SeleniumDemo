package Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

class ThreadedStreamHandler extends Thread {
	private InputStream inputStream;
	private String adminPassword;
	private OutputStream outputStream;
	private PrintWriter printWriter;
	private StringBuilder outputBuffer = new StringBuilder();

	public ThreadedStreamHandler(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public ThreadedStreamHandler(InputStream inputStream, OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.printWriter = new PrintWriter(outputStream);
	}

	public void run() {

		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				outputBuffer.append(line + "\n");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				// ignore this one
			}
		}
	}

	private void doSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// ignore
		}
	}

	public StringBuilder getOutputBuffer() {
		return outputBuffer;
	}

}
