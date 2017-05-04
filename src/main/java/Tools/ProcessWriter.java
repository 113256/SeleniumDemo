package Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Case.Case;
import Main.ProcessMain;
import Main.testClass;

public class ProcessWriter {
	
	//private static FileInputStream file;
	private XSSFWorkbook workbook = null;
	
	private XSSFSheet sheet = null;
	
	//private String filename;
	//private String outputFile;
	
	private int startingCell = 0;
	private String[] outputHeaders;
	private FileOutputStream out;
	/*
	public void close(){
		 //write operation workbook using file out object 
	      try {
			workbook.write(out);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	}
	*/
	public ProcessWriter(XSSFWorkbook workbook) throws IOException{

		this.workbook = workbook;
		int sheetIndex = this.workbook.getSheetIndex("Sheet1");
		if(sheetIndex == -1){
		    sheet = this.workbook.createSheet("Sheet1");
		} else {
			sheet = this.workbook.getSheet("Sheet1");
		}
		setHeaders();
		//sheet = this.workbook.createSheet("Sheet1");
		//sheet = this.workbook.getSheetAt(1);
		startingCell = 0;
		/*this.workbook = workbook;
		switch(processNo){
		case 1:
			sheet = workbook.getSheet("CARDS");
			outputHeaders = Constants.CREDITCARD_OUTPUT;
			startingCell = 107;
			break;
		case 2:
			sheet = workbook.getSheet("Cashline");
			outputHeaders = Constants.CASHLINE_OUTPUT;
			startingCell = 52;
			break;
		case 3:
			sheet = workbook.getSheet("IL");
			outputHeaders = Constants.IL_OUTPUT;
			startingCell = 21;
			break;
		}*/
	}
									
	//starting cell = starting column
	
	public void setHeaders(){
		int startCell = startingCell;
		//String[] outputHeaders = Constants.CREDITCARD_OUTPUT;
		String[] outputHeaders = Constants.FILE_HEADER_OUTPUT;
		
		for(String s : outputHeaders){
			System.out.println(s);
		}
		System.out.println(startingCell);
		for(int i = 0; i < outputHeaders.length; i++, startCell++){
			Row row = null;
			if(sheet.getRow(0)==null){
				row = sheet.createRow(0);
			} else {
				row = sheet.getRow(0);
			}
			row.createCell(startCell).setCellValue(outputHeaders[i]);
			
			//cant read from cell if its blank so create cell
			//cell = sheet.getRow(2).getCell(startingCell);//DG
			//System.out.println(outputHeaders[i]);
			//cell.setCellValue(outputHeaders[i]);
			
		}
		
	}
	
	/*
	public void writeResult(int rowNumber, ArrayList<String> results){
		for(String r : results){
			int startCell = startingCell;
			Row row = sheet.getRow(rowNumber);
			row.createCell(startCell).setCellValue(r);
			startCell++;
		}
		
		}
	 */
	
	public static boolean isRowEmpty(Row row) {
	    //for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
		for (int c = 0; c < 2; c++) {
	        Cell cell = row.getCell(c);
	        if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
	            return false;
	    }
	    return true;
	}
	
	public void writeResult(Case case1) {
		Row row = sheet.createRow(ProcessMain.outputRow);
		ProcessMain.outputRow++;
		
		//System.out.println(sheet == null);
		//Row row = sheet.createRow(rowNumber);
		ArrayList<String> results = new ArrayList<String>();
		//System.out.println("writing app no "+case1.getApplicationNumber());
		
		
		//results.add(case1.getSn());
		
		////System.out.println("write outcome " +case1.getOutcome());
		//results.add(case1.getSubject());
		
		
		
		results.add(case1.getCompany());
		results.add(case1.getTitle());

		results.add(case1.getStatus());
		
		int startCell = startingCell;
								
		for(String r : results){	
			try {
				row.createCell(startCell).setCellValue(r);
			} catch (Exception e){
				row.getCell(startCell).setCellValue(r);
			}
			
			startCell++;
		}
	}
	
	
}
		


