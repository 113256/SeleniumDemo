package Driver;
import java.util.List;

public class IEProtectedMode {
	//private Logger logger = Logger.getLogger(this.getClass());
	private boolean isSetSuccessfully;

	public boolean isSetSuccessfully() {
		return isSetSuccessfully;
	}

	public void setSetSuccessfully(boolean isSetSuccessfully) {
		this.isSetSuccessfully = isSetSuccessfully;
	}

	public enum Zone {
		LOCAL (1,"local intranet")
		,TRUSTED (2,"trusted pages")
		,INTERNET (3,"internet")
		,RESTRICTED (4,"restricted sites")
		;

		private final int zone;
		private final String description;

		private Zone(int zone, String desc) {
			this.zone = zone;
			this.description = desc;
		}

		public int getZone() {
			return zone;
		}

		public String getDescription() {
			return description;
		}

		@Override
		public String toString() {
			return "("+zone+"). "+description;
		}
	}

	public IEProtectedMode(){

	}



	public void setProtectedMode(String selectValue) {
		String value;
		try {
			value = WinRegistry.valueForKey(
					WinRegistry.HKEY_LOCAL_MACHINE,                             //HKEY
					"SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion",           //Key
					"ProductName");
			//logger.info("Windows Distribution = " + value);

//			value = WinRegistry.valueForKey (
//					WinRegistry.HKEY_CURRENT_USER,                             //HKEY
//					"SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\Zones\\1",           //Key
//					"2500");
//			System.out.println("Protected mode for local intranet = " + value);
//
//			List<String> result = WinRegistry.writeValue(WinRegistry.HKEY_CURRENT_USER,
//					"SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\Zones\\1",           //Key
//					"2500",
//					"0",
//					WinRegistry.REG_DWORD
//					);
//			for (String s : result) {
//				System.out.println(s);
//			}
//
//			value = WinRegistry.valueForKey (
//					WinRegistry.HKEY_CURRENT_USER,                             //HKEY
//					"SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\Zones\\1",           //Key
//					"2500");
//			System.out.println("Protected mode for local intranet = " + value);

			for (Zone z: Zone.values()) {
				int zone = z.getZone();
				String desc = z.getDescription();
				String keypath = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\Zones\\";
				String valuename = "2500";
				String valueset = selectValue; // 0 - enabled, 3 - disabled
				value = WinRegistry.valueForKey (
						WinRegistry.HKEY_CURRENT_USER,
						keypath+zone,
						valuename);
				//logger.info("Protected mode for "+desc+" = " + value);

				List<String> result = WinRegistry.writeValue(WinRegistry.HKEY_CURRENT_USER,
						keypath+zone,
						valuename,
						valueset,
						WinRegistry.REG_DWORD
						);
				for (String s : result) {
					//logger.info(s);
				}

				value = WinRegistry.valueForKey (
						WinRegistry.HKEY_CURRENT_USER,
						keypath+zone,
						valuename);
				//logger.info("Protected mode for "+desc+" = " + value);
			}

			this.setSetSuccessfully(true);

		} catch (Exception e) {
			//logger.error("Error when setting ie protected mode: " + e.getMessage());
			this.setSetSuccessfully(false);
		}

	}

	public void setZoomLevel(String value) {
		try {
			WinRegistry.writeValue(WinRegistry.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Internet Explorer\\Zoom", "ZoomFactor", value, WinRegistry.REG_DWORD);
			//logger.info("IE zoom level set to : " + value);
		} catch (Exception e) {
			//logger.error("Error when setting ie zoom level: " + e.getMessage());
		}
	}


}
