package Driver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.internal.WebElementToJsonConverter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class sRemoteWebDriver extends RemoteWebDriver
{
	//public final String filepath = System.getProperty("user.dir")+"/sessionid.txt";
	
	public sRemoteWebDriver(URL url, Capabilities cap) {	
		super(url,cap);
	}
	
	@Override
	public Object executeScript(String script, Object... args) {
	    //Ignore the capability check
		
	    // Escape the quote marks
	    script = script.replaceAll("\"", "\\\"");

	    Iterable<Object> convertedArgs = Iterables.transform(
	        Lists.newArrayList(args), new WebElementToJsonConverter());

	    Map<String, ?> params = ImmutableMap.of(
	        "script", script,
	        "args", Lists.newArrayList(convertedArgs));

	    return execute(DriverCommand.EXECUTE_SCRIPT, params).getValue();
	  }
	
	@Override
	  protected void startSession(Capabilities desiredCapabilities) {
		startSession(desiredCapabilities,null);
	}
	@Override
	protected void startSession(Capabilities desiredCapabilities,
		      Capabilities requiredCapabilities) {
	    String sid = getPreviousSessionIdFromSomeStorage();
	    System.out.println("sid: "+sid);
	    if (sid != null) {
	      setSessionId(sid);
	      try {
	        getCurrentUrl();
	      } catch (WebDriverException e) {
	        // session is not valid
	        sid = null;
	      }
	    }
	    if (sid == null) {
//	    	if (requiredCapabilities == null) requiredCapabilities = new DesiredCapabilities();
	    	
	      super.startSession(desiredCapabilities, requiredCapabilities);
	      saveSessionIdToSomeStorage(getSessionId().toString());
	    }
	  }
	
	String getPreviousSessionIdFromSomeStorage() {
		BufferedReader br = null;

		String filepath = System.getProperty("user.dir")+"/sessionid.txt";
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(filepath));

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				return sCurrentLine;
			}

		} catch (Exception e) {
//			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	void saveSessionIdToSomeStorage(String sid) {
		String filepath = System.getProperty("user.dir")+"/sessionid.txt";
		try {
			//System.out.println("filepath: "+filepath);
		File file = new File(filepath);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(sid);
		bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

