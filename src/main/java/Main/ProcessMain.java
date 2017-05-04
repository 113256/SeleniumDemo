package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Constants.ConfigProperty;
import Driver.WebUI;
import Process.Process;
//import IServe.IServeProcess;
import Tools.Constants;
import Tools.DateLabelFormatter;
import Tools.ProcessWriter;
import Tools.XlsWriter;

import com.dbs.rtpo.constant.MessageEventConstant;
import com.dbs.rtpo.tools.MessageTransfer;
public class ProcessMain extends JFrame implements PropertyChangeListener{
	//keep passing this to IServeCancelV  so that it doesnt throw nullpointerException 
	//if it runs the 2nd time (press start again)
	
	String fromDate = "";
	String toDate = "";
	
	private int processNo = 1;
	Thread startThread = null;
	private String welcomeMessage = "Welcome to DBS";
	private String descriptionMessage = "Boardcast CA Version 1.0";
	
	static JLabel recordsLabel  = new JLabel("x out of n records processed");
	//Date today = new Date();
	JDatePanelImpl datePanel2 = null;
	JDatePickerImpl datePicker2 = null;
	JDatePanelImpl datePanel = null;
	JDatePickerImpl datePicker = null;
	
	private JLabel filePathLabel = new JLabel("Please login");
	//private JButton browserButton = new JButton("Choose File");
	private JButton closeButton = new JButton("Stop");
	
	 JRadioButton p1;  
     JRadioButton p2;    
	
     //private JButton loginButton = new JButton("Login");
	private JButton runButton = new JButton("Start Now");
	private final JLabel statusLabel = new JLabel(
			"                                           ");
	//private static PCommConnector connector = new PCommConnector();
	//private static Connector connector = Connector.getInstance();
	//private static Connector connector = new Connector();
	
	//private Logger log = Logger.getLogger(this.getClass());
	
	public static boolean firstRun = true;
	private String outputFileString = "";
	private static SimpleDateFormat datetime = new SimpleDateFormat ("yyyyMMddhhmmss");
	private static Date now = new Date();
	//private String outputString = "";
	//xlsWriter = new XlsWriter(file.getAbsolutePath(), outputString);
	
	//private static boolean newFile = false;
	//private static File currentFile = null;
	
	private static File currentInputFile;
	private static String currentOutputFile;
	
	private static WebUI driver = null;
	//private Logger log = Logger.getLogger(this.getClass());
	//public static Logger logger = Logger.getLogger(ProcessMain.class);
	public static Logger logger = Logger.getLogger(ProcessMain.class);

	public ProcessWriter processWriter;
	private JComboBox locationList;
	private JComboBox specsList;
	private JTextField monthlyMin;
	private JTextField monthlyMax;
	private JComboBox posLevelList;
	private JComboBox jobTypeList;
	private JTextField keywordField;
	private File outputFile;
	private static ConfigProperty properties;
	public static HashMap<String, String> urlMap = new HashMap<String, String>();
	
	//private static File file = null;
	
	
	public static int outputRow = 1;
	private static String outputString = "cancellationOutput".concat(datetime.format(now)).concat(".xlsx");
	public static XlsWriter xlsWriter = null;
	
	private static void login(){
		
		try{
			driver = new WebUI(true);
		
			driver.navigate().to(Constants.JOBSTREETURL);
			
		} catch (Exception e){
			logger.error("java error");
			e.printStackTrace();
		}
		
		
	}
	
	
	
	public static void updateLabel(final int i, final int size){
		SwingUtilities.invokeLater(new Runnable()
		{
		    public void run()
		    {
		    	recordsLabel.setText("Processing record "+i+" of "+size+" records");
		    }   
		});
	}
	
	public static void updateLabelComplete(){
		SwingUtilities.invokeLater(new Runnable()
		{															
		    public void run()
		    {
		    	recordsLabel.setText("Completed all records");
		    }   
		});
	}
	
	public static void updateLabelText(final String text){
		SwingUtilities.invokeLater(new Runnable()
		{
		    public void run()
		    {
		    	recordsLabel.setText(text);
		    }   
		});
	}
	/*
	public static void startDriver(){
		driver = new WebUI("chromedriver.exe", logger);
		//driver.get("https://x01sicallapp1a.uat.dbs.com:8543/sgiserve/iserve.index.html#/home/search");
	}
	*/
	
