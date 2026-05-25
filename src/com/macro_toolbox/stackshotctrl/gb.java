package com.macro_toolbox.stackshotctrl;

import java.awt.Color;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import mtb.devices.cameras.CameraConfigs;
import mtb.devices.cameras.CameraList;
import mtb.devices.rails.RailBase;
import mtb.swamp.utes.UtesArrays;
import mtb.swamp.utes.UtesFiles;
import mtb.swamp.utes.UtesProps;
import mtb.swamp.windows.Docker;





class gb {
	public static String version = "0.9.9.4";
	public static int telnetPort=1307;
	public static Docker docker;
	public static frame frame=null;
	public static PrintStream ps=null;
	public static boolean openFrameBellows=false;
	public static frameBellows frameBellows=null;
	public static frameDiagnose frameDiagnose=null;
	public static Thread diagnoseThread;
	public static String SSCDIAGNOSE="SSC.DIAGNOSE";
	public static String SSCBELLOWS="SSC.BELLOWS";

	public static int SSCBELLOWSX=470;
	public static int SSCBELLOWSY=60;
	public static int SSCBELLOWSW=680;
	public static int SSCBELLOWSH=590;

	public static String SSCMAIN="SSC.MAIN";
	public static int SSCMAINX=20;
	public static int SSCMAINY=60;
	public static int SSCMAINW=455;
	public static int SSCMAINH=620;
	
	public static String SSCLIVEVIEW="SSC.LIVEVIEW";	
	public static int SSCLIVEVIEWX=470;
	public static int SSCLIVEVIEWY=60;
	public static int SSCLIVEVIEWW=680;
	public static int SSCLIVEVIEWH=590;

	public static String SSCABOUT="SSC.ABOUT";
	public static boolean DOCKED=true;
	public static boolean CHECKVERSION=true;
	public static boolean OUTPUTTXT=true;
	
	
	public static Thread yThread;
	public static boolean scan=false;
	
	
	public static DecimalFormat nf;
	public static DecimalFormatSymbols dfs;
	/*public static boolean MOVING=false;
	public static boolean SHUTTING=false;
	public static boolean LOCKEDMOVE=false;
	public static boolean LOCKEDSEQUENCE=false;
	public static boolean CANCEL=false;*/
	public static boolean PAUSE=false;
	public static boolean LOCKEDSEQUENCE=false;
	
	public static  double SETTLE_TIME=0.1; //(TSETTLE)
	public static  double PULSE_TIME=0.3; //(TPULSE)
	public static  int PULSE_NUMBER=1; //(PICS)
	public static boolean MIRRORLOCKUP=false;
	public static double UNLOCK_PULSE=0.3;
	public static double UNLOCK_TIME=0.3;
	public static  double TOFF=0.1;//(TOFF)
	public static double TIMELAPSE_INTERVAL=0.0;
	public static int TIMELAPSE_NUMBER=0;
	
	public static  double defSETTLE_TIME=0.1; //(TSETTLE)
	public static  double defPULSE_TIME=0.3; //(TPULSE)
	public static boolean defMIRRORLOCKUP=false;
	public static  double defUNLOCK_PULSE=0.3;
	public static  double defUNLOCK_TIME=0.3;
	public static  int defPULSE_NUMBER=1; //(PICS)
	public static  double defTOFF=0.1;//(TOFF)
	public static  double defTIMELAPSE_INTERVAL=0.0;
	public static  int defTIMELAPSE_NUMBER=0;
	//Adaptative Bellow
	public static double MAXCOC=2.5;
	public static double DOF_OVERLAP = 1;
	//Adaptative Bellow defauts
	public static double defMAXCOC=2.5;
	public static double defDOF_OVERLAP = 1;
	
	public static double LOWER_LIMIT=-200.0; //200mm
	public static double UPPER_LIMIT=200.0; //200mm
	public static double RANGE_START=0.0;
	public static double RANGE_END=0.0;
	//public static double POSITION=0.0;
	
	public static double STEP_SIZE=0.1;
	public static int STEP_NUMBER=10;
	
