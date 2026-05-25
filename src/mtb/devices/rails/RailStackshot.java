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
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import ftd2.Ftd2xx;

import mtb.drivers.ftd2xx.FTD2XXHelper;


public class RailStackshot extends RailBase {
	// préférences
	// Stackshot
	private String RAILNAME = "Stackshot";
	private String RAILTYPE = "Stackshot";
	private double MAXSPEED = 2.0;
	private double RAMP_TIME = 2.0; // (TRAMP)
	private double STEPS_PER_REV = 3200.0;
	private double MM_PER_REV = 1.5875;
	private double BACKLASH = 0.1;

	private boolean HIPRECISION = false;
	private int TORQUE = 0; // (TORQUE)
	private int LCD = 10;
	// préférences
	// Stackshot défauts

	private double defMaxSPEED = 2.0;
	private double defRAMP_TIME = 2.0; // (TRAMP)
	private double defSTEPS_PER_REV = 3200.0;
	private double defMM_PER_REV = 1.5875;
	private double defBACKLASH = 0.1;

	private int defTORQUE = 0; // (TORQUE)
	private int defLCD = 10;
	private boolean defHIPRECISION = false;

	

	private threadEvaluate threadEvaluate;

	private Thread threadEvaluateThread;
	
	private FTD2XXHelper ftd;
	

	public RailStackshot(DecimalFormat df) {
		super(df);
		try {
			ftd=new FTD2XXHelper();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ftd=null;
		}
	}

	// PUBLIC METHODS
	public String[] open(String[] settings) {
		String version = "";
		threadEvaluate = null;
		threadEvaluateThread = null;
		if (ftd == null) {
			return null;
		}

		int stackshotindex = ftd.find();
		if (stackshotindex == FTD2XXHelper.FTRESP_NOK) {
			// JOptionPane.showMessageDialog(null, "No Stackshot available");
			final JOptionPane pane = new JOptionPane("No Stackshot available");
			final JDialog d = pane.createDialog((JFrame) null, "Dialog");
			d.setLocation(MouseInfo.getPointerInfo().getLocation().x, MouseInfo
					.getPointerInfo().getLocation().y);
			d.setVisible(true);
			return null;
		}

		try {
			ftd.OPEN(stackshotindex);
		} catch (IOException e) {
			// JOptionPane.showMessageDialog(null, "Can't open Stackshot");
			final JOptionPane pane = new JOptionPane("Can't open Stackshot");
			final JDialog d = pane.createDialog((JFrame) null, "Dialog");
			d.setLocation(MouseInfo.getPointerInfo().getLocation().x, MouseInfo
					.getPointerInfo().getLocation().y);
			d.setVisible(true);
			return null;
		}

		try {
			// 2 8 10 12 18 20 22
			ftd.SET_BITMODE(255, 0x00);
			ftd.SET_BITMODE(255, 0x20);

			try {
				Thread.sleep(500);
			} catch (Exception exc) {
			}

			ftd.SET_BAUDRATE(38400);
			ftd.SET_DATACHARACTERISTICS(8, FTD2XXHelper.FT_STOP_BITS_1, FTD2XXHelper.FT_PARITY_NONE);
			ftd.SET_FLOWCONTROL(Ftd2xx.FT_FLOW_NONE, 0, 0);
			ftd.PURGE(Ftd2xx.FT_PURGE_RX | Ftd2xx.FT_PURGE_TX);
			version = ftd.GET_SOFTWARE_STRING();
		} catch (Exception e) {
			close();
			// JOptionPane.showMessageDialog(null,
			// "Can't configure communications");
			final JOptionPane pane = new JOptionPane(
					"Can't configure communications");
			final JDialog d = pane.createDialog((JFrame) null, "Dialog");
			d.setLocation(MouseInfo.getPointerInfo().getLocation().x, MouseInfo
					.getPointerInfo().getLocation().y);
			d.setVisible(true);
			return null;
		}
		if (version != null) {
			setSettings(settings);
			try {
				ftd.SET_RAIL_CONFIG_DISTANCE_PER_REV_STEPS(STEPS_PER_REV);
				ftd.SET_RAIL_CONFIG_DISTANCE_PER_REV_MM(MM_PER_REV);

				int status;
				do {
					status = ftd.GET_RAIL_STATUS();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				} while (status != FTD2XXHelper.RAIL_STATUS_IDLE);

				ftd.SET_RAIL_POSITION_ZERO();
				ftd.SET_RAIL_CONFIG_BACKLASH(mmToSteps(BACKLASH));

				if (HIPRECISION)
					ftd.SET_RAIL_CONFIG_HOLDING_TORQUE(10);
				else
					ftd.SET_RAIL_CONFIG_HOLDING_TORQUE(TORQUE);
				ftd.SET_RAIL_CONFIG_BACKLIGHT(LCD);
				ftd.SET_RAIL_CONFIG_SPEED(mmToSteps(MAXSPEED));
				ftd.SET_RAIL_CONFIG_RAMP_TIME(RAMP_TIME);

			} catch (IOException e) {
				close();
				// JOptionPane.showMessageDialog(null,
				// "Can't set Stackshot parameters");
				final JOptionPane pane = new JOptionPane(
						"Can't set Stackshot parameters");
				final JDialog d = pane.createDialog((JFrame) null, "Dialog");
				d.setLocation(MouseInfo.getPointerInfo().getLocation().x,
						MouseInfo.getPointerInfo().getLocation().y);
				d.setVisible(true);
				return null;
			}
			// gb.frame.statuslog("Rail Speed : "+gb.MAX_SPEED+" steps/s "+gb.stepsToMm(gb.MAX_SPEED)+" mm/s");
			// gb.frame.statuslog("Ramp time : "+gb.jd.GET_RAIL_CONFIG_RAMP_TIME()+" s");
		} else {
			close();
			return null;
			
		}
		super.open(settings);
		threadEvaluate = new threadEvaluate();
		threadEvaluateThread = new Thread(threadEvaluate);
		threadEvaluateThread.start();
		return getSettings();

	}

