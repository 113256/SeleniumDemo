package Tools;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.log4j.Logger;


//XLSWRITER
public class Writer {
	protected Logger logger = Logger.getLogger(this.getClass());
	protected CSVPrinter csvFilePrinter;
	protected FileWriter fileWriter = null;
	protected CSVFormat csvFileFormat; 
	
	//protected static Writer instance;
	
	/*
	//singleton
	 public static Writer getInstance(String outputPath, CSVFormat csvFileFormat) {
        if( instance == null ) {
            instance = new Writer(outputPath, csvFileFormat);
        }
         return instance;
     }
	 */
	
	public Writer(String outputPath, CSVFormat csvFileFormat){
		try {
			fileWriter = new FileWriter(outputPath);
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
			//write file headers first
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.toString());
		}
	}
	
	/*public void writeRecord(Case case1){
		
	}
	*/
	
	public void close(){
		try {
			fileWriter.flush();
			fileWriter.close();
			csvFilePrinter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.toString());
		}
	}
/*
	public void writeRecord(esrCase case2) {
		// TODO Auto-generated method stub
		
	}
	
	public void writeRecord(eprCase case2) {
		// TODO Auto-generated method stub
		
	}
	*/


}

