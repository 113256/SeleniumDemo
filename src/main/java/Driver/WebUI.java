package Driver;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;

//import IServe.IServeCommonElements.java;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.common.base.Strings;


import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.log4j.Logger;
//import org.apache.commons.configuration.ConfigurationException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Tools.StringUtil;

import com.dbs.rtpo.exception.ProcessException;

public class WebUI{
	  private WebDriver driver;    
	  public Driver.IEProtectedMode IEProtectedMode = null;

	  public Logger logger = Logger.getLogger(this.getClass());
	   private HashMap<String, String> urlMap = new HashMap<String, String>();
	  
	   public void close(){
		   driver.quit();
	   }
	   
	   
	   public void click(By targetElement) throws ProcessException {
			searchTargetElement(targetElement);
			WebElement Webelement= driver.findElement(targetElement);
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("arguments[0].click()", Webelement); 
		}
	   
	   public void altTab() throws AWTException{
		   Robot robot = new Robot();
		   robot.keyPress(KeyEvent.VK_ALT);
		   robot.keyPress(KeyEvent.VK_TAB);
		   robot.delay(200);
		   robot.keyRelease(KeyEvent.VK_ALT);
		   robot.keyRelease(KeyEvent.VK_TAB);
	   }
	   
	   public String getLibraryPath(String folderPrefix){
	        String libraryFolderName = "";
	        String currentDirPath = System.getProperty("user.dir");
	        File batchFileFile = new File(currentDirPath);
	        File batchFilePath = batchFileFile.getParentFile();
	        currentDirPath = batchFilePath.getPath();
	        File directory = new File(currentDirPath);
	        File[] subdirs = directory.listFiles((FilenameFilter) DirectoryFileFilter.DIRECTORY);
	        for (File dir : subdirs) {
	        	logger.info("Directory: " + dir.getName());
	               if (dir.getName().startsWith(folderPrefix)) {
	                     libraryFolderName = dir.getName();
	               }
	        }
	        logger.info("UILibrary folder name: "+ libraryFolderName);
	        return libraryFolderName;
		}
	   
	   public WebUI(boolean chrome){
			String currentPath = System.getProperty("user.dir");
			File currentFile = new File(currentPath);
			File parentFolderPath = currentFile.getParentFile();
			String parentFilePath = parentFolderPath.getAbsolutePath();
			
			if(chrome){
				//get chromedriver from folder called ChromeDriver one level above
				String chromeDriverPath = parentFilePath + File.separator + getLibraryPath("ChromeDriver") + File.separator + "chromedriver.exe";
				logger.info(chromeDriverPath);
				System.setProperty("webdriver.chrome.driver", chromeDriverPath);
				
				ChromeOptions chromeOptions = new ChromeOptions();
				
				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
				//only jack pc
				chromeOptions.setBinary("C:/Users/User/AppData/Local/Google/Chrome/Application/57.0.2987.133/chrome.exe");
				chromeOptions.addArguments("disable-extensions");
				chromeOptions.addArguments("disable-plugins");
				
				try{
					System.setProperty("webdriver.chrome.driver", chromeDriverPath);
					driver = new ChromeDriver(capabilities);
				} catch (WebDriverException e){
					logger.info("webdriver exception: " + "" + e.getMessage());
				} catch (Exception e){
					e.printStackTrace();
					logger.info(e.getMessage());
				}
			} else {
				String ieDriverPath = parentFilePath + File.separator
						+ getLibraryPath("IEDriverServer") + File.separator
						+ "IEDriverServer.exe";
				// chromeDriverPath =
				// "D:/Jack/sg/Workspace/Workspace/RTPO2.0/chromedriver.exe";
				System.out.println(ieDriverPath);
				System.setProperty("webdriver.ie.driver", ieDriverPath);
				
				IEProtectedMode = new Driver.IEProtectedMode();
				IEProtectedMode.setProtectedMode("0");
				IEProtectedMode.setZoomLevel("100000");
				
				System.out.println("initialize WebDriver.");
				if (!IEProtectedMode.isSetSuccessfully()) {
					System.out.println("Override security rule by capabilities.");
					DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
					caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
					
					driver = new InternetExplorerDriver(null, caps, 5555);
				} else {
					System.out.println("Override security rule by reg key.");
					driver = new InternetExplorerDriver(5555);
				}
			}
			
	   }
	   
	   
	 //singleton
	   /*
		 public static WebUI getInstance() {
	        if( instance == null ) {
	            instance = new WebUI();
	        }
	         return instance;
	     }
		*/
	   
	  


