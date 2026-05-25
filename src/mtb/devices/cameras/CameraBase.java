package mtb.devices.cameras;

import java.awt.Image;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CameraBase {
	private String driver; //EDSDK / USER
	private String userName; 
	private String keyID; //jointure sur ID unique pour la caméra (ie : productName+Serial)
	private String productName;
	private boolean fromTemplate=false;

	private boolean cameraConnected = false;
	protected DecimalFormat nf = null;

	public CameraBase(String userName, String driver, DecimalFormat nf) {
		this.userName = userName;
		this.driver = driver;
		this.nf=nf;
	}

	public CameraBase(String userName, String driver, CameraBase cam, DecimalFormat nf) {
		this.driver = driver;
		this.userName = userName;
		this.keyID = cam.keyID;
		this.productName = cam.productName;
		this.fromTemplate = cam.fromTemplate;
		this.nf=nf;
	}

	// PUBLIC METHODS
	public String open(String settings) {
		setSettings(settings);
		setCameraConnected(true);
		return null;
	}

	public String close() {
		if (isCameraConnected())
			setCameraConnected(false);
		return getSettings();
	}

	public String toString() {
		String str = "";
		str += "userName: " + userName + "\n";
		str += "driver: " + driver + "\n";

		str += "keyID: " + keyID + "\n";
		str += "productName: " + productName + "\n";
		str += "fromTemplate: " + fromTemplate + "\n";
		return str;
	}

	protected double getSensorWidth() {
		return -1;
	}
	protected void setSensorWidth(double sensorWidth) {
		
	}

	protected double getSensorHeight() {
		return -1;
	}

	protected void setSensorHeight(double sensorHeight) {
		
	}

	protected double getPixelX() {
		return -1;
	}

	protected void setPixelX(double pixelX) {

	}

	protected double getPixelY() {
		return -1;
	}

	protected void setPixelY(double pixelY) {
	}
	
	public boolean isCameraConnected() {
		return cameraConnected;
	}

	public void setCameraConnected(boolean cameraConnected) {
		this.cameraConnected = cameraConnected;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getKeyID() {
		return keyID;
	}

	public void setKeyID(String keyID) {
		this.keyID = keyID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public boolean isFromTemplate() {
		return fromTemplate;
	}

	public void setFromTemplate(boolean fromTemplate) {
		this.fromTemplate = fromTemplate;
	}

	protected String getSettings() {
		String Str = "";
		Str += driver + "\t";
		Str += userName + "\t";
		Str += keyID + "\t";
		Str += productName + "\t";
		Str += (fromTemplate?1:0) + "\t";
		
		return Str;
	}

	protected void setSettings(String settings) {
		String[] val = settings.split("\t");
		if (val.length >= 1)
			driver = val[0];
		if (val.length >= 2)
			userName = val[1];
		if (val.length >= 3)
			keyID = val[2];
		if (val.length >= 4) 
			productName = val[3];
		if (val.length >= 5) {
			fromTemplate = (val[4].equals("1")?true:false);
		}
			

		
	}

	protected void showPrefs(ArrayList<Image> icontab) {
	}

}