	public static int OP_MODE=0;
	public static final int 
			MO_AutoStep=0,
			MO_AutoDist=1,
			MO_ManualDist=2,
			MO_TotalDist=3,
			MO_DistStep=4,
			MO_Manual=5,
			MO_Continuous=6,
			MO_AdaptiveBellow=7; 
	public static String[] OP_MODE_STR = new String[] {
		"Range+StepNumber (Auto-Step)", 
		"Range+StepSize (Auto-Dist)", 
		"Range+StepSize Manual (ManualDist)", 
		"Distance+StepNumber (Total Dist)", 
		"StepSize+StepNumber (Dist/Step)", 
		"StepSize Manual (Manual)", 
		"Continuous Movement (Continuous)",
		"Adaptive Bellows"};
	
	
	public static final int 
	TEST_CANCEL_ON=0,
	CANCEL_OFF_MOVE=1,
	CANCEL_OFF_SEQUENCE=2, 
	LOCK_MOVE=3,
	LOCK_SEQUENCE=4,
	UNLOCK_MOVE=5,
	UNLOCK_SEQUENCE=6,
	PAUSE_ONOFF=7;
	
	public static double focalLens=0;
	public static double fNumber=0;
	public static double pupilRatio=0;
	public static double measuredMag=0;
	
	//public static int sensorWidth=0;
	//public static int sensorHeight=0;
	//public static int sensorPixelX=0;
	//public static int sensorPixelY=0;
	
	public static double measuredFOV1=0;
	public static double POS1=0.0;
	public static double measuredExtFOV1 = 0;
	public static double EXT1 = 0.0;
	
	public static double measuredFOV2 = 0;
	public static double measuredExtFOV2 = 0;
	public static double POS2 = 0.0;
	public static double EXT2 = 0.0;
	public static double theoricExt = 0.0;

	public static double coc = 0.0;

	public static double dofn = 0;
	public static double doff = 0;
	public static double theoricMag = 0;

	public static double targetFOV = -0.0;
	public static double targetMag = -0.0;
	public static double infinitePos = 0;
	
	public static Color red=new Color(255,0,0);
	public static Color green=new Color(0,255,0);
	public static Color blue=new Color(0,0,255);
	public static Color black=new Color(0,0,0);
	
	
	public static RailBase rh;
	public static CameraList cl;
	public static CameraConfigs cc;

	public static final String
			RAIL_VIRTUAL="Virtual",
			RAIL_STACKSHOT="Stackshot";

	public static int lastRailIndex=0;
	public static ArrayList<String[]> railArray=new ArrayList<String[]>();
	
	public static int lastLensIndex=0;
	public static ArrayList<String[]> lensArray=new ArrayList<String[]>();

	public static int lastSensorIndex=0;
	public static ArrayList<String[]> sensorArray=new ArrayList<String[]>();
	
	public static ArrayList<Image> icons = new ArrayList<Image>();
	
	
	public static int findFirstFreeNewRail(String baseName){
		
		for (int i=0; i<999;i++){
			if(UtesArrays.findInArrayList(gb.railArray, baseName+i)==-1) return i;
		}
		return 1000;

	}

	public static boolean isRailNameExists(String baseName){
		if(UtesArrays.findInArrayList(gb.railArray, baseName)==-1) return false;
		
		return true;

	}
	
	
	public static void propGetLensList(Properties props, Properties parameters) {
		UtesProps.propGetArrayList(props, parameters, "LENS.ARRAY",
				gb.lensArray, 4);
	}

	public static void propGetSensorList(Properties props, Properties parameters) {
		UtesProps.propGetArrayList(props, parameters, "SENSOR.ARRAY",
				gb.sensorArray, 5);
	}

	public static void propGetRailList(Properties props, Properties parameters) {
		UtesProps.propGetArrayList(props, parameters, "RAIL.ARRAY",
				gb.railArray, 11);
	}
	
