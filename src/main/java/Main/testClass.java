package Main;



import java.util.ArrayList;

public class testClass {
	public static ArrayList<duplicateCheck> duplicateCheckList;

	class duplicateCheck{
		public String getCustomerCin() {
			return customerCin;
		}

		public String getCreditCardNumber() {
			return creditCardNumber;
		}

		private String customerCin;
		private String creditCardNumber;
		
		public duplicateCheck(String customerCin, String creditCardNumber){
			this.customerCin = customerCin;
			this.creditCardNumber = creditCardNumber;
		}
	}
	public testClass(){
		duplicateCheckList = new ArrayList<duplicateCheck>();

	}
	public void test(){
		duplicateCheck d = new duplicateCheck("a", "b");
		duplicateCheckList.add(d);
		System.out.println("duplicateList ");
		System.out.println("***duplist ***");
		for(duplicateCheck d1 : duplicateCheckList){
			
			System.out.println(d1.getCustomerCin());
			System.out.println(d1.getCreditCardNumber());
		}
	}
}
