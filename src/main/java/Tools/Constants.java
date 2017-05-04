package Tools;

import org.apache.commons.csv.CSVFormat;


public class Constants {
	public static final int OUTCOME_ERROR = 1;
	public static final int OUTCOME_SUCCESS = 2;
	public static final int OUTCOME_PROCEED = 0;
	public static final int OUTCOME_SKIP = 3;
	
	public static final String TIME_OUT = "Time Out";
	
	public static final String LOCATION = "Title";
	public static final String SPECIALIZATION = "Company";
	public static final String MONTHLYSALMIN = "Min Monthly Salary";
	public static final String MONTHLYSALMAX = "Max Monthly Salary";
	public static final String POSITIONLEVEL = "Position level";
	public static final String JOBTYPE = "jobtype";

	public static final String KEYWORD = "Keyword";
	public static final String STATUS = "Status";
	
	public static final String TITLE = "title";
	public static final String COMPANY = "company";
	

	//public static final String [] FILE_HEADER_INPUT = {NAME, CIN_NUMBER, ACCOUNT_NUMBER, DEBIT_GIRO, SMS_TEMPLATE};
	public static final String [] FILE_HEADER_INPUT = {KEYWORD,LOCATION,SPECIALIZATION,MONTHLYSALMIN,MONTHLYSALMAX,POSITIONLEVEL };
	public static final String [] FILE_HEADER_OUTPUT = {TITLE, COMPANY, STATUS};
	
	//public static final String [] FILE_HEADER = {"documentType","documentNumber","alertType","accountType","status","email","username"};
	public static final CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader(FILE_HEADER_INPUT);
																	
	
	public static final String JOBSTREETURL = "https://www.jobstreet.com.my/en/job-search/job-vacancy.php?ojs=6";
	
}
