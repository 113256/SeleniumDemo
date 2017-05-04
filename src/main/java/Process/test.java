package Process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import Main.ProcessMain;
import Tools.Constants;
import Driver.WebUI;

public class test {
	public static Logger logger = Logger.getLogger(test.class);
	
	static HashMap<String, String> handleMap;
	

	
	public static void main(String[] args){
		String[] debitSplit = "123.123".split("\\.");
		for(String d : debitSplit){
			System.out.println("d :"+d);
		}
		System.out.println(debitSplit[0]);		System.out.println(debitSplit[1]);

	}
	
	
}