	public void switchSpecialTabView(Integer tabPos){
			logger.info("Switch to tab " + tabPos);
			new Actions(driver).keyDown(Keys.CONTROL).perform();
			new Actions(driver).sendKeys("" + tabPos).perform();
			new Actions(driver).keyUp(Keys.CONTROL).perform();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	   public void switchNextTabView(){
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "" + Keys.TAB);
		}
		public void FocusWindow(){
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.focus()"); 
		}
		
		public void switchHandle(String handle){
			driver.switchTo().window(handle);
		}
		
		
	   public String getWindowHandle(){
		   return driver.getWindowHandle();
	   }
	   
	   public void loopHandle() {
			for (String handle : driver.getWindowHandles()) {
				logger.info("handle: "+handle);
				driver.switchTo().window(handle);
			}
		}
	   
	   public Set<String> getWindowHandles(){
		   return driver.getWindowHandles();
	   }
	   
	   public void quit(){
		   driver.quit();
	   }
	   
	   
	  public WebDriver getDriver(){
	     return driver;
	  }
	  
	  public void get(String url){
		  driver.get(url);
	  }
	  

		public boolean exists(By clickId)throws NoSuchElementException, TimeoutException{
			return driver.findElements(clickId).size() != 0;
		}
		public List<WebElement> searchTargetElementList(By targetElement) throws ProcessException {
			List<WebElement> titleElement = null;
			int count = 0;
			boolean end = false;
			while(!end){
				System.out.println(count);
				//driver.switchTo().parentFrame();
				handleAlert();
				try {
					titleElement = driver.findElements(targetElement);
				}catch (Exception e){
					e.printStackTrace();
				}
				
				//check if the element get is null
				if(titleElement != null){
					end = true;
					return titleElement;
				}else{
//					logger.error("Element retrieved is null.");
				}
				
				if(!end){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
//						logger.error("Sleep for 100 milli seconds.");
					}
					logger.error("-c-"+count+"-");
					count++;
					logger.error("Timeout counter: " + count);
					if (count > 150){
						//logger.error("Timeout.");
						handleAlert();
						//click(LOGO);
						throw new ProcessException("Timeout in credit card tab page.");
					}
					
				}
			}
			return null;
		}		
		
		
		public List<WebElement> searchTargetElementListNest(WebElement w, By targetElement) throws ProcessException {
			List<WebElement> titleElement = null;
			int count = 0;
			boolean end = false;
			while(!end){
				System.out.println(count);
				//driver.switchTo().parentFrame();
				handleAlert();
				try {
					titleElement = w.findElements(targetElement);
				}catch (Exception e){
					e.printStackTrace();
				}
				
				//check if the element get is null
				if(titleElement != null){
					end = true;
					return titleElement;
				}else{
//					logger.error("Element retrieved is null.");
				}
				
				if(!end){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
//						logger.error("Sleep for 100 milli seconds.");
					}
					logger.error("-c-"+count+"-");
					count++;
					logger.error("Timeout counter: " + count);
					if (count > 150){
						//logger.error("Timeout.");
						handleAlert();
						//click(LOGO);
						throw new ProcessException("Timeout in credit card tab page.");
					}
					
				}
			}
			return null;
		}		
		
	
		
		private void handlePopUpErrorMessage(By popUpAlert, By popUpAlertOKButton) throws ProcessException {
			WebElement element;
			try{
				element = driver.findElement(popUpAlert);
				if(element.isDisplayed()){
					logger.info("Pop up box is displayed.");
					String message = element.getText().trim();
					logger.info("Pop up box message: " + message);
					WebElement Webelement= driver.findElement(popUpAlertOKButton);
					JavascriptExecutor jse = (JavascriptExecutor)driver;
					try{
						Webelement.click();
					}catch(Exception e){
						jse.executeScript("angular.element(arguments[0]).triggerHandler('click')", Webelement); 
					}
				}
			}catch(Exception e){
//				logger.info("No pop up.");
			}
		}
		
		                	
		public static boolean isEmpty(String string) throws NoSuchElementException, TimeoutException{
			return string.equals("") || string.equals(" ") || string == null;
		}
		
		public void switchFrame(Object id){
			try{
				driver.switchTo().defaultContent();
				recursiveFindFrameId(id);
			}catch(Exception e){
				logger.error("Error when switch frame: " , e);
			}
		}
		public boolean recursiveFindFrameId(Object id) {
			
			if (id == null) return false;
			
			int i=0;

			try { 
				if (id instanceof String) {
					driver.switchTo().frame((String)id);
				} else if (id instanceof Integer) {
					driver.switchTo().frame((Integer)id);
				} else if (id instanceof WebElement) {
					driver.switchTo().frame((WebElement)id);
				}
				return true;
			} catch (Exception e) {
			}
			
			try {
				while (true) {
					driver.switchTo().frame(i);
					if (recursiveFindFrameId(id)) return true;
					i++;
				}
			} catch (Exception e) {
				driver.switchTo().parentFrame();
			}
			return false;
		}
		public void goForm(){
			((JavascriptExecutor) driver).executeScript(
	                "LoadPageToMainForm('https://cti-test.twn.dbs.com/Webflow/FlowApplyListLo?&ICW_AGENTID=6003&LANGUAGEMODE=FIELD1&PROGRAMCODE=612')");
			
		}
		
		public void testjs(){
			((JavascriptExecutor) driver).executeScript(
					"document.getElementById('Frame1').contentDocument.getElementByXpath('//*[@id='tb_body']/tbody/tr[2]/td/table/tbody/tr[3]/td[9]/span')");
		}
		
		public void checkPageIsReady() throws NoSuchElementException, TimeoutException{
		  JavascriptExecutor js = (JavascriptExecutor)driver;
		  																
		  //Initially check ready state of page.
		  if (js.executeScript("return document.readyState").toString().equals("complete")){ 
		   logger.info("Page Is loaded.");
		   return; 
		  } 
		  
		  //This loop will rotate for 25 times to check If page Is ready after every 1 second.
		
		  for (int i=0; i<25; i++){ 
		   try {
		    Thread.sleep(1000);
		    }catch (InterruptedException e) {} 
		   //To check page ready state.
		   if (js.executeScript("return document.readyState").toString().equals("complete")){ 
		    break; 
		   }   
		  }  
		}

		public void searchTargetElement(By targetElement) throws ProcessException, NoSuchElementException, TimeoutException{
			WebElement titleElement = null;
			int count = 0;
			boolean end = false;
			while(!end){
				//this.waitUntilFinishLoading(IServe.IServeCommonElements.LOADING_IMAGE);
				driver.switchTo().parentFrame();
				try {
					titleElement = driver.findElement(targetElement);
				}catch (Exception e){}
				
				//check if the element get is null
				if(titleElement != null){
					end = true;
					break;
				}else{
					//logger.info("Element retrieved is null.");
				}
				
				if(!end){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						//logger.info("Sleep for 100 milli seconds.");
					}
					System.out.print("-c-"+count+"-");
					count++;
					//logger.info("Timeout counter: " + count);
					//if (count > IServe.IServeCommonElements.TIME_FOR_REFRESH/100){
					if (count > 160){
						
						//logger.info("Timeout.");
						//handleAlert();
						//clickBySelenium(IServeCommonElements.LOGO);
						throw new ProcessException("Timeout in Iserve search page.");
					}
				}
			}
		}
		
		public void searchTargetElement(By targetElement, int time) throws ProcessException, NoSuchElementException, TimeoutException{
			WebElement titleElement = null;
			int count = 0;
			boolean end = false;
			while(!end){
				//this.waitUntilFinishLoading(IServe.IServeCommonElements.LOADING_IMAGE);
				driver.switchTo().parentFrame();
				try {
					titleElement = driver.findElement(targetElement);
				}catch (Exception e){}
				
				//check if the element get is null
				if(titleElement != null){
					end = true;
					break;
				}else{
					//logger.info("Element retrieved is null.");
				}
				
				if(!end){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						//logger.info("Sleep for 100 milli seconds.");
					}
					System.out.print("-c-"+count+"-");
					count++;
					//logger.info("Timeout counter: " + count);
					//if (count > IServe.IServeCommonElements.TIME_FOR_REFRESH/100){
					if (count > time){
						
						//logger.info("Timeout.");
						//handleAlert();
						//clickBySelenium(IServeCommonElements.LOGO);
						throw new ProcessException("Timeout in Iserve search page.");
					}
				}
			}
		}
		
		public void handleAlert() throws NoSuchElementException, TimeoutException{
			try {
				Alert alert = driver.switchTo().alert();
				String alertMsg = alert.getText();
				if (!StringUtil.isNullOrEmpty(alertMsg)) {
					logger.info(alertMsg);
					logger.info(alertMsg);
				}
				alert.accept();
			} catch (Exception e) {}
		}
		
		public void select(By targetElement, String selectValue) throws ProcessException, NoSuchElementException, TimeoutException {
			searchTargetElement(targetElement);
			new Select(driver.findElement(targetElement)).selectByValue(selectValue);
		}
		
		public void selectIndex(By targetElement, int selectValue) throws ProcessException, NoSuchElementException, TimeoutException {
			searchTargetElement(targetElement);
			new Select(driver.findElement(targetElement)).selectByIndex(selectValue);
		}
		
		public void selectText(By targetElement, String selectValue) throws ProcessException, NoSuchElementException, TimeoutException{
			searchTargetElement(targetElement);
			new Select(driver.findElement(targetElement)).selectByVisibleText(selectValue);
		}

		public void clickWebElement(WebElement target){
			target.click();
		}
		
		public void clickBySelenium(By targetElement) throws ProcessException, NoSuchElementException, TimeoutException {
			searchTargetElement(targetElement);
			/*
			try{
				driver.findElement(targetElement).click();
			}catch(Exception e){
				logger.info("Error occur when click: " + e.getMessage());
				clickByJavascript(targetElement);
				logger.info("click again.");
			}
			*/
			WebDriverWait wait = new WebDriverWait(driver, 5);
		    wait.until(ExpectedConditions.elementToBeClickable(targetElement));
		    
			driver.findElement(targetElement).click();
		}
		
		public void clickByJavascript(By targetElement) throws ProcessException, NoSuchElementException, TimeoutException{
			searchTargetElement(targetElement);
			WebElement Webelement= driver.findElement(targetElement);
			/*
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("arguments[0].focus()", Webelement); 
			jse.executeScript("arguments[0].click()", Webelement); 
			*/
			
			try{
				JavascriptExecutor jse = (JavascriptExecutor)driver;
				jse.executeScript("arguments[0].focus()", Webelement); 
				jse.executeScript("arguments[0].click()", Webelement); 
			}catch (Exception e){
				logger.info("Error occur when click by javascript: target: " + targetElement.toString() +" "+ e.getMessage());
				clickBySelenium(targetElement);
				logger.info("attempting to click again using selenium.");
			}
		}

		public void input(By targetElement, String inputValue) throws ProcessException, NoSuchElementException, TimeoutException{
			if (!StringUtil.isNullOrEmpty(inputValue)) {
				searchTargetElement(targetElement);

				WebElement element = driver.findElement(targetElement);
				element.clear();
				logger.info("Cleared content.");
				
				clickBySelenium(targetElement);
				logger.info("Clicked content.");
				
				JavascriptExecutor jse = (JavascriptExecutor)driver;
				jse.executeScript("arguments[0].focus()", element); 
				logger.info("focus textfield.");

				StringSelection stringSelection = new StringSelection(inputValue);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
				element.sendKeys(Keys.CONTROL + "v");
				logger.info("clicked control v.");
				
//				element.sendKeys(inputValue);

				if (inputValue.equals(element.getAttribute("value"))) {
					logger.info("*return");
					return;
				} else {
					logger.info("again");
					input2(targetElement, inputValue);
				}
			}
		}
		
		public void simpleInput(By targetElement, String inputValue) throws ProcessException, NoSuchElementException, TimeoutException{
			if (!StringUtil.isNullOrEmpty(inputValue)) {
				searchTargetElement(targetElement);

				WebElement element = driver.findElement(targetElement);
				element.clear();
				logger.info("Cleared content.");
				
				clickBySelenium(targetElement);
				logger.info("Clicked content.");
				
				JavascriptExecutor jse = (JavascriptExecutor)driver;
				jse.executeScript("arguments[0].focus()", element); 
				logger.info("focus textfield.");

				
				element.sendKeys(inputValue);

			}
		}
		
		public void input2(By targetElement, String inputValue) throws ProcessException, NoSuchElementException, TimeoutException{
			if (!StringUtil.isNullOrEmpty(inputValue)) {
				searchTargetElement(targetElement);

				WebElement element = driver.findElement(targetElement);
				element.clear();
//				logger.info("Cleared content.");
				
				clickBySelenium(targetElement);
//				logger.info("Clicked content.");
				
				JavascriptExecutor jse = (JavascriptExecutor)driver;
				jse.executeScript("arguments[0].focus()", element); 
//				logger.info("focus textfield.");
				
				element.sendKeys(inputValue);

				if (inputValue.equals(element.getAttribute("value"))) {
//					logger.info("*return");
					return;
				} else {
//					logger.info("again");
					input(targetElement, inputValue);
				}
			}
		}
		
		public void clearInput(By targetElement) throws ProcessException, NoSuchElementException, TimeoutException{
			searchTargetElement(targetElement);
			driver.findElement(targetElement).clear();
		}

		public String findDataFromValueAttribute(By targetElement) throws ProcessException {
			searchTargetElement(targetElement);
			return driver.findElement(targetElement).getAttribute("value");
		}
		
		public String findDataFromElementText(By targetElement) throws NoSuchElementException, TimeoutException{
			try {
				searchTargetElement(targetElement);
			} catch (ProcessException e) {
				return null;					
			}
			if(driver.findElement(targetElement).getText().trim() == null ){
				return "";
			} else {
				return driver.findElement(targetElement).getText().trim();
			}
		}
		
		
		public String findDataFromElementText(By targetElement, int time) throws NoSuchElementException, TimeoutException{
			try {
				searchTargetElement(targetElement, time);
			} catch (ProcessException e) {
				return null;					
			}
			if(driver.findElement(targetElement).getText().trim() == null ){
				return "";
			} else {
				return driver.findElement(targetElement).getText().trim();
			}
		}
		public String findDataFromElementValue(By targetElement) throws NoSuchElementException, TimeoutException{
			try {
				searchTargetElement(targetElement);
			} catch (ProcessException e) {
				return null;		
			}
			if(driver.findElement(targetElement).getAttribute("value").trim()==null){
				return "";
			} else {
				return driver.findElement(targetElement).getAttribute("value").trim();
			}
			
		}
		
		public List<WebElement> findElements(By val) throws NoSuchElementException, TimeoutException{
			return driver.findElements(val);
		}
		
		public String getCurrentUrl(){
			return driver.getCurrentUrl();
		}
		public TargetLocator switchTo(){
			return driver.switchTo();
		}
		
		public String findDataFromSelect(By targetElement) throws ProcessException, NoSuchElementException, TimeoutException{
			searchTargetElement(targetElement);
			return new Select(driver.findElement(targetElement)).getFirstSelectedOption().getText();
		}
		
		public String findDataFromRadios(By targetElement) throws ProcessException, NoSuchElementException, TimeoutException{
			searchTargetElement(targetElement);
			String value = null;
			for(WebElement element : driver.findElements(targetElement)) {
				if (!StringUtil.isNullOrEmpty(element.getAttribute("CHECKED"))) {
					value = element.getAttribute("value");
				}
			}
			return value;
		}
		
		public boolean isElementDisplayed(By targetElement) throws NoSuchElementException, TimeoutException{
			WebElement element = driver.findElement(targetElement);
			if(element.isDisplayed()){
				return true;
			}else{
				return false;
			}
		}
		
		
		public Navigation navigate() {
			return driver.navigate();
		}


		public void maximize() {
			driver.manage().window().maximize();
			
		}
	

	  
	}