	public String[] close() {
		// if (isRailOpen()) {
		// try {
		// SET_BITMODE(240, 0x20);
		// } catch (IOException e) {
		// }
		// }
		stopAll();
		/*
		 * try { Thread.sleep(100); } catch (InterruptedException e) { }
		 */
		String[] ret = super.close();
		/*
		 * do { try { Thread.sleep(100); } catch (InterruptedException e) { } }
		 * while (threadEvaluateThread.isAlive());
		 */
		if (threadEvaluateThread != null) {
			try {
				threadEvaluateThread.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		finishThreads = false;
		try {
			ftd.CLOSE();
		} catch (IOException e) {
			System.out.println("Exception in close");
		}
		System.out.println("Stackshot closed.");
		return ret;

	}

	public int setPositionZero() {
		super.setPositionZero();
		try {
			ftd.SET_RAIL_POSITION_ZERO();
		} catch (IOException e) {
			return RESP_NOK;
		}
		return RESP_OK;
	}

	public int moveOf(double val) {
		if (Double.compare(val, 0) == 0)
			return RESP_NOK;
		int dir = (int) Math.round(val / Math.abs(val));
		try {
			ftd.RAIL_MOVE(dir < 0 ? FTD2XXHelper.COMM_RAIL_DIR_BACK : FTD2XXHelper.COMM_RAIL_DIR_FWD,
					mmToSteps(Math.abs(val)));
		} catch (IOException e) {
			return RESP_NOK;
		}
		setMoving(true);
		return RESP_OK;
	}

	public int shutterFire(int nb, double shutterduration, double shutterpause) {
		if (nb == 0)
			return RESP_NOK;
		try {
			ftd.RAIL_SHUTTER_FIRE(nb, shutterduration, shutterpause);
		} catch (IOException e) {
			return RESP_NOK;
		}
		setShutting(true);
		return RESP_OK;
	}

	public int stopAll() {
		// System.out.println("FTD2");
		if (threadEvaluateThread != null)
			threadEvaluateThread.interrupt();
		return RESP_OK;
	}

	

	// NEW PUBLIC METHODS
	public int changeRailSpeed(double speed) {
		// GET_RAIL_SPEED
		try {
			ftd.SET_RAIL_CONFIG_SPEED(mmToSteps(speed));
			// RAIL_CONFIG_SAVE(0);
		} catch (IOException e) {
			System.out.println("^^Exception in CHANGE_RAIL_SPEED1");
			return RESP_NOK;
		}// Power on configuration
		return RESP_OK;
	}

	public int changeRampTime(double time) {
		// GET_RAIL_CONFIG_RAMP_TIME
		try {
			ftd.SET_RAIL_CONFIG_RAMP_TIME(time);
			// RAIL_CONFIG_SAVE(0);//Power on configuration
		} catch (IOException e) {
			System.out.println("^^Exception in CHANGE_RAMP_TIME1");
			return RESP_NOK;
		}// Power on configuration
		return RESP_OK;
	}

	public int changeMmPerRev(double val) {
		MM_PER_REV = val;
		try {
			ftd.SET_RAIL_CONFIG_DISTANCE_PER_REV_MM(MM_PER_REV);
		} catch (IOException e) {
			System.out.println("^^Exception in CHANGE_MM_PER_REV");
			return RESP_NOK;
		}
		return RESP_OK;
	}

	public int changeStepsPerRev(double val) {
		STEPS_PER_REV = val;
		try {
			ftd.SET_RAIL_CONFIG_DISTANCE_PER_REV_STEPS(STEPS_PER_REV);
		} catch (IOException e) {
			System.out.println("^^Exception in CHANGE_STEPS_PER_REV");
			return RESP_NOK;
		}
		return RESP_OK;
	}

	public int changeBacklash(double val) {
		try {
			ftd.SET_RAIL_CONFIG_BACKLASH(mmToSteps(val));
		} catch (IOException e) {
			System.out.println("^^Exception in CHANGE_BACKLASH");
			return RESP_NOK;
		}
		return RESP_OK;
	}

	public int changeTorque(int torque) {
		try {
			ftd.SET_RAIL_CONFIG_HOLDING_TORQUE(torque);
		} catch (IOException e) {
			System.out.println("^^Exception in CHANGE_TORQUE");
			return RESP_NOK;
		}
		return RESP_OK;
	}

	public int changeLcd(int light) {
		try {
			ftd.SET_RAIL_CONFIG_BACKLIGHT(light);
		} catch (IOException e) {
			System.out.println("^^Exception in CHANGE_LCD");
			return RESP_NOK;
		}
		return RESP_OK;
	}

	public double mmToSteps(double val) {
		// return (double)Math.round(val*gb.STEPS_PER_REV/gb.MM_PER_REV);
		int signe = 1;
		if (val < 0)
			signe = -1;
		return (double) signe
				* Math.round(Math.abs(val * STEPS_PER_REV / MM_PER_REV));
	}

	public double stepsToMm(double val) {
		return (double) val * MM_PER_REV / STEPS_PER_REV;
	}

	class threadEvaluate implements Runnable {
		public void run() {
			boolean moving = true;
			boolean shutting = false;
			do {
				int status = FTD2XXHelper.RAIL_STATUS_IDLE;
				boolean statemoving = isMoving();
				boolean stateshutting = isShutting();
				//boolean statelocked = gb.LOCKEDSEQUENCE;
				try {
					/*if (statelocked != sequence) {
						//System.out.println("sequence");
						if (!statelocked) {
							//lockInterface(false);
							stateNotifier.stateNotifier(false);
						}
						sequence = statelocked;
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
					if (statemoving || stateshutting) {
						// si on passe de IDLE à autre chose
						if (!moving && !shutting) {
							if(stateNotifier!=null) stateNotifier.stateNotifier(RUNNING);
							//System.out.println("on demarre");
							//lockInterface(true);
							Thread.sleep(100);
						}
						try {
							status = ftd.GET_RAIL_STATUS();
						} catch (IOException e2) {
							System.out.println("^^Exception in threadEvaluate");
						}
						try {
							setHardPosition(stepsToMm(ftd.GET_RAIL_POSITION_CURRENT()));
						} catch (IOException e) {
							System.out.println("^^Exception in threadEvaluate");
						}

						// si on passe d'un mvt à IDLE
						if (status == FTD2XXHelper.RAIL_STATUS_IDLE) {
							if(stateNotifier!=null) stateNotifier.stateNotifier(IDLE);
							/*if (!statelocked)
								lockInterface(false);*/
						}
						moving = (status & FTD2XXHelper.RAIL_STATUS_MOVING) != 0 ? true
								: false;
						setMoving(moving);
						shutting = (status & FTD2XXHelper.RAIL_STATUS_SHUTTER) != 0 ? true
								: false;
						setShutting(shutting);
						if(stateNotifier!=null) stateNotifier.stateNotifier(moving|shutting);
						//showRailPosition();
					}

					Thread.sleep(100);
				} catch (InterruptedException e) {
					try {
						// if ((status & RAIL_STATUS_MOVING)!=0){
						ftd.RAIL_STOP();
						// }
						// if ((status & RAIL_STATUS_SHUTTER)!=0){
						// SHUTTER_STOP();
						// }
						do {
							status = ftd.GET_RAIL_STATUS();
							try {
								Thread.sleep(100);
							} catch (InterruptedException e1) {
							}
						} while (status != FTD2XXHelper.RAIL_STATUS_IDLE);
						setMoving(false);
						setShutting(false);
						moving = false;
						shutting = false;
						//lockInterface(false);
						setHardPosition(stepsToMm(ftd.GET_RAIL_POSITION_CURRENT()));
						//showRailPosition();
						if(stateNotifier!=null) stateNotifier.stateNotifier(IDLE);

					} catch (IOException e1) {
						System.out.println("^^Exception in threadEvaluate");
					}
				}

			} while (!finishThreads);// status!=gb.jd.RAIL_STATUS_IDLE);
		}
	}

