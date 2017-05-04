package Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
	 
public class ConfigProperty {
		String result = "";
		InputStream inputStream;
		private final String homeURL = "homeURL", 
				chromeDriverPath = "chromeDriverPath",
				binary = "binary",
				binaryPath = "binaryPath";
		HashMap<String, String> urlMap = new HashMap<String, String>();
		
		//File configFile = new File("src/Constants/config.properties");
		File configFile = new File("config.properties");
		
		//URL url = getClass().getResource("config.properties");
		//File configFile = new File(url.getPath());
		//File configFile = new File("src/Constants/config.properties");
		
		
		PropertiesConfiguration config = null;
		
		public ConfigProperty(){
															
		}
		
		public HashMap<String, String> setPropValues() throws IOException, ConfigurationException {
			//file in jar
			//config = new PropertiesConfiguration(getClass().getResource("/Constants/config.properties"));
			
			//external file
			config = new PropertiesConfiguration(configFile);
			
			try {
				//Properties prop = new Properties();
				//String propFileName = "config.properties";
																			
				//if the folder containing properties file is in the build path
				//inputStream = getClass().getClassLoader().getResourceAsStream(propFileName); 
				//inputStream = getClass().getResourceAsStream(propFileName);
				//inputStream = ConfigProperty.class.getClassLoader().getResourceAsStream("config.properties");										
				// BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
				// get the property value and print it out
				
				urlMap.put(homeURL, config.getString("homeURL").replace("\r", ""));
				urlMap.put(chromeDriverPath, config.getString("chromeDriverPath").replace("\r", ""));
				urlMap.put(binary, config.getString("binary").replace("\r", ""));
				urlMap.put(binaryPath, config.getString("binaryPath").replace("\r", ""));

															
				//result = "url List = " + homeURL + ", " + estatementRegisterURL + ", " + ePaymentReminderURL;
				System.out.println(result);
			} catch (Exception e) {
				System.out.println("Exception: " + e);
			} finally {
				//inputStream.close();
			}
			return urlMap;
		}
		
		
		public HashMap<String, String> setPropValues2() throws IOException, ConfigurationException {
			//config = new PropertiesConfiguration(getClass().getResource(configFile));
			Properties prop = new Properties();
			inputStream = ConfigProperty.class.getClassLoader().getResourceAsStream("Case/config.properties");	
			prop.load(inputStream);
			
			try {
				//Properties prop = new Properties();
				//String propFileName = "config.properties";
																			
				//if the folder containing properties file is in the build path
				//inputStream = getClass().getClassLoader().getResourceAsStream(propFileName); 
				//inputStream = getClass().getResourceAsStream(propFileName);
				//inputStream = ConfigProperty.class.getClassLoader().getResourceAsStream("config.properties");										
				// BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
				// get the property value and print it out
				
				urlMap.put(homeURL, prop.getProperty("homeURL").replace("\r", ""));
				urlMap.put(chromeDriverPath, prop.getProperty("chromeDriverPath").replace("\r", ""));
				urlMap.put(binary, config.getString("binary").replace("\r", ""));
				urlMap.put(binaryPath, config.getString("binaryPath").replace("\r", ""));

				//result = "url List = " + homeURL + ", " + estatementRegisterURL + ", " + ePaymentReminderURL;
				System.out.println(result);
			} catch (Exception e) {
				System.out.println("Exception: " + e);
			} finally {
				//inputStream.close();
			}
			return urlMap;
		}
	}

