package Tools;

public class StringUtil {
	public static boolean isEmpty(String string){
		
		if(string==null){
			return true;
		}
		string = string.replace(" ", "");
		return string.equals("") || string.equals(" ") || string == null || string.equals("null");
	}
	public static boolean isNullOrEmpty(String string){
		string = string.replace(" ", "");
		return string.equals("") || string.equals(" ") || string == null || string.equals("null");
	}
}
