package Process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import Case.Case;
import Driver.WebUI;
import Main.ProcessMain;
import Tools.Constants;
import Tools.EmailSender;
import Tools.ProcessWriter;
import Tools.StringUtil;
import Tools.XlsReader;

import com.dbs.rtpo.exception.ProcessException;


//import IServe.IServeProcess;
//import com.dbs.rtpo.logon.ProcessMain;


public class Process {
	public static Logger logger = Logger.getLogger(ProcessMain.class);

	protected static ArrayList<Case> caseList = new ArrayList<Case>();
	//must be static or subclass cant access the same value
	protected static Case currentOutputCase = null;
	protected static WebUI driver = null;
	//private Map<String,RequestDetail> requestSummary;
	//private CustomerRequest customerRequest;
	//public IServeCreditCardTabPage CreditCardPage;
	//public IServeCashlineTabPage CashlinePage;
	//public Logger logger = Logger.getLogger(this.getClass());
	//private IServeProcess iserveProcess = null;
	protected String filename = null;
	protected static  ArrayList<String> keywords;
	//protected ProcessWriter processWriter;
	protected XSSFWorkbook workbook = null;
	
	private static String vplusOutcome = "";
	private String fromDate = "";
	private String toDate = "";
	
	HashMap<String,String> handleMap = null;

	private boolean vplusStarted = false;
	String currentScreenshotName = "";
	String pngFolderPath = "C:\\Users\\User\\Desktop\\sgbroadcast test";
	//private XSSFWorkbook workbook;
	/*													
	public IServe(WebDriver driver,CustomerRequest customerRequest,Map<String,RequestDetail> requestSummary){
		this.driver = driver;
		this.customerRequest = customerRequest;
		this.requestSummary = requestSummary;
	}
	*/
	//private static WebUI webUI;
	//public ProcessWriter processWriter;
	
	public Process(WebUI driver){
		//logger.error("fromdate = "+fromDate+" , toDate = "+toDate);
		//this.fromDate = fromDate;
		//this.toDate = toDate;
		//ibAdmin = new IBAdmin(driver, logger);
		//this.workbook = workbook;
		//this.filename = filename;
		Process.driver = driver;
		/*
		try {
			processWriter = new ProcessWriter(workbook);
			//processWriter.setHeaders(); //output headers already exist
		} catch (IOException e) {
			logger.error("Write error process");
			e.printStackTrace();
		}
		*/
	}
	
	/*
	public static void main(String[] args){
		//startDriver();
		//Process process = new Process();
		//process.start();
		//IServeProcess iserveProcess = new IServeProcess(driver);
		//iserveProcess.start();
	}
	*/
	

	
	
