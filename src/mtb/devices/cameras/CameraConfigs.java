package mtb.devices.cameras;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import mtb.drivers.edsdk.CanonUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CameraConfigs {
	ArrayList<CameraBase> cameraConfigs = new ArrayList<CameraBase>();
	CameraBase currentConfig=null;
	DecimalFormat nf;
	//static boolean configFromTemplates=false;
	public static int NEW=1;
	public static int EDIT=2;
	
	
	public CameraConfigs(DecimalFormat nf) {
		this.nf=nf;
	}

	
	public String toString(){
		String str="";
		for (int i = 0; i < cameraConfigs.size(); i++) {
			str+= cameraConfigs.get(i).toString();
		}
		return str;
	}

	public double getSensorWidth() {
		if(size()==0) return 0;
		return getCurrentCamera().getSensorWidth();
	}
	public void setSensorWidth(double sensorWidth) {
		getCurrentCamera().setSensorWidth(sensorWidth);
	}

	public double getSensorHeight() {
		if(size()==0) return 0;
		return getCurrentCamera().getSensorHeight();
	}

	public void setSensorHeight(double sensorHeight) {
		getCurrentCamera().setSensorHeight(sensorHeight);
	}

	public double getPixelX() {
		if(size()==0) return 0;
		return getCurrentCamera().getPixelX();
	}

	public void setPixelX(double pixelX) {
		getCurrentCamera().setPixelX(pixelX);
	}

	public double getPixelY() {
		if(size()==0) return 0;
		return getCurrentCamera().getPixelY();
	}

	public void setPixelY(double pixelY) {
		getCurrentCamera().setPixelY(pixelY);
	}
	
	
	
	// get a String[] constructed with an index of an ArrayList
	public String[] listUserName() {
		String[] list = new String[cameraConfigs.size()];

		for (int i = 0; i < cameraConfigs.size(); i++) {
			list[i] = cameraConfigs.get(i).getUserName();
		}
		return list;
	}

	// sort an ArrayList on index
	public void sort() {
		Collections.sort(cameraConfigs, new Comparator<CameraBase>() {
			public int compare(CameraBase a, CameraBase b) {
				return Integer.signum(a.getUserName()
						.compareTo(b.getUserName()));
			}
		});
	}
	
	public int size(){
		return cameraConfigs.size();
	}
	
	public int findIndexByUserName(String val) {
		int res = -1;
		for (int i = 0; i < cameraConfigs.size(); i++) {
			if (val.equals(cameraConfigs.get(i).getUserName())) {
				res = i;
				break;
			}
		}
		return res;
	}

	public CameraBase findCameraByUserName(String val) {
		for (int i = 0; i < cameraConfigs.size(); i++) {
			if (val.equals(cameraConfigs.get(i).getUserName())) {
				return cameraConfigs.get(i);
			}
		}
		return null;
	}

	
	public int findCurrentCameraIndex() {
		int res = -1;
		if(currentConfig!=null){
			for (int i = 0; i < cameraConfigs.size(); i++) {
				if (currentConfig.getUserName().equals(cameraConfigs.get(i).getUserName())) {
					res = i;
					break;
				}
			}
		}
		return res;
	}
	
	
	public CameraBase getCurrentCamera(){
		return currentConfig;
	}

	public int setCurrentCameraByUserName(String userName){
		int res = -1;
		for (int i = 0; i < cameraConfigs.size(); i++) {
			if (userName.equals(cameraConfigs.get(i).getUserName())) {
				res = i;
				currentConfig = cameraConfigs.get(i);
				break;
			}
		}
		return res;
	}
	
	public int deleteCurrentConfig(){
		int res=-1;
		res=findIndexByUserName(currentConfig.getUserName());
		if(res>=0){
			cameraConfigs.remove(res);
			res-=1;
		}
		if(res>=0){
			currentConfig=cameraConfigs.get(res);
			
		} else {
			if(size()>0) {
				currentConfig=cameraConfigs.get(0);
				res=0;
			}
				
			else
				currentConfig=null;
		}
		return res;
		
	}
	
	public String getFirstFreeName(String baseName) {
		int i = 0;
		String str = null;
		do {
			if(i==0) str=baseName;
			else str = baseName +"-"+ i;
			if (findIndexByUserName(str) == -1)
				break;
			i++;
		} while (true);
		return str;

	}

	

	public String getSettings(){
	        String Str="";
	        for(int i=0;i<cameraConfigs.size();i++) {
	        	Str+=cameraConfigs.get(i).getSettings();
	        	Str+="\t";
	        }
	        Str+=((currentConfig!=null)?currentConfig.getUserName():"")+"\t\t";
	     return Str;
	}
	 
	public void setSettings(String string){
		if(string==null) return;
		String [] list = string.split("\t\t");
		for(int i=0;i<list.length-1;i++){
			String[] cam = list[i].split("\t");
			if(findIndexByUserName(cam[1])==-1){
				if(cam[0].equals(CameraUser.DRIVER)) {
					CameraUser nc = new CameraUser("", "", nf);
					nc.setSettings(list[i]);
					cameraConfigs.add(nc);
				}
				if(cam[0].equals(CameraCanon.DRIVER)) {
					CameraCanon nc = new CameraCanon("", "", nf);
					nc.setSettings(list[i]);
					cameraConfigs.add(nc);
				}
			}
		}
		if(list.length==0) currentConfig=null;
		else currentConfig=list[list.length-1]!=null?findCameraByUserName(list[list.length-1]):null;
	}
	
	
	
	
	
	
	
	//préférences
	
	
		JPanel contentPane;
		
		JTextField textFieldSensorName;
		JComboBox comboBoxCamera;
		JButton btnCancel;
		JComboBox comboBoxMainImage;
		JComboBox comboBoxSecondaryImage;
		JComboBox comboBoxCompress;
		ArrayList<Image> icons;
		private JTextField textFieldSensorWidth;
		private JTextField textFieldSensorHeight;
		private JTextField textFieldPixelX;
		private JTextField textFieldPixelY;
		private JTextField textFieldPixelSize;
		private JRadioButton rdbtnTemplates;
		private JRadioButton rdbtnDetectedCameras;
		//private JButton btnDetect;
		JButton btnOk;
		
		CameraBase editcamera=null;
		int iaction;
	/**
	 * @wbp.parser.entryPoint
	 */
	public void showPrefs(ArrayList<Image> icontab, final CameraList cl, int action){
		/**
		 * Create the frame.
		 */
		icons=icontab;
		iaction=action;
	
		//System.out.println("BEFORE::::::::"+currentConfig);
		if(size()!=0 && action==EDIT) {
			if(currentConfig.getDriver().equals(CameraUser.DRIVER)) {
				editcamera = new CameraUser(currentConfig.getUserName(), currentConfig.getDriver(), nf);
				editcamera.setKeyID(CameraUser.DRIVER);
				editcamera.setProductName(CameraUser.DRIVER);
				editcamera.setSensorHeight(currentConfig.getSensorHeight());
				editcamera.setSensorWidth(currentConfig.getSensorWidth());
				editcamera.setPixelX(currentConfig.getPixelX());
				editcamera.setPixelY(currentConfig.getPixelY());
			} else {
				editcamera = new CameraCanon(currentConfig.getUserName(), currentConfig.getDriver(), (CameraCanon) currentConfig, nf);
			}
		}
			
		if(action==NEW){
			String name = getFirstFreeName("New Config");
			editcamera = new CameraUser(name, CameraUser.DRIVER, nf);
			editcamera.setKeyID(CameraUser.DRIVER);
			editcamera.setProductName(CameraUser.DRIVER);
		}
		
		
		final JDialog framePreferences = new JDialog();
		framePreferences.setModalityType(ModalityType.APPLICATION_MODAL);
		
		framePreferences.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				framePreferences.dispose();
			}
		});
		framePreferences.setTitle("Sensor Config");
		framePreferences.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		int mousex=MouseInfo.getPointerInfo().getLocation().x;
		int mousey = MouseInfo.getPointerInfo().getLocation().y;
		if(mousex<0 && mousex>-544) mousex=-544;
		if(mousey<0 && mousex>-269) mousex=-269;
		framePreferences.setBounds(mousex, mousey, 544, 269);
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:pref"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "2, 2, 3, 1, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel("Sensor Name : ");
		panel.add(lblNewLabel, "1, 1, right, default");
		textFieldSensorName = new JTextField();
		panel.add(textFieldSensorName, "3, 1");
		textFieldSensorName.setColumns(10);
		textFieldSensorName.setText(editcamera.getUserName());
		
		
		textFieldSensorName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setCONFIGNAME();
			}
		});
		textFieldSensorName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCONFIGNAME();
			}
		});
		
		JButton btnNameauto = new JButton();
		btnNameauto.setToolTipText("Fill Name Auto");
		Image img = Toolkit.getDefaultToolkit().getImage("icons/autoname.png");
		btnNameauto.setIcon(new ImageIcon(img));
		btnNameauto.setPreferredSize(new Dimension(20, 20));
		btnNameauto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(comboBoxCamera.getSelectedIndex()<=0) return;
				String name = comboBoxCamera.getSelectedItem().toString();
				String base=null;
				if(name.equals(CameraUser.DRIVER)) {
					base=CameraUser.DRIVER+"@"+textFieldPixelX.getText()+"x"+textFieldPixelY.getText();
					textFieldSensorName.setText(getFirstFreeName(base));
					setCONFIGNAME();					
				} else {
					if(editcamera.isFromTemplate()) {
						base="tpl:"+editcamera.getProductName();
						base+="@"+comboBoxMainImage.getSelectedItem();
						textFieldSensorName.setText(getFirstFreeName(base));
						setCONFIGNAME();					
						
					} else {
						base=editcamera.getProductName()+"-"+editcamera.getKeyID();
						base+="@"+comboBoxMainImage.getSelectedItem();
						textFieldSensorName.setText(getFirstFreeName(base));
						setCONFIGNAME();					
					}
				}
			}
		});
		panel.add(btnNameauto, "5, 1");
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Config from :", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_4, "2, 4, 3, 1, fill, fill");
		panel_4.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		
		rdbtnTemplates = new JRadioButton("Templates");
		rdbtnTemplates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editcamera.setFromTemplate(true);
				initializeDialog(cl);
				//comboBoxCamera.firePopupMenuWillBecomeInvisible(); 
				
				//framePreferences.pack();
			}
		});
		panel_4.add(rdbtnTemplates, "1, 1");
		
		rdbtnDetectedCameras = new JRadioButton("User Cameras");
		rdbtnDetectedCameras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editcamera.setFromTemplate(false); 
				initializeDialog(cl);
				//comboBoxCamera.firePopupMenuWillBecomeInvisible();
				//framePreferences.pack();
			}
		});

		panel_4.add(rdbtnDetectedCameras, "3, 1");
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnTemplates);
		group.add(rdbtnDetectedCameras);
		
		//btnDetect = new JButton("Detect");
		//btnDetect.setToolTipText("Detect Connected Cameras");
		//panel_4.add(btnDetect, "5, 1");
		/*btnDetect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cl.refreshCameraList();
				initializeDialog(cl);
			}
		});*/
			
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Device Selection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_1, "2, 6, 3, 1, fill, fill");
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblMotorSpeed = new JLabel("Camera : ");
		panel_1.add(lblMotorSpeed, "1, 1, right, default");
		
		comboBoxCamera = new JComboBox();
		panel_1.add(comboBoxCamera, "3, 1, fill, default");
		comboBoxCamera.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent arg0) {
			}
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
				String result = (String) comboBoxCamera.getSelectedItem();
				//System.out.println(result);

			
				if(result==null) return;
				if(editcamera.isFromTemplate()) {
					if (result.equals(editcamera.getProductName()))
						return;
					if(comboBoxCamera.getSelectedIndex()<=0) return;

					//cameraConfigs.remove(findIndexByUserName(currentConfig.getUserName()));
					String name = getFirstFreeName("New Config");

					editcamera=new CameraCanon(name, CameraCanon.DRIVER, nf);
					editcamera.setKeyID(name);
					editcamera.setProductName(result);
					editcamera.setFromTemplate(true);
					((CameraCanon) editcamera).setImageQuality(
							CanonUtils.mainFormatNameToImageQuality(result, 
									CanonUtils.getMainFormatNames(result)[0], 0)
									);

					//System.out.println(getCurrentCamera());
					//System.out.println("x:"+getCurrentCamera().getPixelX());
					initializeDialog(cl);
				} else {
					if (cl.findCameraByKeyID(editcamera.getKeyID())!=null &&
							result.equals(cl.findCameraByKeyID(editcamera.getKeyID()).getUserName()))
						return;
					if(comboBoxCamera.getSelectedIndex()<=0) return;
					
					double x=0;
					double y=0;
					double w=0;
					double h=0;
					if(result.equals(CameraUser.DRIVER)) {
						x=editcamera.getPixelX();
						y=editcamera.getPixelY();
						w=editcamera.getSensorWidth();
						h=editcamera.getSensorHeight();
					}
					
					//if(size()!=0) cameraConfigs.remove(findIndexByUserName(currentConfig.getUserName()));
					
					String name = getFirstFreeName("New Config");
					if(!result.equals(CameraUser.DRIVER)) {
						editcamera= new CameraCanon(name, CameraCanon.DRIVER,
								(CameraCanon) cl.findCameraByUserName(result), nf);
					} else {
						editcamera = new CameraUser(name, CameraUser.DRIVER, nf);
						editcamera.setKeyID(CameraUser.DRIVER);
						editcamera.setProductName(CameraUser.DRIVER);
						editcamera.setPixelX(x);
						editcamera.setPixelY(y);
						editcamera.setSensorWidth(w);
						editcamera.setSensorHeight(h);
					}
					
					initializeDialog(cl);
				}
					

				
				
			}
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
			}
		});
		comboBoxCamera.setModel(new DefaultComboBoxModel());
			
		
		btnCancel = new JButton("Reset");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(size()!=0 && iaction==EDIT) {
					if(currentConfig.getDriver().equals(CameraUser.DRIVER)) {
						editcamera = new CameraUser(currentConfig.getUserName(), currentConfig.getDriver(), nf);
					} else {
						editcamera = new CameraCanon(currentConfig.getUserName(), currentConfig.getDriver(), (CameraCanon) currentConfig, nf);
					}
				}
				if(iaction==NEW){
					String name = getFirstFreeName("New Config");
					editcamera = new CameraUser(name, CameraUser.DRIVER, nf);
					editcamera.setKeyID(CameraUser.DRIVER);
					editcamera.setProductName(CameraUser.DRIVER);
				};
				initializeDialog(cl);
			}
		});
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if(iaction==EDIT){
					deleteCurrentConfig();					
				}
				cameraConfigs.add(editcamera);
				currentConfig=editcamera;
				framePreferences.dispose();
			}
		});
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_3, "2, 8, 3, 1, fill, fill");
		panel_3.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblWidth = new JLabel("Width : ");
		panel_3.add(lblWidth, "1, 1, right, default");
		
		textFieldSensorWidth = new JTextField();
		panel_3.add(textFieldSensorWidth, "3, 1, fill, default");
		textFieldSensorWidth.setColumns(10);
		textFieldSensorWidth.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setSensorWidth();
			}
		});
		textFieldSensorWidth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSensorWidth();
			}
		});
		
		JLabel lblMm = new JLabel("mm");
		panel_3.add(lblMm, "5, 1");
		
		JLabel lblHeight = new JLabel("Height : ");
		panel_3.add(lblHeight, "9, 1, right, default");
		
		textFieldSensorHeight = new JTextField();
		panel_3.add(textFieldSensorHeight, "11, 1, fill, default");
		textFieldSensorHeight.setColumns(10);
		textFieldSensorHeight.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setSensorHeight();
			}
		});
		textFieldSensorHeight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSensorHeight();
			}
		});
		
		JLabel lblMm_2 = new JLabel("mm");
		panel_3.add(lblMm_2, "13, 1");
		
		JLabel lblNbPixelsX = new JLabel("Nb Pixels X : ");
		panel_3.add(lblNbPixelsX, "1, 3, right, default");
		
		textFieldPixelX = new JTextField();
		panel_3.add(textFieldPixelX, "3, 3, fill, default");
		textFieldPixelX.setColumns(10);
		textFieldPixelX.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setPixelX();
			}
		});
		textFieldPixelX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPixelX();
			}
		});
		
		JLabel lblNbPixelsY = new JLabel("Nb Pixels Y : ");
		panel_3.add(lblNbPixelsY, "9, 3, right, default");
		
		textFieldPixelY = new JTextField();
		panel_3.add(textFieldPixelY, "11, 3, fill, default");
		textFieldPixelY.setColumns(10);
		textFieldPixelY.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setPixelY();
			}
		});
		textFieldPixelY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPixelY();
			}
		});
		
		JLabel lblPixelSize = new JLabel("Pixel Size : ");
		panel_3.add(lblPixelSize, "1, 5, right, default");
		
		textFieldPixelSize = new JTextField();
		textFieldPixelSize.setEnabled(false);
		textFieldPixelSize.setEditable(false);
		panel_3.add(textFieldPixelSize, "3, 5, fill, default");
		textFieldPixelSize.setColumns(10);
		
		JLabel lblm = new JLabel("\u00B5m");
		panel_3.add(lblm, "5, 5");
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Image Type", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_2, "2, 10, 3, 1, fill, fill");
		panel_2.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblMain = new JLabel("Main Image");
		panel_2.add(lblMain, "1, 1");
		
		JLabel lblSecondary = new JLabel("Secondary Image");
		panel_2.add(lblSecondary, "3, 1");
		
		JLabel lblCompressQuality = new JLabel("Compress Quality");
		panel_2.add(lblCompressQuality, "5, 1");
		
		comboBoxMainImage = new JComboBox();
		panel_2.add(comboBoxMainImage, "1, 3");
		comboBoxMainImage.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent arg0) {
			}
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
				String result = (String) comboBoxMainImage.getSelectedItem();
				// System.out.println(result);
				((CameraCanon) editcamera).setImageQuality(
						CanonUtils.mainFormatNameToImageQuality(
								editcamera.getProductName(), 
								result, 
								((CameraCanon) editcamera).getImageQuality()) );
				initializeDialog(cl);
			
				if (result == null)
					return;

			}
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
			}
		});
		comboBoxMainImage.setModel(new DefaultComboBoxModel());
		
		comboBoxSecondaryImage = new JComboBox();
		panel_2.add(comboBoxSecondaryImage, "3, 3");
		comboBoxSecondaryImage.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent arg0) {
			}
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
				String result = (String) comboBoxSecondaryImage.getSelectedItem();
				// System.out.println(result);
				((CameraCanon) editcamera).setImageQuality(
						CanonUtils.secondFormatNameToImageQuality(
								editcamera.getProductName(), 
								result, 
								((CameraCanon) editcamera).getImageQuality()) );
				initializeDialog(cl);
				
				if (result == null)
					return;

			}
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
			}
		});
		comboBoxSecondaryImage.setModel(new DefaultComboBoxModel());


		
		comboBoxCompress = new JComboBox();
		panel_2.add(comboBoxCompress, "5, 3");
		comboBoxCompress.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent arg0) {
			}
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
				String result = (String) comboBoxCompress.getSelectedItem();
				// System.out.println(result);
				int qual=-1;
				if(comboBoxMainImage.getSelectedItem().toString().startsWith("Jpeg")) {
					qual=CanonUtils.mainCompressNameToImageQuality(
							editcamera.getProductName(), 
							result, 
							((CameraCanon) editcamera).getImageQuality()); 
				} else if(comboBoxSecondaryImage.getSelectedItem().toString().startsWith("Jpeg")) {
					qual=CanonUtils.secondCompressNameToImageQuality(
							editcamera.getProductName(), 
							result, 
							((CameraCanon) editcamera).getImageQuality()); 
				}
							
							
				((CameraCanon) editcamera).setImageQuality(qual);
				initializeDialog(cl);
			
				if (result == null)
					return;

			}
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
			}
		});
		comboBoxCompress.setModel(new DefaultComboBoxModel());

		
		contentPane.add(btnOk, "2, 12");
		contentPane.add(btnCancel, "4, 12");

		
		
		
		
		
		
		initializeDialog(cl);
		
		
		
		framePreferences.setIconImages(icons);
		framePreferences.pack();
		framePreferences.setVisible(true);
	}

	
	
	
	
	
	
	
	
	
	
	//TODO
	void initializeDialog(final CameraList cl) {
		//dialog setup
		
		//radiobuttons
		if(editcamera.isFromTemplate()) {
			rdbtnTemplates.setSelected(true);
			//rdbtnDetectedCameras.setSelected(false);
			//btnDetect.setEnabled(false);
		}
		else  {
			//rdbtnTemplates.setSelected(false);
			rdbtnDetectedCameras.setSelected(true);
			//btnDetect.setEnabled(true);
		}

		//textfields (set to true si CameraUSer (see bellow)
		textFieldSensorWidth.setEnabled(false);
		textFieldSensorHeight.setEnabled(false);
		textFieldPixelX.setEnabled(false);
		textFieldPixelY.setEnabled(false);
		//combobox main, secondary and compress
		//set to false si CameraUser (see above)
		comboBoxMainImage.setEnabled(true);
		comboBoxSecondaryImage.setEnabled(true);
		comboBoxCompress.setEnabled(true);
		//if(size()==0) {
		//	comboBoxMainImage.setEnabled(false);
		//	comboBoxSecondaryImage.setEnabled(false);
		//	comboBoxCompress.setEnabled(false);
		//} 
				
		//combobox camera
		if(editcamera.isFromTemplate()) {
			
			comboBoxSecondaryImage.setEnabled(false);
			comboBoxCompress.setEnabled(false);

			comboBoxCamera.removeAllItems();
			String[] list = CanonUtils.listCameras();
			comboBoxCamera.addItem("Select Template");
			for (int i = 0; i < list.length; i++) {
				comboBoxCamera.addItem(list[i]);
				//System.out.println(list[i]);
			}
			
			int index = CanonUtils.findIndexByProductName(editcamera.getProductName());
			comboBoxCamera.setSelectedIndex(index==-1?0:index+1);
			//if(index==-1)
			//	comboBoxCamera.firePopupMenuWillBecomeInvisible(); 
		} else {
			comboBoxCamera.removeAllItems();
			String[] list = cl.listUserName();
			if(list.length!=0) {
				comboBoxCamera.addItem("Select Camera");
				for (int i = 0; i < list.length; i++) {
					System.out.println(list[i]);
					comboBoxCamera.addItem(list[i]);
				}

			}

			System.out.println(getCurrentCamera());
			System.out.println(editcamera);
			comboBoxCamera.setSelectedIndex(cl.findIndexByKeyID(editcamera.getKeyID())+1);
			if(editcamera.getDriver().equals(CameraUser.DRIVER)) {
				textFieldSensorWidth.setEnabled(true);
				textFieldSensorHeight.setEnabled(true);
				textFieldPixelX.setEnabled(true);
				textFieldPixelY.setEnabled(true);
				comboBoxMainImage.setEnabled(false);
				comboBoxSecondaryImage.setEnabled(false);
				comboBoxCompress.setEnabled(false);
			}
		}
		
		//comboboxmain
		comboBoxMainImage.removeAllItems();
		if(!editcamera.getDriver().equals(CameraUser.DRIVER)) {
			String[] list = CanonUtils.getMainFormatNames(editcamera.getProductName());
			for (int i = 0; i < list.length; i++) {
				comboBoxMainImage.addItem(list[i]);
			}
			
			int mainIndex = CanonUtils.ImageQualityToMainImageIndex(
					editcamera.getProductName(),
				((CameraCanon) editcamera).getImageQuality());
			comboBoxMainImage.setSelectedIndex(mainIndex==-1?0:mainIndex);
		}
		//comboboxsecondary
		comboBoxSecondaryImage.removeAllItems();
		if(!editcamera.getDriver().equals(CameraUser.DRIVER)) {
			String[] list = CanonUtils.getSecondFormatNames(
					editcamera.getProductName(),
					comboBoxMainImage.getSelectedItem().toString());
			comboBoxSecondaryImage.addItem("None");
			if(list!=null) {
				for (int i = 0; i < list.length; i++) {
					comboBoxSecondaryImage.addItem(list[i]);
				}
			}
				int secondIndex = CanonUtils.ImageQualityToSecondImageIndex(
						editcamera.getProductName(),
					comboBoxMainImage.getSelectedItem().toString(),
					((CameraCanon) editcamera).getImageQuality());
			comboBoxSecondaryImage.setSelectedIndex(secondIndex+1);
		}
		//compress
		comboBoxCompress.removeAllItems();
		if(!editcamera.getDriver().equals(CameraUser.DRIVER)) {
			if(comboBoxMainImage.getSelectedItem().toString().startsWith("Jpeg") ||
					(comboBoxSecondaryImage.getSelectedIndex()!=-1 && comboBoxSecondaryImage.getSelectedItem().toString().startsWith("Jpeg"))) {
				String[] list = CanonUtils.getCompressFormatsNames(editcamera.getProductName());
				for (int i = 0; i < list.length; i++) {
					comboBoxCompress.addItem(list[i]);
				}
					int compressIndex = CanonUtils.ImageQualityToCompressIndex(
							editcamera.getProductName(),
						((CameraCanon) editcamera).getImageQuality());
				comboBoxCompress.setSelectedIndex(compressIndex==-1?0:compressIndex);
			} else {
				comboBoxCompress.setEnabled(false);
			}
		}

		
		//System.out.println(comboBoxCamera.getSelectedIndex());
		if(comboBoxCamera.getSelectedIndex()<=0) {
			comboBoxMainImage.setEnabled(false);
			comboBoxSecondaryImage.setEnabled(false);
			comboBoxCompress.setEnabled(false);
		}
		
		textFieldSensorWidth.setText(nf.format(editcamera.getSensorWidth()));
		textFieldSensorHeight.setText(nf.format(editcamera.getSensorHeight()));
		textFieldPixelX.setText(nf.format(editcamera.getPixelX()));
		textFieldPixelY.setText(nf.format(editcamera.getPixelY()));
		valuesCalculate();

		textFieldSensorName.setText(editcamera.getUserName());
		//OK
		if(comboBoxCamera.getSelectedIndex()<=0) {
			btnOk.setEnabled(false);
		} else {
			btnOk.setEnabled(true);
		}

	}
	
	
	void setCONFIGNAME(){
		String val=null;
		
		val=textFieldSensorName.getText();
		if(val.equals("") || findIndexByUserName(val)!=-1){
			val=editcamera.getUserName();
		}
		textFieldSensorName.setText(val);
		if(val.equals(editcamera)) {
			return;
		}
		
		editcamera.setUserName(val);
	}
	
	void setSensorWidth() {
		double val = 0;
		try {
			val = nf.parse(textFieldSensorWidth.getText()).doubleValue();
		} catch (ParseException e1) {
			val = editcamera.getSensorWidth();
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldSensorWidth.setText(nf.format(val));
		if (val == editcamera.getSensorWidth()) {
			return;
		}

		editcamera.setSensorWidth(val);
		valuesCalculate();
	}

	void setSensorHeight() {
		double val = 0;
		try {
			val = nf.parse(textFieldSensorHeight.getText()).doubleValue();
		} catch (ParseException e1) {
			val = editcamera.getSensorHeight();
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldSensorHeight.setText(nf.format(val));
		if (val == editcamera.getSensorHeight()) {
			return;
		}
		editcamera.setSensorHeight(val);
		valuesCalculate();
	}

	void setPixelX() {
		double val = 0;
		try {
			val = nf.parse(textFieldPixelX.getText()).doubleValue();
		} catch (ParseException e1) {
			val = editcamera.getPixelX();
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldPixelX.setText(nf.format(val));
		if (val == editcamera.getPixelX()) {
			return;
		}
		editcamera.setPixelX(val);
		valuesCalculate();
	}

	void setPixelY() {
		double val = 0;
		try {
			val = nf.parse(textFieldPixelY.getText()).intValue();
		} catch (ParseException e1) {
			val = editcamera.getPixelY();
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldPixelY.setText(nf.format(val));
		if (val == editcamera.getPixelY()) {
			return;
		}

		editcamera.setPixelY(val);
		valuesCalculate();
	}
	
	public void valuesCalculate() {

			// sensor calc
			double pixelsize = ((double) editcamera.getSensorWidth()) * 1000
					/ editcamera.getPixelX();
			if (Double.isNaN(pixelsize)) {
				textFieldPixelSize.setText("");
			} else {
				textFieldPixelSize.setText(nf.format(pixelsize));
			}
			
	}
	
}