	public String[] getSettings() {

		String Str = "";
		Str += RAILNAME + "\t";
		Str += RAILTYPE + "\t";
		Str += nf.format(MAXSPEED) + "\t";
		Str += nf.format(RAMP_TIME) + "\t";
		Str += nf.format(this.getCurrentPosition()) + "\t";
		Str += nf.format(MM_PER_REV) + "\t";
		Str += nf.format(BACKLASH) + "\t";
		Str += nf.format(TORQUE) + "\t";
		Str += nf.format(LCD) + "\t";
		Str += nf.format(HIPRECISION ? 1 : 0) + "\t";
		Str += nf.format(STEPS_PER_REV) + "\t\t";

		return Str.split("\t");
	}

	public void setSettings(String[] settings) {
		if (settings[1] == null) {
			RAILNAME = settings[0];
			return;
		}

		try {
			if (settings.length >= 1)
				RAILNAME = settings[0];
			// if(a.length>=2)
			// RAILTYPE=a[1];
			if (settings.length >= 3)
				MAXSPEED = nf.parse(settings[2]).doubleValue();
			if (settings.length >= 4)
				RAMP_TIME = nf.parse(settings[3]).doubleValue();
			if (settings.length >= 5) {
				super.setPositionZero();
				setOfsPosition(nf.parse(settings[4]).doubleValue());

			}
			if (settings.length >= 6)
				MM_PER_REV = nf.parse(settings[5]).doubleValue();
			if (settings.length >= 7)
				BACKLASH = nf.parse(settings[6]).doubleValue();
			if (settings.length >= 8)
				TORQUE = nf.parse(settings[7]).intValue();
			if (settings.length >= 9)
				LCD = nf.parse(settings[8]).intValue();

			if (settings.length >= 10)
				HIPRECISION = (nf.parse(settings[9]).intValue() == 1) ? true
						: false;
			if (settings.length >= 11)
				STEPS_PER_REV = nf.parse(settings[10]).doubleValue();

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
			val = MAXSPEED;
		}
		if (val < 0) {
			val = 0;
		}
		if (val == MAXSPEED) {
			return;
		}
		MAXSPEED = val;
		changeRailSpeed(MAXSPEED);
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
		changeRampTime(RAMP_TIME);
	}

