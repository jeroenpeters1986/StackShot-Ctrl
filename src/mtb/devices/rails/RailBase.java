package mtb.devices.rails;

import java.awt.Image;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;







public class RailBase {
	public static final int 
	RESP_OK=0,
	RESP_NOK=-1;
	public static final boolean IDLE=false, RUNNING=true;

	private boolean railConnected=false;	
	protected boolean finishThreads=false;
	
	private double hardPosition=0;
	private double ofsPosition=0;
	
	private boolean moving=false;
	private boolean shutting=false;
	protected DecimalFormat nf;
			
	protected RailBase(DecimalFormat nf){
		this.nf=nf;
	}
	
	//PUBLIC METHODS
	public String[] open(String [] settings) {
		setRailConnected(true);
		return null;
	}
	
	public String[] close() {
		if(isRailConnected()) setRailConnected(false);
		finishThreads=true;
		return getSettings();
	}
	
	public double getCurrentPosition(){
		return getSoftPosition();
	}
	
	public int setPositionZero(){
		setHardPosition(0);
		setOfsPosition(0);
		return RESP_OK;
	}
	
	public int moveOf(double val){
		if (Double.compare(val, 0)==0) return RESP_NOK;
		return RESP_OK;
	}

	
	public int shutterFire(int nb, double shutterduration, double shutterpause){
		return RESP_OK;
	}
	
	public int stopAll(){
		//System.out.println("rail");
		return RESP_OK;
	}
		
	
	//PRIVATE METHODS
	public double getSoftPosition(){
		return getOfsPosition()+getHardPosition();
	}
	
	public boolean isRailConnected() {
		return railConnected;
	}

	protected void setRailConnected(boolean railOpen) {
		this.railConnected = railOpen;
	}
	
	
	protected double getHardPosition() {
		return hardPosition;
	}

	protected void setHardPosition(double pos) {
		hardPosition = pos;
	}

	protected double getOfsPosition() {
		return ofsPosition;
	}

	protected void setOfsPosition(double pos) {
		ofsPosition = pos;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public boolean isShutting() {
		return shutting;
	}

	
	
	//CALLBACK called during action loop to override 
    public interface StateNotifier{
        public void stateNotifier(boolean running);
    }
    
    protected StateNotifier stateNotifier;
	public void addStateNotifier(StateNotifier callback){
		this.stateNotifier=callback;
	}
	//CALLBACK called during action loop to override 
    
	
	protected void setShutting(boolean shutting) {
		this.shutting = shutting;
	}

	
	
	public String[] getSettings(){ 
		return null;
	}
	 
	protected void setSettings(String[]settings){
	}
	
	public void showPrefs(ArrayList<Image> icontab){}



	public void setRAILNAME(String str) {
	}

	public void setMOTORSPEED(String str) {
	}

	public void setTRAMP(String str) {
	}

	public void setTORQUE(String str) {
	}

	public void setBACKLASH(String str) {
	}

	public void setMMPERREV(String str) {
	}

	public void setSTEPSPERREV(String str) {
	}
	
	public void setHIPRECISION(String str) {
	}
	
	public void setLCD(String str) {
	}
	
	
	
	public String getRAILNAME() {
		return null;
	}
	
	public String getRAILTYPE() {
		return null;
	}

	public double getMOTORSPEED() {
		return 0;
	}

	public double getTRAMP() {
		return 0;
	}

	public int getTORQUE() {
		return 0;
	}

	public double getBACKLASH() {
		return 0;
	}

	public double getMMPERREV() {
		return 0;
	}

	public double getSTEPSPERREV() {
		return 0;
	}
	
	public boolean getHIPRECISION() {
		return false;
	}
	
	public boolean getLCD() {
		return false;
	}
	
	
	
	
	







}




