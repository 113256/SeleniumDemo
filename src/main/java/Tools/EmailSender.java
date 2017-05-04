package Tools;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.log4j.Logger;


//import com.dbs.rtpo.tw.cti.util.ConfigTool;
//import com.dbs.statement.request.process.RtpoFileReader;
//import com.dbs.statement.request.process.SRDetail;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class EmailSender {
	
	public static Logger logger = Logger.getLogger(EmailSender.class.getClass());
	
	public static void main(String[] args) {  
  
//    	//MailItem: https://msdn.microsoft.com/en-us/library/office/ff861332.aspx
//        ActiveXComponent oOutlook = new ActiveXComponent("Outlook.Application");  
//    	//ActiveXComponent oOutlook = new ActiveXComponent("Application");
//        Dispatch.call(oOutlook ,"GetNamespace","MAPI").toDispatch();
//        Dispatch email = Dispatch.invoke(oOutlook.getObject(),"CreateItem", Dispatch.Get, new Object[] { "0" }, new int[0]).toDispatch();  
//        Dispatch.put(email, "To", "samyeung@dbs.com");  
//        Dispatch.put(email, "Subject", "Test Mail");  
//        Dispatch.put(email, "Body", "hello");  
//        //Dispatch.put(email, "Body", getCuerpoEmail("C:\\archivo.html"));
//        Dispatch.put(email, "ReadReceiptRequested", "false"); 
//        
//        Dispatch attachs = Dispatch.get(email, "Attachments").toDispatch();
//        Dispatch.call(attachs, "Add",System.getProperty("user.dir")+"/sessionid.txt");
//        try {  
//            Dispatch.call(email, "Send");  
//        } catch (com.jacob.com.ComFailException e) {  
//            e.printStackTrace();  
//        }  
		System.out.println("Don0e");
		loadJacobLibrary();
		
//		SRDetail detail = new SRDetail();
//		detail.setCustomerType("BizCare");
//		detail.setAcctNum("123");
//		detail.setAcctCny("HKD");
//		detail.setLcin("lcin123");
//		detail.setStartDate("01/07/2016");
//		detail.setEndDate("03/07/2016");
//		detail.setEmail("123@dbs.com");
		System.out.println("Don1e");
//		sendEmail(Zip.compress("C:/Users/samyeung/Desktop/1036713.pdf","C:/Users/samyeung/Desktop/1036713.zip","123"),detail );
		//EmailSender.sendEmail();
		String sendStatus = EmailSender.sendEmail(Zip.compress("60004677488.pdf","60004677488.zip","dbsdbs"));
		System.out.println(sendStatus);
		System.out.println("Done");
    }
    
    public static String sendEmail(File file, String text, String subject) {  
    	String emailReceiver = "jackchan715@hotmail.com" ;
    	String logMessage = "";
    	File templateFile = null;
    	//MailItem: https://msdn.microsoft.com/en-us/library/office/ff861332.aspx
        ActiveXComponent oOutlook = new ActiveXComponent("Outlook.Application");  
        Dispatch.call(oOutlook ,"GetNamespace","MAPI").toDispatch();
        Dispatch email = Dispatch.invoke(oOutlook.getObject(),"CreateItem", Dispatch.Get, new Object[] { "0" }, new int[0]).toDispatch();
        Dispatch.put(email, "Subject", subject);  
        /*
        if("BizCare".toLowerCase().equals(detail.getCustomerType().toLowerCase())){
//        	Dispatch.put(email, "To", ConfigTool.getInstance().getProperty("BIZCARE_EMAIL")); 
        	Dispatch.put(email, "To", emailReceiver ); 
        	logger.info("Email sent to: " + emailReceiver);
        	templateFile = new File(System.getProperty("user.dir") + File.separator + "BizCareTemplate.txt");
        	Dispatch.put(email, "Body", "CIF: " + detail.getLcin() + " " + "\n" + detail.getAcctNum() + " " + detail.getAcctCny() + " " +detail.getStartDate() + "~" + detail.getEndDate() + "\n" + "TO " + detail.getEmail() + "\n" + "\n" +"\n"+ RtpoFileReader.readFile(templateFile));  
        }else{
//        	Dispatch.put(email, "To", ConfigTool.getInstance().getProperty("NOT_BIZCARE_EMAIL")); 
        	Dispatch.put(email, "To", emailReceiver); 
        	logger.info("Email sent to: " + emailReceiver);
        	templateFile = new File(System.getProperty("user.dir") + File.separator + "NotBizCareTemplate.txt");
        	Dispatch.put(email, "Body", "CIF: " + detail.getLcin() + " " + "\n" + detail.getAcctNum() + " " + detail.getAcctCny() + " " +detail.getStartDate() + "~" + detail.getEndDate() + "\n" + "TO " + detail.getEmail() + "\n" + "\n" +"\n"+ RtpoFileReader.readFile(templateFile));  
        }
        */
        //Dispatch.put(email, "To", ConfigTool.getInstance().getProperty("BIZCARE_EMAIL")); 
    	Dispatch.put(email, "To", emailReceiver ); 
    	logger.info("Email sent to: " + emailReceiver);
    	
    	String templateText = text;


    	// = new File(System.getProperty("user.dir") + File.separator + "BizCareTemplate.txt");
    	Dispatch.put(email, "Body", templateText);
        Dispatch.put(email, "ReadReceiptRequested", "false"); 
        
        Dispatch attachs = Dispatch.get(email, "Attachments").toDispatch();
        Dispatch.call(attachs, "Add",file.getAbsolutePath());
        try {  
            Dispatch.call(email, "Send");  
        } catch (com.jacob.com.ComFailException e) {  
        	logger.error("Error when sending email: " , e);
        	logMessage = "Failed to send email";
        }  
        
        return logMessage;
    }
    
    public static String sendEmail() {  
    	String emailReceiver = "jackchan715@hotmail.com" ;
    	String logMessage = "";
    	//MailItem: https://msdn.microsoft.com/en-us/library/office/ff861332.aspx
        ActiveXComponent oOutlook = new ActiveXComponent("Outlook.Application"); 
        System.out.println("a");
        Dispatch.call(oOutlook ,"GetNamespace","MAPI").toDispatch();
        System.out.println("a");

        Dispatch email = Dispatch.invoke(oOutlook.getObject(),"CreateItem", Dispatch.Get, new Object[] { "0" }, new int[0]).toDispatch();
        
        System.out.println("a");

        
        Dispatch.put(email, "Subject", "test");  
//        Dispatch.put(email, "Body", "test");  
        Dispatch.put(email, "Body", "getcuerpo");  
        Dispatch.put(email, "To", emailReceiver ); 
        Dispatch.put(email, "ReadReceiptRequested", "false"); 
        
        System.out.println("a");

        try {  
            Dispatch.call(email, "Send");  
        } catch (com.jacob.com.ComFailException e) {  
        	logger.error("Error when sending email: " , e);
        	logMessage = "Failed to send email";
        }  
        
        return logMessage;
    }
    
    public static String getCuerpoEmail(String fileName)
   {
       String message = null;
       FileInputStream file;
       try {
           file = new FileInputStream(fileName);
           byte[] b = new byte[file.available()];
           file.read(b);
           file.close();
           message = new String(b);
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
       return message;
   }
    
    public static void loadJacobLibrary(){
        //load library
        String batchFile = System.getProperty("java.library.path") + File.pathSeparator + System.getProperty("user.dir") ;
        File batchFileFile = new File(batchFile);
        File batchFilePath = batchFileFile.getParentFile();
        String path = batchFilePath.getAbsolutePath();
        String libPath = path + File.separator + getLibraryPath("JacobLibrary") + File.separator;
        System.out.println(libPath);
        System.setProperty("java.library.path",libPath);
        Field fieldSysPath;
        try {
            fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch(Exception e){
            e.printStackTrace();
        }catch(Throwable e){
        	e.printStackTrace();
        }
    }
    
    public static String getLibraryPath(String libPrefix){
        String libraryFolderName = "";
        String currentDirPath = System.getProperty("user.dir");
        File batchFileFile = new File(currentDirPath);
        File batchFilePath = batchFileFile.getParentFile();
        currentDirPath = batchFilePath.getPath();
        File directory = new File(currentDirPath);
        File[] subdirs = directory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
        for (File dir : subdirs) {
            if (dir.getName().startsWith(libPrefix)) {
                libraryFolderName = dir.getName();
            }
        }
        return libraryFolderName;
    }

}
