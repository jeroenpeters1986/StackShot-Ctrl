package com.macro_toolbox.stackshotctrl;

import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

public class framePrefs extends JDialog {
	// préférences

	private JPanel contentPanePrefs;

	int pulsenumber = gb.PULSE_NUMBER;
	double settletime = gb.SETTLE_TIME;
	double pulsetime = gb.PULSE_TIME;
	boolean mirrorlockup = gb.MIRRORLOCKUP;
	double unlockpulse = gb.UNLOCK_PULSE;
	double unlocktime = gb.UNLOCK_TIME;
	double toff = gb.TOFF;
	double timelapseinterval = gb.TIMELAPSE_INTERVAL;
	int timelapsenumber = gb.TIMELAPSE_NUMBER;

	double maxcoc = gb.MAXCOC;
	double dofoverlap = gb.DOF_OVERLAP;
	private JTextField textFieldPICS;
	private JTextField textFieldTSETTLE;
	private JTextField textFieldTPULSE;
	private JCheckBox checkBoxMIRRORLOCKUP;
	private JTextField textFieldUNLOCKPULSE;
	private JTextField textFieldTUNLOCK;
	private JTextField textFieldTOFF;
	private JTextField textFieldTIMELAPSE_INTERVAL;

	private JTextField textFieldMAXCOC;
	private JTextField textFieldDOFOVERLAP;
	private JButton btnReset;
	private JTextField textFieldTIMELAPSE_NUMBER;


