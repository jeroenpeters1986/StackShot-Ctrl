package mtb.devices.cameras;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import mtb.drivers.edsdk.CanonUtils;
import mtb.drivers.edsdk.EdsdkHelper;
import mtb.drivers.edsdk.EdsdkHelper.CameraRef;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CameraCanon extends CameraBase {
	public static String DRIVER = "EDSDK";
	EdsdkHelper edsdk;
	private CameraRef cameraRef;
	private String ownerName;
	private String makerName;
	private String firmwareVersion;
	private int batteryLevel;
	private int saveTo;
	private String currentStorage;
	private String currentFolder;

	private String batteryQuality;

	private String bodyIDEx;
	private String hDDirectoryStructure;
	private String copyright;
	private String artist;
	private int imageQuality;

	protected CameraCanon(String userName, String driver, DecimalFormat nf) {
		super(userName, driver, nf);
		setDriver(DRIVER);
	}

	public CameraCanon(String userName, String driver, CameraCanon cam,
			DecimalFormat nf) {
		super(userName, driver, cam, nf);
		setDriver(DRIVER);
		this.ownerName = cam.ownerName;
		this.makerName = cam.makerName;
		this.firmwareVersion = cam.firmwareVersion;
		this.batteryLevel = cam.batteryLevel;
		this.saveTo = cam.saveTo;
		this.currentStorage = cam.currentStorage;
		this.currentFolder = cam.currentFolder;
		this.batteryQuality = cam.batteryQuality;
		this.bodyIDEx = cam.bodyIDEx;
		this.hDDirectoryStructure = cam.hDDirectoryStructure;
		this.copyright = cam.copyright;
		this.artist = cam.artist;
		this.imageQuality = cam.imageQuality;
	}

	public String toString() {
		String str = "";
		str += super.toString();
		str += "ownerName: " + ownerName + "\n";
		str += "makerName: " + makerName + "\n";
		str += "firmwareVersion: " + firmwareVersion + "\n";
		str += "batteryLevel: " + batteryLevel + "\n";
		str += "saveTo: " + saveTo + "\n";
		str += "currentStorage: " + currentStorage + "\n";
		str += "currentFolder: " + currentFolder + "\n";

		str += "batteryQuality: " + batteryQuality + "\n";

		str += "bodyIDEx: " + bodyIDEx + "\n";
		str += "hDDirectoryStructure: " + hDDirectoryStructure + "\n";
		str += "copyright: " + copyright + "\n";
		str += "artist: " + artist + "\n";
		str += "imageQuality: " + imageQuality + "\n\n";
		return str;
	}

	public CameraRef getCameraRef() {
		return cameraRef;
	}

	public void setCameraRef(CameraRef cameraRef) {
		this.cameraRef = cameraRef;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getMakerName() {
		return makerName;
	}

	public void setMakerName(String makerName) {
		this.makerName = makerName;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public int getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public int getSaveTo() {
		return saveTo;
	}

	public void setSaveTo(int saveTo) {
		this.saveTo = saveTo;
	}

	public String getCurrentStorage() {
		return currentStorage;
	}

	public void setCurrentStorage(String currentStorage) {
		this.currentStorage = currentStorage;
	}

	public String getCurrentFolder() {
		return currentFolder;
	}

	public void setCurrentFolder(String currentFolder) {
		this.currentFolder = currentFolder;
	}

	public String getBatteryQuality() {
		return batteryQuality;
	}

	public void setBatteryQuality(String batteryQuality) {
		this.batteryQuality = batteryQuality;
	}

	public String getBodyIDEx() {
		return bodyIDEx;
	}

	public void setBodyIDEx(String bodyIDEx) {
		this.bodyIDEx = bodyIDEx;
	}

	public String gethDDirectoryStructure() {
		return hDDirectoryStructure;
	}

	public void sethDDirectoryStructure(String hDDirectoryStructure) {
		this.hDDirectoryStructure = hDDirectoryStructure;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public int getImageQuality() {
		return imageQuality;
	}

	public void setImageQuality(int imageQuality) {
		this.imageQuality = imageQuality;
	}

	public double getSensorWidth() {
		double sensorWidth;
		sensorWidth = CanonUtils.getSensorWidth(getProductName());
		return sensorWidth;
	}

	public double getSensorHeight() {
		double sensorHeight;
		sensorHeight = CanonUtils.getSensorHeight(getProductName());
		return sensorHeight;
	}

	public double getPixelX() {
		double pixelX;
		// System.out.println(getProductName()+" "+ imageQuality);
		pixelX = CanonUtils.getPixelX(getProductName(), imageQuality);
		// System.out.println("pixelX:"+pixelX);
		return pixelX;
	}

	public double getPixelY() {
		double pixelY;
		pixelY = CanonUtils.getPixelY(getProductName(), imageQuality);
		return pixelY;
	}

	public String getSettings() {

		String sup = super.getSettings();
		String Str = "";
		Str += nf.format(saveTo) + "\t";
		Str += currentFolder + "\t";
		Str += bodyIDEx + "\t";
		Str += copyright + "\t";
		Str += artist + "\t";
		Str += nf.format(imageQuality) + "\t";
		// Str += "\t";

		return sup + Str;
	}

	public void setSettings(String settings) {
		super.setSettings(settings);
		String[] val = settings.split("\t");
		try {
			if (val.length >= 6)
				saveTo = nf.parse(val[5]).intValue();
			if (val.length >= 7)
				currentFolder = val[6];
			if (val.length >= 8)
				bodyIDEx = val[7];
			if (val.length >= 9)
				copyright = val[8];
			if (val.length >= 10)
				artist = val[9];
			if (val.length >= 11)
				imageQuality = nf.parse(val[10]).intValue();

		} catch (ParseException e) {
			System.out.println("ParseException in CameraCanon.setSettings");
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	// préférences
	static class ImagePanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Image image = null;

		public ImagePanel(BufferedImage image) {
			changeImage(image);
		}

		public void changeImage(BufferedImage image){
			this.image = image;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(image==null) return;
			int pw=this.getWidth();
			int ph=this.getHeight();
			int iw=image.getWidth(null);
			int ih=image.getHeight(null);
			int w=0;
			int h=0;
			if((((double)pw)/iw)<(((double)ph)/ih)){
				w=pw;
				h=(int) (Math.abs((double)pw/iw)*ih);
			} else {
				h=ph;
				w=(int) (Math.abs((double)ph/ih)*iw);
			}
			g.drawImage(image, 0, 0, w, h, null);
		}
	}

	ArrayList<Image> icons;
	ImagePanel panel;
	JFrame frame;
	RefreshImageWorker refreshImageWorker;
	/**
	 * @wbp.parser.entryPoint
	 */
	public void CanonControl(ArrayList<Image> icontab, final CameraList cl,
			int action) {

		/**
		 * Create the frame.
		 */
		icons = icontab;
		try {
			panel = new ImagePanel(ImageIO.read(new File("IMG_0066.JPG")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		frame = new JFrame("LiveView");
		frame.setName("SSC.LIVEVIEW");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				refreshImageWorker.cancel(true);
				
								
			}
		});

		 frame.setSize(320, 320/3*2);
	        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	        frame.getContentPane().add(panel, BorderLayout.CENTER);
	        panel.setLayout(new FormLayout(new ColumnSpec[] {
	        		FormFactory.RELATED_GAP_COLSPEC,
	        		ColumnSpec.decode("right:default"),
	        		FormFactory.RELATED_GAP_COLSPEC,
	        		FormFactory.BUTTON_COLSPEC,
	        		FormFactory.RELATED_GAP_COLSPEC,
	        		ColumnSpec.decode("default:grow"),},
	        	new RowSpec[] {
	        		FormFactory.RELATED_GAP_ROWSPEC,
	        		FormFactory.DEFAULT_ROWSPEC,
	        		FormFactory.RELATED_GAP_ROWSPEC,
	        		FormFactory.DEFAULT_ROWSPEC,
	        		FormFactory.RELATED_GAP_ROWSPEC,
	        		FormFactory.DEFAULT_ROWSPEC,
	        		FormFactory.RELATED_GAP_ROWSPEC,
	        		FormFactory.DEFAULT_ROWSPEC,}));
	        
	        JLabel lblMode = new JLabel("Mode");
	        lblMode.setOpaque(true);
	        panel.add(lblMode, "2, 2, right, default");
	        
	        JComboBox comboBoxMode = new JComboBox();
	        panel.add(comboBoxMode, "4, 2, fill, default");
	        
	        JLabel lblTv = new JLabel("Tv");
	        lblTv.setOpaque(true);
	        panel.add(lblTv, "2, 4, right, default");
	        
	        JComboBox comboBoxTv = new JComboBox();
	        panel.add(comboBoxTv, "4, 4, fill, default");
	        
	        JLabel lblAv = new JLabel("Av");
	        lblAv.setOpaque(true);
	        panel.add(lblAv, "2, 6, right, default");
	        
	        JComboBox comboBoxAv = new JComboBox();
	        panel.add(comboBoxAv, "4, 6, fill, default");
	        
	        JLabel lblIso = new JLabel("Iso");
	        lblIso.setOpaque(true);
	        panel.add(lblIso, "2, 8, right, default");
	        
	        JComboBox comboBoxIso = new JComboBox();
	        panel.add(comboBoxIso, "4, 8, fill, default");
	        frame.setVisible(true);
	       refreshImageWorker = new RefreshImageWorker();
	       refreshImageWorker.execute();

	}
	
	
	
	
	class RefreshImageWorker extends SwingWorker<Void, Void> {

			protected Void  doInBackground() throws Exception {
				if (edsdk == null) {
					try {
						edsdk = new EdsdkHelper();
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}

				}
				if (edsdk == null)
					return null;
				
				//System.out.println("cameralist start");
				ArrayList<CameraRef> list = edsdk.getCameraListTask();
				//System.out.println("cameralist end");
				if (list == null)
					return null;
				
				//System.out.println("mykey:"+getKeyID());
				for (int i = 0; i < list.size(); i++) {
					CameraRef camref = list.get(i);
					//System.out.println("key:"+camref.keyID);
					
					//System.out.println("pos:"+pos);
					if(camref.keyID.equals(getKeyID())) {
						setCameraRef(camref);
						//System.out.println("found");
						break;
					}
				}

				if(getCameraRef()==null) return null;
				
				edsdk.openSessionTask(getCameraRef());
				edsdk.beginLiveViewTask(getCameraRef());
				while(true){
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						break;
					} 
					BufferedImage image = edsdk.downloadLiveViewImageTask(getCameraRef());

					

					if( image != null ){
						//System.out.println(image.getWidth() + "," + image.getHeight());
						image.flush(); 
				        panel.changeImage(image);
				        frame.repaint();
					}
				}
				System.out.println("breaked");
				edsdk.endLiveViewTask(getCameraRef());
				edsdk.closeSessionTask(getCameraRef());
				return null;

	        }
	

			protected void done()
		    {
		    }

	}
	
	

}