	public void setTORQUE(String str) {
		int val = 0;
		try {
			val = nf.parse(str).intValue();
		} catch (ParseException e1) {
			val = TORQUE;
		}
		if (val < 0)
			val = 0;
		if (val > 10)
			val = 10;
		if (val == TORQUE) {
			return;
		}
		TORQUE = val;
		if (HIPRECISION)
			changeTorque(10);
		else
			changeTorque(TORQUE);
	}

	public void setBACKLASH(String str) {
		double val = 0;
		try {
			val = nf.parse(str).doubleValue();
		} catch (ParseException e1) {
			val = BACKLASH;
		}
		if (val < 0) {
			val = 0;
		}
		if (val == BACKLASH) {
			return;
		}
		BACKLASH = val;
		changeBacklash(BACKLASH);

	}

	public void setMMPERREV(String str) {
		double val = 0;
		try {
			val = nf.parse(str).doubleValue();
		} catch (ParseException e1) {
			val = MM_PER_REV;
		}
		if (val < 0) {
			val = 0;
		}
		if (val == MM_PER_REV) {
			return;
		}
		MM_PER_REV = val;
		changeMmPerRev(MM_PER_REV);
	}

	public void setSTEPSPERREV(String str) {
		double val = 0;
		try {
			val = nf.parse(str).doubleValue();
		} catch (ParseException e1) {
			val = STEPS_PER_REV;
		}
		if (val < 0) {
			val = 0;
		}
		val = Math.abs(val);
		if (val == STEPS_PER_REV) {
			return;
		}
		STEPS_PER_REV = val;
		changeStepsPerRev(STEPS_PER_REV);
	}
	