	public static void propLoadGlobals() {
		Properties defsettings = new Properties( );
        try {
			defsettings.load(new FileInputStream("configuration.raw"));
        } catch (FileNotFoundException e1) {
			System.out.println("FileNotFoundException in Main");
			JOptionPane.showMessageDialog(null, "FileNotFoundException in Main");
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Can't load properties file. Using defaults");
		}
       

		Properties usersettings = new Properties();
		UtesProps.loadXMLProperties(usersettings, "configuration.props");

		gb.LOWER_LIMIT = UtesProps.propGetDouble(defsettings, usersettings,
				"LOWER_LIMIT", gb.LOWER_LIMIT, gb.nf);
		gb.UPPER_LIMIT = UtesProps.propGetDouble(defsettings, usersettings,
				"UPPER_LIMIT", gb.UPPER_LIMIT, gb.nf);
		gb.RANGE_START = UtesProps.propGetDouble(defsettings, usersettings,
				"RANGE_START", gb.RANGE_START, gb.nf);
		gb.RANGE_END = UtesProps.propGetDouble(defsettings, usersettings,
				"RANGE_END", gb.RANGE_END, gb.nf);
		gb.STEP_SIZE = UtesProps.propGetDouble(defsettings, usersettings,
				"STEP_SIZE", gb.STEP_SIZE, gb.nf);
		gb.STEP_NUMBER = UtesProps.propGetInt(defsettings, usersettings,
				"STEP_NUMBER", gb.STEP_NUMBER, gb.nf);
		gb.PULSE_TIME = UtesProps.propGetDouble(defsettings, usersettings, "TPULSE",
				gb.PULSE_TIME, gb.nf);
		gb.TIMELAPSE_INTERVAL = UtesProps.propGetDouble(defsettings, usersettings,
				"TIMELAPSE_INTERVAL", gb.TIMELAPSE_INTERVAL, gb.nf);
		gb.TIMELAPSE_NUMBER = UtesProps.propGetInt(defsettings, usersettings,
				"TIMELAPSE_NUMBER", gb.TIMELAPSE_NUMBER, gb.nf);
		gb.TOFF = UtesProps.propGetDouble(defsettings, usersettings, "TOFF",
				gb.TOFF, gb.nf);
		gb.SETTLE_TIME = UtesProps.propGetDouble(defsettings, usersettings,
				"TSETTLE", gb.SETTLE_TIME, gb.nf);
		gb.PULSE_NUMBER = UtesProps.propGetInt(defsettings, usersettings, "PICS",
				gb.PULSE_NUMBER, gb.nf);
		gb.MIRRORLOCKUP = (UtesProps.propGetInt(defsettings, usersettings,
				"MIRRORLOCKUP", gb.MIRRORLOCKUP ?1:0, gb.nf)==1)?true:false;
		gb.UNLOCK_PULSE = UtesProps.propGetDouble(defsettings, usersettings,
				"UNLOCK_PULSE", gb.UNLOCK_PULSE, gb.nf);
		gb.UNLOCK_TIME = UtesProps.propGetDouble(defsettings, usersettings,
				"TUNLOCK", gb.UNLOCK_TIME, gb.nf);
		gb.OP_MODE = UtesProps.propGetInt(defsettings, usersettings, "OP_MODE",
				gb.OP_MODE, gb.nf);
		gb.focalLens = UtesProps.propGetDouble(defsettings, usersettings,
				"FocalLens", gb.focalLens, gb.nf);
		gb.fNumber = UtesProps.propGetDouble(defsettings, usersettings, "fNumber",
				gb.fNumber, gb.nf);
		gb.pupilRatio = UtesProps.propGetDouble(defsettings, usersettings,
				"PupilRatio", gb.pupilRatio, gb.nf);
		gb.measuredFOV1 = UtesProps.propGetDouble(defsettings, usersettings,
				"MeasuredFOV1", gb.measuredFOV1, gb.nf);
		gb.measuredFOV2 = UtesProps.propGetDouble(defsettings, usersettings,
				"MeasuredFOV2", gb.measuredFOV2, gb.nf);
		gb.measuredExtFOV1 = UtesProps.propGetDouble(defsettings, usersettings,
				"MeasuredExtFOV1", gb.measuredExtFOV1, gb.nf);
		gb.measuredExtFOV2 = UtesProps.propGetDouble(defsettings, usersettings,
				"MeasuredExtFOV2", gb.measuredExtFOV2, gb.nf);
		//gb.sensorWidth = UtesProps.propGetInt(defsettings, usersettings,
		//		"SensorWidth", gb.sensorWidth, gb.nf);
		//gb.sensorPixelX = UtesProps.propGetInt(defsettings, usersettings,
		//		"SensorPixelX", gb.sensorPixelX, gb.nf);
		gb.POS1 = UtesProps.propGetDouble(defsettings, usersettings, "POS1",
				gb.POS1, gb.nf);
		gb.POS2 = UtesProps.propGetDouble(defsettings, usersettings, "POS2",
				gb.POS2, gb.nf);
		gb.EXT1 = UtesProps.propGetDouble(defsettings, usersettings, "EXT1",
				gb.EXT1, gb.nf);
		gb.EXT2 = UtesProps.propGetDouble(defsettings, usersettings, "EXT2",
				gb.EXT2, gb.nf);
		gb.theoricExt = UtesProps.propGetDouble(defsettings, usersettings,
				"TheoricExt", gb.theoricExt, gb.nf);
		gb.MAXCOC = UtesProps.propGetDouble(defsettings, usersettings, "MAXCOC",
				gb.MAXCOC, gb.nf);
		gb.DOF_OVERLAP = UtesProps.propGetDouble(defsettings, usersettings,
				"DOF_OVERLAP", gb.DOF_OVERLAP, gb.nf);
		// double pos=gb.propGetDouble(settings, "LASTPOSITION", 0);
		
		propGetLensList(defsettings, usersettings);
		propGetSensorList(defsettings, usersettings);
		propGetRailList(defsettings, usersettings);

		gb.lastLensIndex = UtesProps.propGetInt(defsettings, usersettings,
				"LensIndex", 0, gb.nf);
		gb.lastSensorIndex = UtesProps.propGetInt(defsettings, usersettings,
				"SensorIndex", 0, gb.nf);
		gb.lastRailIndex = UtesProps.propGetInt(defsettings, usersettings,
				"RailIndex", 0, gb.nf);

		gb.openFrameBellows = (UtesProps.propGetInt(defsettings, usersettings,
				"FRAMEBELLOWS", gb.openFrameBellows?1:0, gb.nf) == 1) ? true : false;
		
		gb.CHECKVERSION = (UtesProps.propGetInt(defsettings, usersettings,
				"CHECKVERSION", gb.CHECKVERSION ? 1 : 0, gb.nf) == 1) ? true
				: false;
		gb.OUTPUTTXT = (UtesProps.propGetInt(defsettings, usersettings, "OUTPUTTXT",
				gb.OUTPUTTXT ? 1 : 0, gb.nf) == 1) ? true : false;

		gb.cc.setSettings(UtesProps.propGetString(defsettings, usersettings, "CC"));
		gb.cl.setSettings(UtesProps.propGetString(defsettings, usersettings, "CL"));
		

		// System.out.println("Loaded toastHints :"+gb.toastHints);
	}
	
	
	public static void propPutLensList(Properties parameters) {
		UtesProps.propPutArrayList(parameters, "LENS.ARRAY", gb.lensArray);
	}

