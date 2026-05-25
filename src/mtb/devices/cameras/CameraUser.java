package mtb.devices.cameras;

import java.text.DecimalFormat;
import java.text.ParseException;

public class CameraUser extends CameraBase {
	public static String DRIVER="USER";
	private double sensorWidth;
	private double sensorHeight;
	private double pixelX;
	private double pixelY;

	protected CameraUser(String userName, String driver, DecimalFormat nf) {
		super(userName, driver, nf);
		setDriver(DRIVER);
	}

	public String toString() {
		String str = "";
		str += super.toString();
		str += "sensorWidth: " + sensorWidth + "\n";
		str += "sensorHeight: " + sensorHeight + "\n";
		str += "pixelX: " + pixelX + "\n";
		str += "pixelY: " + pixelY + "\n";
		return str;
	}

	public double getSensorWidth() {
		return sensorWidth;
	}

	public void setSensorWidth(double sensorWidth) {
		this.sensorWidth = sensorWidth;
	}

	public double getSensorHeight() {
		return sensorHeight;
	}

	public void setSensorHeight(double sensorHeight) {
		this.sensorHeight = sensorHeight;
	}

	public double getPixelX() {
		//System.out.println("in base");
		return pixelX;
	}

	public void setPixelX(double pixelX) {
		this.pixelX = pixelX;
	}

	public double getPixelY() {
		return pixelY;
	}

	public void setPixelY(double pixelY) {
		this.pixelY = pixelY;
	}

	public String getSettings() {

		String sup = super.getSettings();
		String Str = "";
		Str += nf.format(sensorWidth) + "\t";
		Str += nf.format(sensorHeight) + "\t";
		Str += nf.format(pixelX) + "\t";
		Str += nf.format(pixelY) + "\t";
		
		return sup+Str;
	}

	public void setSettings(String settings) {
		super.setSettings(settings);
		String[] val = settings.split("\t");
		try {
			if (val.length >= 6)
				sensorWidth = nf.parse(val[5]).intValue();
			if (val.length >= 7)
				sensorHeight = nf.parse(val[6]).intValue();
			if (val.length >= 8)
				pixelX = nf.parse(val[7]).intValue();
			if (val.length >= 9)
				pixelY = nf.parse(val[8]).intValue();
		} catch (ParseException e) {
			System.out.println("ParseException in CameraCanon.setSettings");
		}
	}

}