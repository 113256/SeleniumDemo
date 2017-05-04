package Case;
import Process.Process;
public class Case {
	public String getCompany() {
		return company;
	}



	public void setCompany(String company) {
		this.company = company;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}




	private String company;
	private String title;	
	//private String fromDate;
	//private String toDate;
	private String status;
	
	

	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public Case(String company, String title){
		this.company = company;
		this.title = title;
		
		
		Process.addCase(this);
	}
	
	

}
