package Tools;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
 
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
//http://www.codejava.net/coding/how-to-read-excel-files-in-java-using-apache-poi

public class XlsReader {
	private String fileName;
	private int lastColumn;
	private static String currentHeader = "";
	
	public XlsReader(String fileName){
		this.fileName = fileName;
	}
	
	private static String handleCell(int type, Cell cell){
		String val;
		DataFormatter df = new DataFormatter();
		switch(type) {
		case Cell.CELL_TYPE_BOOLEAN:
			//System.out.println("bool");
			//val = cell.getBooleanCellValue();
			//val = df.formatCellValue(cell);
			//System.out.print(cell.getBooleanCellValue() + "\t\t");
			//cell.setCellType(Cell.CELL_TYPE_STRING);
			//val = cell.getStringCellValue();
			val = df.formatCellValue(cell);
			break;
		case Cell.CELL_TYPE_NUMERIC:
			cell.setCellType(Cell.CELL_TYPE_STRING);
			val = cell.getStringCellValue();
			
			break;
		case Cell.CELL_TYPE_STRING:
			//System.out.println("str");
			//System.out.println("str");
			val = cell.getStringCellValue();
			//val = df.formatCellValue(cell);
			//System.out.print(cell.getStringCellValue() + "\t\t");
			break;
		case Cell.CELL_TYPE_FORMULA:
			//System.out.println("f");
			//System.out.println(cell.getCachedFormulaResultType());
			val = handleCell(cell.getCachedFormulaResultType(), cell);
			//System.out.println("val");
			//val = df.formatCellValue(cell);
			break;	
		default:
			val = "";
			//val = df.formatCellValue(cell);
			break;
		}	
		return val;
	}
    
	public ArrayList<String> getRecords(){
		String[] headers = null;
		
		
		//DataFormatter df = new DataFormatter();
		ArrayList<String> records = null;
		XSSFWorkbook workbook = null;
		try {
	 		records = new ArrayList<String>();
			FileInputStream file = new FileInputStream(new File(fileName));
			
			//Get the workbook instance for XLS file 
			//HSSFWorkbook workbook = new HSSFWorkbook(file);
			workbook = new XSSFWorkbook(file);
			int n = workbook.getNumberOfSheets();
			System.out.println("sheets" +n);
			//Get first sheet from the workbook
			//HSSFSheet sheet = workbook.getSheetAt(0);
			//XSSFSheet sheet = workbook.getSheetAt(0);//xssf instead of hssf for xlsx to work
			
			String processName = "";
			
			processName = "Sheet1";
			headers = Constants.FILE_HEADER_INPUT;					
			lastColumn = 6;
			
				
			XSSFSheet sheet  = workbook.getSheetAt(0);
			//final int MY_MINIMUM_COLUMN_COUNT = 116;
			//SN	Product	Org Type	CDM Type	Tab	Updates

			int rowStart = Math.min(1, sheet.getFirstRowNum());
		    int rowEnd = Math.max(1400, sheet.getLastRowNum());
		    String val = "";									
		    for (int rowNum = 1; rowNum < rowEnd; rowNum++) {

			   boolean blank = false;
		       Row r = sheet.getRow(rowNum);
		       if (r == null) {
		          // This whole row is empty
		          // Handle it as needed
		          continue;
		       }

		       //int lastColumn = Math.max(r.getLastCellNum(), MY_MINIMUM_COLUMN_COUNT);
		       //int lastColumn = 116;
		       																		
		       for (int cn = 0; cn < lastColumn; cn++) {
		    	   currentHeader = headers[cn];
		    	   
		    	 val = "";
		          Cell cell = r.getCell(cn, Row.CREATE_NULL_AS_BLANK);//dont ignore blanks from start column to end column
		          if (cell == null) {
		             val = "";
		          } else {
		             // Do something useful with the cell's contents
		        	  val = handleCell(cell.getCellType(), cell);
		        	  
		        	  //System.out.print(val+"-");
		          }
		          //System.out.println(headers[cn]+"- ");
		          
		          
																
					//System.out.println("cdm : "+h.get("cdmType"));
					
					//System.out.println(val);
					if(cn == 0 && val.equals("") && rowNum > 1){	
						System.out.println("break");
						blank = true;
						//break;		
						//System.out.println("size "+records.size());
						System.out.println("Records: "+records);
													
						file.close();
						workbook.close();
						return records;
					} else {
						
					}
		       }
		       
		       if(!blank){
					records.add(val);
		            System.out.println();
				}
				
		       //records.add(h);
		       System.out.println();
		    }
			
			
			//FileOutputStream out = new FileOutputStream(new File("test.xlsx"));
			//workbook.write(out);
			//out.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
return records;
	}
	
    public static void main(String[] args) throws IOException {
    	XlsReader r = new XlsReader("checkbalsmsInput.xlsx");
    	r.getRecords();
    }
    
    public static boolean isEmpty(String string){
		string = string.replace(" ", "");
		return string.equals("") || string.equals(" ") || string == null;
	}
 
}