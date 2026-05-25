package mtb.devices.rails;

import java.awt.Dialog.ModalityType;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;




public class RailVirtual extends RailBase {
	private String RAILNAME="Virtual";
	private String RAILTYPE="Virtual";
	private double MAX_SPEED=2;
	private double RAMP_TIME=2;
	
	private double defMAX_SPEED=2;
	private double defRAMP_TIME=2;
	
	private threadEvaluate threadEvaluate;
	private Thread threadEvaluateThread;
	private threadMove threadMove;
	private Thread threadMoveThread;
	private threadShutter threadShutter;
	private Thread threadShutterThread;

	public RailVirtual(DecimalFormat nf) {
		super(nf);
	}
	//PUBLIC METHODS
	public String[] open(String[] settings) {
		setSettings(settings);
		super.open(settings);

		threadEvaluate = new threadEvaluate();
		threadEvaluateThread = new Thread(threadEvaluate);
		threadEvaluateThread.start();

		return getSettings();
	}
	
	public String[] close(){
		stopAll();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			}
		

		String [] ret = super.close();
		try {
			threadEvaluateThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finishThreads=false;
		System.out.println("Virtual closed.");
		return ret;
			
	}
	
	public int moveOf(double val){
		super.moveOf(val);
		
		threadMove = new threadMove(val);
		threadMoveThread = new Thread(threadMove);
		threadMoveThread.start();
		return RESP_OK;
	}

	
	public int shutterFire(int nb, double shutterduration, double shutterpause){
		threadShutter= new threadShutter(nb,shutterduration,shutterpause);
		threadShutterThread = new Thread(threadShutter);
		threadShutterThread.start();
		return RESP_OK;
	}
	
	public int stopAll(){
		//System.out.println("simul");
		threadEvaluateThread.interrupt();
		return RESP_OK;
	}
		
	public double getRailSpeed() {
		return MAX_SPEED;
	}

	protected void setRailSpeed(double speed) {
		MAX_SPEED = speed;
	}


	public double getRampTime() {
		return RAMP_TIME;
	}

	private void setRampTime(double time) {
		RAMP_TIME = time;
	}
	

	class threadMove implements Runnable {
		private double offset=0;
		public threadMove(double offset){
			this.offset=offset;
		}
		