	/**
	 * Create the frame.
	 */
	public framePrefs() {

		pulsenumber = gb.PULSE_NUMBER;
		settletime = gb.SETTLE_TIME;
		pulsetime = gb.PULSE_TIME;
		mirrorlockup = gb.MIRRORLOCKUP;
		unlockpulse = gb.UNLOCK_PULSE;
		unlocktime = gb.UNLOCK_TIME;
		toff = gb.TOFF;
		timelapseinterval = gb.TIMELAPSE_INTERVAL;
		timelapsenumber = gb.TIMELAPSE_NUMBER;

		maxcoc = gb.MAXCOC;
		dofoverlap = gb.DOF_OVERLAP;
		// final JDialog framePreferences = new JDialog();
		setModalityType(ModalityType.APPLICATION_MODAL);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				gb.PULSE_NUMBER = pulsenumber;
				gb.SETTLE_TIME = settletime;
				gb.PULSE_TIME = pulsetime;
				gb.MIRRORLOCKUP = mirrorlockup;
				gb.UNLOCK_PULSE = unlockpulse;
				gb.UNLOCK_TIME = unlocktime;
				gb.TOFF = toff;
				gb.TIMELAPSE_INTERVAL = timelapseinterval;
				gb.TIMELAPSE_NUMBER = timelapsenumber;

				gb.MAXCOC = maxcoc;
				gb.DOF_OVERLAP = dofoverlap;
				// on force les textFields pour éviter leur modifiication par
				// lostfocus
				textFieldPICS.setText(gb.nf.format(gb.PULSE_NUMBER));
				textFieldTSETTLE.setText(gb.nf.format(gb.SETTLE_TIME));
				textFieldTPULSE.setText(gb.nf.format(gb.PULSE_TIME));
				checkBoxMIRRORLOCKUP.setSelected(gb.MIRRORLOCKUP);
				textFieldUNLOCKPULSE.setText(gb.nf.format(gb.UNLOCK_PULSE));
				textFieldUNLOCKPULSE.setEnabled(gb.MIRRORLOCKUP);
				textFieldTUNLOCK.setText(gb.nf.format(gb.UNLOCK_TIME));
				textFieldTUNLOCK.setEnabled(gb.MIRRORLOCKUP);
				textFieldTOFF.setText(gb.nf.format(gb.TOFF));
				textFieldTIMELAPSE_INTERVAL.setText(gb.nf
						.format(gb.TIMELAPSE_INTERVAL));
				textFieldTIMELAPSE_NUMBER.setText(gb.nf
						.format(gb.TIMELAPSE_NUMBER));

				textFieldMAXCOC.setText(gb.nf.format(gb.MAXCOC));
				textFieldDOFOVERLAP.setText(gb.nf.format(gb.DOF_OVERLAP));

				dispose();
			}
		});
		setTitle("Preferences");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(MouseInfo.getPointerInfo().getLocation().x, MouseInfo
				.getPointerInfo().getLocation().y, 276, 296);
		contentPanePrefs = new JPanel();
		contentPanePrefs.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanePrefs);
		contentPanePrefs
				.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.GROWING_BUTTON_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.GROWING_BUTTON_COLSPEC, }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.PREF_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Sequence",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanePrefs.add(panel, "2, 2, 3, 1, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,},
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		JLabel lblPicturesPerStep = new JLabel("Pictures per Step : ");
		panel.add(lblPicturesPerStep, "1, 1, right, default");

		textFieldPICS = new JTextField();
		textFieldPICS.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				setPICS();
			}
		});
		textFieldPICS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setPICS();
			}
		});
		panel.add(textFieldPICS, "3, 1, fill, default");
		textFieldPICS.setColumns(10);
		textFieldPICS.setText(gb.nf.format(gb.PULSE_NUMBER));

		JLabel lblSettleTimes = new JLabel("Settle Time (s) : ");
		panel.add(lblSettleTimes, "1, 3, right, default");

		textFieldTSETTLE = new JTextField();
		textFieldTSETTLE.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setTSETTLE();
			}
		});
		textFieldTSETTLE.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTSETTLE();
			}
		});
		panel.add(textFieldTSETTLE, "3, 3, fill, default");
		textFieldTSETTLE.setColumns(10);
		textFieldTSETTLE.setText(gb.nf.format(gb.SETTLE_TIME));
		
		

		JLabel lblPulseTimes = new JLabel("Pulse Time (s) : ");
		panel.add(lblPulseTimes, "1, 5, right, default");
		
				textFieldTPULSE = new JTextField();
				textFieldTPULSE.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
						setTPULSE();
					}
				});
				textFieldTPULSE.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						setTPULSE();
					}
				});
				panel.add(textFieldTPULSE, "3, 5, fill, default");
				textFieldTPULSE.setColumns(10);
				textFieldTPULSE.setText(gb.nf.format(gb.PULSE_TIME));
		
		
		
		JLabel lblTimeOffBetween = new JLabel(
				"Time Off Between Pictures (s) : ");
		panel.add(lblTimeOffBetween, "1, 7, right, default");
		
				textFieldTOFF = new JTextField();
				textFieldTOFF.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
						setTOFF();
					}
				});
				textFieldTOFF.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						setTOFF();
					}
				});
				panel.add(textFieldTOFF, "3, 7, fill, default");
				textFieldTOFF.setColumns(10);
				textFieldTOFF.setText(gb.nf.format(gb.TOFF));
		
		
		
		JLabel lblMirrorLockup = new JLabel("More Pulse : ");
		panel.add(lblMirrorLockup, "1, 9, right, default");
		
		checkBoxMIRRORLOCKUP = new JCheckBox("");
		panel.add(checkBoxMIRRORLOCKUP, "3, 9, center, center");
		checkBoxMIRRORLOCKUP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (checkBoxMIRRORLOCKUP.isSelected()) {
					gb.MIRRORLOCKUP = true;
					textFieldTUNLOCK.setEnabled(true);
					textFieldUNLOCKPULSE.setEnabled(true);
				} else {
					gb.MIRRORLOCKUP = false;
					textFieldTUNLOCK.setEnabled(false);
					textFieldUNLOCKPULSE.setEnabled(false);
				}
			}
		});
		
				
		
		
		JLabel lblPulseTimeTo = new JLabel("Pulse Time (s) : ");
		lblPulseTimeTo.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblPulseTimeTo, "1, 11, right, default");
		
		textFieldUNLOCKPULSE = new JTextField();
		textFieldUNLOCKPULSE.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setUNLOCKPULSE();
			}
		});
		textFieldUNLOCKPULSE.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setUNLOCKPULSE();

			}
		});
		textFieldUNLOCKPULSE.setText(gb.nf.format(gb.UNLOCK_PULSE));
		textFieldUNLOCKPULSE.setColumns(10);
		panel.add(textFieldUNLOCKPULSE, "3, 11, fill, default");
		
		
		JLabel lblTUNLOCK = new JLabel("Time Off (s) : ");
		lblTUNLOCK.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblTUNLOCK, "1, 13, right, default");
		
		textFieldTUNLOCK = new JTextField();
		textFieldTUNLOCK.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setTUNLOCK();
			}
		});
		textFieldTUNLOCK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setTUNLOCK();

			}
		});
		textFieldTUNLOCK.setText(gb.nf.format(gb.UNLOCK_TIME));
		textFieldTUNLOCK.setColumns(10);
		panel.add(textFieldTUNLOCK, "3, 13, fill, default");	
		

		JLabel lblTimeLapseInterval = new JLabel("Time Lapse Interval (s) : ");
		lblTimeLapseInterval.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblTimeLapseInterval, "1, 15, right, default");

		textFieldTIMELAPSE_INTERVAL = new JTextField();
		textFieldTIMELAPSE_INTERVAL.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setTIMELAPSEINTERVAL();
			}
		});
		textFieldTIMELAPSE_INTERVAL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTIMELAPSEINTERVAL();
			}
		});
		panel.add(textFieldTIMELAPSE_INTERVAL, "3, 15, fill, default");
		textFieldTIMELAPSE_INTERVAL.setColumns(10);
		textFieldTIMELAPSE_INTERVAL
				.setText(gb.nf.format(gb.TIMELAPSE_INTERVAL));

		JLabel lblTimeLapseSeq = new JLabel("Time Lapse Number of Seq : ");
		panel.add(lblTimeLapseSeq, "1, 17, right, default");

		textFieldTIMELAPSE_NUMBER = new JTextField();
		textFieldTIMELAPSE_NUMBER.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setTIMELAPSENUMBER();
			}
		});
		textFieldTIMELAPSE_NUMBER.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setTIMELAPSENUMBER();

			}
		});
		textFieldTIMELAPSE_NUMBER.setText(gb.nf.format(gb.TIMELAPSE_NUMBER));
		textFieldTIMELAPSE_NUMBER.setColumns(10);
		panel.add(textFieldTIMELAPSE_NUMBER, "3, 17, fill, default");
		
		
		
		
		
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"),
				"Adaptive Bellows Preferences", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		contentPanePrefs.add(panel_1, "2, 4, 3, 1, fill, fill");
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.PREF_COLSPEC, },
				new RowSpec[] { FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblMaxCoc = new JLabel("Max Circle Of confusion (x) : ");
		panel_1.add(lblMaxCoc, "1, 1, right, default");

		textFieldMAXCOC = new JTextField();
		panel_1.add(textFieldMAXCOC, "3, 1");
		textFieldMAXCOC.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setMAXCOC();
			}
		});
		textFieldMAXCOC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setMAXCOC();
			}
		});
		textFieldMAXCOC.setColumns(10);
		textFieldMAXCOC.setText(gb.nf.format(gb.MAXCOC));

		JLabel lblDofOverlap = new JLabel("DOF Overlap (x) : ");
		panel_1.add(lblDofOverlap, "1, 3, right, default");

		textFieldDOFOVERLAP = new JTextField();
		panel_1.add(textFieldDOFOVERLAP, "3, 3");
		textFieldDOFOVERLAP.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setDOFOVERLAP();
			}
		});
		textFieldDOFOVERLAP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDOFOVERLAP();
			}
		});
		textFieldDOFOVERLAP.setColumns(10);
		textFieldDOFOVERLAP.setText(gb.nf.format(gb.DOF_OVERLAP));

		btnReset = new JButton("Defaults");
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textFieldPICS.setText(gb.nf.format(gb.defPULSE_NUMBER));
				setPICS();
				textFieldTSETTLE.setText(gb.nf.format(gb.defSETTLE_TIME));
				setTSETTLE();
				
				checkBoxMIRRORLOCKUP.setSelected(gb.defMIRRORLOCKUP);
				gb.MIRRORLOCKUP = gb.defMIRRORLOCKUP;
				checkBoxMIRRORLOCKUP.setSelected(gb.MIRRORLOCKUP);
				textFieldUNLOCKPULSE.setEnabled(gb.MIRRORLOCKUP);
				textFieldTUNLOCK.setEnabled(gb.MIRRORLOCKUP);
				
				textFieldUNLOCKPULSE.setText(gb.nf.format(gb.defUNLOCK_PULSE));
				setUNLOCKPULSE();
				textFieldTUNLOCK.setText(gb.nf.format(gb.defUNLOCK_TIME));
				setTUNLOCK();
				textFieldTPULSE.setText(gb.nf.format(gb.defPULSE_TIME));
				setTPULSE();
				textFieldTOFF.setText(gb.nf.format(gb.defTOFF));
				setTOFF();
				textFieldTIMELAPSE_INTERVAL.setText(gb.nf
						.format(gb.defTIMELAPSE_INTERVAL));
				setTIMELAPSEINTERVAL();
				textFieldTIMELAPSE_NUMBER.setText(gb.nf
						.format(gb.defTIMELAPSE_NUMBER));
				setTIMELAPSENUMBER();

				textFieldMAXCOC.setText(gb.nf.format(gb.defMAXCOC));
				setMAXCOC();
				textFieldDOFOVERLAP.setText(gb.nf.format(gb.defDOF_OVERLAP));
				setDOFOVERLAP();
			}
		});

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		contentPanePrefs.add(btnOk, "2, 6");
		contentPanePrefs.add(btnReset, "4, 6");
		
		
		checkBoxMIRRORLOCKUP.setSelected(gb.MIRRORLOCKUP);
		textFieldUNLOCKPULSE.setEnabled(gb.MIRRORLOCKUP);
		textFieldTUNLOCK.setEnabled(gb.MIRRORLOCKUP);

		
		setIconImages(gb.icons);
		pack();
		setVisible(true);
	}

	void setPICS() {
		int val = 0;
		try {
			val = gb.nf.parse(textFieldPICS.getText()).intValue();

			// gb.frame.statuslog("Motor Speed sets to : "+gb.stepsToMm(gb.MAX_SPEED));
		} catch (ParseException e1) {
			val = gb.PULSE_NUMBER;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldPICS.setText(gb.nf.format(val));
		if (val == gb.PULSE_NUMBER) {
			return;
		}
		gb.PULSE_NUMBER = val;
	}

	void setTSETTLE() {
		double val = 0;
		try {
			val = gb.nf.parse(textFieldTSETTLE.getText()).doubleValue();

			// gb.frame.statuslog("Motor Speed sets to : "+gb.stepsToMm(gb.MAX_SPEED));
		} catch (ParseException e1) {
			val = gb.SETTLE_TIME;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldTSETTLE.setText(gb.nf.format(val));
		if (val == gb.SETTLE_TIME) {
			return;
		}
		gb.SETTLE_TIME = val;
	}

	void setTOFF() {
		double val = 0;
		try {
			val = gb.nf.parse(textFieldTOFF.getText()).doubleValue();

			// gb.frame.statuslog("Motor Speed sets to : "+gb.stepsToMm(gb.MAX_SPEED));
		} catch (ParseException e1) {
			val = gb.TOFF;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldTOFF.setText(gb.nf.format(val));
		if (val == gb.TOFF) {
			return;
		}
		gb.TOFF = val;
	}

	void setUNLOCKPULSE() {
		double val = 0;
		try {
			val = gb.nf.parse(textFieldUNLOCKPULSE.getText()).doubleValue();

			// gb.frame.statuslog("Motor Speed sets to : "+gb.stepsToMm(gb.MAX_SPEED));
		} catch (ParseException e1) {
			val = gb.UNLOCK_PULSE;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldUNLOCKPULSE.setText(gb.nf.format(val));
		if (val == gb.UNLOCK_PULSE) {
			return;
		}
		gb.UNLOCK_PULSE = val;
	}
	
	void setTUNLOCK() {
		double val = 0;
		try {
			val = gb.nf.parse(textFieldTUNLOCK.getText()).doubleValue();

			// gb.frame.statuslog("Motor Speed sets to : "+gb.stepsToMm(gb.MAX_SPEED));
		} catch (ParseException e1) {
			val = gb.UNLOCK_TIME;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldTUNLOCK.setText(gb.nf.format(val));
		if (val == gb.UNLOCK_TIME) {
			return;
		}
		gb.UNLOCK_TIME = val;
	}
	
	void setTIMELAPSEINTERVAL() {
		double val = 0;
		try {
			val = gb.nf.parse(textFieldTIMELAPSE_INTERVAL.getText())
					.doubleValue();

			// gb.frame.statuslog("Motor Speed sets to : "+gb.stepsToMm(gb.MAX_SPEED));
		} catch (ParseException e1) {
			val = gb.TIMELAPSE_INTERVAL;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldTIMELAPSE_INTERVAL.setText(gb.nf.format(val));
		if (val == gb.TIMELAPSE_INTERVAL) {
			return;
		}
		gb.TIMELAPSE_INTERVAL = val;
	}

	void setTIMELAPSENUMBER() {
		int val = 0;
		try {
			val = gb.nf.parse(textFieldTIMELAPSE_NUMBER.getText()).intValue();

		} catch (ParseException e1) {
			val = gb.TIMELAPSE_NUMBER;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldTIMELAPSE_NUMBER.setText(gb.nf.format(val));
		if (val == gb.TIMELAPSE_NUMBER) {
			return;
		}
		gb.TIMELAPSE_NUMBER = val;
	}

	void setTPULSE() {
		double val = 0;
		try {
			val = gb.nf.parse(textFieldTPULSE.getText()).doubleValue();

			// gb.frame.statuslog("Motor Speed sets to : "+gb.stepsToMm(gb.MAX_SPEED));
		} catch (ParseException e1) {
			val = gb.PULSE_TIME;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldTPULSE.setText(gb.nf.format(val));
		if (val == gb.PULSE_TIME) {
			return;
		}
		gb.PULSE_TIME = val;
	}

	void setMAXCOC() {
		double val = 0;
		try {
			val = gb.nf.parse(textFieldMAXCOC.getText()).doubleValue();

			// gb.frame.statuslog("Motor Speed sets to : "+gb.stepsToMm(gb.MAX_SPEED));
		} catch (ParseException e1) {
			val = gb.MAXCOC;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldMAXCOC.setText(gb.nf.format(val));
		if (val == gb.MAXCOC) {
			return;
		}
		gb.MAXCOC = val;
	}

	void setDOFOVERLAP() {
		double val = 0;
		try {
			val = gb.nf.parse(textFieldDOFOVERLAP.getText()).doubleValue();
			// gb.frame.statuslog("Ramp Time sets to : "+gb.RAMP_TIME);
		} catch (ParseException e1) {
			val = gb.DOF_OVERLAP;
			// e1.printStackTrace();
		}
		if (val < 0) {
			val = 0;
		}
		textFieldDOFOVERLAP.setText(gb.nf.format(val));
		if (val == gb.DOF_OVERLAP) {
			return;
		}
		gb.DOF_OVERLAP = val;
	}

}