	public static void propPutSensorList(Properties parameters) {
		UtesProps.propPutArrayList(parameters, "SENSOR.ARRAY", gb.sensorArray);
	}

	public static void propPutRailList(Properties parameters) {
		UtesProps.propPutArrayList(parameters, "RAIL.ARRAY", gb.railArray);
	}
	
	public static void propSaveGlobals() {

		Properties parameters = new Properties();
		UtesProps.loadXMLProperties(parameters, "configuration.props");

		UtesProps.propPutDouble(parameters, "LOWER_LIMIT", gb.LOWER_LIMIT,
				gb.nf);
		UtesProps.propPutDouble(parameters, "UPPER_LIMIT", gb.UPPER_LIMIT,
				gb.nf);
		UtesProps.propPutDouble(parameters, "RANGE_START", gb.RANGE_START,
				gb.nf);
		UtesProps.propPutDouble(parameters, "RANGE_END", gb.RANGE_END, gb.nf);
		UtesProps.propPutDouble(parameters, "STEP_SIZE", gb.STEP_SIZE, gb.nf);
		UtesProps.propPutInt(parameters, "STEP_NUMBER", gb.STEP_NUMBER, gb.nf);
		UtesProps.propPutDouble(parameters, "TPULSE", gb.PULSE_TIME, gb.nf);
		UtesProps.propPutDouble(parameters, "TIMELAPSE_INTERVAL",
				gb.TIMELAPSE_INTERVAL, gb.nf);
		UtesProps.propPutInt(parameters, "TIMELAPSE_NUMBER",
				gb.TIMELAPSE_NUMBER, gb.nf);
		UtesProps.propPutDouble(parameters, "TOFF", gb.TOFF, gb.nf);
		UtesProps.propPutDouble(parameters, "TSETTLE", gb.SETTLE_TIME, gb.nf);
		UtesProps.propPutInt(parameters, "PICS", gb.PULSE_NUMBER, gb.nf);
		UtesProps.propPutInt(parameters, "MIRRORLOCKUP", gb.MIRRORLOCKUP?1:0, gb.nf);
		UtesProps.propPutDouble(parameters, "UNLOCK_PULSE", gb.UNLOCK_PULSE, gb.nf);
		UtesProps.propPutDouble(parameters, "TUNLOCK", gb.UNLOCK_TIME, gb.nf);
		UtesProps.propPutInt(parameters, "OP_MODE", gb.OP_MODE, gb.nf);
		UtesProps.propPutDouble(parameters, "FocalLens", gb.focalLens, gb.nf);
		UtesProps.propPutDouble(parameters, "fNumber", gb.fNumber, gb.nf);
		UtesProps.propPutDouble(parameters, "PupilRatio", gb.pupilRatio, gb.nf);
		UtesProps.propPutDouble(parameters, "MeasuredFOV1", gb.measuredFOV1,
				gb.nf);
		UtesProps.propPutDouble(parameters, "MeasuredFOV2", gb.measuredFOV2,
				gb.nf);
		UtesProps.propPutDouble(parameters, "MeasuredExtFOV1",
				gb.measuredExtFOV1, gb.nf);
		UtesProps.propPutDouble(parameters, "MeasuredExtFOV2",
				gb.measuredExtFOV2, gb.nf);

		//UtesProps.propPutInt(parameters, "SensorWidth", gb.sensorWidth, gb.nf);
		//UtesProps
		//		.propPutInt(parameters, "SensorPixelX", gb.sensorPixelX, gb.nf);
		UtesProps.propPutDouble(parameters, "POS1", gb.POS1, gb.nf);
		UtesProps.propPutDouble(parameters, "POS2", gb.POS2, gb.nf);
		UtesProps.propPutDouble(parameters, "EXT1", gb.EXT1, gb.nf);
		UtesProps.propPutDouble(parameters, "EXT2", gb.EXT2, gb.nf);
		UtesProps.propPutDouble(parameters, "TheoricExt", gb.theoricExt, gb.nf);
		UtesProps.propPutDouble(parameters, "MAXCOC", gb.MAXCOC, gb.nf);
		UtesProps.propPutDouble(parameters, "DOF_OVERLAP", gb.DOF_OVERLAP,
				gb.nf);
		UtesProps.propPutInt(parameters, "FRAMEBELLOWS", gb.openFrameBellows ? 1 : 0,
				gb.nf);
		UtesProps.propPutInt(parameters, "CHECKVERSION", gb.CHECKVERSION ? 1 : 0,
				gb.nf);
		UtesProps.propPutInt(parameters, "OUTPUTTXT", gb.OUTPUTTXT ? 1 : 0,
				gb.nf);
		// double pos=gb.propGetDouble(settings, "LASTPOSITION", 0);
		propPutLensList(parameters);
		propPutSensorList(parameters);
		propPutRailList(parameters);
		UtesProps.propPutInt(parameters, "LensIndex", gb.lastLensIndex, gb.nf);
		UtesProps.propPutInt(parameters, "SensorIndex", gb.lastSensorIndex, gb.nf);
		UtesProps.propPutInt(parameters, "RailIndex", gb.lastRailIndex, gb.nf);
		UtesProps.propPutString(parameters, "CC", gb.cc.getSettings());
		UtesProps.propPutString(parameters, "CL", gb.cl.getSettings());
		

		UtesProps.saveXMLProperties(parameters, "configuration.props");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	public static String[] lensList() {
		return UtesArrays.listArrayList(gb.lensArray);
	}

	public static String[] sensorList() {
		return UtesArrays.listArrayList(gb.sensorArray);
	}
	
	public static String[] railList() {
		return UtesArrays.listArrayList(gb.railArray);
	}

	
	
	public static void cleanOldInstalls(){
		//delete old files
		UtesFiles.delete("jre6");
		UtesFiles.delete("StackShotCtrl_lib/forms-1.3.0-src.zip");
		UtesFiles.delete("StackShotCtrl_lib/mtb-icon.ico");
		UtesFiles.delete("StackShotCtrl_lib/jd2xx.jar");
    	//delete("ftd2xx.dll");
    	//delete("ftd2xx64.dll");
		UtesFiles.delete("jtd2xx.dll");
    	//delete("launch4j.log");
		UtesFiles.delete("Simple Stackshot Controler.exe");

    	/*Properties settings = new Properties( );
        try {
			settings.load(new FileInputStream("configuration.props"));
			settings.remove("MAX_SPEED");
			settings.remove("TRAMP");
			settings.remove("BACKLASH");
			settings.remove("TORQUE");
			settings.remove("LCD");
			settings.remove("MM_PER_REV");
			settings.remove("LASTPOSITION");
			settings.remove("RAIL");
			settings.remove("SIMUL.HELPER");
			settings.remove("FTD2.HELPER");
			
			settings.store(new FileOutputStream("configuration.props"),null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
 
	}
	
	

	
	public static void setBellowsZero(){
		if (gb.frameBellows==null) {
			if(gb.measuredFOV1!=0) {
				gb.POS1-= gb.rh.getCurrentPosition();
			}
			if (gb.measuredFOV2!=0) {
				gb.POS2-=gb.rh.getCurrentPosition();
			}
		} else {
			gb.frameBellows.setZeroOUT();
		}
}
	
	public static void setBellowsPosition(double val) {
		if (gb.frameBellows==null) {
			double ext = gb.POS1-gb.rh.getCurrentPosition();
			double magfromext = ext/gb.focalLens;
			gb.theoricMag=gb.measuredMag+magfromext;

			double newFOV=(gb.cc.getSensorWidth())/gb.theoricMag;
			
			//Calcul effective fnumber
			//fnumber eff=fnumber*((m/P)+1)
			double efffnum=gb.fNumber*((gb.theoricMag/gb.pupilRatio)+1);
			if (Double.isInfinite(gb.theoricMag)) {
				efffnum=gb.fNumber;
			}
			
			double landa=550;//nm
			double airydisk = 2*1.22*landa/1000*efffnum;
			
			double frontfocus = gb.fNumber*(1+1/gb.theoricMag);
			gb.dofn = -gb.fNumber*gb.coc*(1+gb.theoricMag/gb.pupilRatio)/(gb.theoricMag*gb.theoricMag+gb.theoricMag*(gb.fNumber*gb.coc/1000)/(gb.focalLens))/1000;
			double quotient = gb.theoricMag*gb.theoricMag-gb.theoricMag*(gb.fNumber*gb.coc/1000)/(gb.focalLens);
			if (quotient<0) quotient=0;
			gb.doff = gb.fNumber*gb.coc*(1+gb.theoricMag/gb.pupilRatio)/quotient/1000;

		} else {
			gb.frameBellows.setBellowsPositionOUT(val);
		}
	}
	
	
	public static void processShoot() throws InterruptedException {
		processShoot(0);
	}
	
	public static void processShoot(int step) throws InterruptedException {
		class Code implements Runnable {
			int step=0;
			
			Code(int stepnumber){
				step=stepnumber;
			}
			
			@Override
			public void run(){
				gb.LOCKEDSEQUENCE=true;
				gb.frame.lockInterface(true);
				try {
					process();
				} catch (InterruptedException e) {
					gb.frame.statuslog("CANCELED");
				}
				gb.LOCKEDSEQUENCE=false;
				gb.frame.lockInterface(false);
				
			}
			
			public void process()  throws InterruptedException {
				gb.frame.statuslog("Doing shot "+(step>0?step:"")+ " pulse length "+gb.PULSE_TIME+"s");
				gb.rh.shutterFire(1,gb.PULSE_TIME,0.001);
				do {
					Thread.sleep(50);
				} while (gb.rh.isShutting());
				//gb.frame.statuslog("DONE");

				gb.frame.statuslog("Waiting Time Off "+gb.TOFF+"s");
				int sleep = (int)gb.TOFF*1000-10;
				if (sleep>0){	
					Thread.sleep(sleep);
				}
				//gb.frame.statuslog("DONE");
				
				if(gb.MIRRORLOCKUP){
					gb.frame.statuslog("2nd pulse "+gb.UNLOCK_PULSE+"s");
					gb.rh.shutterFire(1,gb.UNLOCK_PULSE,0.001);
					do {
						Thread.sleep(50);
					} while (gb.rh.isShutting());
					//gb.frame.statuslog("DONE");
					gb.frame.statuslog("2nd Time off "+gb.UNLOCK_TIME+"s");
					sleep = (int)gb.UNLOCK_TIME*1000-10;
					if (sleep>0){	
						Thread.sleep(sleep);
					}
					//gb.frame.statuslog("DONE");
				}
				gb.frame.statuslog("DONE");
			}
		}
		Code code = new Code(step);
		if (!SwingUtilities.isEventDispatchThread()) {
			code.process();
		} else {
			//gb.frame.statuslog("thread");
			gb.yThread = new Thread(code);
			gb.yThread.start();
		}
	}
	
	
}