	//when keyword matches									
	protected int run(LinkedHashMap<String,String> map) throws NoSuchElementException, TimeoutException, ProcessException {
		logger.info(map);
		driver.clickBySelenium(By.xpath("//*[@id='header_advancesearch_link']"));
		driver.input(By.xpath("//*[@id='key']"), map.get(Constants.KEYWORD));
		
		if(!map.get(Constants.LOCATION).contains("Select")){
			driver.clickBySelenium(By.xpath("//*[@id='tbLocOpen']/div[2]"));
			//wait till location popup appears after clicking select location
			driver.searchTargetElement(By.xpath("//*[@id='TB_window']"));
			switch(map.get(Constants.LOCATION)){
				case "Johor":
					driver.clickBySelenium(By.xpath("//*[@id='location50100']"));//johor
					break;
				case "Kuala Lumpur":
					driver.clickBySelenium(By.xpath("//*[@id='location50300']"));//kl
					break;
			}
			driver.clickBySelenium(By.xpath("//*[@id='locConBtn']"));
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//IT-Software IT-Hardware
		if(!map.get(Constants.SPECIALIZATION).contains("Select")){		
			driver.clickBySelenium(By.xpath("//*[@id='tbSpeOpen']/div[2]"));
			driver.searchTargetElement(By.xpath("//*[@id='TB_window']"));
			
			switch(map.get(Constants.SPECIALIZATION)){
			case "IT-Software":
				driver.clickBySelenium(By.xpath("//*[@id='specialization191']"));
				break;
			case "IT-Hardware":
				driver.clickBySelenium(By.xpath("//*[@id='specialization192']"));
				break;
			}
			driver.clickBySelenium(By.xpath("//*[@id='speConBtn']"));
		}
		
		driver.simpleInput(By.xpath("//*[@id='salary']"), map.get(Constants.MONTHLYSALMIN));
		driver.simpleInput(By.xpath("//*[@id='salary-max']"), map.get(Constants.MONTHLYSALMAX));
		
		if(!map.get(Constants.POSITIONLEVEL).contains("Select")){
			switch(map.get(Constants.POSITIONLEVEL)){
			case "Manager":
				driver.clickBySelenium(By.xpath("//*[@id='position2']"));
				break;
			case "Junior Executive":
				driver.clickBySelenium(By.xpath("//*[@id='position4']"));//junior executive
				break;
			case "Non-Executive":
				driver.clickBySelenium(By.xpath("//*[@id='position6']"));
				break;
			}
		}
		
		driver.clickBySelenium(By.xpath("//*[@id='showExtra']/span"));//expand
		
		if(!map.get(Constants.JOBTYPE).contains("Select")){
			while(true){		
				try{
					switch(map.get(Constants.JOBTYPE)){
					case "Full Time/Contract":
						driver.clickBySelenium(By.xpath("//*[@id='job-type5']"));//full time/contract
						break;
					case "Part Time/Temporary":
						driver.clickBySelenium(By.xpath("//*[@id='job-type10']"));//full time/contract
						break;
					case "Internship":
						driver.clickBySelenium(By.xpath("//*[@id='job-type16']"));//full time/contract
						break;
					}
					break;
				} catch (Exception e){
					driver.clickBySelenium(By.xpath("//*[@id='showExtra']/span"));//expand
				}
				
			}
		}
		
		driver.selectText(By.xpath("//*[@id='experience-min']"), "1 year");
		driver.selectText(By.xpath("//*[@id='experience-max']"), "18 years");

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.click(By.xpath("//*[@id='sOptCon']/div[8]/div[1]/div[2]/input"));
		
		driver.checkPageIsReady();
		
		int index = 2;
		while(true){
			try{
				System.out.println("------");
				String title = driver.findDataFromElementText(By.xpath("/html/body/div[1]/div[3]/div[1]/div[2]/div["+index+"]/div[1]/div[2]/a"),10);
				
				System.out.println("title = "+ title +" null or empty? "+StringUtil.isEmpty(title));
				if(title==null){
					title = driver.findDataFromElementText(By.xpath("/html/body/div[1]/div[3]/div[1]/div[2]/div["+index+"]/div[1]/div[1]/a"),10);
					System.out.println("alternative title = "+ title +" null or empty? "+StringUtil.isEmpty(title));
				}
				String company = driver.findDataFromElementText(By.xpath("/html/body/div[1]/div[3]/div[1]/div[2]/div["+index+"]/div[1]/h3"),10);
				System.out.println("company = "+ company +" null or empty? "+StringUtil.isEmpty(company));
			//System.out.println("company = "+ driver.findDataFromElementText(By.xpath("/html/body/div[1]/div[3]/div[1]/div[2]/div[2]/div[1]/h3")));
				
				if(StringUtil.isEmpty(company) || StringUtil.isEmpty(title))
				{
					System.out.println("no more records");
					break;
				} else {
					Case case1 = new Case(company, title);
					case1.setStatus("completed");
					ProcessMain.xlsWriter.writeResult(case1);
				}
				index++;
			} catch (Exception e){
				e.printStackTrace();
				System.out.println("exception");
				break;
			}
		}
		return Tools.Constants.OUTCOME_SUCCESS;
	}

	
	public void start(LinkedHashMap<String,String> searchMap){	
		ProcessMain.updateLabelText("Running...");
		EmailSender.loadJacobLibrary();

		caseList = new ArrayList<Case>();
		
		XlsReader reader = new XlsReader(filename);
		String url = driver.getCurrentUrl();
		driver.checkPageIsReady();
		
		LinkedHashMap<String, String> testMap = new LinkedHashMap<String,String>();
		testMap.put(Constants.KEYWORD, "java");
		testMap.put(Constants.LOCATION, "test");

		try {
			run(searchMap);
		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public static void addCase(Case case1){
		caseList.add(case1);
	}
	
	
	
}