	public static void main(String[] args){
		/*
		try {
			xlsWriter = new XlsWriter(outputString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		firstRun = true;
		login();//UNCOMMENT FOR REAL
		
		SwingUtilities.invokeLater(new Runnable() {
			 public void run() {
				 new ProcessMain();
		 }
		});												
	}
		     		
													
	public ProcessMain(){
		logger.error("starting.>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..");
		MessageTransfer.getInstance().addChangeListener(this);

		changeToWindowsLookAndFeel();

		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		// init JFrame
		JFrame frame = new JFrame("UAT");
		frame.setIconImage(new ImageIcon(this.getClass().getResource(
				"/images/frameIcon.png")).getImage());
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
																
		frame.add(this.assignScaledBackground(), BorderLayout.CENTER);
		frame.add(this.prepareTitlePanel(), BorderLayout.NORTH);
		frame.add(this.prepareButtonPanel(), BorderLayout.SOUTH);

		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setSize(600, 600);
		frame.setResizable(false);
		frame.setVisible(true);
		
		
		// init jobstreetFrame
		JFrame frameJ = new JFrame("JobStreet Parameters");
		//frameJ.setIconImage(new ImageIcon(this.getClass().getResource("/images/frameIcon.png")).getImage());
		frameJ.setLayout(new BorderLayout());
		frameJ.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
																
		//frameJ.add(this.assignScaledBackground(), BorderLayout.CENTER);
		//frame.add(this.prepareTitlePanel(), BorderLayout.NORTH);
		frameJ.add(this.prepareButtonPanelJ(), BorderLayout.NORTH);
		//frameJ.pack();
		frameJ.setLocationByPlatform(true);
		frameJ.setSize(600, 600);
		frameJ.setResizable(true);
		frameJ.setVisible(true);
		
			 
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try
				{if(driver != null)driver.close();
				} catch (Exception e1){}
			}
			public void windowClosed(WindowEvent e) {
				try
				{if(driver != null)driver.close();
				} catch (Exception e1){}
					
			}
		});

		filePathLabel.setVisible(false);
		//runButton.setEnabled(false); //
		
	}
	
	private JPanel prepareTitlePanel() {
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new GridBagLayout());
		titlePanel.setBackground(Color.BLACK);
		titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 10, 0,
				new Color(204, 0, 0)));
																
		// DBS Logo
		JLabel dbsLabel = new JLabel();
		dbsLabel.setIcon(new ImageIcon(this.getClass().getResource(
				"/images/logo.png")));
		dbsLabel.setBackground(Color.BLACK);
		dbsLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
		dbsLabel.setOpaque(true);

		// Slogan
		JLabel dbsSloganLabel = new JLabel();
		ImageIcon icon = new ImageIcon(this.getClass().getResource(
				"/images/tagline.png"));
		Image image = icon.getImage();
		dbsSloganLabel.setIcon(new ImageIcon(image.getScaledInstance(
				image.getWidth(null) / 2, image.getHeight(null) / 2,
				Image.SCALE_SMOOTH)));
		dbsSloganLabel.setBackground(Color.BLACK);
		dbsSloganLabel.setHorizontalAlignment(JLabel.CENTER);
		dbsSloganLabel.setVerticalAlignment(JLabel.CENTER);
		dbsSloganLabel.setOpaque(true);
															
		this.addComponentWithConstraint(titlePanel, dbsLabel, 0, 0, 1, 1, 80,
				100, GridBagConstraints.WEST, GridBagConstraints.BOTH);
		this.addComponentWithConstraint(titlePanel, dbsSloganLabel, 1, 0, 1, 1,
				20, 100, GridBagConstraints.EAST, GridBagConstraints.BOTH);

		return titlePanel;
	}
	
	public static void done(){
		try {
			xlsWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		try {
			//xlsWriter.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
	}
	private JPanel prepareButtonPanelJ() {
		JPanel panel = new JPanel(new GridBagLayout());
		JLabel keyword = new JLabel("Enter keyword");
		keywordField = new JTextField(10);
		
		this.addComponentWithConstraint(panel, keyword, 0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, 
				GridBagConstraints.BOTH, 1, 1, 1, 1, 0, 0);
		this.addComponentWithConstraint(panel, keywordField, 1, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, 
				GridBagConstraints.BOTH, 1, 1, 1, 1, 0, 0);
		
		String[] locations = {"Johor", "Kuala Lumpur"};
		locationList = new JComboBox(locations);
		locationList.setSelectedIndex(0);
		this.addComponentWithConstraint(panel, locationList, 0, 1, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, 
				GridBagConstraints.BOTH, 1, 1, 1, 1, 0, 0);
		
		String[] specs = {"IT-Software", "IT-Hardware"};
		specsList = new JComboBox(specs);
		specsList.setSelectedIndex(0);
		this.addComponentWithConstraint(panel, specsList, 1, 1, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, 
				GridBagConstraints.BOTH, 1, 1, 1, 1, 0, 0);
		
		
		JLabel monthlyMinLabel = new JLabel("Monthly Salary (Min)");
		monthlyMin = new JTextField(10);
		
		JLabel monthlyMaxLabel = new JLabel("Monthly Salary (Max)");
		monthlyMax = new JTextField(10);
		
		this.addComponentWithConstraint(panel, monthlyMinLabel, 0, 2, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, 
				GridBagConstraints.BOTH, 1, 1, 1, 1, 0, 0);
		this.addComponentWithConstraint(panel, monthlyMin, 1, 2, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, 
				GridBagConstraints.BOTH, 1, 1, 1, 1, 0, 0);
		this.addComponentWithConstraint(panel, monthlyMaxLabel, 0, 3, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, 
				GridBagConstraints.BOTH, 1, 1, 1, 1, 0, 0);
		this.addComponentWithConstraint(panel, monthlyMax, 1, 3, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, 
				GridBagConstraints.BOTH, 1, 1, 1, 1, 0, 0);
		

		String[] posLevel = {"Manager", "Junior Executive", "Non-Executive"};
		posLevelList = new JComboBox(posLevel);
		posLevelList.setSelectedIndex(0);
		this.addComponentWithConstraint(panel, posLevelList, 0, 4, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, 
				GridBagConstraints.BOTH, 1, 1, 1, 1, 0, 0);
		
		String[] jobType = {"Full Time/Contract", "Part Time/Temporary", "Internship"};
		jobTypeList = new JComboBox(jobType);
		jobTypeList.setSelectedIndex(0);
		this.addComponentWithConstraint(panel, jobTypeList, 1, 4, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, 
				GridBagConstraints.BOTH, 1, 1, 1, 1, 0, 0);
		
		posLevelList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			
			}
		});
		
		return panel;
	}
	
	private JPanel prepareButtonPanel() {
		//http://www.codejava.net/java-se/swing/how-to-use-jdatepicker-to-display-calendar-component
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
	    int year = cal.get(Calendar.YEAR);
	    int month = cal.get(Calendar.MONTH);
	    int day = cal.get(Calendar.DAY_OF_MONTH);
	    // etc.
	    
		//datepicker
		UtilDateModel model = new UtilDateModel();
		model.setDate(year, month, day);
		model.setSelected(true);
		datePanel = new JDatePanelImpl(model);
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		
		UtilDateModel model2 = new UtilDateModel();
		model2.setDate(year, month, day);
		model2.setSelected(true);
		datePanel2 = new JDatePanelImpl(model2);
		datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
		System.out.println(model.getValue());
		//datePicker2.getJFormattedTextField().getText();
		
		JPanel selectionPanel = new JPanel();	
	    
		JPanel footer = new JPanel();
		JPanel dateFrame = new JPanel();
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
																
	    selectionPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
														
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(45, 45, 45));
		buttonPanel.setLayout(new GridBagLayout());
																		
		JPanel browserPanel = new JPanel();
		browserPanel.setBackground(new Color(45, 45, 45));
		browserPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		closeButton.setVisible(true);
		browserPanel.add(closeButton);
		//browserPanel.add(browserButton);

		browserPanel.add(filePathLabel);
		//browserPanel.add(configButton);
		
		//browserPanel.add(selectionPanel);
		filePathLabel.setForeground(Color.WHITE);

		runButton.setPreferredSize(new Dimension(100, 30));
		runButton.setMinimumSize(new Dimension(100, 30));	
												
		//loginButton.setPreferredSize(new Dimension(100, 30));
		//loginButton.setMinimumSize(new Dimension(100, 30));
								
		//browserButton.setPreferredSize(new Dimension(100, 30));
		//browserButton.setMinimumSize(new Dimension(100, 30));
		
		closeButton.setPreferredSize(new Dimension(100, 30));
		closeButton.setMinimumSize(new Dimension(100, 30));
														
		JPanel startPanel = new JPanel();
		startPanel.setBackground(new Color(45, 45, 45));
		startPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		startPanel.add(runButton);
		//startPanel.add(loginButton);

		statusLabel.setForeground(new Color(204, 0, 0));
		statusLabel.setFont(statusLabel.getFont().deriveFont(15f));
															
		this.addComponentWithConstraint(buttonPanel, browserPanel, 0, 0, 1, 1,
				30, 80, GridBagConstraints.WEST, GridBagConstraints.BOTH, 20,
				20, 20, 5, 0, 0);								
		this.addComponentWithConstraint(buttonPanel, startPanel, 1, 0, 2, 1,
				10, 80, GridBagConstraints.EAST, GridBagConstraints.BOTH, 10,
				0, 10, 20, 0, 0);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBackground(new Color(143, 140, 149));
																
		statusPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		footer.add(recordsLabel);
		statusPanel.add(statusLabel);
		dateFrame.add(datePicker);
		dateFrame.add(datePicker2);
		//datePicker
		//datePicker.addActionListener(actionListener);
		
		datePicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				toDate = datePicker2.getJFormattedTextField().getText();
				fromDate =datePicker.getJFormattedTextField().getText();

			}
		});
			/*		
		browserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				if (filePathLabel.getText() != null
						&& filePathLabel.getText().length() > 0) {
					File file = new File(filePathLabel.getText());
					if (file.exists() && file.isFile()) {
						fileChooser.setCurrentDirectory(file.getParentFile());
					}
				}
				FileFilter filter = new FileNameExtensionFilter(".xlsx", "xlsx");
				fileChooser.setFileFilter(filter);
				int returnValue = fileChooser.showOpenDialog(null);
					
				if (returnValue == JFileChooser.APPROVE_OPTION ) {
					File selectedFile = fileChooser.getSelectedFile();
					filePathLabel.setText(selectedFile.getAbsolutePath());
					runButton.setEnabled(true);
				}
			}
		});
		*/
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Thread closeThread = new Thread(){
					public void run(){
						startThread.interrupt();				
					}
				};
				closeThread.start();			
			}												
		});
		/*
		loginButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				try{
					login();
				} catch (Exception e){
					logger.error("ee");
					e.printStackTrace();
				}
			}
		});
		*/
		
		runButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				//outputRow = 1;

				logger.error("outputrow "+ProcessMain.outputRow);
				/*
				try {
					checkFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				*/
				 startThread = new Thread() {
					 
				      public void run() {
				    	/*
				    	try {
				  			xlsWriter = new XlsWriter(outputString);//start new xlsWriter each time you click "Start" because you need a new instance for each workbook.write() you call
				  		} catch (IOException e) {
				  			// TODO Auto-generated catch block
				  			e.printStackTrace();
				  		}
				    	*/  
				    	  try {
							checkFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.error("invalid session");
							e.printStackTrace();
						} catch (InterruptedException e){
							logger.error("end");
							return;
						}
				      	}
				      };
				startThread.start();
				
				}
			}
		);
		
		bottomPanel.add(selectionPanel);
		bottomPanel.add(footer);
		bottomPanel.add(dateFrame);
		bottomPanel.add(buttonPanel);
		bottomPanel.add(statusPanel);
		
		selectionPanel.setVisible(true);
		
		
		return bottomPanel;
	}													

	private JLabel assignScaledBackground() {

		ImageIcon icon = new ImageIcon(this.getClass().getResource(
				"/images/cc01.jpg"));
		Image image = icon.getImage();
		JLabel backgroundLabel = new JLabel(new ImageIcon(
				image.getScaledInstance(600, 600, Image.SCALE_SMOOTH)));
		backgroundLabel.setLayout(new BorderLayout());
										
		JLabel welcomeMessageLabel = new JLabel(welcomeMessage);
		welcomeMessageLabel.setForeground(Color.white);
		welcomeMessageLabel.setFont(welcomeMessageLabel.getFont().deriveFont(
				25f));

		// sJLabel recordsLabel  = new JLabel("x out of n records processed");
														
		JLabel programDescriptionLabel = new JLabel(descriptionMessage);
		programDescriptionLabel.setForeground(Color.white);
		programDescriptionLabel.setFont(programDescriptionLabel.getFont()
				.deriveFont(25f));

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setOpaque(false);
		this.addComponentWithConstraint(panel, welcomeMessageLabel, 0, 0, 1, 1,
				100, 100, GridBagConstraints.WEST, GridBagConstraints.BOTH, 0,
				10, 0, 10, 0, 0);
		
		this.addComponentWithConstraint(panel, programDescriptionLabel, 0, 1,
				1, 1, 100, 100, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, 0, 10, 20, 0, 0, 0);
															
		backgroundLabel.add(panel, BorderLayout.SOUTH);
		return backgroundLabel;
	}

	private void addComponentWithConstraint(JPanel panel, Component component,
			int x, int y, int rowSpan, int columnSpan, double weightX,
			double weightY, int anchor, int fill) {
		panel.add(component, new GridBagConstraints(x, y, rowSpan, columnSpan,
				weightX, weightY, anchor, fill, new Insets(2, 2, 2, 2), 5, 5));
	}
															
	private void addComponentWithConstraint(JPanel panel, Component component,
			int x, int y, int rowSpan, int columnSpan, double weightX,
			double weightY, int anchor, int fill, int marginTop,
			int marginRight, int marginBottom, int marginLeft, int paddingX,
			int paddingY) {
		panel.add(component, new GridBagConstraints(x, y, rowSpan, columnSpan,
				weightX, weightY, anchor, fill, new Insets(marginTop,
						marginRight, marginBottom, marginLeft), paddingX,
				paddingY));
	}
																	
	private void changeToWindowsLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
			//logger.error(e.getStackTrace());//stacktrace to string
			logger.error(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public void propertyChange(final PropertyChangeEvent event) {
		if (MessageEventConstant.STATUS.equals(event.getPropertyName())) {
			logger.error("EXECUTED " + event.getNewValue());
			statusLabel.setText((String) event.getNewValue());
			statusLabel.invalidate();
			statusLabel.validate();
			statusLabel.paintImmediately(statusLabel.getVisibleRect());
		}
	}
														
	public void checkFile() throws IOException, InterruptedException{
		//currentFile = file;
		
		//start(file);
		start();
		
		/*
  	  if (filePathLabel.getText() != null
					|| filePathLabel.getText().length() > 0) {
			file = new File(filePathLabel.getText());
			
			if (file.exists() && file.isFile()) {
				
				currentFile = file;
				
					//start(file);
					start();
					//runButton.setEnabled(false);
				
			} else {
				MessageTransfer.getInstance().fireEvent(
						MessageEventConstant.STATUS,
						"Please select an xls file before execute");
			}
			
		} */
  	  } 
    
	
	//public void start(File file){
	public void start(){
		//if(testMode){
		//parse excel file
		//readExcel();
		logger.error("Starting");
		
			logger.error("firstrun: "+firstRun);
			if(firstRun){
				logger.error("new output");
				
				datetime = new SimpleDateFormat ("yyyyMMddhhmmss");
				now = new Date();
				
				//currentInputFile = file;
				//outputFile = file.getAbsolutePath();
				outputFileString = "C:/Users/User/Desktop/seleniumDemo/selenuimDemoOutput";
				outputString = outputFileString.substring(0,outputFileString.length()-5).concat(datetime.format(now)).concat(".xlsx");
				
				currentOutputFile = outputString;
				logger.error(outputString);
				//outputFile = new File(currentOutputFile);
				
			} else {					
				logger.error("same file");
				//outputFile = new File(currentOutputFile);
				//outputString = currentOutputFile;
			}
			
			try {
				//xlsWriter = new XlsWriter(file.getAbsolutePath(), outputString);	
				xlsWriter = new XlsWriter();
				xlsWriter.start(outputString, firstRun);
				//xlsWriter.setHeaders();

				//processWriter = new ProcessWriter(outputString);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ProcessMain.updateLabelText("File is currently locked, close it first");
			}
			
			if(firstRun){
				firstRun = false;
			}
			
			XSSFWorkbook workbook = xlsWriter.getWorkbook();
			//System.out.println(workbook.)
			logger.error(processNo);	
			
			
			Process iserveProcess = null;
			
			
			//job info hashmap
			LinkedHashMap<String, String> searchInfoMap = new LinkedHashMap<String, String>();
			searchInfoMap.put(Constants.KEYWORD, keywordField.getText());
			searchInfoMap.put(Constants.LOCATION, (String)locationList.getSelectedItem());
			searchInfoMap.put(Constants.SPECIALIZATION, (String)specsList.getSelectedItem());
			searchInfoMap.put(Constants.MONTHLYSALMIN, monthlyMin.getText());
			searchInfoMap.put(Constants.MONTHLYSALMAX, monthlyMax.getText());
			searchInfoMap.put(Constants.POSITIONLEVEL, (String)posLevelList.getSelectedItem());
			searchInfoMap.put(Constants.JOBTYPE, (String)jobTypeList.getSelectedItem());


			iserveProcess = new Process(driver);
			
			iserveProcess.start(searchInfoMap);
			//iserveProcess.startVPlusTest();
			
        	ProcessMain.updateLabelComplete();
        	
        	done();
			logger.error("-----------end-----------");
			
		
	}
	
}
