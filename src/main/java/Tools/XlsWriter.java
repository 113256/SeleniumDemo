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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Case.Case;
import Main.ProcessMain;

public class XlsWriter {
	
	private static FileInputStream file = null;
	private static XSSFWorkbook workbook;
	
	
	private String filename;
	private String outputFile;
	
	private FileOutputStream outFile;
	
	private XSSFSheet sheet = null;
	
	//private String filename;
	//private String outputFile;
	
	private int startingCell = 0;
	private String[] outputHeaders;
		
	public XlsWriter(){
	}
	
	public void start(String outputFile, boolean firstRun) throws IOException{
		//this.filename = outputFile;
		this.outputFile = outputFile;
		//file = new FileInputStream(new File(this.filename));
		System.out.println("outputfile "+this.outputFile);
		if(firstRun){
			//System.out.println(filename+"out: "+outputFile);
			//not workbook = new XSSFWorkbook(file) because that output file will copy input file at the start
			workbook = new XSSFWorkbook();
			
		} else {
			//subsequent runs- appending to output file of 1st run.
			workbook = new XSSFWorkbook(new FileInputStream(outputFile));
		}
		
		init();
		
	}
	
	public void start(String filename, String outputFile, boolean copyInput) throws IOException{
		this.filename = filename;
		this.outputFile = outputFile;
		file = new FileInputStream(new File(this.filename));
		
		if(!filename.equals(outputFile)){
			System.out.println(filename+"out: "+outputFile);

			if(copyInput){
				//either transfer or workbook = new XSSFWorkbook(file)
				workbook = new XSSFWorkbook(file);
				//transfer(filename, outputFile);
			} else {
				//not workbook = new XSSFWorkbook(file) because that output file will copy input file at the start
				workbook = new XSSFWorkbook();
			}
		} else {
			//subsequent runs- appending to output file of 1st run.
			//workbook = new XSSFWorkbook(file);
			workbook = new XSSFWorkbook(new FileInputStream(file));
		}
		
		init();
		
	}

	private void init(){
		//this.workbook = workbook;
		int sheetIndex = workbook.getSheetIndex("Sheet1");
		if(sheetIndex == -1){
		    sheet = workbook.createSheet("Sheet1");
		} else {
			sheet = workbook.getSheet("Sheet1");
		}
		setHeaders();
		//sheet = this.workbook.createSheet("Sheet1");
		//sheet = this.workbook.getSheetAt(1);
		startingCell = 0;
	}
	
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
			
		}
		
	}
	
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
		System.out.println("output row = "+ProcessMain.outputRow);
		//System.out.println(sheet == null);
		//Row row = sheet.createRow(rowNumber);
		ArrayList<String> results = new ArrayList<String>();
		//System.out.println("writing app no "+case1.getApplicationNumber());
		
		
		//results.add(case1.getSn());
		
		////System.out.println("write outcome " +case1.getOutcome());
		//results.add(case1.getSubject());
		
		
		
		results.add(case1.getCompany());
		//results.add(case1.getFromDate());
		//results.add(case1.getToDate());
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
	
	public void close() throws IOException{
		System.out.println("Closing");
		
		try{
		//file.close();
		} catch (Exception e){
			System.out.println("ignore this message if theresno input file");
		}
		
		//outFile =new FileOutputStream(outputFile);
		outFile =new FileOutputStream(this.outputFile);
		workbook.write(outFile);
		workbook.close();
		outFile.close();
	}
	
	public XSSFWorkbook getWorkbook(){
		return XlsWriter.workbook;
	}
	
	public static void main(String args[]){
		//test
		try {
			transfer("inputx.xlsx", "inputx.xlsx");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//copy file
	public static void transfer(String filename1, String filename2) throws IOException{
		if(filename1.equals(filename2)){
			return;
		}
		FileChannel src = new FileInputStream(filename1).getChannel();
		  FileChannel dest = new FileOutputStream(filename2).getChannel();
		  //dest.transferFrom(src, 0, src.size());

	}
	
}
