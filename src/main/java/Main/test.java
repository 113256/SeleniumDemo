package Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class test {
	public static void main(String[] args){
		System.out.println(titleContainsKeyword("abac123"));
		System.out.println(getQueryMap("http://www.bursamalaysia.com/market/listed-companies/company-announcements/#/?category=all&sub_category=all&alphabetical=All&date_from=13/03/2017&date_to=13/03/2017&page=1"));
	
		String url= "http://www.bursamalaysia.com/market/listed-companies/company-announcements/#/?category=all&sub_category=all&alphabetical=All&date_from=13/03/2017&date_to=13/03/2017&page=1";
		int page = 5;
		System.out.println(url.replaceAll("page=[0-9]", "page="+page));
	}
	
	public static boolean titleContainsKeyword(String title){
		ArrayList<String> a = new ArrayList<String>();
		a.add("abc");
		a.add("def");
		for(String c : a){
			if(title.toLowerCase().contains(c.toLowerCase())){
				return true;
			}
		}
		return false;
	}
	
	public static Map<String, String> getQueryMap(String query)  
	{  
	    String[] params = query.split("&");  
	    Map<String, String> map = new HashMap<String, String>();  
	    for (String param : params)  
	    {  
	        String name = param.split("=")[0];  
	        String value = param.split("=")[1];  
	        map.put(name, value);  
	    }  
	    return map;  
	}
}