	public void setHIPRECISION(String str) {
		if(str.equals("on")) {
			HIPRECISION=true;
			changeTorque(10);
		}
		if(str.equals("off")) {
			HIPRECISION=false;
			changeTorque(TORQUE);
		}
		return;
	}
	
	public void setLCD(String str) {
		if(str.equals("on")) {
			LCD=10;
			changeLcd(10);
		}
		if(str.equals("off")) {
			LCD = 1;
			changeLcd(1);
		}
	}
	
	
	public String getRAILNAME() {
		return RAILNAME;
	}
	
	
	public String getRAILTYPE() {
		return RAILTYPE;
	}

	public double getMOTORSPEED() {
		return MAXSPEED;
	}

	public double getTRAMP() {
		return RAMP_TIME;
	}

	public int getTORQUE() {
		return TORQUE;
	}

	public double getBACKLASH() {
		return BACKLASH;
	}

	public double getMMPERREV() {
		return MM_PER_REV;
	}

	public double getSTEPSPERREV() {
		return STEPS_PER_REV;
	}
	
	public boolean getHIPRECISION() {
		return HIPRECISION;
	}
	
	public boolean getLCD() {
		return LCD==10?true:false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// préférences
	JPanel contentPane;
	JTextField textFieldMAXSPEED;
	JTextField textFieldTRAMP;
	JTextField textFieldTORQUE;
	JTextField txtBacklash;
	JTextField txtMmPerRev;
	JTextField txtStepsPerRev;
	JCheckBox chckbxHiprecision;
	JCheckBox chckbxLcdbacklight;
	JButton btnReset;

	String railname = RAILNAME;
	double maxspeed = MAXSPEED;
	double ramptime = RAMP_TIME;
	int torque = TORQUE;
	int lcd = LCD;
	// bool hi=HI_PRECISION;
	double steps_per_rev = STEPS_PER_REV;
	double mm_per_rev = MM_PER_REV;
	double bcklash = BACKLASH;
	boolean hiprecision = HIPRECISION;
	JTextField textFieldRailName;
	ArrayList<Image> icons;
	/**
	 * @wbp.parser.entryPoint
	 */

	public void showPrefs(ArrayList<Image> icontab) {

		/**
		 * @wbp.parser.entryPoint
		 */
		icons=icontab;
		railname = RAILNAME;
		maxspeed = MAXSPEED;
		ramptime = RAMP_TIME;
		torque = TORQUE;
		lcd = LCD;
		steps_per_rev = STEPS_PER_REV;
		mm_per_rev = MM_PER_REV;
		bcklash = BACKLASH;
		hiprecision = HIPRECISION;
		final JDialog framePreferences = new JDialog();
		framePreferences.setModalityType(ModalityType.APPLICATION_MODAL);
		framePreferences.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				RAILNAME = railname;
				MAXSPEED = maxspeed;
				RAMP_TIME = ramptime;
				TORQUE = torque;
				BACKLASH = bcklash;
				MM_PER_REV = mm_per_rev;
				STEPS_PER_REV = steps_per_rev;
				LCD = lcd;
				HIPRECISION = hiprecision;
				// on force les textFields pour éviter leur modifiication par
				// lostfocus
				textFieldRailName.setText(RAILNAME);
				textFieldMAXSPEED.setText(nf.format(MAXSPEED));
				textFieldTRAMP.setText(nf.format(RAMP_TIME));
				textFieldTORQUE.setText(nf.format(TORQUE));
				txtBacklash.setText(nf.format(BACKLASH));
				txtMmPerRev.setText(nf.format(MM_PER_REV));
				txtStepsPerRev.setText(nf.format(STEPS_PER_REV));
				if (LCD == 10) {
					chckbxLcdbacklight.setSelected(true);
				} else {
					chckbxLcdbacklight.setSelected(false);
				}
				if (HIPRECISION) {
					chckbxHiprecision.setSelected(true);
				} else {
					chckbxHiprecision.setSelected(false);
				}

				changeStepsPerRev(STEPS_PER_REV);
				changeMmPerRev(MM_PER_REV);
				changeBacklash(BACKLASH);
				if (HIPRECISION)
					changeTorque(10);
				else
					changeTorque(TORQUE);
				changeLcd(LCD);

				changeRailSpeed(MAXSPEED);
				changeRampTime(RAMP_TIME);

				open(close());

				framePreferences.dispose();

			}
		});
		framePreferences.setTitle("STACKSHOT");
		framePreferences.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		framePreferences.setBounds(MouseInfo.getPointerInfo().getLocation().x,
				MouseInfo.getPointerInfo().getLocation().y, 243, 310);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		framePreferences.setContentPane(contentPane);
		contentPane
				.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.GROWING_BUTTON_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.GROWING_BUTTON_COLSPEC, }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		JPanel panel = new JPanel();
		contentPane.add(panel, "2, 2, 3, 1, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC, },
				new RowSpec[] { FormFactory.DEFAULT_ROWSPEC, }));

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
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Rail Preferences",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_1, "2, 4, 3, 1, fill, fill");
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"), }, new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblMotorSpeed = new JLabel("Motor Speed (mm/s) : ");
		panel_1.add(lblMotorSpeed, "1, 1, right, default");

		textFieldMAXSPEED = new JTextField();
		panel_1.add(textFieldMAXSPEED, "3, 1");
		textFieldMAXSPEED.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setMOTORSPEED();
			}
		});
		textFieldMAXSPEED.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMOTORSPEED();
			}
		});
		textFieldMAXSPEED.setColumns(10);
		textFieldMAXSPEED.setText(nf.format(MAXSPEED));

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

		JLabel lblMotorTorque = new JLabel("Motor Torque : ");
		panel_1.add(lblMotorTorque, "1, 5, right, default");

		textFieldTORQUE = new JTextField();
		panel_1.add(textFieldTORQUE, "3, 5");
		textFieldTORQUE.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setTORQUE();
			}
		});
		textFieldTORQUE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTORQUE();
			}
		});
		textFieldTORQUE.setColumns(10);
		textFieldTORQUE.setText(nf.format(TORQUE));

		JLabel lblHighPrecision = new JLabel("High Precision : ");
		panel_1.add(lblHighPrecision, "1, 7, right, default");

		chckbxHiprecision = new JCheckBox("");
		panel_1.add(chckbxHiprecision, "3, 7, center, default");
		chckbxHiprecision.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxHiprecision.isSelected()) {
					HIPRECISION = true;
					changeTorque(10);
				} else {
					HIPRECISION = false;
					changeTorque(TORQUE);
				}

			}
		});
		if (HIPRECISION) {
			chckbxHiprecision.setSelected(true);
		} else {
			chckbxHiprecision.setSelected(false);
		}

		JLabel lblLcdBacklight = new JLabel("LCD Backlight : ");
		panel_1.add(lblLcdBacklight, "1, 9, right, default");

		chckbxLcdbacklight = new JCheckBox("");
		panel_1.add(chckbxLcdbacklight, "3, 9, center, default");
		chckbxLcdbacklight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxLcdbacklight.isSelected()) {
					LCD = 10;
					changeLcd(10);
				} else {
					LCD = 1;
					changeLcd(1);
				}
			}
		});
		if (LCD == 10) {
			chckbxLcdbacklight.setSelected(true);
		} else {
			chckbxLcdbacklight.setSelected(false);
		}

		JLabel lblBacklash = new JLabel("Backlash (mm) : ");
		panel_1.add(lblBacklash, "1, 11, right, default");

		txtBacklash = new JTextField();
		panel_1.add(txtBacklash, "3, 11");
		txtBacklash.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setBACKLASH();
			}
		});
		txtBacklash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBACKLASH();
			}
		});
		txtBacklash.setColumns(10);

		txtBacklash.setText(nf.format(BACKLASH));

		JLabel label = new JLabel("Dist/Rev (mm) : ");
		panel_1.add(label, "1, 13, right, default");

		txtMmPerRev = new JTextField();
		panel_1.add(txtMmPerRev, "3, 13");
		txtMmPerRev.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setMMPERREV();
			}
		});
		txtMmPerRev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMMPERREV();
			}
		});
		txtMmPerRev.setColumns(10);
		txtMmPerRev.setText(nf.format(MM_PER_REV));

		JLabel lblDistrev = new JLabel("Steps/Rev (step) : ");
		panel_1.add(lblDistrev, "1, 15, right, default");

		txtStepsPerRev = new JTextField();
		panel_1.add(txtStepsPerRev, "3, 15");
		txtStepsPerRev.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setSTEPSPERREV();
			}
		});
		txtStepsPerRev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSTEPSPERREV();
			}
		});
		txtStepsPerRev.setColumns(10);
		txtStepsPerRev.setText(nf.format(STEPS_PER_REV));

		btnReset = new JButton("Defaults");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MAXSPEED = defMaxSPEED;
				RAMP_TIME = defRAMP_TIME;
				TORQUE = defTORQUE;
				BACKLASH = defBACKLASH;
				MM_PER_REV = defMM_PER_REV;
				STEPS_PER_REV = defSTEPS_PER_REV;
				LCD = defLCD;
				HIPRECISION = defHIPRECISION;

				// on force les textFields pour éviter leur modifiication par
				// lostfocus
				textFieldMAXSPEED.setText(nf.format(MAXSPEED));
				textFieldTRAMP.setText(nf.format(RAMP_TIME));
				textFieldTORQUE.setText(nf.format(TORQUE));
				txtBacklash.setText(nf.format(BACKLASH));
				txtMmPerRev.setText(nf.format(MM_PER_REV));
				txtStepsPerRev.setText(nf.format(STEPS_PER_REV));
				if (LCD == 10) {
					chckbxLcdbacklight.setSelected(true);
				} else {
					chckbxLcdbacklight.setSelected(false);
				}
				if (HIPRECISION) {
					chckbxHiprecision.setSelected(true);
				} else {
					chckbxHiprecision.setSelected(false);
				}

				changeStepsPerRev(STEPS_PER_REV);
				changeMmPerRev(MM_PER_REV);
				changeBacklash(BACKLASH);
				if (HIPRECISION)
					changeTorque(10);
				else
					changeTorque(TORQUE);
				changeLcd(LCD);

				changeRailSpeed(MAXSPEED);
				changeRampTime(RAMP_TIME);
			}
		});

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				open(close());
				framePreferences.dispose();
			}
		});
		contentPane.add(btnOk, "2, 6, fill, default");
		contentPane.add(btnReset, "4, 6, fill, default");

		if (LCD == 1)
			chckbxLcdbacklight.setSelected(false);
		else
			chckbxLcdbacklight.setSelected(true);
		framePreferences.setIconImages(icons);

		// 4. Size the frame.
		framePreferences.pack();

		// 5. Show it.
		framePreferences.setVisible(true);
	}

	void setRAILNAME() {
		String val = null;
		val = textFieldRailName.getText();
		if (val.equals("")) {
			val = RAILNAME;
		}
		textFieldRailName.setText(val);
		if (val == RAILNAME) {
			return;
		}
		RAILNAME = val;
	}

	void setMOTORSPEED() {
		double val = 0;
		try {
			val = nf.parse(textFieldMAXSPEED.getText()).doubleValue();

			// gb.frame.statuslog("Motor Speed sets to : "+gb.stepsToMm(gb.MAX_SPEED));
		} catch (ParseException e1) {
			val = MAXSPEED;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldMAXSPEED.setText(nf.format(val));
		if (val == MAXSPEED) {
			return;
		}
		MAXSPEED = val;
		changeRailSpeed(MAXSPEED);
	}

	void setTRAMP() {
		double val = 0;
		try {
			val = nf.parse(textFieldTRAMP.getText()).doubleValue();
			// gb.frame.statuslog("Ramp Time sets to : "+gb.RAMP_TIME);
		} catch (ParseException e1) {
			val = RAMP_TIME;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldTRAMP.setText(nf.format(val));
		if (val == RAMP_TIME) {
			return;
		}
		RAMP_TIME = val;
		changeRampTime(RAMP_TIME);
	}

	void setTORQUE() {
		int val = 0;
		try {
			val = nf.parse(textFieldTORQUE.getText()).intValue();
		} catch (ParseException e1) {
			val = TORQUE;
			// e1.printStackTrace();
		}
		if (val < 0)
			val = 0;
		if (val > 10)
			val = 10;
		textFieldTORQUE.setText(nf.format(val));
		if (val == TORQUE) {
			return;
		}
		TORQUE = val;
		if (HIPRECISION)
			changeTorque(10);
		else
			changeTorque(TORQUE);
	}

	void setBACKLASH() {
		double val = 0;
		try {
			val = nf.parse(txtBacklash.getText()).doubleValue();
			// gb.frame.statuslog("Backlash sets to : "+gb.stepsToMm(gb.BACKLASH));
		} catch (ParseException e1) {
			val = BACKLASH;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		txtBacklash.setText(nf.format(val));
		if (val == BACKLASH) {
			return;
		}
		BACKLASH = val;
		changeBacklash(BACKLASH);

	}

	void setMMPERREV() {
		double val = 0;
		try {
			val = nf.parse(txtMmPerRev.getText()).doubleValue();
		} catch (ParseException e1) {
			val = MM_PER_REV;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		txtMmPerRev.setText(nf.format(val));
		if (val == MM_PER_REV) {
			return;
		}
		MM_PER_REV = val;
		changeMmPerRev(MM_PER_REV);
	}

	void setSTEPSPERREV() {
		double val = 0;
		try {
			val = nf.parse(txtStepsPerRev.getText()).doubleValue();
			// JOptionPane.showMessageDialog(null,"val "+val);
		} catch (ParseException e1) {
			val = STEPS_PER_REV;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		val = Math.abs(val);
		txtStepsPerRev.setText(nf.format(val));
		if (val == STEPS_PER_REV) {
			return;
		}
		STEPS_PER_REV = val;
		changeStepsPerRev(STEPS_PER_REV);
	}

}
