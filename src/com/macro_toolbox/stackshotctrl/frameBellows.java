package com.macro_toolbox.stackshotctrl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import mtb.devices.cameras.CameraCanon;
import mtb.devices.cameras.CameraConfigs;
import mtb.swamp.utes.UtesArrays;
import mtb.swamp.utes.UtesNumbers;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class frameBellows extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldFocal;
	private JLabel lblLensFocal;
	private JLabel lblX;
	private JLabel lblMm;

	private JLabel lblFNumber;
	private JTextField textFieldfNumber;
	private JLabel lblAperture;
	private JTextField textFieldAperture;
	private JLabel lblPupilRatio;
	private JTextField textFieldPupilRatio;
	private JTextField textFieldMeasuredMag1;
	private JLabel lblMeasuredMag;
	private JLabel lblExitPupilAperture;
	private JTextField textFieldExitAperture;
	private JLabel lblEffectiveFnumber;
	private JTextField textFieldEffectiveFNumber;
	private JPanel panel_1;
	private JLabel lblMeasuredFOV;
	private JTextField textFieldMeasuredFOV1;
	private JLabel lblMm_3;
	private JLabel lblX_2;
	private JLabel lblCalibration;
	private JLabel lblMagnification;
	private JPanel panel_3;
	private JTextField textFieldTargetMag;
	private JLabel lblX_3;
	private JLabel lblMm_6;
	private JLabel lblMm_7;
	private JLabel lblMeasurment;
	private JSeparator separator_2;
	private JPanel panel_4;
	private JLabel lblPixelSize;
	private JTextField textFieldPixelSize;
	private JLabel lblm;
	private JLabel lblCircleOfConfusion;
	private JTextField textFieldCoC;
	private JLabel lblm_1;
	private JLabel lblAiryDiskDiam;
	private JTextField textFieldAiryDisk;
	private JLabel lblm_2;
	private JTextField textFieldPos1;
	private JLabel lblPos1;
	private JLabel lblMm_8;
	private JLabel lblMeasuredFov;
	private JTextField textFieldMeasuredFOV2;
	private JLabel lblMm_9;
	private JLabel lblMeasuredMag_1;
	private JTextField textFieldMeasuredMag2;
	private JLabel lblX_4;
	private JLabel lblPos2;
	private JTextField textFieldTheoricExt;
	private JLabel lblMm_10;
	private JLabel lblTheoricMag;
	private JTextField textFieldTheoricMag;
	private JLabel lblTheoricFov;
	private JTextField textFieldTheoricFOV;
	private JLabel lblMm_11;
	private JLabel lblX_5;
	private JPanel panel_6;
	private JTextField textFieldPos2;
	private JLabel lblPosition_2;
	private JLabel lblMm_12;
	private JLabel lblFov;
	private JTextField textFieldtargetFOV;
	private JLabel lblMm_13;
	private JLabel lblDofn;
	private JTextField textFieldDOFn1;
	private JLabel lblMm_1;
	private JLabel lblDoff;
	private JTextField textFieldDOFf1;
	private JLabel lblMm_2;
	private JLabel lblDof;
	private JTextField textFieldDOF1;
	private JLabel lblDofn_1;
	private JTextField textFieldDOFn2;
	private JLabel lblMm_14;
	private JLabel lblDoff_1;
	private JTextField textFieldDOFf2;
	private JLabel lblMm_15;
	private JLabel lblDof_1;
	private JTextField textFieldDOF2;
	private JLabel lblMm_16;
	private JLabel lblMm_17;
	private JLabel lblDofn_2;
	private JTextField textFieldTheoricDOFn;
	private JLabel lblMm_18;
	private JLabel lblDoff_2;
	private JTextField textFieldTheoricDOFf;
	private JLabel lblMm_19;
	private JLabel lblDof_2;
	private JTextField textFieldTheoricDOF;
	private JLabel lblMm_20;
	private JRadioButton rdbtnHyperfocal;
	private JLabel lblFocusDist;
	private JTextField textFieldFrontFocus;
	private JLabel lblMm_21;
	private JLabel lblInfPos;
	private JTextField textFieldInfPos;
	private JLabel lblMm_23;
	private JRadioButton rdbtnInfinity;
	private JLabel lblSensor;
	private JLabel lblMeasure;
	private JSeparator separator_5;
	private JComboBox comboBoxLens;
	private JComboBox comboBoxSensor;
	private JButton btnXLens;
	private JButton btnConfigSensor;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JScrollPane scrollPane_3;
	private JLabel lbloptional;
	//private JButton btnLiveview;

	private PopupMenu sensorSelectPopup;

	/**
	 * Create the frame.
	 */
	public frameBellows() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					WindowSaver.saveSettings();
					gb.docker.deregisterDockee(gb.frameBellows);
					gb.frameBellows = null;
					gb.openFrameBellows = false;
					// System.exit(0);
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
		});
		gb.openFrameBellows = true;
		setTitle("Adaptive Bellows Workflow");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 669);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(
				new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("fill:default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("fill:default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("fill:default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("fill:default:grow"), }));

		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "2, 2, fill, fill");

		panel_1 = new JPanel();
		scrollPane.setViewportView(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Equipment",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		lblCalibration = new JLabel("Lens");
		lblCalibration.setFont(lblCalibration.getFont().deriveFont(
				lblCalibration.getFont().getStyle() | Font.BOLD));
		panel_1.add(lblCalibration, "1, 1, left, default");

		comboBoxLens = new JComboBox();
		comboBoxLens
				.setToolTipText("<html>You can save your Lens by typing a name in the combobox and hitting ENTER. <br>You can delete a Lens by clicking on the X Button. <br>You can edit a Lens by selecting it in the combobox, then after values edition, <br>select the combobox textfield and hit ENTER.</html>");

		JTextField lensEditor = (JTextField) comboBoxLens.getEditor()
				.getEditorComponent();

		comboBoxLens.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
				// System.out.println("ici 2");
				String result = (String) comboBoxLens.getSelectedItem();
				// System.out.println(result);
				int index;
				if (result == null)
					return;

				index = UtesArrays.findInArrayList(gb.lensArray, result);
				if (index == -1) {
					index = gb.lastLensIndex;
					JTextField lensEditor = (JTextField) comboBoxLens
							.getEditor().getEditorComponent();
					if (gb.lensArray.size() > 0) {
						comboBoxLens.setSelectedIndex(index);
						lensEditor.setText(gb.lensArray.get(index)[0]);
					} else {
						lensEditor.setText("");
					}
				} else {
					String focal = gb.lensArray.get(index)[1];
					String fnumber = gb.lensArray.get(index)[2];
					String pupil = gb.lensArray.get(index)[3];
					textFieldFocal.setText(focal);
					setFocal();
					textFieldfNumber.setText(fnumber);
					setfNumber();
					textFieldPupilRatio.setText(pupil);
					setPupilRatio();
					gb.lastLensIndex = index;

				}
			}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
			}
		});

		comboBoxLens.setEditable(true);
		panel_1.add(comboBoxLens, "3, 1, 7, 1, fill, default");
		comboBoxLens.setModel(new DefaultComboBoxModel(gb.lensList()));

		btnXLens = new JButton("");
		btnXLens.setToolTipText("Delete Lens from listing.");
		btnXLens.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String result = (String) comboBoxLens.getSelectedItem();
				if (result == null)
					return;
				int index;
				index = UtesArrays.findInArrayList(gb.lensArray, result);
				if (index == -1) {
				} else {
					gb.lensArray.remove(index);
					comboBoxLens.removeAllItems();
					String[] list = gb.lensList();
					for (int i = 0; i < list.length; i++) {
						comboBoxLens.addItem(list[i]);
					}
				}
				JTextField lensEditor = (JTextField) comboBoxLens.getEditor()
						.getEditorComponent();
				gb.lastLensIndex = 0;
				if (gb.lensArray.size() > 0) {
					comboBoxLens.setSelectedIndex(0);
					lensEditor.setText(gb.lensArray.get(0)[0]);
					String focal = gb.lensArray.get(0)[1];
					String fnumber = gb.lensArray.get(0)[2];
					String pupil = gb.lensArray.get(0)[3];
					textFieldFocal.setText(focal);
					setFocal();
					textFieldfNumber.setText(fnumber);
					setfNumber();
					textFieldPupilRatio.setText(pupil);
					setPupilRatio();
				} else {
					lensEditor.setText("");
				}

			}
		});
		panel_1.add(btnXLens, "11, 1, left, default");
		Image img = Toolkit.getDefaultToolkit().getImage("icons/config.png");
		btnXLens.setIcon(new ImageIcon(img));

		btnXLens.setPreferredSize(new Dimension(20, 20));

		lblLensFocal = new JLabel("Focal : ");
		panel_1.add(lblLensFocal, "1, 3");

		textFieldFocal = new JTextField();
		textFieldFocal
				.setToolTipText("<html>If your lens is focused on infinity, then <br>the official Focal Distance is what you need to use. <br>If your lens is not focused on infinity then <br>it's focal distance is surely lower than the official value. <br>In this case you can use the official value in a first step. <br>The use of the optional Measure 2 will <br>allow you to calculate the real Focal Distance.</html>");
		textFieldFocal.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				setFocal();
			}
		});
		panel_1.add(textFieldFocal, "3, 3");
		textFieldFocal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFocal();
			}

		});
		textFieldFocal.setColumns(10);

		lblMm = new JLabel("mm");
		panel_1.add(lblMm, "5, 3");

		lblFNumber = new JLabel("fNumber : ");
		panel_1.add(lblFNumber, "9, 3");

		textFieldfNumber = new JTextField();
		textFieldfNumber
				.setToolTipText("<html>Just use the fNumber that you set up your lens.</html>");
		textFieldfNumber.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setfNumber();
			}
		});
		panel_1.add(textFieldfNumber, "11, 3");
		textFieldfNumber.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setfNumber();
			}
		});
		textFieldfNumber.setColumns(10);
		
				lblPupilRatio = new JLabel("Pupil Ratio : ");
				panel_1.add(lblPupilRatio, "1, 5");
		
				textFieldPupilRatio = new JTextField();
				textFieldPupilRatio
						.setToolTipText("<html>This value corresponds to the ratio between the exit and <br>the entrance Pupils. <br>This ratio is used to calculate the DOF and has some impact <br>on near focusing calculations. <br>If you can't measure it, just use the value 1. <br>In this case the DOF calculated may not be realistic but should be <br>sufficient if you play with the DOF Overlap ratio (in Preferences).</html>");
				textFieldPupilRatio.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
						setPupilRatio();
					}
				});
				panel_1.add(textFieldPupilRatio, "3, 5");
				textFieldPupilRatio.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						setPupilRatio();
					}
				});
				textFieldPupilRatio.setColumns(10);
		
				lblX = new JLabel("X");
				panel_1.add(lblX, "5, 5");
		
				lblAperture = new JLabel("<html>Entrance <br>Aperture : </html>");
				panel_1.add(lblAperture, "9, 5");
		
				textFieldAperture = new JTextField();
				panel_1.add(textFieldAperture, "11, 5");
				textFieldAperture.setEditable(false);
				textFieldAperture.setColumns(10);
		
				lblMm_6 = new JLabel("mm");
				panel_1.add(lblMm_6, "13, 5");
		
				lblExitPupilAperture = new JLabel("<html>Exit <br>Aperture : </html>");
				panel_1.add(lblExitPupilAperture, "17, 5");
		
				textFieldExitAperture = new JTextField();
				panel_1.add(textFieldExitAperture, "19, 5");
				textFieldExitAperture.setEditable(false);
				textFieldExitAperture.setColumns(10);
		
				lblMm_7 = new JLabel("mm");
				panel_1.add(lblMm_7, "21, 5");

		lblSensor = new JLabel("Sensor");
		lblSensor.setFont(lblSensor.getFont().deriveFont(
				lblSensor.getFont().getStyle() | Font.BOLD));
		panel_1.add(lblSensor, "1, 7, left, default");

		comboBoxSensor = new JComboBox();
		comboBoxSensor
				.setToolTipText("<html>You can save your Sensor by typing a name in the combobox and hitting ENTER. <br>You can delete a Sensor by clicking on the X Button. <br>You can edit a Sensor by selecting it in the combobox, then after values edition, <br>select the combobox textfield and hit ENTER.</html>");
		comboBoxSensor.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
				String result = (String) comboBoxSensor.getSelectedItem();
				int index;
				if (result == null)
					return;

				index = gb.cc.findIndexByUserName(result);
				if (index == -1) {
				} else {
					gb.cc.setCurrentCameraByUserName(result);
					valuesCalculate();
				}
			}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
			}
		});
		panel_1.add(comboBoxSensor, "3, 7, 7, 1, fill, default");
		comboBoxSensor.setModel(new DefaultComboBoxModel(gb.cc.listUserName()));

		if (gb.cc.size() != 0)
			comboBoxSensor.setSelectedIndex(gb.cc.findCurrentCameraIndex());

		sensorSelectPopup = new PopupMenu();
		/*MenuItem mntmCameraControl = new MenuItem("Camera Control");
		mntmCameraControl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(gb.cc.getCurrentCamera().getDriver());
				if(gb.cc.getCurrentCamera().getDriver().equals(CameraCanon.DRIVER)) {
					((CameraCanon)gb.cc.getCurrentCamera()).CanonControl(gb.icons, gb.cl, CameraConfigs.NEW);
				}

			}
		});*/
		MenuItem mntmNewSensor = new MenuItem("New Sensor");
		mntmNewSensor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gb.rh == null)
					return;
				gb.cc.showPrefs(gb.icons, gb.cl, CameraConfigs.NEW);
				gb.cc.sort();
				// System.out.println(gb.cl.toString());
				// for(int i=0;i<gb.rh.getSettings().length;i++) {
				// gb.railArray.get(gb.lastRailIndex)[i] =
				// gb.rh.getSettings()[i];
				// }
				valuesCalculate();
				if (gb.cc.size() == 0)
					return;

				comboBoxSensor.removeAllItems();
				String[] list = gb.cc.listUserName();
				for (int i = 0; i < list.length; i++) {
					comboBoxSensor.addItem(list[i]);
				}
				comboBoxSensor.setSelectedIndex(gb.cc.findCurrentCameraIndex());

			}
		});
		MenuItem mntmEditSensor = new MenuItem("Edit Sensor");
		mntmEditSensor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gb.rh == null)
					return;
				gb.cc.showPrefs(gb.icons, gb.cl, CameraConfigs.EDIT);
				gb.cc.sort();
				// System.out.println(gb.cl.toString());
				// for(int i=0;i<gb.rh.getSettings().length;i++) {
				// gb.railArray.get(gb.lastRailIndex)[i] =
				// gb.rh.getSettings()[i];
				// }
				valuesCalculate();
				if (gb.cc.size() == 0)
					return;
				comboBoxSensor.removeAllItems();
				String[] list = gb.cc.listUserName();
				for (int i = 0; i < list.length; i++) {
					comboBoxSensor.addItem(list[i]);
				}
				comboBoxSensor.setSelectedIndex(gb.cc.findCurrentCameraIndex());

			}
		});
		MenuItem mntmDeleteSensor = new MenuItem("Delete Current Sensor");
		mntmDeleteSensor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int newindex = gb.cc.deleteCurrentConfig();
				comboBoxSensor.removeAllItems();
				String[] list = gb.cc.listUserName();
				for (int i = 0; i < list.length; i++) {
					comboBoxSensor.addItem(list[i]);
				}
				comboBoxSensor.setSelectedIndex(newindex);
				valuesCalculate();
			}
		});
		// Add components to pop-up menu
		//sensorSelectPopup.add(mntmCameraControl);
		//sensorSelectPopup.addSeparator();
		sensorSelectPopup.add(mntmNewSensor);
		sensorSelectPopup.add(mntmEditSensor);
		sensorSelectPopup.add(mntmDeleteSensor);

		btnConfigSensor = new JButton("");
		btnConfigSensor.setToolTipText("Sensor Settings");
		btnConfigSensor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (gb.rh == null) {
						gb.frameBellows.getContentPane().add(sensorSelectPopup);
						sensorSelectPopup.show(btnConfigSensor, 0, 0);

					} else {
						gb.frameBellows.getContentPane().add(sensorSelectPopup);
						sensorSelectPopup.show(btnConfigSensor, 0, 0);

					}
				}
			}
		});
		panel_1.add(btnConfigSensor, "11, 7, left, default");
		// Image img=Toolkit.getDefaultToolkit().getImage("icons/erase.png");
		btnConfigSensor.setIcon(new ImageIcon(img));

		btnConfigSensor.setPreferredSize(new Dimension(20, 20));

		//btnLiveview = new JButton("LiveView");
		//btnLiveview.addActionListener(new ActionListener() {
		//	@Override
		//	public void actionPerformed(ActionEvent arg0) {
		//		gb.cl.refreshCameraList();
		//		System.out.println(gb.cl.toString());

				/*
				 * 
				 * Pointer context = new Pointer(0); EdsObjectEventHandler
				 * handler = new EdsObjectEventHandler() {
				 * 
				 * @Override public NativeLong apply(NativeLong inEvent,
				 * Edsdk.EdsBaseRef inRef, Pointer inContext) {
				 * System.out.println( "Event!!!" + inEvent.doubleValue() + ", "
				 * + inContext ); if( inEvent.intValue() == 516 ){
				 * //CanonUtils.download( inRef, null, true ); } return new
				 * NativeLong( -1 ); //return -1; } };
				 * 
				 * lib.EdsSetCameraStateEventHandler( camera[0], new NativeLong(
				 * Edsdk.kEdsObjectEvent_All ), handler, context );
				 * 
				 * // Do stuff here, like ... take an image... result =
				 * lib.EdsSendCommand( camera[0], new NativeLong(
				 * Edsdk.kEdsCameraCommand_TakePicture ), new NativeLong( 0 )
				 * ).intValue(); if( result != Edsdk.EDS_ERR_OK ){
				 * System.out.println( "Error: " + EdsdkHelper.errName( result )
				 * ); }
				 * 
				 * 
				 * 
				 * // Do stuff here, like ... take an image... //result =
				 * EDSDK.EdsSendCommand( camera[0], new NativeLong(
				 * TestLibrary.kEdsCameraCommand_TakePicture ), new NativeLong(
				 * 0 ) ); //check( result );
				 * 
				 * // Wait a little bit! //dispatchMessages();
				 */
		//	}
		//});
		//panel_1.add(btnLiveview, "19, 7");
		
				lblPixelSize = new JLabel("Pixel Size : ");
				panel_1.add(lblPixelSize, "1, 9");
		
				textFieldPixelSize = new JTextField();
				textFieldPixelSize
						.setToolTipText("<html>Pixel Size is simply the result of the ratio between Width and Nb Pixels X. <br>The Height and Nb Pixel Y values are not used.</html>");
				panel_1.add(textFieldPixelSize, "3, 9");
				textFieldPixelSize.setEditable(false);
				textFieldPixelSize.setColumns(10);
		
				lblm = new JLabel("\u00B5m");
				panel_1.add(lblm, "5, 9");
		
				lblCircleOfConfusion = new JLabel("Circle of Confusion Diam : ");
				panel_1.add(lblCircleOfConfusion, "9, 9");
		
				textFieldCoC = new JTextField();
				textFieldCoC
						.setToolTipText("<html>Circle of Confusion Diam corresponds to the maximum size of a blurred point able <br>to resolve to a pixel. <br>This value is the Pixel Size multiplicated by the Max Circle of <br>Confusion (x) ratio found in the Preference window. <br>The ratio is sensor dependent. <br>If theCircle of Confusion Diam value is red, it means that the Airy Disk is greater than <br>the Circle of Confusion and that you have a lose of resolution due to diffraction.</html>");
				panel_1.add(textFieldCoC, "11, 9");
				textFieldCoC.setEditable(false);
				textFieldCoC.setColumns(10);
				
						lblm_1 = new JLabel("\u00B5m");
						panel_1.add(lblm_1, "13, 9");
		
				lblEffectiveFnumber = new JLabel("<html>Effective <br>fNumber : </html>");
				panel_1.add(lblEffectiveFnumber, "1, 11");
				
						textFieldEffectiveFNumber = new JTextField();
						textFieldEffectiveFNumber
								.setToolTipText("<html>The Effective fNumber corresponds to the apparent aperture as seen at the sensor plane. <br>The Effective fNumber is dynamicaly calculated with the fNumber, the Pupil Ratio and the Magnification.</html>");
						textFieldEffectiveFNumber.setEditable(false);
						panel_1.add(textFieldEffectiveFNumber, "3, 11");
						textFieldEffectiveFNumber.setColumns(10);
						
								lblAiryDiskDiam = new JLabel("Airy Disk Diam : ");
								panel_1.add(lblAiryDiskDiam, "9, 11");
								
										textFieldAiryDisk = new JTextField();
										textFieldAiryDisk
												.setToolTipText("<html>The Airy Disk Diam corresponds to the smallest disk an entering ray of light would <br>produce on the sensor. <br>This is a representation of the diffraction. <br>This disk is dynamicaly calculated with the Effective fNumber. <br>If the value is red, it means that the Airy Disk is greater than the minimal Circle of <br>Confusion and that you have a lose of resolution due to diffraction.</html>");
										panel_1.add(textFieldAiryDisk, "11, 11");
										textFieldAiryDisk.setEditable(false);
										textFieldAiryDisk.setColumns(10);
										
												lblm_2 = new JLabel("\u00B5m");
												panel_1.add(lblm_2, "13, 11");
		lensEditor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("ici ");
				String result = (String) comboBoxLens.getSelectedItem();
				int index;
				if (result == null)
					return;
				index = UtesArrays.findInArrayList(gb.lensArray, result);
				String focal = textFieldFocal.getText();
				String fnumber = textFieldfNumber.getText();
				String pupil = textFieldPupilRatio.getText();
				if (index == -1) {
					gb.lensArray.add(new String[] { result, focal, fnumber,
							pupil });
					UtesArrays.sort(gb.lensArray);
					comboBoxLens.removeAllItems();
					String[] list = gb.lensList();
					for (int i = 0; i < list.length; i++) {
						comboBoxLens.addItem(list[i]);
					}
					index = UtesArrays.findInArrayList(gb.lensArray, result);
				} else {
					gb.lensArray.get(index)[1] = focal;
					gb.lensArray.get(index)[2] = fnumber;
					gb.lensArray.get(index)[3] = pupil;
				}

				comboBoxLens.setSelectedIndex(index);
				gb.lastLensIndex = index;
			}
		});
		lensEditor.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				// System.out.println("Choix : ");
				int index = gb.lastLensIndex;
				JTextField lensEditor = (JTextField) comboBoxLens.getEditor()
						.getEditorComponent();
				if (gb.lensArray.size() > 0) {
					lensEditor.setText(gb.lensArray.get(index)[0]);
				} else {
					lensEditor.setText("");
				}
			}
		});
		if (!gb.lensArray.isEmpty())
			comboBoxLens.setSelectedIndex(gb.lastLensIndex);

		scrollPane_1 = new JScrollPane();
		contentPane.add(scrollPane_1, "2, 4, fill, fill");

		panel_4 = new JPanel();
		scrollPane_1.setViewportView(panel_4);
		panel_4.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Calibration",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setLayout(new FormLayout(
				new ColumnSpec[] { FormFactory.PREF_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		lblMeasurment = new JLabel("Measure 1");
		lblMeasurment.setFont(lblMeasurment.getFont().deriveFont(
				lblMeasurment.getFont().getStyle() | Font.BOLD));
		panel_4.add(lblMeasurment, "1, 1, left, default");

		separator_2 = new JSeparator();
		panel_4.add(separator_2, "3, 1, 17, 1");

		lblMeasuredFOV = new JLabel("Measured FOV : ");
		panel_4.add(lblMeasuredFOV, "1, 3");

		textFieldMeasuredFOV1 = new JTextField();
		textFieldMeasuredFOV1
				.setToolTipText("<html>When you put a value in this field, Simple Stackshot Controller records the current <br>Stackshot Position and calculates the corresponding Magnification and Depth of Fields. <br>DOFn is the Depth of Field in front of the focus plane. <br>DOFf is the Depth of Field behind the focus plane and <br>DOF is the addition of DOFn and DOFf. </html>");
		textFieldMeasuredFOV1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (gb.OP_MODE == gb.MO_AdaptiveBellow)
					setMeasuredFOV1();
				else
					setMeasuredExtFOV1();
			}
		});
		textFieldMeasuredFOV1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gb.OP_MODE == gb.MO_AdaptiveBellow)
					setMeasuredFOV1();
				else
					setMeasuredExtFOV1();
			}

		});
		panel_4.add(textFieldMeasuredFOV1, "3, 3");
		textFieldMeasuredFOV1.setColumns(10);

		lblMm_3 = new JLabel("mm");
		panel_4.add(lblMm_3, "5, 3");

		lblMeasuredMag = new JLabel("Measured Mag : ");
		panel_4.add(lblMeasuredMag, "9, 3");

		textFieldMeasuredMag1 = new JTextField();
		textFieldMeasuredMag1.setEditable(false);
		panel_4.add(textFieldMeasuredMag1, "11, 3");
		textFieldMeasuredMag1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (UtesNumbers.isNumber(textFieldMeasuredMag1.getText()) == false) {
					textFieldMeasuredMag1.setText(gb.nf.format(gb.measuredMag));
					return;
				}
				valuesCalculate();
			}
		});
		textFieldMeasuredMag1.setColumns(10);

		lblX_2 = new JLabel("X");
		panel_4.add(lblX_2, "13, 3, left, default");

		lblPos1 = new JLabel("Position : ");
		panel_4.add(lblPos1, "17, 3, right, default");

		textFieldPos1 = new JTextField();
		textFieldPos1.setEditable(false);
		textFieldPos1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setMeasuredEXT1();
			}
		});
		textFieldPos1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setMeasuredEXT1();
			}

		});
		panel_4.add(textFieldPos1, "19, 3, fill, default");
		textFieldPos1.setColumns(10);

		lblMm_8 = new JLabel("mm");
		panel_4.add(lblMm_8, "21, 3");

		lblDofn = new JLabel("DOFn : ");
		panel_4.add(lblDofn, "1, 5, right, default");

		textFieldDOFn1 = new JTextField();
		textFieldDOFn1.setEditable(false);
		panel_4.add(textFieldDOFn1, "3, 5, fill, default");
		textFieldDOFn1.setColumns(10);

		lblMm_1 = new JLabel("mm");
		panel_4.add(lblMm_1, "5, 5");

		lblDoff = new JLabel("DOFf : ");
		panel_4.add(lblDoff, "9, 5, right, default");

		textFieldDOFf1 = new JTextField();
		textFieldDOFf1.setEditable(false);
		panel_4.add(textFieldDOFf1, "11, 5, fill, default");
		textFieldDOFf1.setColumns(10);

		lblMm_2 = new JLabel("mm");
		panel_4.add(lblMm_2, "13, 5");

		lblDof = new JLabel("DOF : ");
		panel_4.add(lblDof, "17, 5, right, default");

		textFieldDOF1 = new JTextField();
		textFieldDOF1.setEditable(false);
		panel_4.add(textFieldDOF1, "19, 5, fill, default");
		textFieldDOF1.setColumns(10);

		lblMm_16 = new JLabel("mm");
		panel_4.add(lblMm_16, "21, 5");

		lblMeasure = new JLabel("Measure 2");
		lblMeasure.setFont(lblMeasure.getFont().deriveFont(
				lblMeasure.getFont().getStyle() | Font.BOLD));
		panel_4.add(lblMeasure, "1, 7, left, default");

		lbloptional = new JLabel("(optional)");
		panel_4.add(lbloptional, "3, 7");

		separator_5 = new JSeparator();
		panel_4.add(separator_5, "5, 7, 15, 1");

		lblMeasuredFov = new JLabel("Measured FOV : ");
		panel_4.add(lblMeasuredFov, "1, 9");

		textFieldMeasuredFOV2 = new JTextField();
		textFieldMeasuredFOV2
				.setToolTipText("<html>This value is optional and has only to be entered when your lens is not focused on infinity or <br>if you have a doubt on the real focal length. <br>When you use it, the Focal Length is recalculated and becomes blue.</html>");
		panel_4.add(textFieldMeasuredFOV2, "3, 9");
		textFieldMeasuredFOV2.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (gb.OP_MODE == gb.MO_AdaptiveBellow)
					setMeasuredFOV2();
				else
					setMeasuredExtFOV2();
			}
		});
		textFieldMeasuredFOV2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.OP_MODE == gb.MO_AdaptiveBellow)
					setMeasuredFOV2();
				else
					setMeasuredExtFOV2();
			}
		});
		textFieldMeasuredFOV2.setColumns(10);

		lblMm_9 = new JLabel("mm");
		panel_4.add(lblMm_9, "5, 9");

		lblMeasuredMag_1 = new JLabel("Measured Mag : ");
		panel_4.add(lblMeasuredMag_1, "9, 9");

		textFieldMeasuredMag2 = new JTextField();
		panel_4.add(textFieldMeasuredMag2, "11, 9");
		textFieldMeasuredMag2.setEditable(false);
		textFieldMeasuredMag2.setColumns(10);

		lblX_4 = new JLabel("X");
		panel_4.add(lblX_4, "13, 9");

		lblPos2 = new JLabel("Position : ");
		panel_4.add(lblPos2, "17, 9");

		textFieldPos2 = new JTextField();
		panel_4.add(textFieldPos2, "19, 9");
		textFieldPos2.setEditable(false);
		textFieldPos2.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setMeasuredEXT2();
			}
		});
		textFieldPos2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setMeasuredEXT2();
			}

		});
		textFieldPos2.setColumns(10);

		lblMm_10 = new JLabel("mm");
		panel_4.add(lblMm_10, "21, 9");

		lblDofn_1 = new JLabel("DOFn : ");
		panel_4.add(lblDofn_1, "1, 11");

		textFieldDOFn2 = new JTextField();
		panel_4.add(textFieldDOFn2, "3, 11");
		textFieldDOFn2.setEditable(false);
		textFieldDOFn2.setColumns(10);

		lblMm_14 = new JLabel("mm");
		panel_4.add(lblMm_14, "5, 11");

		lblDoff_1 = new JLabel("DOFf : ");
		panel_4.add(lblDoff_1, "9, 11");

		textFieldDOFf2 = new JTextField();
		panel_4.add(textFieldDOFf2, "11, 11");
		textFieldDOFf2.setEditable(false);
		textFieldDOFf2.setColumns(10);

		lblMm_15 = new JLabel("mm");
		panel_4.add(lblMm_15, "13, 11");

		lblDof_1 = new JLabel("DOF : ");
		panel_4.add(lblDof_1, "17, 11");

		textFieldDOF2 = new JTextField();
		panel_4.add(textFieldDOF2, "19, 11");
		textFieldDOF2.setEditable(false);
		textFieldDOF2.setColumns(10);

		lblMm_17 = new JLabel("mm");
		panel_4.add(lblMm_17, "21, 11");

		scrollPane_2 = new JScrollPane();
		scrollPane_2
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		contentPane.add(scrollPane_2, "2, 6, fill, fill");

		panel_6 = new JPanel();
		scrollPane_2.setViewportView(panel_6);
		panel_6.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Theoric Values",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_6.setLayout(new FormLayout(
				new ColumnSpec[] { FormFactory.PREF_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		lblTheoricFov = new JLabel("Theoric FOV : ");
		panel_6.add(lblTheoricFov, "1, 1, right, default");

		textFieldTheoricFOV = new JTextField();
		textFieldTheoricFOV
				.setToolTipText("<html>You should see here the current Field Of View (width) on your camera. <br>The value is based on the callibration measures and on the current rail position.</html>");
		panel_6.add(textFieldTheoricFOV, "3, 1");
		textFieldTheoricFOV.setEditable(false);
		textFieldTheoricFOV.setColumns(10);

		lblMm_11 = new JLabel("mm");
		panel_6.add(lblMm_11, "5, 1");

		lblTheoricMag = new JLabel("Theoric Mag : ");
		panel_6.add(lblTheoricMag, "9, 1, right, default");

		textFieldTheoricMag = new JTextField();
		textFieldTheoricMag.setToolTipText("The current Magnification.");
		panel_6.add(textFieldTheoricMag, "11, 1");
		textFieldTheoricMag.setEditable(false);
		textFieldTheoricMag.setColumns(10);

		lblX_5 = new JLabel("X");
		panel_6.add(lblX_5, "13, 1");

		lblPosition_2 = new JLabel("Extension : ");
		panel_6.add(lblPosition_2, "17, 1, right, default");

		textFieldTheoricExt = new JTextField();
		textFieldTheoricExt
				.setToolTipText("Another view of the current rail position.");
		panel_6.add(textFieldTheoricExt, "19, 1");
		textFieldTheoricExt.setEditable(false);
		textFieldTheoricExt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setTheoricExt();
			}
		});
		textFieldTheoricExt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTheoricExt();
			}

		});
		textFieldTheoricExt.setColumns(10);

		lblMm_12 = new JLabel("mm");
		panel_6.add(lblMm_12, "21, 1");

		lblDofn_2 = new JLabel("DOFn : ");
		panel_6.add(lblDofn_2, "1, 3, right, default");

		textFieldTheoricDOFn = new JTextField();
		textFieldTheoricDOFn
				.setToolTipText("The nearest point of focus relative to the position of the focus plane.");
		textFieldTheoricDOFn.setEditable(false);
		panel_6.add(textFieldTheoricDOFn, "3, 3, fill, default");
		textFieldTheoricDOFn.setColumns(10);

		lblMm_18 = new JLabel("mm");
		panel_6.add(lblMm_18, "5, 3");

		lblDoff_2 = new JLabel("DOFf : ");
		panel_6.add(lblDoff_2, "9, 3, right, default");

		textFieldTheoricDOFf = new JTextField();
		textFieldTheoricDOFf
				.setToolTipText("The farthest point of focus relative to the position of the focus plane.");
		textFieldTheoricDOFf.setEditable(false);
		panel_6.add(textFieldTheoricDOFf, "11, 3, fill, default");
		textFieldTheoricDOFf.setColumns(10);

		lblMm_19 = new JLabel("mm");
		panel_6.add(lblMm_19, "13, 3");

		lblDof_2 = new JLabel("DOF : ");
		panel_6.add(lblDof_2, "17, 3, right, default");

		textFieldTheoricDOF = new JTextField();
		textFieldTheoricDOF.setToolTipText("Current Depth Of Field.");
		textFieldTheoricDOF.setEditable(false);
		panel_6.add(textFieldTheoricDOF, "19, 3, fill, default");
		textFieldTheoricDOF.setColumns(10);

		lblMm_20 = new JLabel("mm");
		panel_6.add(lblMm_20, "21, 3");

		lblFocusDist = new JLabel("Front focus dist : ");
		panel_6.add(lblFocusDist, "1, 5, right, default");

		textFieldFrontFocus = new JTextField();
		textFieldFrontFocus
				.setToolTipText("Distance between the Front Principal Plane (something a bit virtual) and the focus plane.");
		textFieldFrontFocus.setEditable(false);
		panel_6.add(textFieldFrontFocus, "3, 5, fill, default");
		textFieldFrontFocus.setColumns(10);

		lblMm_21 = new JLabel("mm");
		panel_6.add(lblMm_21, "5, 5");

		lblInfPos = new JLabel("\u221E Pos : ");
		panel_6.add(lblInfPos, "17, 5, right, default");

		textFieldInfPos = new JTextField();
		textFieldInfPos
				.setToolTipText("Where we have to move the Rail to reach Infinity.");
		textFieldInfPos.setEditable(false);
		panel_6.add(textFieldInfPos, "19, 5, fill, default");
		textFieldInfPos.setColumns(10);

		lblMm_23 = new JLabel("mm");
		panel_6.add(lblMm_23, "21, 5");

		scrollPane_3 = new JScrollPane();
		scrollPane_3
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		contentPane.add(scrollPane_3, "2, 8, fill, fill");

		panel_3 = new JPanel();
		scrollPane_3.setViewportView(panel_3);
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Move To",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.PREF_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormFactory.DEFAULT_ROWSPEC, }));

		lblFov = new JLabel("FOV : ");
		panel_3.add(lblFov, "1, 1, right, default");

		textFieldtargetFOV = new JTextField();
		textFieldtargetFOV
				.setToolTipText("<html>When you enter a FOV value that you want to reach and hit ENTER, <br>the rail will move to the position giving this FOV.</html>");
		textFieldtargetFOV.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setTargetFOV();
			}
		});
		panel_3.add(textFieldtargetFOV, "3, 1, fill, default");
		textFieldtargetFOV.setColumns(10);

		lblMm_13 = new JLabel("mm");
		panel_3.add(lblMm_13, "5, 1");

		lblMagnification = new JLabel("Magnification : ");
		panel_3.add(lblMagnification, "9, 1, right, default");

		textFieldTargetMag = new JTextField();
		textFieldTargetMag
				.setToolTipText("<html>When you enter a Magnification value that you want to reach and hit ENTER, <br>the rail will move to the position giving this Magnification. <br>If you want to go to infinity, just enter 0.</html>");
		textFieldTargetMag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setTargetMag();
			}
		});
		panel_3.add(textFieldTargetMag, "11, 1, fill, default");
		textFieldTargetMag.setColumns(10);

		lblX_3 = new JLabel("X");
		panel_3.add(lblX_3, "13, 1");

		rdbtnHyperfocal = new JRadioButton("Hyperfocal");
		rdbtnHyperfocal
				.setToolTipText("The rail will move to the nearest position where the DOFf is reaching infinity.");
		rdbtnHyperfocal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setHyperfocal();
			}
		});
		panel_3.add(rdbtnHyperfocal, "17, 1, fill, default");

		rdbtnInfinity = new JRadioButton("\u221E");
		rdbtnInfinity
				.setToolTipText("The rail will move to the position where the focus plane is at infinity.");
		rdbtnInfinity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setInfinite();
			}
		});
		panel_3.add(rdbtnInfinity, "21, 1");
		setIconImages(gb.icons);
		valuesInit();
		//activateBellowsFieldsOUT();
		valuesCalculate();
		//if (gb.rh != null)
		//	setBellowsPositionOUT(gb.rh.getCurrentPosition());
		//gb.frame.setUpperLimitEDT(frame.NOPE, gb.UPPER_LIMIT);
		//setUpperLimitColorOUT();

	}

	public void valuesInit() {
		textFieldFocal.setText(gb.nf.format(gb.focalLens));
		textFieldfNumber.setText(gb.nf.format(gb.fNumber));
		textFieldPupilRatio.setText(gb.nf.format(gb.pupilRatio));
		textFieldMeasuredFOV1.setText(gb.nf.format(gb.measuredFOV1));
		textFieldMeasuredFOV2.setText(gb.nf.format(gb.measuredFOV2));
		textFieldPos1.setText(gb.nf.format(gb.POS1));
		textFieldTheoricExt.setText(gb.nf.format(gb.POS2));
	}

	public void valuesCalculate() {
		try {
			// sensor calc
			double pixelsize = (gb.cc.getSensorWidth()) * 1000
					/ gb.cc.getPixelX();
			if (Double.isNaN(pixelsize)) {
				textFieldPixelSize.setText("");
			} else {
				textFieldPixelSize.setText(gb.nf.format(pixelsize));
			}
			// double coc =
			// Math.sqrt(sensorwidth*sensorwidth+sensorheight*sensorheight)*1000/1500;
			gb.coc = pixelsize * gb.MAXCOC;
			if (Double.isNaN(gb.coc)) {
				textFieldCoC.setText("");
			} else {
				textFieldCoC.setText(gb.nf.format(gb.coc));
			}

			// measure 1
			double measuredFOV1 = gb.nf.parse(textFieldMeasuredFOV1.getText())
					.doubleValue();
			double measuredmag1 = (gb.cc.getSensorWidth())
					/ measuredFOV1;
			if (measuredFOV1 == 0) {
				textFieldtargetFOV.setEditable(false);
				textFieldTargetMag.setEditable(false);
				rdbtnHyperfocal.setEnabled(false);
				rdbtnInfinity.setEnabled(false);
			} else {
				textFieldtargetFOV.setEditable(true);
				textFieldTargetMag.setEditable(true);
				rdbtnHyperfocal.setEnabled(true);
				rdbtnInfinity.setEnabled(true);
			}
			gb.measuredMag = measuredmag1;
			if (Double.isNaN(measuredmag1)) {
				textFieldMeasuredMag1.setText("");
			} else {
				textFieldMeasuredMag1.setText(gb.nf.format(measuredmag1));
			}

			if (gb.OP_MODE == gb.MO_AdaptiveBellow) {
				if (Double.isNaN(gb.POS1)) {
					textFieldPos1.setText("");
				} else {
					if (measuredFOV1 == 0) {
						textFieldPos1.setText(gb.nf.format(0));
					} else {
						textFieldPos1.setText(gb.nf.format(gb.POS1));
					}
				}

			} else {
				if (Double.isNaN(gb.EXT1)) {
					textFieldPos1.setText("");
				} else {
					if (measuredFOV1 == 0) {
						textFieldPos1.setText(gb.nf.format(0));
					} else {
						textFieldPos1.setText(gb.nf.format(gb.EXT1));
					}
				}
			}

			// measure 2
			double measuredFOV2 = gb.nf.parse(textFieldMeasuredFOV2.getText())
					.doubleValue();
			double measuredmag2 = (gb.cc.getSensorWidth())
					/ measuredFOV2;

			if (Double.isNaN(measuredmag2)) {
				textFieldMeasuredMag2.setText("");
			} else {
				textFieldMeasuredMag2.setText(gb.nf.format(measuredmag2));
			}

			if (gb.OP_MODE == gb.MO_AdaptiveBellow) {
				if (Double.isNaN(gb.POS2)) {
					textFieldPos2.setText("");
				} else {
					if (measuredFOV2 == 0) {
						textFieldPos2.setText(gb.nf.format(0));
					} else {
						textFieldPos2.setText(gb.nf.format(gb.POS2));
					}
				}
			} else {
				if (Double.isNaN(gb.EXT2)) {
					textFieldPos2.setText("");
				} else {
					if (measuredFOV2 == 0) {
						textFieldPos2.setText(gb.nf.format(0));
					} else {
						textFieldPos2.setText(gb.nf.format(gb.EXT2));
					}
				}
			}

			// if measuredFOV alrs n recalcule la focale
			double focal = gb.nf.parse(textFieldFocal.getText()).doubleValue();
			if (measuredFOV2 > 0) {
				double ext = 0;
				if (gb.OP_MODE == gb.MO_AdaptiveBellow) {
					ext = gb.POS1 - gb.POS2;
				} else {
					ext = gb.EXT1 - gb.EXT2;
				}
				double magfromext = measuredmag2 - measuredmag1;
				focal = ext / magfromext;
				if (Double.isNaN(focal)) {
					textFieldFocal.setText("");
				} else {
					textFieldFocal.setText(gb.nf.format(focal));
				}
				textFieldFocal.setForeground(gb.blue);
			}

			// lens calc
			gb.focalLens = focal;
			double fnumber = gb.nf.parse(textFieldfNumber.getText())
					.doubleValue();
			double aperture = (focal) / fnumber;
			if (Double.isNaN(aperture)) {
				textFieldAperture.setText("");
			} else {
				textFieldAperture.setText(gb.nf.format(aperture));
			}

			double pupilratio = gb.nf.parse(textFieldPupilRatio.getText())
					.doubleValue();
			if (Double.isNaN(aperture * pupilratio)) {
				textFieldExitAperture.setText("");
			} else {
				textFieldExitAperture.setText(gb.nf.format(aperture
						* pupilratio));
			}

			// double focusdist1 = focal*(1+1/measuredmag1);
			double dofn1 = -fnumber
					* gb.coc
					* (1 + measuredmag1 / pupilratio)
					/ (measuredmag1 * measuredmag1 * (1 + (fnumber * gb.coc / 1000)
							/ (focal * measuredmag1))) / 1000;
			double quotient = measuredmag1 * measuredmag1 - measuredmag1
					* (gb.fNumber * gb.coc / 1000) / (gb.focalLens);
			if (quotient < 0)
				quotient = 0;
			double doff1 = fnumber * gb.coc * (1 + measuredmag1 / pupilratio)
					/ quotient / 1000;
			if (Double.isNaN(dofn1)) {
				textFieldDOFn1.setText("");
			} else {
				textFieldDOFn1.setText(gb.nf.format(dofn1));
			}
			if (Double.isNaN(doff1)) {
				textFieldDOFf1.setText("");
			} else {
				textFieldDOFf1.setText(gb.nf.format(doff1));
			}
			if (Double.isNaN(doff1 - dofn1)) {
				textFieldDOF1.setText("");
			} else {
				textFieldDOF1.setText(gb.nf.format(doff1 - dofn1));
			}

			// double focusdist2 = focal*(1+1/measuredmag2);
			double dofn2 = -fnumber
					* gb.coc
					* (1 + measuredmag2 / pupilratio)
					/ (measuredmag2 * measuredmag2 * (1 + (fnumber * gb.coc / 1000)
							/ (focal * measuredmag2))) / 1000;
			quotient = measuredmag2 * measuredmag2 - measuredmag2
					* (gb.fNumber * gb.coc / 1000) / (gb.focalLens);
			if (quotient < 0)
				quotient = 0;
			double doff2 = fnumber * gb.coc * (1 + measuredmag2 / pupilratio)
					/ quotient / 1000;
			if (Double.isNaN(dofn2)) {
				textFieldDOFn2.setText("");
			} else {
				textFieldDOFn2.setText(gb.nf.format(dofn2));
			}
			if (Double.isNaN(doff2)) {
				textFieldDOFf2.setText("");
			} else {
				textFieldDOFf2.setText(gb.nf.format(doff2));
			}
			if (Double.isNaN(doff2 - dofn2)) {
				textFieldDOF2.setText("");
			} else {
				textFieldDOF2.setText(gb.nf.format(doff2 - dofn2));
			}

			// calculate infinite position
			if (gb.OP_MODE == gb.MO_AdaptiveBellow) {
				double magfromext = -gb.measuredMag;
				double ext = magfromext * gb.focalLens;
				gb.infinitePos = gb.POS1 - ext;
				if (Double.isNaN(gb.infinitePos)) {
					gb.infinitePos = 0;
				}
			} else {
				gb.infinitePos = 0;
			}
			textFieldInfPos.setText(gb.nf.format(gb.infinitePos));

			// Calcul effective NAo
			// NAeffo=1/(2*fnumber*((1/m)+(1/P)))
			// double workfnum = efffnum/newmag;
			// textFieldWorkFNum.setText(gb.nf.format(workfnum));

			// double n=1;//indice de l'air
			// NA=nsin(arctan(D/2/f)) avec D=diam entrance pupil
			// =>NAw=nsin(arctan(1/2/fnumber))
			// =>NAw=
			// double NAw = n*Math.sin(Math.atan(1.0/2/workfnum));
			// textFieldNA.setText(gb.nf.format(NAw));

			// m=f/(g-f) => g = f/m+f (g=dist lens-subject)
			// double g=((double)focal)/newmag+focal;
			// m=h/g => h=m*g (h=dist sensor-lens)
			// => h=f(1+m)
			// d=h+g
			// double h=focal+newmag*focal;

			// double wd_change = focal*(1/newmag-1/natmag);
			// double wd_change = 36.0*focal*(1+1.0/newmag)/24.0;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (gb.rh != null)
			setBellowsPositionOUT(gb.rh.getCurrentPosition());
		gb.frame.setUpperLimitEDT(frame.NOPE, gb.UPPER_LIMIT);
		setUpperLimitColorOUT();
		
		activateBellowsFieldsOUT();
	}

	public void setBellowsPositionOUT(double val) {
		class Code implements Runnable {
			private double val;

			public Code(double val) {
				this.val = val;
			}

			@Override
			public void run() {
				// gb.POS2=this.val;
				// textFieldTheoricExt.setText(gb.nf.format(this.val));
				if (gb.rh == null)
					return;
				double ext = 0;
				if (gb.OP_MODE != gb.MO_AdaptiveBellow) {
					ext = gb.theoricExt - gb.EXT1;
				} else {
					ext = gb.POS1 - val;
				}

				double magfromext = ext / gb.focalLens;
				// System.out.println("magfromext "+magfromext);
				gb.theoricMag = gb.measuredMag + magfromext;
				if (gb.theoricMag < 0)
					gb.theoricMag = 0;
				if (Double.isInfinite(gb.theoricMag)
						|| Double.isNaN(gb.theoricMag)) {
					textFieldTheoricMag.setText("");
				} else {
					textFieldTheoricMag.setText(gb.nf.format(gb.theoricMag));
				}

				if (gb.OP_MODE == gb.MO_AdaptiveBellow) {
					double theoricExt = gb.theoricMag * gb.focalLens;
					textFieldTheoricExt.setText(gb.nf.format(theoricExt));
				}

				double newFOV = (gb.cc.getSensorWidth())
						/ gb.theoricMag;
				if (Double.isInfinite(gb.theoricMag)
						|| Double.isNaN(gb.theoricMag)) {
					textFieldTheoricFOV.setText("");
				} else {
					textFieldTheoricFOV.setText(gb.nf.format(newFOV));
				}

				// Calcul effective fnumber
				// fnumber eff=fnumber*((m/P)+1)
				double efffnum = gb.fNumber
						* ((gb.theoricMag / gb.pupilRatio) + 1);
				if (Double.isInfinite(gb.theoricMag)) {
					efffnum = gb.fNumber;
				}
				if (Double.isNaN(efffnum)) {
					textFieldEffectiveFNumber.setText("");
				} else {
					textFieldEffectiveFNumber.setText(gb.nf.format(efffnum));
				}

				double landa = 550;// nm
				double airydisk = 2 * 1.22 * landa / 1000 * efffnum;
				if (Double.isNaN(airydisk)) {
					textFieldAiryDisk.setText("");
				} else {
					textFieldAiryDisk.setText(gb.nf.format(airydisk));
				}

				if (airydisk > gb.coc) {
					textFieldCoC.setForeground(gb.red);
					textFieldAiryDisk.setForeground(gb.red);
				} else {
					textFieldCoC.setForeground(gb.black);
					textFieldAiryDisk.setForeground(gb.black);
				}

				double frontfocus = gb.focalLens * (1 + 1 / gb.theoricMag);
				gb.dofn = -gb.fNumber
						* gb.coc
						* (1 + gb.theoricMag / gb.pupilRatio)
						/ (gb.theoricMag * gb.theoricMag + gb.theoricMag
								* (gb.fNumber * gb.coc / 1000) / (gb.focalLens))
						/ 1000;
				double quotient = gb.theoricMag * gb.theoricMag - gb.theoricMag
						* (gb.fNumber * gb.coc / 1000) / (gb.focalLens);
				if (quotient < 0)
					quotient = 0;
				gb.doff = gb.fNumber * gb.coc
						* (1 + gb.theoricMag / gb.pupilRatio) / quotient / 1000;

				if (Double.isInfinite(gb.theoricMag)
						|| Double.isNaN(gb.theoricMag)) {
					textFieldFrontFocus.setText("");
					textFieldTheoricDOFn.setText("");
					textFieldTheoricDOFf.setText("");
					textFieldTheoricDOF.setText("");
				} else {
					textFieldFrontFocus.setText(gb.nf.format(frontfocus));// 95.56
					textFieldTheoricDOFn.setText(gb.nf.format(gb.dofn));// -16.307
					textFieldTheoricDOFf.setText(gb.nf.format(gb.doff));
					textFieldTheoricDOF
							.setText(gb.nf.format(gb.doff - gb.dofn));
				}

			}
		}
		Code code = new Code(val);
		if (SwingUtilities.isEventDispatchThread()) {
			code.run();
		} else {
			// try {
			// SwingUtilities.invokeAndWait(code);
			SwingUtilities.invokeLater(code);
			// } catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
	}

	public void setZeroOUT() {
		class Code implements Runnable {
			public Code() {
			}

			@Override
			public void run() {
				if (gb.measuredFOV1 != 0) {
					gb.POS1 -= gb.rh.getCurrentPosition();
					textFieldPos1.setText(gb.nf.format(gb.POS1));
				}
				if (gb.measuredFOV2 != 0) {
					gb.POS2 -= gb.rh.getCurrentPosition();
					textFieldPos2.setText(gb.nf.format(gb.POS2));
				}
				textFieldTheoricExt.setText(gb.nf.format(0));
				valuesCalculate();
			}
		}
		Code code = new Code();
		if (SwingUtilities.isEventDispatchThread()) {
			code.run();
		} else {
			try {
				// SwingUtilities.invokeAndWait(code);
				SwingUtilities.invokeAndWait(code);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}

	}

	public void activateBellowsFieldsOUT() {
		class Code implements Runnable {
			public Code() {
			}

			@Override
			public void run() {
				if (gb.OP_MODE == gb.MO_AutoStep) {
					textFieldPos1.setEditable(true);
					lblPos1.setText("Extension\n(mm) : ");
					textFieldPos2.setEditable(true);
					lblPos2.setText("Extension\n(mm) : ");
					textFieldTheoricExt.setEditable(true);
					lblInfPos.setText("∞ Ext \n(mm) : ");

				}
				if (gb.OP_MODE == gb.MO_AutoDist) {
					textFieldPos1.setEditable(true);
					lblPos1.setText("Extension\n(mm) : ");
					textFieldPos2.setEditable(true);
					lblPos2.setText("Extension\n(mm) : ");
					textFieldTheoricExt.setEditable(true);
					lblInfPos.setText("∞ Ext \n(mm) : ");
				}
				if (gb.OP_MODE == gb.MO_ManualDist) {
					textFieldPos1.setEditable(true);
					lblPos1.setText("Extension\n(mm) : ");
					textFieldPos2.setEditable(true);
					lblPos2.setText("Extension\n(mm) : ");
					textFieldTheoricExt.setEditable(true);
					lblInfPos.setText("∞ Ext \n(mm) : ");
				}
				if (gb.OP_MODE == gb.MO_TotalDist) {
					textFieldPos1.setEditable(true);
					lblPos1.setText("Extension\n(mm) : ");
					textFieldPos2.setEditable(true);
					lblPos2.setText("Extension\n(mm) : ");
					textFieldTheoricExt.setEditable(true);
					lblInfPos.setText("∞ Ext \n(mm) : ");
				}
				if (gb.OP_MODE == gb.MO_DistStep) {
					textFieldPos1.setEditable(true);
					lblPos1.setText("Extension\n(mm) : ");
					textFieldPos2.setEditable(true);
					lblPos2.setText("Extension\n(mm) : ");
					textFieldTheoricExt.setEditable(true);
					lblInfPos.setText("∞ Ext \n(mm) : ");
				}
				if (gb.OP_MODE == gb.MO_Manual) {
					textFieldPos1.setEditable(true);
					lblPos1.setText("Extension\n(mm) : ");
					textFieldPos2.setEditable(true);
					lblPos2.setText("Extension\n(mm) : ");
					textFieldTheoricExt.setEditable(true);
					lblInfPos.setText("∞ Ext \n(mm) : ");
				}
				if (gb.OP_MODE == gb.MO_Continuous) {
					textFieldPos1.setEditable(true);
					lblPos1.setText("Extension\n(mm) : ");
					textFieldPos2.setEditable(true);
					lblPos2.setText("Extension\n(mm) : ");
					textFieldTheoricExt.setEditable(true);
					lblInfPos.setText("∞ Ext \n(mm) : ");
				}

				if (gb.OP_MODE == gb.MO_AdaptiveBellow) {
					textFieldPos1.setEditable(false);
					lblPos1.setText("Position\n(mm) : ");
					textFieldPos2.setEditable(false);
					lblPos2.setText("Position\n(mm) : ");
					textFieldTheoricExt.setEditable(false);
					lblInfPos.setText("∞ Pos \n(mm) : ");
				}
			}
		}
		Code code = new Code();
		if (SwingUtilities.isEventDispatchThread()) {
			code.run();
		} else {
			try {
				// SwingUtilities.invokeAndWait(code);
				SwingUtilities.invokeAndWait(code);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}

	}

	public void tempBellowsDeactivateFieldsOUT(boolean on) {
		class Code implements Runnable {
			private boolean on;

			public Code(boolean on) {
				this.on = on;
			}

			@Override
			public void run() {
				gb.frameBellows.textFieldFocal.setEditable(this.on);
				gb.frameBellows.textFieldfNumber.setEditable(this.on);
				gb.frameBellows.textFieldPupilRatio.setEditable(this.on);
				gb.frameBellows.textFieldMeasuredFOV1.setEditable(this.on);
				gb.frameBellows.textFieldMeasuredFOV2.setEditable(this.on);
				gb.frameBellows.comboBoxLens.setEnabled(this.on);
				gb.frameBellows.comboBoxSensor.setEnabled(this.on);
				gb.frameBellows.btnXLens.setEnabled(this.on);
				gb.frameBellows.btnConfigSensor.setEnabled(this.on);
				if (gb.measuredFOV1 == 0) {
					gb.frameBellows.textFieldtargetFOV.setEditable(false);
					gb.frameBellows.textFieldTargetMag.setEditable(false);
					gb.frameBellows.rdbtnHyperfocal.setEnabled(false);
					gb.frameBellows.rdbtnInfinity.setEnabled(false);
				} else {
					gb.frameBellows.textFieldtargetFOV.setEditable(this.on);
					gb.frameBellows.textFieldTargetMag.setEditable(this.on);
					gb.frameBellows.rdbtnHyperfocal.setEnabled(this.on);
					gb.frameBellows.rdbtnInfinity.setEnabled(this.on);
					if (this.on) {
						gb.frameBellows.rdbtnHyperfocal.setSelected(false);
						gb.frameBellows.rdbtnInfinity.setSelected(false);
					}
				}
			}
		}

		Code code = new Code(on);
		if (SwingUtilities.isEventDispatchThread()) {
			code.run();
		} else {
			SwingUtilities.invokeLater(code);
		}
	}

	public void tempDeactivateHyperfocusOUT() {
		class Code implements Runnable {
			@Override
			public void run() {
				gb.frameBellows.rdbtnHyperfocal.setSelected(false);
				gb.frameBellows.rdbtnInfinity.setSelected(false);
			}
		}

		Code code = new Code();
		if (SwingUtilities.isEventDispatchThread()) {
			code.run();
		} else {
			SwingUtilities.invokeLater(code);
		}
	}

	void setFocal() {
		double val = 0;
		try {
			val = gb.nf.parse(textFieldFocal.getText()).doubleValue();
		} catch (ParseException e1) {
			val = gb.focalLens;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldFocal.setText(gb.nf.format(val));
		if (val == gb.focalLens) {
			return;
		}
		gb.focalLens = val;
		Color black = new Color(0, 0, 0);
		textFieldFocal.setForeground(black);
		gb.measuredFOV2 = 0;
		gb.POS2 = 0;
		textFieldMeasuredFOV2.setText(gb.nf.format(gb.measuredFOV2));
		textFieldPos2.setText(gb.nf.format(gb.POS2));
		//if (gb.rh != null)
		//	setBellowsPositionOUT(gb.rh.getCurrentPosition());
		valuesCalculate();
		//gb.frame.setUpperLimitEDT(frame.NOPE, gb.UPPER_LIMIT);
		//setUpperLimitColorOUT();
	}

	void setfNumber() {
		double val = 0;
		try {
			val = gb.nf.parse(textFieldfNumber.getText()).doubleValue();
		} catch (ParseException e1) {
			val = gb.fNumber;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldfNumber.setText(gb.nf.format(val));
		if (val == gb.fNumber) {
			return;
		}

		gb.fNumber = val;
		valuesCalculate();
		//if (gb.rh != null)
		//	setBellowsPositionOUT(gb.rh.getCurrentPosition());
		//gb.frame.setUpperLimitEDT(frame.NOPE, gb.UPPER_LIMIT);
		//setUpperLimitColorOUT();
	}

	void setPupilRatio() {
		double val = 0;
		try {
			val = gb.nf.parse(textFieldPupilRatio.getText()).doubleValue();
		} catch (ParseException e1) {
			val = gb.pupilRatio;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldPupilRatio.setText(gb.nf.format(val));
		if (val == gb.pupilRatio) {
			return;
		}

		gb.pupilRatio = val;

		valuesCalculate();
		//if (gb.rh != null)
		//	setBellowsPositionOUT(gb.rh.getCurrentPosition());
		//gb.frame.setUpperLimitEDT(frame.NOPE, gb.UPPER_LIMIT);
		//setUpperLimitColorOUT();
	}

	void setMeasuredFOV1() {
		if (gb.rh == null)
			return;
		double val = 0;
		try {
			val = gb.nf.parse(textFieldMeasuredFOV1.getText()).doubleValue();
		} catch (ParseException e1) {
			val = gb.measuredFOV1;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldMeasuredFOV1.setText(gb.nf.format(val));

		gb.measuredFOV1 = val;
		gb.POS1 = gb.rh.getCurrentPosition();
		valuesCalculate();
		//if (gb.rh != null)
		//	setBellowsPositionOUT(gb.rh.getCurrentPosition());
		//gb.frame.setUpperLimitEDT(frame.NOPE, gb.UPPER_LIMIT);
		//setUpperLimitColorOUT();
	}

	void setMeasuredExtFOV1() {
		if (gb.rh == null)
			return;
		double val = 0;
		try {
			val = gb.nf.parse(textFieldMeasuredFOV1.getText()).doubleValue();
		} catch (ParseException e1) {
			val = gb.measuredExtFOV1;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldMeasuredFOV1.setText(gb.nf.format(val));

		gb.measuredExtFOV1 = val;
		valuesCalculate();
		//setBellowsPositionOUT(0);
		//gb.frame.setUpperLimitEDT(frame.NOPE, gb.UPPER_LIMIT);
		//setUpperLimitColorOUT();
	}

	void setMeasuredEXT1() {
		if (gb.rh == null)
			return;
		double val = 0;
		try {
			val = gb.nf.parse(textFieldPos1.getText()).doubleValue();
		} catch (ParseException e1) {
			val = gb.EXT1;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldPos1.setText(gb.nf.format(val));

		System.out.println("EXT1: " + val);
		gb.EXT1 = val;
		valuesCalculate();
		//setBellowsPositionOUT(0);
		//gb.frame.setUpperLimitEDT(frame.NOPE, gb.UPPER_LIMIT);
		//setUpperLimitColorOUT();
	}

	void setMeasuredFOV2() {
		if (gb.rh == null)
			return;
		double val = 0;
		try {
			val = gb.nf.parse(textFieldMeasuredFOV2.getText()).doubleValue();
		} catch (ParseException e1) {
			val = gb.measuredFOV2;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldMeasuredFOV2.setText(gb.nf.format(val));

		gb.measuredFOV2 = val;
		gb.POS2 = gb.rh.getCurrentPosition();
		valuesCalculate();
		//if (gb.rh != null)
		//	setBellowsPositionOUT(gb.rh.getCurrentPosition());
		//gb.frame.setUpperLimitEDT(frame.NOPE, gb.UPPER_LIMIT);
		//setUpperLimitColorOUT();
	}

	void setMeasuredExtFOV2() {
		if (gb.rh == null)
			return;
		double val = 0;
		try {
			val = gb.nf.parse(textFieldMeasuredFOV2.getText()).doubleValue();
		} catch (ParseException e1) {
			val = gb.measuredExtFOV2;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldMeasuredFOV2.setText(gb.nf.format(val));

		gb.measuredExtFOV2 = val;
		valuesCalculate();
		//setBellowsPositionOUT(0);
		//gb.frame.setUpperLimitEDT(frame.NOPE, gb.UPPER_LIMIT);
		//setUpperLimitColorOUT();
	}

	void setMeasuredEXT2() {
		if (gb.rh == null)
			return;
		double val = 0;
		try {
			val = gb.nf.parse(textFieldPos2.getText()).doubleValue();
		} catch (ParseException e1) {
			val = gb.EXT2;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldPos2.setText(gb.nf.format(val));

		gb.EXT2 = val;
		valuesCalculate();
		//setBellowsPositionOUT(0);
		//gb.frame.setUpperLimitEDT(frame.NOPE, gb.UPPER_LIMIT);
		//setUpperLimitColorOUT();
	}

	void setTheoricExt() {
		if (gb.rh == null)
			return;
		double val = 0;
		try {
			val = gb.nf.parse(textFieldTheoricExt.getText()).doubleValue();
		} catch (ParseException e1) {
			val = gb.theoricExt;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldTheoricExt.setText(gb.nf.format(val));

		gb.theoricExt = val;
		valuesCalculate();
		//setBellowsPositionOUT(0);
		//gb.frame.setUpperLimitEDT(frame.NOPE, gb.UPPER_LIMIT);
		//setUpperLimitColorOUT();
	}

	void setTargetFOV() {
		if (gb.rh == null)
			return;
		if (textFieldtargetFOV.getText().equals("")) {
			return;
		}
		if (UtesNumbers.isNumber(textFieldtargetFOV.getText()) == false) {
			textFieldtargetFOV.setText(gb.nf.format(gb.targetFOV));
			return;
		}
		int val = 0;
		try {
			val = gb.nf.parse(textFieldtargetFOV.getText()).intValue();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		gb.targetFOV = val;
		textFieldTargetMag.setText("");
		rdbtnHyperfocal.setSelected(false);
		rdbtnInfinity.setSelected(false);
		double mag = (gb.cc.getSensorWidth()) / gb.targetFOV;
		double magfromext = mag - gb.measuredMag;
		double ext = magfromext * gb.focalLens;
		double position = gb.POS1 - ext;
		gb.frame.statuslog("Moving to position " + gb.nf.format(position)
				+ " to reach a FOV of " + gb.targetFOV);
		gb.frame.moveToEDTOUT(frame.NOPE, position);
	}

	void setTargetMag() {
		if (gb.rh == null)
			return;
		if (textFieldTargetMag.getText().equals("")) {
			return;
		}
		if (UtesNumbers.isNumber(textFieldTargetMag.getText()) == false) {
			textFieldTargetMag.setText(gb.nf.format(gb.targetMag));
			return;
		}
		double val = 0;
		try {
			val = gb.nf.parse(textFieldTargetMag.getText()).doubleValue();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		gb.targetMag = val;
		textFieldtargetFOV.setText("");
		rdbtnHyperfocal.setSelected(false);
		rdbtnInfinity.setSelected(false);
		double magfromext = gb.targetMag - gb.measuredMag;
		double ext = magfromext * gb.focalLens;
		double position = gb.POS1 - ext;
		gb.frame.statuslog("Moving to position " + gb.nf.format(position)
				+ " to reach a mag of " + gb.targetMag);
		gb.frame.moveToEDTOUT(frame.NOPE, position);
	}

	void setHyperfocal() {
		if (gb.rh == null)
			return;
		if (!rdbtnHyperfocal.isSelected()) {
			return;
		}
		textFieldtargetFOV.setText("");
		textFieldTargetMag.setText("");
		rdbtnInfinity.setSelected(false);
		double coc = 0;
		try {
			coc = gb.nf.parse(textFieldCoC.getText()).doubleValue();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double hypermag = gb.fNumber * coc / gb.focalLens / 1000;
		double magfromext = hypermag - gb.measuredMag;
		double ext = magfromext * gb.focalLens;
		double position = gb.POS1 - ext;
		gb.frame.statuslog("Moving to position " + gb.nf.format(position)
				+ " to reach hyperfocal");
		gb.frame.moveToEDTOUT(frame.NOPE, position);
	}

	void setInfinite() {
		if (gb.rh == null)
			return;
		if (!rdbtnInfinity.isSelected()) {
			return;
		}
		textFieldtargetFOV.setText("");
		textFieldTargetMag.setText("");
		rdbtnHyperfocal.setSelected(false);
		gb.frame.statuslog("Moving to position " + gb.nf.format(gb.infinitePos)
				+ " to reach infinity");
		gb.frame.moveToEDTOUT(frame.NOPE, gb.infinitePos);
	}

	public void setUpperLimitColorOUT() {
		class Code implements Runnable {
			@Override
			public void run() {
				textFieldInfPos.setForeground(gb.black);
				if (gb.OP_MODE == gb.MO_AdaptiveBellow) {
					if (gb.UPPER_LIMIT > gb.infinitePos) {
						textFieldInfPos.setForeground(gb.blue);
					}
					if (gb.RANGE_START > gb.infinitePos) {
						textFieldInfPos.setForeground(gb.red);
					}
					if (gb.RANGE_END > gb.infinitePos) {
						textFieldInfPos.setForeground(gb.red);
					}
				}
			}
		}
		Code code = new Code();
		if (SwingUtilities.isEventDispatchThread()) {
			code.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(code);
			} catch (InvocationTargetException e) {
				// e.printStackTrace();
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}
		return;
	}

	public int showAdaptiveBellowsStats(int dir) {
		int stepnumber = 0;
		boolean finish = false;
		gb.frame.statuslog("");
		gb.frame.statuslog("Adaptive Bellow Mode");
		double position = gb.RANGE_START;
		boolean enregister = true;

		double startsize = 0;
		double endsize = 0;
		do {
			if (dir == -1) {
				if (position <= gb.RANGE_END) {
					finish = true;
					enregister = false;
				}
			} else {
				if (position >= gb.RANGE_END) {
					finish = true;
					enregister = false;
				}
			}
			double ext = gb.POS1 - position;
			double magfromext = ext / gb.focalLens;
			double theoricMag = gb.measuredMag + magfromext;
			if (theoricMag < 0) {
				theoricMag = 0;
				enregister = false;
			}
			// double newFOV=((double)gb.SensorWidth)/theoricMag;
			// double efffnum=gb.fNumber*((theoricMag/gb.PupilRatio)+1);
			// if (Double.isInfinite(gb.theoricMag)) {
			// efffnum=gb.fNumber;
			// }

			// double landa=550;//nm
			// double airydisk = 2*1.22*landa/1000*efffnum;
			double frontfocus = gb.focalLens * (1 + 1 / theoricMag);

			double offset;
			if (dir == -1) {
				double dofn = -gb.fNumber
						* gb.coc
						* (1 + theoricMag / gb.pupilRatio)
						/ (theoricMag * theoricMag + theoricMag
								* (gb.fNumber * gb.coc / 1000) / (gb.focalLens))
						/ 1000;
				offset = -Math.abs(dofn * gb.DOF_OVERLAP);
			} else {
				double quotient = theoricMag * theoricMag - theoricMag
						* (gb.fNumber * gb.coc / 1000) / (gb.focalLens);
				if (quotient < 0)
					quotient = 0;
				double doff = gb.fNumber * gb.coc
						* (1 + theoricMag / gb.pupilRatio) / quotient / 1000;
				offset = Math.abs(doff * gb.DOF_OVERLAP);
			}
			// System.out.println("offset : "+offset);
			double targetfocus = offset + frontfocus;
			// System.out.println("targetfocus : "+targetfocus);
			double targetmag = 1 / (targetfocus / gb.focalLens - 1);
			// System.out.println("targetmag : "+targetmag);
			double dep = (theoricMag - targetmag) * gb.focalLens;
			if (startsize == 0)
				startsize = dep;
			// System.out.println("ext : "+ext);
			position = dep + position;
			// System.out.println("val : "+dep);
			if (dir == -1) {
				if (position <= gb.RANGE_END) {
					position = gb.RANGE_END;
					enregister = false;
				}
			} else {
				if (position >= gb.RANGE_END) {
					position = gb.RANGE_END;
					enregister = false;
				}
			}
			if (enregister)
				endsize = dep;
			// System.out.println("range end : "+gb.RANGE_END+" position "+position);
			// System.out.println("val : "+endsize);
			stepnumber += 1;
		} while (finish != true);
		int totalsteps = stepnumber - 1;
		gb.frame.statuslog("There will be " + totalsteps + " steps processed.");
		gb.frame.statuslog("Step Size at beginning will be of "
				+ gb.nf.format(startsize) + "mm.");
		gb.frame.statuslog("Step Size at end will be of "
				+ gb.nf.format(endsize) + "mm.");
		double cstepsize = Math.min(startsize, endsize);
		int cstepnumber = (int) Math.round(Math
				.abs((gb.RANGE_END - gb.RANGE_START) / cstepsize));
		gb.frame.statuslog("If you used a constant Step Size of "
				+ gb.nf.format(cstepsize) + "mm the sequence would have taken "
				+ cstepnumber + " steps.");
		gb.frame.statuslog("Adaptive Bellows allows your camera to save "
				+ (cstepnumber - totalsteps) + " triggers.");
		gb.frame.statuslog("Click on Restart to Start");
		return totalsteps;
	}
}