		public void run() {
			boolean finish=false;
			//System.out.println("offset ="+offset);
			setMoving(true);
			int dir = (int)Math.round(offset/Math.abs(offset));
			//System.out.println("dir ="+dir);
			
			double initpos = getHardPosition();
			//System.out.println("initpos ="+initpos);
			double g=getRailSpeed()/getRampTime();
			//System.out.println("g ="+g);
			double dconst=g*getRampTime()*getRampTime()/2; //distance à partir de laquelle g=0
			//System.out.println("dconst ="+dconst);
			double posconst = initpos+dir*dconst;//position de vitesse constance
			//System.out.println("posconst ="+posconst);
			double starttime = System.currentTimeMillis();
			//System.out.println("starttime ="+starttime);
			double elapsedtime=0;
			double pos=initpos;
			do {
				elapsedtime=(System.currentTimeMillis()-starttime)/1000;
				//System.out.print("ET ="+elapsedtime);
				//si on est en phase d'accélération
				if (elapsedtime<getRampTime()){
					pos = initpos+dir*g*elapsedtime*elapsedtime/2;
					if(Math.abs(pos-initpos)>Math.abs(offset)){
						pos=initpos+offset;
						finish=true;
					}
					//System.out.println(" ACC newpos ="+pos);
					setHardPosition(pos);
				} else {
					pos=posconst+dir*(elapsedtime-getRampTime())*getRailSpeed();
					if(Math.abs(pos-initpos)>Math.abs(offset)) {
						pos=initpos+offset;
						finish=true;
					}
					//System.out.println(" CST newpos ="+pos);
					setHardPosition(pos);
				}
					
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					break;
				}
				//System.out.print("delta ="+(Math.abs(pos-initpos)-Math.abs(offset)));
			} while (!finish);
			setMoving(false);
		}
	}
	
	class threadShutter implements Runnable {
		public int nb=0;
		public double shutterduration=0;
		public double shutterpause=0;
		
		public threadShutter(int nb, double shutterduration, double shutterpause) {
			this.nb=nb;
			this.shutterduration=shutterduration;
			this.shutterpause=shutterpause;
		}
		
		public void run() {
			setShutting(true);
			for (int i=0;i<nb;i++) {
				try {
					Thread.sleep((long) (shutterduration*1000));
				} catch (InterruptedException e) {
					break;
				}
				try {
					Thread.sleep((long) (shutterpause*1000));
				} catch (InterruptedException e) {
					break;
				}
			}
			setShutting(false);
		}
	}
	
	protected class threadEvaluate implements Runnable {
		public void run() {
			boolean moving=true;
			boolean shutting=false;
			//boolean sequence=false;
			do {
				boolean statemoving=isMoving();
				boolean stateshutting=isShutting();
				//boolean statelocked=gb.LOCKEDSEQUENCE;
				
				/*if(statelocked!=sequence){
					//System.out.println("sequence");
					if(!statelocked) {
						 lockInterface(false);
					}
					sequence=statelocked;
				}*/
				//si on passe d'un mvt à IDLE
				if(!statemoving&&!stateshutting){
					if(moving||shutting) {
						//System.out.println("on s'arrete");
						if(stateNotifier!=null) stateNotifier.stateNotifier(IDLE);
						//if(!gb.LOCKEDSEQUENCE) lockInterface(false);
						moving=false;
						shutting=false;
						//showRailPosition();
					}
				}
				if (statemoving||stateshutting) {
					//System.out.println("on demarre");
					//si on passe de IDLE à autre chose
					//if(!moving&&!shutting) {
					//	lockInterface(true);
					//}
				
					moving=statemoving;
					shutting=stateshutting;
					if(stateNotifier!=null) stateNotifier.stateNotifier(moving|shutting);
					//showRailPosition();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					if (statemoving) {
						threadMoveThread.interrupt();
					}
					if (shutting) {
						threadShutterThread.interrupt();
					}
				}
			} while (!finishThreads);//status!=gb.jd.RAIL_STATUS_IDLE);	
			//System.out.println("EXIT X");
		}

	}
	

	public String[] getSettings(){
    	//System.out.println("prop");
		//try {
	    //    Properties settings = new Properties( );
	    //    settings.load(new FileInputStream("configuration.props"));
	        
	        String Str="";
	        Str+=RAILNAME+"\t";
	        Str+=RAILTYPE+"\t";
	        Str+=nf.format(this.getRailSpeed())+"\t";
	        Str+=nf.format(this.getRampTime())+"\t";
	        Str+=nf.format(this.getCurrentPosition())+"\t";
	        Str+="0\t";
	        Str+="0\t";
	        Str+="0\t";
	        Str+="0\t";
	        Str+="0\t";
	        Str+="0\t\t";
	    //    settings.setProperty("SIMUL.HELPER",Str);

	     //   settings.store(new FileOutputStream("configuration.props"),null);
		//} catch (FileNotFoundException e) {
			System.out.println(this.getCurrentPosition());
		//	e.printStackTrace();
		//} catch (IOException e) {
		//	System.out.println("IOException in simulHelper.saveSettings");
		//	e.printStackTrace();
		//}
	     return Str.split("\t");
	}
	 
	public void setSettings(String[] settings){
		if(settings[1]==null) {
			RAILNAME=settings[0];
			return;
		}
		//Properties settings = new Properties();
		//try {
		//	settings.load(new FileInputStream("configuration.props"));
		//} catch (FileNotFoundException e1) {
		//	System.out.println("FileNotFoundException in simulHelper.loadSettings");
		//	e1.printStackTrace();
		//} catch (IOException e1) {
		//	System.out.println("IOException in simulHelper.loadSettings");
		//	e1.printStackTrace();
		//}
		
		//String v=settings.getProperty("SIMUL.HELPER");
		//if(v==null) return;
		//String[] a=v.split("\t");
		//System.out.println(a.length);
		try {
			if(settings.length>=1)
				RAILNAME=settings[0];
	        //if(a.length>=2)
				//RAILTYPE=a[1];
			if(settings.length>=3)
				setRailSpeed(nf.parse(settings[2]).doubleValue());	
			if(settings.length>=4)
				setRampTime(nf.parse(settings[3]).doubleValue());
			
			if(settings.length>=5){
				super.setPositionZero();
				setOfsPosition(nf.parse(settings[4]).doubleValue());
			}
		} catch (ParseException e) {
			System.out.println("ParseException in simulHelper.loadSettings");
		}
		
		
		
		
		
	}
	
	public void setRAILNAME(String str) {
		String val = str;
		if (val.equals("")) {
			val = RAILNAME;
		}
		if (val == RAILNAME) {
			return;
		}
		RAILNAME = val;
	}

	public void setMOTORSPEED(String str) {
		double val = 0;
		try {
			val = nf.parse(str).doubleValue();
		} catch (ParseException e1) {
			val = MAX_SPEED;
		}
		if (val < 0) {
			val = 0;
		}
		if (val == MAX_SPEED) {
			return;
		}
		MAX_SPEED = val;
	}

	public void setTRAMP(String str) {
		double val = 0;
		try {
			val = nf.parse(str).doubleValue();
		} catch (ParseException e1) {
			val = RAMP_TIME;
		}
		if (val < 0) {
			val = 0;
		}
		if (val == RAMP_TIME) {
			return;
		}
		RAMP_TIME = val;
	}

	
	
	public String getRAILNAME() {
		return RAILNAME;
	}
	
	public String getRAILTYPE() {
		return RAILTYPE;
	}

	public double getMOTORSPEED() {
		return MAX_SPEED;
	}

	public double getTRAMP() {
		return RAMP_TIME;
	}

	
	
	
	
	
	
	
	//préférences
	
		JPanel contentPane;
		
		JTextField textFieldSPEED;
		JTextField textFieldTRAMP;
		JButton btnReset;
		
		String railname=RAILNAME;
		double maxspeed=MAX_SPEED;
		double ramptime=RAMP_TIME;
		JTextField textFieldRailName;
		ArrayList<Image> icons;
	/**
	 * @wbp.parser.entryPoint
	 */
	public void showPrefs(ArrayList<Image> icontab){
		/**
		 * Create the frame.
		 */
		icons=icontab;
		railname=RAILNAME;
		maxspeed=MAX_SPEED;
		ramptime=RAMP_TIME;
		final JDialog framePreferences = new JDialog();
		framePreferences.setModalityType(ModalityType.APPLICATION_MODAL);
		
		framePreferences.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				RAILNAME=railname;
				MAX_SPEED=maxspeed;
				RAMP_TIME=ramptime;
				//on force les textFields pour éviter leur modifiication par lostfocus
				textFieldRailName.setText(RAILNAME);
				textFieldSPEED.setText(nf.format(MAX_SPEED));
				textFieldTRAMP.setText(nf.format(RAMP_TIME));
				
				open(close());
				
				framePreferences.dispose();
			}
		});
		framePreferences.setTitle("VIRTUAL");
		framePreferences.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		framePreferences.setBounds(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y, 290, 283);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		framePreferences.setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:pref"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "2, 2, 3, 1, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel("Rail Name : ");
		panel.add(lblNewLabel, "1, 1, right, default");
		textFieldRailName = new JTextField();
		panel.add(textFieldRailName, "3, 1");
		textFieldRailName.setColumns(10);
		textFieldRailName.setText(RAILNAME);
		textFieldRailName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setRAILNAME();
			}
		});
		textFieldRailName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setRAILNAME();
			}
		});
			
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Rail Preferences", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_1, "2, 4, 3, 1, fill, fill");
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblMotorSpeed = new JLabel("Motor Speed (mm/s) : ");
		panel_1.add(lblMotorSpeed, "1, 1, right, default");
		
		textFieldSPEED = new JTextField();
		panel_1.add(textFieldSPEED, "3, 1");
		textFieldSPEED.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setSPEED();
			}
		});
		textFieldSPEED.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSPEED();
			}
		});
		textFieldSPEED.setColumns(10);
		textFieldSPEED.setText(nf.format(MAX_SPEED));
		
		JLabel lblRampTime = new JLabel("Ramp Time (s) : ");
		panel_1.add(lblRampTime, "1, 3, right, default");
		
		textFieldTRAMP = new JTextField();
		panel_1.add(textFieldTRAMP, "3, 3");
		textFieldTRAMP.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setTRAMP();
			}
		});
		textFieldTRAMP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTRAMP();
			}
		});
		textFieldTRAMP.setColumns(10);
		textFieldTRAMP.setText(nf.format(RAMP_TIME));
		
		
		
		btnReset = new JButton("Defaults");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldSPEED.setText(nf.format(defMAX_SPEED));
				setSPEED();
				textFieldTRAMP.setText(nf.format(defRAMP_TIME));
				setTRAMP();
			}
		});
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				open(close());
				framePreferences.dispose();
			}
		});
		contentPane.add(btnOk, "2, 6");
		contentPane.add(btnReset, "4, 6");
		
		
		framePreferences.setIconImages(icons);
		framePreferences.pack();
		framePreferences.setVisible(true);
	}

	void setRAILNAME(){
		String val=null;
		val=textFieldRailName.getText();
		if(val.equals("")){
			val=RAILNAME;
		}
		textFieldRailName.setText(val);
		if(val==RAILNAME) {
			return;
		}
		RAILNAME=val;
	}
	
	void setSPEED(){
		double val=0;
		try {
			val=nf.parse(textFieldSPEED.getText()).doubleValue();

			//gb.frame.statuslog("Motor Speed sets to : "+gb.stepsToMm(gb.MAX_SPEED));
		} catch (ParseException e1) {
			val=MAX_SPEED;
			//e1.printStackTrace();
		}
		if(val<0){
			val=0;
		}
		textFieldSPEED.setText(nf.format(val));
		if(val==MAX_SPEED) {
			return;
		}
		MAX_SPEED=val;
	}
	
	void setTRAMP(){
		double val=0;
		try {
			val=nf.parse(textFieldTRAMP.getText()).doubleValue();
			//gb.frame.statuslog("Ramp Time sets to : "+gb.RAMP_TIME);
		} catch (ParseException e1) {
			val=RAMP_TIME;
			//e1.printStackTrace();
		}
		if(val<0){
			val=0;
		}
		textFieldTRAMP.setText(nf.format(val));
		if(val==RAMP_TIME) {
			return;
		}
		RAMP_TIME=val;
	}
	
	
	
	
	
	
}
