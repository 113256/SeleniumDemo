package Process;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Driver.WebUI;

public class testClass extends Process{

	public testClass(WebUI driver, String filename, XSSFWorkbook workbook) {
		super(driver, filename, workbook);
		// TODO Auto-generated constructor stub
	}
	
	public void test(){
		System.out.println(currentOutputCase.getCcNumber());
	}

}
