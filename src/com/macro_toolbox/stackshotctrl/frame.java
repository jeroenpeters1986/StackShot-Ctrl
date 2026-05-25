package com.macro_toolbox.stackshotctrl;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MouseInfo;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import mtb.devices.rails.RailBase;
import mtb.devices.rails.RailBase.StateNotifier;
import mtb.devices.rails.RailStackshot;
import mtb.devices.rails.RailVirtual;
import mtb.swamp.utes.UtesArrays;
import mtb.swamp.utes.UtesNumbers;
import mtb.swamp.utes.UtesStrings;
import mtb.swamp.utes.UtesTelnet;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class frame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField textFieldPosition;
	private JButton btnBck;
	private boolean btnBackDown=false; 
	private JButton btnFwd;
	private boolean btnFwdDown=false; 
	private JLabel lblRangeStart;
	private JLabel lblRangeEnd;
	private JTextField textFieldRangeStart;
	private JTextField textFieldRangeEnd;
	private JButton btnSetStart;
	private JButton btnSetEnd;
	private static JTextArea textAreaLog;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JScrollPane scrollPane;
	private JLabel lblStepSize;
	private JTextField textFieldStepSize;
	private JLabel lblStepNumber;
	private JTextField textFieldStepNumber;
	private JButton btnBckStep;
	private JButton btnFwdStep;
	private JButton btnBckXSteps;
	private JButton btnFwdXSteps;
	private JLabel lblLowerLimit;
	private JTextField textFieldLowerLimit;
	private JButton btnSetLower;
	private JLabel lblUpperLimit;
	private JTextField textFieldUpperLimit;
	private JButton btnSetUpper;
	private JButton btnClearLower;
	private JButton btnClearUpper;
	private JButton btnGotoStart;
	private JButton btnGotoEnd;
	private JMenuBar menuBar;
	private JMenu mnEdit;
	private JMenuItem mntmBellows;
	private JComboBox comboBoxOpMode;
	private JLabel lblDistance;
	private JTextField textFieldDistance;
	private JPanel panel_3;
	public JButton btnSetZero;
	private JLabel lblRangemm;
	private JTextField textFieldRange;
	private JLabel lblRealdistance;
	private JButton btnStartL;
	private JButton btnStartR;
	private JLabel lblAlert;

	public static final int 
	NOPE=0,
	DETACH=1,
	OUT=2;
	private JMenu mnHelp;
	private JMenuItem mntmAbout;
	private JMenuItem mntmDocumentation;
	private JCheckBoxMenuItem chckbxmntmDocked;
	//private JMenuItem mntmRailPrefs;
	private JButton btnConfig;
	private JPanel panel_4;
	private JComboBox comboBoxRail;
	private JMenuItem mntmPreferences;
	private JButton btnShutter;
	private JPanel panel_5;
	private PopupMenu railSelectPopup;

	/**
	 * Create the frame.
	 */
	public frame() {
		addWindowStateListener(new WindowStateListener() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				if (e.getNewState() == ICONIFIED) {
					if(gb.frameBellows!=null) {
						gb.frameBellows.setVisible(false);
					}
	            }
				if (e.getNewState() == NORMAL) {
					if(gb.frameBellows!=null) {
						gb.frameBellows.setVisible(true);
					}
	            }
	        }
				
		});
		setTitle("Simple Stackshot Controller");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				 try {
					 //gb.setBellowsZero();
					 //setZeroEDT(NOPE);
					 gb.frame=null;
	                 WindowSaver.saveSettings( );
	                 //System.exit(0);
	             } catch (Exception ex) {
	            	 System.out.println("Exception in frame-savesettings1");
	                 System.out.println(ex);
	             }
				if(gb.rh!=null) {
					if(gb.rh!=null){
                    	for(int i=0;i<gb.rh.getSettings().length;i++) {
                    		//System.out.println(gb.rh.getSettings()[i]);
        					gb.railArray.get(gb.lastRailIndex)[i] = gb.rh.getSettings()[i];
                    	}
                		
                	}
					gb.rh.close();
					gb.rh=null;
					gb.propSaveGlobals();
				}
				if(gb.ps!=null) gb.ps.close();
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 467, 707);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mntmBellows = new JMenuItem("Adaptative Bellow Workflow");
		mntmBellows.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
		mntmBellows.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(gb.frameBellows==null){
					try {
						
						gb.frameBellows = new frameBellows();
						gb.frameBellows.setName(gb.SSCBELLOWS);
						gb.frameBellows.setVisible(true);
						if(gb.DOCKED) gb.docker.registerDockee(gb.frameBellows, gb.SSCBELLOWS);//, Docker.EAST_DOCKED);
						//gb.frame.getContentPane().add(gb.docker.getDockToolbar(), BorderLayout.SOUTH);
					} catch (Exception e) {
						System.out.println("Exception in frame-new frame operations");
						e.printStackTrace();
					}
				}
			}
		});
		mnEdit.add(mntmBellows);
		
		chckbxmntmDocked = new JCheckBoxMenuItem("Docked");
		chckbxmntmDocked.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		chckbxmntmDocked.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AbstractButton aButton = (AbstractButton) arg0.getSource();
		        boolean selected = aButton.getModel().isSelected();
		        if (selected) {
		        	gb.DOCKED=true;
		        	if(gb.frameBellows!=null){
						try {
							gb.docker.registerDockee(gb.frameBellows,gb.SSCBELLOWS);//, Docker.EAST_DOCKED);
							//gb.frame.getContentPane().add(gb.docker.getDockToolbar(), BorderLayout.SOUTH);
						} catch (Exception e) {
							System.out.println("Exception in CheckBoxDock");
							e.printStackTrace();
						}
					}
		        } else {
		        	gb.DOCKED=false;
		        	if(gb.frameBellows!=null){
						try {
							gb.docker.deregisterDockee(gb.frameBellows);//, Docker.EAST_DOCKED);
							//gb.frame.getContentPane().add(gb.docker.getDockToolbar(), BorderLayout.SOUTH);
						} catch (Exception e) {
							System.out.println("Exception in CheckBoxUnDock");
							e.printStackTrace();
						}
					}
		        }
			
			}
		});
		mnEdit.add(chckbxmntmDocked);
		if(gb.DOCKED) chckbxmntmDocked.setSelected(true);
		else chckbxmntmDocked.setSelected(false);
		
		mntmPreferences = new JMenuItem("Preferences");
		mntmPreferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		mntmPreferences.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//showPrefs();
				framePrefs framePreferences = new framePrefs();
				
				if(gb.frameBellows!=null) gb.frameBellows.valuesCalculate();
			}
		});
		mnEdit.add(mntmPreferences);
		
		JMenuItem menuItem = mnEdit.add(new AbstractAction("Quit") {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent evt) {
                try {
                	
    				
                    WindowSaver.saveSettings( );
                    //System.exit(0);
                } catch (Exception ex) {
                	System.out.println("Exception in frame-savesettings2");
                    System.out.println(ex);
                }

            }
        });
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				WindowEvent wev = new WindowEvent(gb.frame, WindowEvent.WINDOW_CLOSING);
				Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
			}
		});
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JLabel label = new JLabel();
			    Font font = label.getFont();

			    // create some css from the label's font
			    StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
			    style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
			    style.append("font-size:" + font.getSize() + "pt;");

			    // html content
			    JEditorPane ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">" //
			            + "Version " + gb.version + "<br />"
			            + (gb.telnetPort!=0 ? "Telnet command interface on port "+gb.telnetPort : "No Telnet command interface") + "<br />"
   			    		+ "Copyright 2014 macro-toolbox.com<br />"
			            + "You can get support there : " + "<a href=\"http://macro-toolbox.com/\">http://www.macro-toolbox.com</a>"
			            + "</body></html>");

			    // handle link events
			    ep.addHyperlinkListener(new HyperlinkListener()
			    {
			        @Override
			        public void hyperlinkUpdate(HyperlinkEvent e)
			        {
			            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
							try {
								Desktop.getDesktop().browse(new URI(e.getURL().toString()));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (URISyntaxException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} // roll your own link launcher or use Desktop if J6+
			        }
			    });
			    ep.setEditable(false);
			    ep.setBackground(label.getBackground());

			    // show			 
			    //JOptionPane.showMessageDialog(gb.frame, ep);
				final JOptionPane pane=new JOptionPane(ep);
				final JDialog d=pane.createDialog((JFrame)null, "Dialog");
			    d.setLocation(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
			    d.setVisible(true);
			}
		});
		
		mntmDocumentation = new JMenuItem("Documentation");
		mntmDocumentation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		mntmDocumentation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("http://macro-toolbox.com/index.php?page=documentation"));
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnHelp.add(mntmDocumentation);
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow(3)"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.PARAGRAPH_GAP_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		comboSetRailEDT(DETACH, false);
		
		panel_5 = new JPanel();
		contentPane.add(panel_5, "2, 2, fill, fill");
		panel_5.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("pref:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		
		comboBoxRail = new JComboBox();
		panel_5.add(comboBoxRail, "1, 1, fill, default");
		comboBoxRail.setFocusable(false);
		comboBoxRail.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {
			}
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
				railComboSelect();
/*				String result = (String) comboBoxRail.getSelectedItem();
				// System.out.println(result);
				int index;
			
				if (result == null)
					return;

				index = UtesArrays.findInArrayList(gb.railArray, result);
				if(index==gb.lastRailIndex) return;
				if (index == -1) {
					index = gb.lastRailIndex;
					if (gb.railArray.size() > 0) {
						comboBoxRail.setSelectedIndex(index);
						//lensEditor.setText(gb.LensArray.get(index)[0]);
					} else {
						//lensEditor.setText("");
					}
				} else {
					gb.lastRailIndex = index;
				}
				
		        if (gb.frame!=null){
			       	comboSetRailEDT(DETACH, false);
		        }*/
			}
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					
				}
			}
		});
		comboBoxRail.setModel(new DefaultComboBoxModel(gb.railList()));
		if (!gb.railArray.isEmpty())
			comboBoxRail.setSelectedIndex(gb.lastRailIndex);
		
		
		railSelectPopup = new PopupMenu();
        MenuItem mntmRailSettings = new MenuItem("Rail Settings");
        mntmRailSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gb.rh==null) return;
				gb.rh.showPrefs(gb.icons);
				railComboRefresh();
				/*for(int i=0;i<gb.rh.getSettings().length;i++) {
					gb.railArray.get(gb.lastRailIndex)[i] = gb.rh.getSettings()[i];	
				}
				String name = gb.railArray.get(gb.lastRailIndex)[0];
				UtesArrays.sort(gb.railArray);
				comboBoxRail.removeAllItems();
				String[] list = gb.railList();
				for (int i = 0; i < list.length; i++) {
					comboBoxRail.addItem(list[i]);
				}
				int index = UtesArrays.findInArrayList(gb.railArray, name);
				comboBoxRail.setSelectedIndex(index);
				gb.lastRailIndex = index;*/

			}
		});

        Menu newRailMenu = new Menu("New");
        MenuItem mntmNewStackshot = new MenuItem("Stackshot");
        mntmNewStackshot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				railComboNewStackshot();
				/*String baseName= "Stackshot - New ";
				String newName = baseName+gb.findFirstFreeNewRail(baseName);
				gb.railArray.add(new String[] { newName, gb.RAIL_STACKSHOT, "", "", "", 
						"", "", "", "", "", "" });
				UtesArrays.sort(gb.railArray);
				comboBoxRail.removeAllItems();
				String[] list = gb.railList();
				for (int i = 0; i < list.length; i++) {
					comboBoxRail.addItem(list[i]);
				}
				int index = UtesArrays.findInArrayList(gb.railArray, newName);
				comboBoxRail.setSelectedIndex(index);
				gb.lastRailIndex = index;
				comboSetRailEDT(DETACH, true);*/
			}
		});

        
        MenuItem mntmNewVirtual = new MenuItem("Virtual");
        mntmNewVirtual.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				railComboNewVirtual();
				/*String baseName= "Virtual - New ";
				String newName = baseName+gb.findFirstFreeNewRail(baseName);

				gb.railArray.add(new String[] { newName, gb.RAIL_VIRTUAL, "", "", "", 
						"", "", "", "", "", "" });
				UtesArrays.sort(gb.railArray);
				comboBoxRail.removeAllItems();
				String[] list = gb.railList();
				for (int i = 0; i < list.length; i++) {
					comboBoxRail.addItem(list[i]);
				}
				int index = UtesArrays.findInArrayList(gb.railArray, newName);
				comboBoxRail.setSelectedIndex(index);
				gb.lastRailIndex = index;
				comboSetRailEDT(DETACH, true);*/
			}
		});
        MenuItem mntmDeleteRail = new MenuItem("Delete current");
        mntmDeleteRail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String result = (String) comboBoxRail.getSelectedItem();
				if (result == null)
					return;
				railComboDelete(result);
				/*int index;
				index = UtesArrays.findInArrayList(gb.railArray, result);
				if (index == -1) {
				} else {
					gb.railArray.remove(index);
					comboBoxRail.removeAllItems();
					String[] list = gb.railList();
					for (int i = 0; i < list.length; i++) {
						comboBoxRail.addItem(list[i]);
					}
				}
				gb.lastRailIndex = 0;
				if (gb.railArray.size() > 0) {
					comboBoxRail.setSelectedIndex(0);
				} else {
				}
				comboSetRailEDT(DETACH, false);*/
			}
		});
        // Add components to pop-up menu
        railSelectPopup.add(mntmRailSettings);
        railSelectPopup.addSeparator();
        railSelectPopup.add(newRailMenu);
        newRailMenu.add(mntmNewStackshot);
        newRailMenu.add(mntmNewVirtual);
        railSelectPopup.add(mntmDeleteRail);
		
		panel_4 = new JPanel();
		contentPane.add(panel_4, "4, 2, left, fill");
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));
		btnConfig = new JButton();
		btnConfig.setToolTipText("Rail Config.");
		
		btnConfig.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                	if(gb.rh==null) {
                        gb.frame.getContentPane().add(railSelectPopup);
                        railSelectPopup.show(btnConfig, 0, 0);
                		
                	} else {
                    	if (!cancel(gb.TEST_CANCEL_ON)){
                            gb.frame.getContentPane().add(railSelectPopup);
                            railSelectPopup.show(btnConfig, 0, 0);
        				}
                		
                	}
                }
            }
        });
		/*btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//if (!gb.rh.cancel(gb.TEST_CANCEL_ON)){
				//	gb.rh.showPrefs();
				//}
				//JFrame frame = new JFrame("Popup Menu Example");
			    //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    //frame.setContentPane(new PopupPrefs());
			    //frame.setSize(300, 300);
			    //frame.setVisible(true);
				
		        gb.frame.add(popup);
		        popup.show(gb.frame, 100, 100);
			}
		});*/
		panel_4.add(btnConfig);
		 Image img=Toolkit.getDefaultToolkit().getImage("icons/config.png");
		//btnNewButton.setIcon(new ImageIcon("J:/priv-collection/workspace/StackShot Ctrl/icons/config.png"));
	    btnConfig.setIcon(new ImageIcon(img));
	    
	    btnConfig.setPreferredSize(new Dimension(20,20));
		
		panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Operation Mode", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_3, "2, 4, 3, 1, fill, fill");
		panel_3.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		comboBoxOpMode = new JComboBox();
		comboBoxOpMode.setFocusable(false);
		comboBoxOpMode.setToolTipText("Select Operation Mode.");
		comboBoxOpMode.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {
				//System.out.println(arg0.getSource());
			}
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
//				System.out.println(arg0.getSource());
		        gb.OP_MODE = comboBoxOpMode.getSelectedIndex();
		        if (gb.frame!=null){
		        	gb.frame.statuslog("Op Mode selected : "+comboBoxOpMode.getSelectedItem());
				    //updateLabel(petName);
			       	comboSetEDT(DETACH);
		        }

			}
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					
				}
			}
		});

	/*	comboBoxOpMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        gb.OP_MODE = comboBoxOpMode.getSelectedIndex();
		        if (gb.frame!=null){
		        	gb.frame.statuslog("Op Mode selected : "+comboBoxOpMode.getSelectedItem());
				    //updateLabel(petName);
			       	comboSetEDT(DETACH);
		        }
			}
		});*/
		panel_3.add(comboBoxOpMode, "1, 1, fill, default");
		comboBoxOpMode.setModel(new DefaultComboBoxModel(gb.OP_MODE_STR));
		comboBoxOpMode.setSelectedIndex(gb.OP_MODE);
		
		btnStartL = new JButton("<html>&lt; Start (<a style='text-decoration:underline'>d</a>)</html>");
		btnStartL.setToolTipText("Launch the selected Operation Mode Sequence.");
		btnStartL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(gb.rh==null) return;
				btnStartL_actionPerformed(arg0);
			}
		});
		
		btnStartL.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "btnStartLD");
		btnStartL.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "btnStartLF");

		btnStartL.getActionMap().put("btnStartLD", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(gb.rh==null) return;
				btnStartL_actionPerformed(arg0);
			}
		});

		panel_3.add(btnStartL, "3, 1");
		
		btnStartR = new JButton("<html>Start &gt; (<a style='text-decoration:underline'>g</a>)</html>");
		btnStartR.setToolTipText("Launch the selected Operation Mode Sequence (forwards).");
		btnStartR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(gb.rh==null) return;
				btnStartR_actionPerformed(arg0);
			}
		});
		btnStartR.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0), "btnStartRD");
		btnStartR.getActionMap().put("btnStartRG", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(gb.rh==null) return;
				btnStartR_actionPerformed(arg0);
			}
		});

		panel_3.add(btnStartR, "5, 1");
		
		btnSetZero = new JButton("<html>Set Zer<a style='text-decoration:underline'>o</a></html>");
		btnSetZero.setToolTipText("Set the current Rail Position as the new Zero Position.");
		btnSetZero.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					gb.setBellowsZero();
					setZeroEDT(NOPE);
				}
			}
		});
		btnSetZero.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_O, 0), "btnSetZero");
		btnSetZero.getActionMap().put("btnSetZero", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					gb.setBellowsZero();
					setZeroEDT(NOPE);
				}
			}
		});
		panel_3.add(btnSetZero, "3, 3");
		
		btnShutter = new JButton("<html><a style='text-decoration:underline'>S</a>hutter</html>");
		btnShutter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					//gb.rh.shutterFire(1,gb.PULSE_TIME,0.001);
					try {
						gb.processShoot();
					} catch (InterruptedException e) {
					}
				}
			}
		});
		btnShutter.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "btnShutter");
		btnShutter.getActionMap().put("btnShutter", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					try {
						gb.processShoot();
					} catch (InterruptedException e) {
					}
				}
			}
		});
		
		btnShutter.setToolTipText("Do a shutter test.");
		panel_3.add(btnShutter, "5, 3");
				
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Position (mm)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_1, "2, 6, 3, 1, fill, fill");
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblLabelPosition = new JLabel("Position : ");
		panel_1.add(lblLabelPosition, "1, 1, 3, 1, right, default");
		
		textFieldPosition = new JTextField();
		textFieldPosition.setToolTipText("<html>Here you can type the Position <br>where you want to move the rail.</html>");
		textFieldPosition.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (gb.rh==null) return;
				validateNewPositionEDT(DETACH);
			}
		});
		textFieldPosition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gb.rh==null) return;
				validateNewPositionEDT(DETACH);
			}
		});
		panel_1.add(textFieldPosition, "5, 1, fill, default");
		textFieldPosition.setColumns(10);
		
		btnBck = new JButton("<html>Bck (<a style='text-decoration:underline'>v</a>)</html>");
		btnBck.setToolTipText("<html>Move the rail backward. <br>The rail stops when you release the mouse button.</html>");
		panel_1.add(btnBck, "7, 1");
		btnBck.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					moveToEDTOUT(DETACH, gb.LOWER_LIMIT);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (gb.rh==null) return;
				stopRailEDT(DETACH);
			}
		});
		btnBck.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_V,0, false ), "btnBck pressed");
		btnBck.getActionMap().put("btnBck pressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh==null) return;
				if(btnBackDown) return;
				btnBackDown=true;
				if (!cancel(gb.TEST_CANCEL_ON)){
					moveToEDTOUT(DETACH, gb.LOWER_LIMIT);
				}
			}
		});
		btnBck.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_V,0, true ), "btnBck released");
		btnBck.getActionMap().put("btnBck released", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh==null) return;
				btnBackDown=false;
				stopRailEDT(DETACH);
			}
		});
		
		
		btnFwd = new JButton("<html>Fwd (<a style='text-decoration:underline'>r</a>)</html>");
		btnFwd.setToolTipText("<html>Move the rail forward. <br>The rail stops when you release the mouse button.</html>");
		panel_1.add(btnFwd, "9, 1");
		btnFwd.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					moveToEDTOUT(DETACH, gb.UPPER_LIMIT);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (gb.rh==null) return;
				stopRailEDT(DETACH);
			}
		});
		
		btnFwd.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R,0, false ), "btnFwd pressed");
		btnFwd.getActionMap().put("btnFwd pressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh==null) return;
				if(btnFwdDown) return;
				btnFwdDown=true;
				if (!cancel(gb.TEST_CANCEL_ON)){
					moveToEDTOUT(DETACH, gb.UPPER_LIMIT);
				}
			}
		});
		btnFwd.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R,0, true ), "btnFwd released");
		btnFwd.getActionMap().put("btnFwd released", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh==null) return;
				btnFwdDown=false;
				stopRailEDT(DETACH);
			}
		});
		
		lblStepSize = new JLabel("Step Size : ");
		panel_1.add(lblStepSize, "1, 3, 3, 1, right, default");
		
		
		
		textFieldStepSize = new JTextField();
		textFieldStepSize.setToolTipText("<html>When this value has a meaning <br>for the Stackshot hardware controller, <br>then it has the same meaning here. <br>When it have no meaning (Adaptive Bellows), <br>you can use it as you want.</html>");
		textFieldStepSize.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (gb.rh==null) return;
				valuesValidateEDT(DETACH);
			}
		});
		textFieldStepSize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gb.rh==null) return;
				valuesValidateEDT(DETACH);
			}
		});
		
				panel_1.add(textFieldStepSize, "5, 3, fill, default");
				textFieldStepSize.setColumns(10);
			
				btnBckStep = new JButton("<html>Bck 1 Step (<a style='text-decoration:underline'>b</a>)</html>");
				btnBckStep.setToolTipText("Move the rail of one StepSize backward.");
				btnBckStep.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
							moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()-gb.STEP_SIZE);
						}
					}
				});
				
				btnBckStep.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_B,0), "btnBckStep");
				btnBckStep.getActionMap().put("btnBckStep", new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
							moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()-gb.STEP_SIZE);
						}
					}
				});
				panel_1.add(btnBckStep, "7, 3");
				
				btnFwdStep = new JButton("<html>Fwd 1 Step (<a style='text-decoration:underline'>t</a>)</html>");
				btnFwdStep.setToolTipText("Move the rail of one StepSize forward.");
				btnFwdStep.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
							moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()+gb.STEP_SIZE);
						}
					}
				});
				btnFwdStep.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_T,0), "btnFwdStep");
				btnFwdStep.getActionMap().put("btnFwdStep", new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
							moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()+gb.STEP_SIZE);
						}
					}
				});

				panel_1.add(btnFwdStep, "9, 3");
				
				lblStepNumber = new JLabel("Step Numbers : ");
				panel_1.add(lblStepNumber, "1, 5, 3, 1, right, default");
				
				textFieldStepNumber = new JTextField();
				textFieldStepNumber.setToolTipText("<html>When this value has a meaning <br>for the Stackshot hardware controller, <br>then it has the same meaning here. <br>When it have no meaning (Adaptive Bellows), <br>you can use it as you want.</html>");
				textFieldStepNumber.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
						if (gb.rh==null) return;
						valuesValidateEDT(DETACH);
					}
				});
				textFieldStepNumber.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (gb.rh==null) return;
						valuesValidateEDT(DETACH);
					}
				});
				panel_1.add(textFieldStepNumber, "5, 5, fill, default");
				textFieldStepNumber.setColumns(10);

				
				btnBckXSteps = new JButton("<html>Bck x Steps (<a style='text-decoration:underline'>c</a>)</html>");
				btnBckXSteps.setToolTipText("Move the rail of StepSize x StepNumbers backward.");
				btnBckXSteps.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
							moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()-gb.STEP_NUMBER*gb.STEP_SIZE);
						}
					}
				});
				btnBckXSteps.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C,0), "btnBckXSteps");
				btnBckXSteps.getActionMap().put("btnBckXSteps", new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
							moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()-gb.STEP_NUMBER*gb.STEP_SIZE);
						}
					}
				});
				panel_1.add(btnBckXSteps, "7, 5");
				
				btnFwdXSteps = new JButton("<html>Fwd x Steps (<a style='text-decoration:underline'>e</a>)</html>");
				btnFwdXSteps.setToolTipText("Move the rail of StepSize x StepNumbers forward.");
				btnFwdXSteps.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
							moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()+gb.STEP_NUMBER*gb.STEP_SIZE);
						}
					}
				});
				btnFwdXSteps.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E,0), "btnFwdXSteps");
				btnFwdXSteps.getActionMap().put("btnFwdXSteps", new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
							moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()+gb.STEP_NUMBER*gb.STEP_SIZE);
						}
					}
				});
				panel_1.add(btnFwdXSteps, "9, 5");
				
				lblDistance = new JLabel("Travel Distance : ");
				panel_1.add(lblDistance, "1, 7, 3, 1, right, default");
				
				textFieldDistance = new JTextField();
				textFieldDistance.setToolTipText("<html>When this value has a meaning <br>for the Stackshot hardware controller, <br>then it has the same meaning here. <br>When it have no meaning (Adaptive Bellows), <br>you can use it as you want.</html>");
				textFieldDistance.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (gb.rh==null) return;
						valuesValidateEDT(DETACH);
					}
				});
				textFieldDistance.setEditable(false);
				panel_1.add(textFieldDistance, "5, 7, fill, default");
				textFieldDistance.setColumns(10);
				
				lblRealdistance = new JLabel("RealDistance");
				panel_1.add(lblRealdistance, "7, 7");

		
		panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Range (mm)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_2, "2, 8, 3, 1, fill, fill");
		panel_2.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		lblLowerLimit = new JLabel("Lower Limit : ");
		panel_2.add(lblLowerLimit, "3, 1, center, default");
		
		lblRangeStart = new JLabel("Start : ");
		panel_2.add(lblRangeStart, "5, 1, center, default");
		
		lblRangeEnd = new JLabel("End : ");
		panel_2.add(lblRangeEnd, "7, 1, center, default");
		
		lblUpperLimit = new JLabel("Upper Limit : ");
		panel_2.add(lblUpperLimit, "9, 1, center, default");
		
		btnGotoStart = new JButton("<html>Goto Start (<a style='text-decoration:underline'>Cy</a>)</html>");
		btnGotoStart.setToolTipText("<html>When clicked the rail moves to the <br>start of the defined range.</html>");
		btnGotoStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					moveToEDTOUT(DETACH, gb.RANGE_START);
				}
			}
		});
		btnGotoStart.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y,InputEvent.CTRL_DOWN_MASK), "btnGotoStart");
		btnGotoStart.getActionMap().put("btnGotoStart", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					moveToEDTOUT(DETACH, gb.RANGE_START);
				}
			}
		});
	   
		panel_2.add(btnGotoStart, "5, 3");
		
		btnGotoEnd = new JButton("<html>Goto End (<a style='text-decoration:underline'>Cu</a>)</html>");
		btnGotoEnd.setToolTipText("<html>When clicked the rail moves to the end of <br>the defined range.</html>");
		btnGotoEnd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					moveToEDTOUT(DETACH, gb.RANGE_END);
				}
			}
		});
		btnGotoEnd.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_U,InputEvent.CTRL_DOWN_MASK), "btnGotoEnd");
		btnGotoEnd.getActionMap().put("btnGotoEnd", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					moveToEDTOUT(DETACH, gb.RANGE_END);
				}
			}
		});

		panel_2.add(btnGotoEnd, "7, 3");
		
		textFieldLowerLimit = new JTextField();
		panel_2.add(textFieldLowerLimit, "3, 5");
		textFieldLowerLimit.setEditable(false);
		textFieldLowerLimit.setColumns(10);
		
		textFieldRangeStart = new JTextField();
		textFieldRangeStart.setEditable(false);
		panel_2.add(textFieldRangeStart, "5, 5, fill, default");
		textFieldRangeStart.setColumns(10);
		
		textFieldRangeEnd = new JTextField();
		textFieldRangeEnd.setEditable(false);
		panel_2.add(textFieldRangeEnd, "7, 5, fill, default");
		textFieldRangeEnd.setColumns(10);
		
		textFieldUpperLimit = new JTextField();
		panel_2.add(textFieldUpperLimit, "9, 5");
		textFieldUpperLimit.setEditable(false);
		textFieldUpperLimit.setColumns(10);

		
		btnSetLower = new JButton("<html>Set Lower (<a style='text-decoration:underline'>h</a>)</html>");
		btnSetLower.setToolTipText("<html>When clicked, this button gets the current position <br>and set it as the Lower safety limit.</html>");
		panel_2.add(btnSetLower, "3, 7");
		btnSetLower.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					setLowerLimitEDT(DETACH, gb.rh.getCurrentPosition());
				}
			}
		});
		btnSetLower.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_H,0), "btnSetLower");
		btnSetLower.getActionMap().put("btnSetLower", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					setLowerLimitEDT(DETACH, gb.rh.getCurrentPosition());
				}
			}
		});
		
		btnSetStart = new JButton("<html>Set Start (<a style='text-decoration:underline'>y</a>)</html>");
		btnSetStart.setToolTipText("<html>When clicked, this button gets the current position <br>and sets it as the start of the range. <br>This range has to be define for the ranged and <br>for the Adaptive Bellows sequences.</html>");
		panel_2.add(btnSetStart, "5, 7, fill, default");
		btnSetStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					setRangeStartEDT(DETACH, gb.rh.getCurrentPosition());
					rangeValidateEDT(DETACH);
				}
			}
		});
		btnSetStart.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y,0), "btnSetStart");
		btnSetStart.getActionMap().put("btnSetStart", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					setRangeStartEDT(DETACH, gb.rh.getCurrentPosition());
					rangeValidateEDT(DETACH);
				}
			}
		});

		
		btnSetEnd = new JButton("<html>Set End (<a style='text-decoration:underline'>u</a>)</html>");
		btnSetEnd.setToolTipText("<html>When clicked, this button gets the current position <br>and sets it as the end of the range. <br>This range has to be define for the ranged and <br>for the Adaptive Bellows sequences.</html>");
		panel_2.add(btnSetEnd, "7, 7, fill, default");
		btnSetEnd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					setRangeEndEDT(DETACH, gb.rh.getCurrentPosition());
					rangeValidateEDT(DETACH);
				}
			}
		});
		btnSetEnd.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_U,0), "btnSetEnd");
		btnSetEnd.getActionMap().put("btnSetEnd", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					setRangeEndEDT(DETACH, gb.rh.getCurrentPosition());
					rangeValidateEDT(DETACH);
				}
			}
		});
		
		btnSetUpper = new JButton("<html>Set Upper (<a style='text-decoration:underline'>j</a>)</html>");
		btnSetUpper.setToolTipText("<html>When clicked, this button gets the current position <br>and set it as the Upper safety limit.</html>");
		panel_2.add(btnSetUpper, "9, 7");
		btnSetUpper.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					setUpperLimitEDT(DETACH, gb.rh.getCurrentPosition());
				}

			}
		});
		btnSetUpper.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_J,0), "btnSetUpper");
		btnSetUpper.getActionMap().put("btnSetUpper", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					setUpperLimitEDT(DETACH, gb.rh.getCurrentPosition());
				}
			}
		});
		
		btnClearLower = new JButton("<html>Clear Lower (<a style='text-decoration:underline'>Ch</a>)</html>");
		btnClearLower.setToolTipText("<html>When clicked this button frees the Lower safety limit <br>and sets it to -200mm.</html>");
		panel_2.add(btnClearLower, "3, 9");
		btnClearLower.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					setLowerLimitEDT(DETACH, -200.0);
				}
			}
		});
		btnClearLower.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_H,InputEvent.CTRL_DOWN_MASK ), "btnClearLower");
		btnClearLower.getActionMap().put("btnClearLower", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					setLowerLimitEDT(DETACH, -200.0);
				}
			}
		});

		
		lblRangemm = new JLabel("Range (mm) : ");
		panel_2.add(lblRangemm, "5, 9, right, default");
		
		textFieldRange = new JTextField();
		textFieldRange.setToolTipText("The total range, in term of distance.");
		textFieldRange.setEditable(false);
		panel_2.add(textFieldRange, "7, 9, fill, default");
		textFieldRange.setColumns(10);
		
		btnClearUpper = new JButton("<html>Clear Upper (<a style='text-decoration:underline'>Cj</a>)</html>");
		btnClearUpper.setToolTipText("<html>When clicked this button frees the Upper safety limit <br>and sets it to +200mm.</html>");
		panel_2.add(btnClearUpper, "9, 9");
		btnClearUpper.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					setUpperLimitEDT(DETACH, 200.0);
				}
			}
		});
		btnClearUpper.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_J,InputEvent.CTRL_DOWN_MASK ), "btnClearUpper");
		btnClearUpper.getActionMap().put("btnClearUpper", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
					setUpperLimitEDT(DETACH, 200.0);
				}
			}
		});
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Status Log", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel, "2, 10, 3, 1, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("min:grow"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		scrollPane = new JScrollPane();
		panel.add(scrollPane, "1, 1, fill, fill");
		
		textAreaLog = new JTextArea();
		textAreaLog.setLineWrap(true);
		scrollPane.setViewportView(textAreaLog);
		textAreaLog.setEditable(false);
		
		lblAlert = new JLabel("Click on 'C' or on any button here or on the Stackshot to cancel!!!");
		lblAlert.setForeground(Color.RED);
		contentPane.add(lblAlert, "2, 12, 3, 1, center, default");
		setIconImages(gb.icons);
		frameInitEDT(DETACH);
		
		//this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,InputEvent.CTRL_DOWN_MASK ), "CancelWithSpace");
		//this.getRootPane().getActionMap().put("CancelWithSpace", new AbstractAction() {
		//	public void actionPerformed(ActionEvent arg0) {
		//		if (gb.rh!=null) gb.rh.cancel(gb.TEST_CANCEL_ON);
		//		System.out.println("stroke");
		//	}
		//});
		
		
	}
	
	public void btnStartL_actionPerformed(ActionEvent arg0) {
		if(gb.OP_MODE==gb.MO_AdaptiveBellow) {
			if(Math.abs(gb.RANGE_START-gb.infinitePos)<0.001){
				final JOptionPane pane=new JOptionPane("Can't start from an infinite DOFn.\nPlease change the Range Start Position and retry.");
				final JDialog d=pane.createDialog((JFrame)null, "Dialog");
			    d.setLocation(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
			    d.setVisible(true);
				return;
			}
		}
		if(gb.OP_MODE!=gb.MO_Continuous){
			if (!cancel(gb.PAUSE_ONOFF)){
				doSequenceEDT(-1);
				btnStartL.setText("<html>Pause (<a style='text-decoration:underline'>f</a>)</html>");
				btnStartR.setText("<html>Pause (<a style='text-decoration:underline'>f</a>)</html>");
				btnStartL.getActionMap().put("btnStartLD", null);
				btnStartR.getActionMap().put("btnStartRG", null);
				btnStartL.getActionMap().put("btnStartLF", null);
				btnStartL.getActionMap().put("btnStartLF", new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						btnStartL_actionPerformed(arg0);
					}
				});
			} else {
				if(gb.PAUSE){
					btnStartL.setText("<html>Restart (<a style='text-decoration:underline'>f</a>)</html>");
					btnStartR.setText("<html>Restart (<a style='text-decoration:underline'>f</a>)</html>");
					btnStartL.getActionMap().put("btnStartLD", null);
					btnStartR.getActionMap().put("btnStartRG", null);
					btnStartL.getActionMap().put("btnStartLF", null);
					btnStartL.getActionMap().put("btnStartLF", new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							btnStartL_actionPerformed(arg0);
						}
					});
				}
				else{
					btnStartL.setText("<html>Pause (<a style='text-decoration:underline'>f</a>)</html>");
					btnStartR.setText("<html>Pause (<a style='text-decoration:underline'>f</a>)</html>");
					btnStartL.getActionMap().put("btnStartLD", null);
					btnStartR.getActionMap().put("btnStartRG", null);
					btnStartL.getActionMap().put("btnStartLF", null);
					btnStartL.getActionMap().put("btnStartLF", new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							btnStartL_actionPerformed(arg0);
						}
					});
				}
			}
		} else {
			if (!cancel(gb.TEST_CANCEL_ON)){
				doSequenceEDT(-1);					
			}
		}
	}

	public void btnStartR_actionPerformed(ActionEvent arg0) {
		if(gb.OP_MODE!=gb.MO_Continuous){
			//System.out.println("start no continuous");
			if (!cancel(gb.PAUSE_ONOFF)){
				doSequenceEDT(1);
				btnStartL.setText("<html>Pause (<a style='text-decoration:underline'>f</a>)</html>");
				btnStartR.setText("<html>Pause (<a style='text-decoration:underline'>f</a>)</html>");
				btnStartL.getActionMap().put("btnStartLD", null);
				btnStartR.getActionMap().put("btnStartRG", null);
				btnStartL.getActionMap().put("btnStartLF", null);
				btnStartL.getActionMap().put("btnStartLF", new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						btnStartL_actionPerformed(arg0);
					}
				});
			} else {
				if(gb.PAUSE){
					btnStartL.setText("<html>Restart (<a style='text-decoration:underline'>f</a>)</html>");
					btnStartR.setText("<html>Restart (<a style='text-decoration:underline'>f</a>)</html>");
					btnStartL.getActionMap().put("btnStartLD", null);
					btnStartR.getActionMap().put("btnStartRG", null);
					btnStartL.getActionMap().put("btnStartLF", null);
					btnStartL.getActionMap().put("btnStartLF", new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							btnStartL_actionPerformed(arg0);
						}
					});
				}
				else{
					btnStartL.setText("<html>Pause (<a style='text-decoration:underline'>f</a>)</html>");
					btnStartR.setText("<html>Pause (<a style='text-decoration:underline'>f</a>)</html>");
					btnStartL.getActionMap().put("btnStartLD", null);
					btnStartR.getActionMap().put("btnStartRG", null);
					btnStartL.getActionMap().put("btnStartLF", null);
					btnStartL.getActionMap().put("btnStartLF", new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							btnStartL_actionPerformed(arg0);
						}
					});
				}
			}
		} else {
			if (!cancel(gb.TEST_CANCEL_ON)){
				doSequenceEDT(1);
			}
		}
	}
	
	//TODO
	boolean laststate=false;
	public void stateNotifier(boolean state){
		if(state!=laststate){
			if(!gb.LOCKEDSEQUENCE) {
				lockInterface(state);
			}
			laststate=state;
		}
		showRailPosition();
	}
	
	protected synchronized void showRailPosition() {
		if (gb.frame != null) {
			gb.frame.setPositionOUT(gb.rh.getCurrentPosition());
		}
		if (gb.OP_MODE == gb.MO_AdaptiveBellow && gb.frameBellows!=null)
			gb.frameBellows.setBellowsPositionOUT(gb.rh.getCurrentPosition());
	}
	
	protected synchronized void lockInterface(boolean val) {
		if (gb.frame != null) {
			gb.frame.activateAlertOUT(val ? true : false);
			gb.frame.tempDeactivateFieldsOUT(val ? false : true);
		}
		if (gb.frameBellows!=null)
			gb.frameBellows.tempBellowsDeactivateFieldsOUT(val ? false : true);

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void startTelnetServer(){
		//start telnet serveur and callback
		UtesTelnet.Callback cbfunc = new UtesTelnet.Callback() {
			public void doJob(String helloMsg, Socket clisock, BufferedReader din, BufferedWriter dout) {
				try {
					//BufferedReader din=new BufferedReader(new InputStreamReader(clisock.getInputStream(),"US-ASCII"));
					//BufferedWriter dout=new BufferedWriter(new OutputStreamWriter(clisock.getOutputStream()));

					//InputStreamReader din=new InputStreamReader(cliSock.getInputStream(),"US-ASCII");
					//OutputStreamWriter dout=new OutputStreamWriter(cliSock.getOutputStream());


					char[] buffer= new char[256];
					Thread.sleep(500);
					if(din.ready()) {
						//System.out.println("ready");
						din.read(buffer, 0, 255);
						//System.out.println("got");
					}
					
					String command="";
					dout.write(helloMsg+clisock.getLocalAddress()+"\n\r");
					dout.flush();
					boolean allow=true;
					command="";
					String str="";
					String[]tab=null;
					while(allow) {

						
						
						do {
							if(din.ready()){
								tab=null;
								Arrays.fill(buffer, '\0');
								//System.out.println("buffer:"+UtesArrays.charArrayToHexString(buffer));
								//System.out.println("ready");
								din.read(buffer, 0, 255);
								//System.out.println("got");
								
								//System.out.println("buffer:"+UtesArrays.charArrayToHexString(buffer));
								//System.out.println("buffer:"+UtesStrings.CStringtoString(buffer));
								str += UtesStrings.CStringtoString(buffer);
							}
							//UtesTelnet.broadcastMsg("hello\r\n");
							str = str.replace("\r\n", "\n\r");
							tab = str.split("\r");
							//System.out.println("tab[0]:"+UtesArrays.charArrayToHexString(tab[0].toCharArray()));
							//System.out.println("tab[0]:"+tab[0]);
							if(tab!=null && tab.length>0 && tab[0].endsWith("\n")) {
								command=tab[0];
								//System.out.println("command:"+UtesArrays.charArrayToHexString(command.toCharArray()));
								//System.out.println("command:"+command);
								if(command.endsWith("\n")) {
									command = command.replace("\n", "");
									parseTelnetCommand(clisock, dout, command);
									if(command.equals("quit")) {
										allow=false;
										UtesTelnet.broadcastMsg("bye\r\n");
										Thread.sleep(2000);
									}	
								}
								str="";
								if(tab.length>1){
									for (int i=1;i<tab.length;i++) {
										str+=tab[i]+"\r";	
									}
								}

								
								//System.out.println("command1:"+UtesArrays.charArrayToHexString(command.toCharArray()));
								//System.out.println("command1:"+command);

							}
							//Thread.sleep(500);
						} while (allow && str.length()!=0 && !str.endsWith("\r"));

						
						Thread.sleep(50);
						//System.out.println("not");

					}
					                        
					
					
					UtesTelnet.closeClient(clisock);
					System.out.println("socket closed");
				} 
				catch(Exception ex) {
					ex.printStackTrace();
				}

			}
		};
		//

		int portoffset=0;
		do {
			try {
				UtesTelnet.createServer("MTB StackShot Ctrl "+gb.version, gb.telnetPort+portoffset, cbfunc);
				//serveur créé on enregistre le port.
				gb.telnetPort += portoffset;
				System.out.println("telnet command interface created on port "+gb.telnetPort);
				gb.frame.statuslog("telnet command interface created on port "+gb.telnetPort);
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				portoffset+=1;
			}
			
		} while(portoffset<10);
		
		//si on n'a pas pu créer de serveur
		if(portoffset>=10) {
			System.out.println("Could not create a telnet command interface.");
			gb.frame.statuslog("Could not create a telnet command interface.");
			gb.telnetPort=0;
		}
	}
	
	public void parseTelnetCommand(Socket clisock, BufferedWriter dout, String command) throws Exception{
		class Code implements Runnable {
			Socket clisock;
			String command;
			BufferedWriter dout;
			Code(Socket clisock, BufferedWriter dout, String command) {
				this.clisock=clisock;
				this.command=command;
				this.dout=dout;
			}
			@Override
			public void run(){

				gb.frame.statuslog(clisock.getInetAddress()+":"+clisock.getPort()+" : "+command);
				String[] parts = command.split(" ");
				int nb=parts.length;
				
				if(parts[0].equals("quit")) {
					return;
				}
				try {
					if(parts[0].equals("?")) {
						dout.write(
								"config : setting up parameters. try 'config ?'\n\r"+
								"rail : setting up parameters. try 'config ?'\n\r"+
								"mode   : operation mode control. try 'mode ?'\n\r"+
								"set    : values setting. try 'set ?'\n\r"+
								"move   : rail control. try 'move ?'\n\r"+
								"quit   : close the telnet session\n\r"
								);
						dout.flush();
						return;
					}
	
					
					if(parts[0].equals("config")) {
						if(nb>1 && parts[1].equals("?")) {
							dout.write(
									"config pps x :mode start define how many picture to take at each step\n\r"+
									"config st x  : define time to wait after a move (s)\n\r"+
									"config pt x  : define how long the pulse stay on to trigger the camera (s)\n\r"+
									"config to x  : define time to wait between each picture (s)\n\r"+
									"config ml x  : set mirror lockup mode (1/0)\n\r"+
									"config mlp x : define pulse time to unlock mirror (s)\n\r"+
									"config mlt x : define time to wait after miror lockup (s)\n\r"+
									"config tli x : define time to wait between sequence when in timelapse mode (s)\n\r"+
									"config tln x : define how many sequence to do when in timelapse mode (s)\n\r"+
									"config coc x : define the maximal circle of confusion when in adaptive bellows\n\r" +
									"               mode\n\r"+
									"config do x  : define the percentage of DOF overlap between each move when in\n\r" +
									"               adaptive bellows mode (%)\n\r" +
									"config get   : output the current config values\n\r");
							dout.flush();
							return;
						}
						
						if(nb>1 && parts[1].equals("get")) {
							UtesTelnet.broadcastMsg("PICTURESPERSTEP : "+gb.PULSE_NUMBER);
							UtesTelnet.broadcastMsg("SETTLETIME : "+gb.SETTLE_TIME);
							UtesTelnet.broadcastMsg("PULSETIME : "+gb.PULSE_TIME);
							UtesTelnet.broadcastMsg("TIMEOFF : "+gb.TOFF);
							UtesTelnet.broadcastMsg("MIRRORLOCKUP : "+gb.MIRRORLOCKUP);
							UtesTelnet.broadcastMsg("PULSETIMETOUNLOCK : "+gb.UNLOCK_PULSE);
							UtesTelnet.broadcastMsg("SETTLETIMEAFTERLOCKUP : "+gb.UNLOCK_TIME);
							UtesTelnet.broadcastMsg("TIMELAPSEINTERVAL : "+gb.TIMELAPSE_INTERVAL);
							UtesTelnet.broadcastMsg("TIMELAPSENUMBER : "+gb.TIMELAPSE_NUMBER);
							UtesTelnet.broadcastMsg("MAXCOC : "+gb.MAXCOC);
							UtesTelnet.broadcastMsg("DOFOVERLAP : "+gb.DOF_OVERLAP);
							return;
						}
						
						if(nb>2 && parts[1].equals("pps")) {
							int val=0;
							try {
								val=gb.nf.parse(parts[2]).intValue();
							} catch (ParseException e1) {
								val=gb.PULSE_NUMBER;
							}
							if(val<0){
								val=0;
							}
							gb.PULSE_NUMBER=val;
							UtesTelnet.broadcastMsg("PICTURESPERSTEP : "+gb.PULSE_NUMBER);
							return;
						}

						if(nb>2 && parts[1].equals("st")) {
							double val=0;
							try {
								val=gb.nf.parse(parts[2]).doubleValue();
							} catch (ParseException e1) {
								val=gb.SETTLE_TIME;
							}
							if(val<0){
								val=0;
							}
							gb.SETTLE_TIME=val;
							UtesTelnet.broadcastMsg("SETTLETIME : "+gb.SETTLE_TIME);
							return;
						}
						
						if(nb>2 && parts[1].equals("to")) {
							double val=0;
							try {
								val=gb.nf.parse(parts[2]).doubleValue();
							} catch (ParseException e1) {
								val=gb.TOFF;
							}
							if(val<0){
								val=0;
							}
							gb.TOFF=val;
							UtesTelnet.broadcastMsg("TIMEOFF : "+gb.TOFF);
							return;
						}
						
						if(nb>2 && parts[1].equals("ml")) {
							int val=0;
							try {
								val=gb.nf.parse(parts[2]).intValue();
							} catch (ParseException e1) {
								val=gb.MIRRORLOCKUP?1:0;
							}
							if(val<0 || val>1){
								val=0;
							}
							gb.MIRRORLOCKUP=(val==1)?true:false;
							UtesTelnet.broadcastMsg("MIRRORLOCKUP : "+gb.MIRRORLOCKUP);
							return;
						}

						if(nb>2 && parts[1].equals("mlp")) {
							double val=0;
							try {
								val=gb.nf.parse(parts[2]).doubleValue();
							} catch (ParseException e1) {
								val=gb.UNLOCK_PULSE;
							}
							if(val<0){
								val=0;
							}
							gb.UNLOCK_PULSE=val;
							UtesTelnet.broadcastMsg("PULSETIMETOUNLOCK : "+gb.UNLOCK_PULSE);
							return;
						}
						
						if(nb>2 && parts[1].equals("mlt")) {
							double val=0;
							try {
								val=gb.nf.parse(parts[2]).doubleValue();
							} catch (ParseException e1) {
								val=gb.UNLOCK_TIME;
							}
							if(val<0){
								val=0;
							}
							gb.UNLOCK_TIME=val;
							UtesTelnet.broadcastMsg("SETTLETIMEAFTERLOCKUP : "+gb.UNLOCK_TIME);
							return;
						}
						
						if(nb>2 && parts[1].equals("pt")) {
							double val=0;
							try {
								val=gb.nf.parse(parts[2]).doubleValue();
							} catch (ParseException e1) {
								val=gb.PULSE_TIME;
							}
							if(val<0){
								val=0;
							}
							gb.PULSE_TIME=val;
							UtesTelnet.broadcastMsg("PULSETIME : "+gb.PULSE_TIME);
							return;
						}
						
						if(nb>2 && parts[1].equals("tli")) {
							double val=0;
							try {
								val=gb.nf.parse(parts[2]).doubleValue();
							} catch (ParseException e1) {
								val=gb.TIMELAPSE_INTERVAL;
							}
							if(val<0){
								val=0;
							}
							gb.TIMELAPSE_INTERVAL=val;
							UtesTelnet.broadcastMsg("TIMELAPSEINTERVAL : "+gb.TIMELAPSE_INTERVAL);
							return;
						}
						
						if(nb>2 && parts[1].equals("tln")) {
							int val=0;
							try {
								val=gb.nf.parse(parts[2]).intValue();
							} catch (ParseException e1) {
								val=gb.TIMELAPSE_NUMBER;
							}
							if(val<0){
								val=0;
							}
							gb.TIMELAPSE_NUMBER=val;
							UtesTelnet.broadcastMsg("TIMELAPSENUMBER : "+gb.TIMELAPSE_NUMBER);
							return;
						}
						
						if(nb>2 && parts[1].equals("coc")) {
							double val=0;
							try {
								val=gb.nf.parse(parts[2]).doubleValue();
							} catch (ParseException e1) {
								val=gb.MAXCOC;
							}
							if(val<0){
								val=0;
							}
							gb.MAXCOC=val;
							UtesTelnet.broadcastMsg("MAXCOC : "+gb.MAXCOC);
							return;
						}

						if(nb>2 && parts[1].equals("do")) {
							double val=0;
							try {
								val=gb.nf.parse(parts[2]).doubleValue();
							} catch (ParseException e1) {
								val=gb.DOF_OVERLAP;
							}
							if(val<0){
								val=0;
							}
							gb.DOF_OVERLAP=val;
							UtesTelnet.broadcastMsg("DOFOVERLAP : "+gb.DOF_OVERLAP);
							return;
						}
					}
					
					
					if(parts[0].equals("mode")) {
						if(nb>1 && parts[1].equals("?")) {
							dout.write(
									"mode select autostep/autodist/manualdist/totaldist/diststep/manual/continuous/adaptive\n\r" +
									"             : select operation mode\n\r" +
									"mode start   : start/restart/pause the sequence. When you are using a non\n\r" +
									"               ranged sequence, it's equivalent 'mode start left'\n\r" +
									"mode start left  : start/pause the sequence to the left. When you are using\n\r" +
									"                   a non ranged sequence\n\r" +
									"mode start right : start/pause the sequence to the right. When you are using\n\r" +
									"                   a non ranged sequence\n\r" +
									"mode stop    : stop the sequence\n\r" +
									"mode shutter : do a shutter test\n\r"
									);
									
							dout.flush();
							return;
						}
						if(nb>2 && parts[1].equals("select")) {
							if(parts[2].equals("autostep")) {
								gb.OP_MODE = gb.MO_AutoStep; }
							else if(parts[2].equals("autodist")) {
								gb.OP_MODE = gb.MO_AutoDist; 
							}
							else if(parts[2].equals("manualdist")) {
								gb.OP_MODE = gb.MO_ManualDist; 
							}
							else if(parts[2].equals("totaldist")) {
								gb.OP_MODE = gb.MO_TotalDist; 
							}
							else if(parts[2].equals("diststep")) {
								gb.OP_MODE = gb.MO_DistStep; 
							}
							else if(parts[2].equals("manual")) {
								gb.OP_MODE = gb.MO_Manual; 
							}
							else if(parts[2].equals("continuous")) {
								gb.OP_MODE = gb.MO_Continuous; 
							}
							else if(parts[2].equals("adaptive")) {
								gb.OP_MODE = gb.MO_AdaptiveBellow; 
							}
							else {
								return; 
							}
							
							comboBoxOpMode.setSelectedIndex(gb.OP_MODE);
							if (gb.frame!=null){
								gb.frame.statuslog("Op Mode selected : "+comboBoxOpMode.getSelectedItem());
							    comboSetEDT(DETACH);
							    comboBoxOpMode.revalidate();
							    comboBoxOpMode.repaint();
								return;
							}
							return;
						}
						if(nb>1 && parts[1].equals("start")) {
							if(nb>2 && parts[2].equals("left")) {
								if(gb.rh==null) return;
								btnStartL_actionPerformed(null);
								return;
							}
							if(nb>2 && parts[2].equals("right")) {
								if(gb.rh==null) return;
								if(gb.OP_MODE==gb.MO_TotalDist || gb.OP_MODE==gb.MO_DistStep
										|| gb.OP_MODE==gb.MO_Manual || gb.OP_MODE==gb.MO_Continuous) {
									btnStartR_actionPerformed(null);						
								}
	
								return;
							}
							if(gb.rh==null) return;
							btnStartL_actionPerformed(null);
							return;
						}
						if(nb>1 && parts[1].equals("stop")) {
							if (gb.rh!=null)
								cancel(gb.TEST_CANCEL_ON);
							return;
						}
						
	
						if(nb>1 && parts[1].equals("shutter")) {
							if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
								gb.rh.shutterFire(1,gb.PULSE_TIME,0.001);
							}
							return;
						}
					}
					

	
					
					
					
					if(parts[0].equals("move")) {
						if(nb>1 && parts[1].equals("?")) {
							dout.write(
									"move bck1step    : move the rail one stepsize backward\n\r" +
									"move fwd1step    : move the rail one stepsize forward\n\r" +
									"move bckxsteps   : move the rail stepsizeXstepnumbers backward\n\r" +
									"move fwdxsteps   : move the rail stepsizeXstepnumbers forward\n\r" +
									"move bck steps x : move the rail x stepsize backward\n\r" +
									"move fwd steps x : move the rail x stepsize forward\n\r" + 
									"move bck mm x    : move the rail x mm bacward\n\r" +
									"move fwd mm x    : move the rail x mm forward\n\r"+
									"move to x     : move the rail to the x position (mm)\n\r" +
									"move to start : move the rail to the start position (mm)\n\r" +
									"move to end   : move the rail to the end position (mm)\n\r"
									);
									
							dout.flush();
							return;
						}
						if(nb>1 && parts[1].equals("bck1step")) {
							if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
								moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()-gb.STEP_SIZE);
							}
							return;
						}
						
						
						if(nb>1 && parts[1].equals("fwd1step")) {
							if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
								moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()+gb.STEP_SIZE);
							}
							return;
						}

						if(nb>1 && parts[1].equals("bckxsteps")) {
							if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
								moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()-gb.STEP_NUMBER*gb.STEP_SIZE);
							}
							return;
						}
						
						
						if(nb>1 && parts[1].equals("fwdxsteps")) {
							if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
								moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()+gb.STEP_NUMBER*gb.STEP_SIZE);
							}
							return;
						}
						
						if(nb>3 && parts[1].equals("bck")) {
							if(parts[2].equals("steps")) {
								float val;
								try {
									val = gb.nf.parse(parts[3]).floatValue();
									System.out.println(val);
								} catch (ParseException e1) {
									return;
								}
								if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
									moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()-val*gb.STEP_SIZE);
								}
							}
							if(parts[2].equals("mm")) {
								float val;
								try {
									val = gb.nf.parse(parts[3]).floatValue();
									System.out.println(val);
								} catch (ParseException e1) {
									return;
								}
								if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
									moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()-val);
								}
							}

							return;
						}
						if(nb>3 && parts[1].equals("fwd")) {
							if(parts[2].equals("steps")) {
								float val;
								try {
									val = gb.nf.parse(parts[3]).floatValue();
								} catch (ParseException e1) {
									return;
								}
								if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
									moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()+val*gb.STEP_SIZE);
								}
							}
							if(parts[2].equals("mm")) {
								float val;
								try {
									val = gb.nf.parse(parts[3]).floatValue();
								} catch (ParseException e1) {
									return;
								}
								if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
									moveToEDTOUT(DETACH, gb.rh.getCurrentPosition()+val);
								}
							}

							return;
						}
						
						if(nb>2 && parts[1].equals("to")) {
							if(parts[2].equals("start")) {
								if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
									moveToEDTOUT(DETACH, gb.RANGE_START);
								}
								return;
							}
							if(parts[2].equals("end")) {
								if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
									moveToEDTOUT(DETACH, gb.RANGE_END);
								}
								return;
							}
							if (gb.rh==null) return;
							textFieldPosition.setText(parts[2]);
							validateNewPositionEDT(DETACH);
							return;
						}
					}
					
					
					if(parts[0].equals("set")) {
						if(nb>1 && parts[1].equals("?")) {
							dout.write(
									"set stepsize x   : define the step size (mm)\n\r" +
									"set stepnumber x : define the stepnumber (mm)\n\r" +
									
									"set zero    : set the current position as the new zero position\n\r" +
									"set lower       : define the lower safety limit to the current position\n\r"+
									"set lower clear : sets lower safety limit to -200.0\n\r"+
									"set lower x     : define the lower safety limit (mm)\n\r" +
									"set upper       : define the upper safety limit to the current position\n\r"+
									"set upper clear : sets upper safety limit to -200.0\n\r"+
									"set upper x     : define the upper safety limit (mm)\n\r" +
									
									"set start   : define the start position to the current position\n\r"+
									"set start x : define the start position (mm)\n\r" +
									"set end     : define the end position to the current position\n\r"+
									"set end x   : define the end position (mm)\n\r"
									);
									
							dout.flush();
							return;
						}
						if(nb>2 && parts[1].equals("stepsize")) {
							if (gb.rh==null) return;
							if(gb.OP_MODE==gb.MO_AutoDist || gb.OP_MODE==gb.MO_ManualDist
									|| gb.OP_MODE==gb.MO_DistStep || gb.OP_MODE==gb.MO_Manual
									|| gb.OP_MODE==gb.MO_Continuous || gb.OP_MODE==gb.MO_AdaptiveBellow ) {
								textFieldStepSize.setText(parts[2]);
								valuesValidateEDT(DETACH);
							}
							
							return;
						}
						if(nb>2 && parts[1].equals("stepnumber")) {
							if (gb.rh==null) return;
							if(gb.OP_MODE==gb.MO_AutoStep || gb.OP_MODE==gb.MO_TotalDist
									|| gb.OP_MODE==gb.MO_DistStep || gb.OP_MODE==gb.MO_AdaptiveBellow ) {
								textFieldStepNumber.setText(parts[2]);
								valuesValidateEDT(DETACH);
							}
							return;
						}
						if(nb==2 && parts[1].equals("zero")) {
							if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
								gb.setBellowsZero();
								setZeroEDT(NOPE);
							}
							return;
						}
						if(nb>2 && parts[1].equals("lower")) {
							if(parts[2].equals("clear")) {
								if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
									setLowerLimitEDT(DETACH, -200.0);
								}
								return;
							}
							float val;
							try {
								val = gb.nf.parse(parts[2]).floatValue();
							} catch (ParseException e1) {
								return;
							}
							if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
								setLowerLimitEDT(DETACH, val);
							}
							return;
						} else if(nb==2 && parts[1].equals("lower")){
							if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
								setLowerLimitEDT(DETACH, gb.rh.getCurrentPosition());
							}
							return;
						}
						if(nb>2 && parts[1].equals("upper")) {
							if(parts[2].equals("clear")) {
								if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
									setUpperLimitEDT(DETACH, 200.0);
								}
								return;
							}

							float val;
							try {
								val = gb.nf.parse(parts[2]).floatValue();
							} catch (ParseException e1) {
								return;
							}
							if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
								setUpperLimitEDT(DETACH, val);
							}
							return;
						} else if(nb==2 && parts[1].equals("upper")){
							if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
								setUpperLimitEDT(DETACH, gb.rh.getCurrentPosition());
							}
							return;
						}
						if(nb>2 && parts[1].equals("start")) {
							float val;
							try {
								val = gb.nf.parse(parts[2]).floatValue();
							} catch (ParseException e1) {
								return;
							}
							if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
								setRangeStartEDT(DETACH, val);
								rangeValidateEDT(DETACH);
							}
							return;
						} else if(nb==2 && parts[1].equals("start")){
							if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
								setRangeStartEDT(DETACH, gb.rh.getCurrentPosition());
								rangeValidateEDT(DETACH);
							}
							return;
						}
						if(nb>2 && parts[1].equals("end")) {
							float val;
							try {
								val = gb.nf.parse(parts[2]).floatValue();
							} catch (ParseException e1) {
								return;
							}
							if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
								setRangeEndEDT(DETACH, val);
								rangeValidateEDT(DETACH);
							}
							return;
						} else if(nb==2 && parts[1].equals("end")){
							if (gb.rh!=null && !cancel(gb.TEST_CANCEL_ON)){
								setRangeEndEDT(DETACH, gb.rh.getCurrentPosition());
								rangeValidateEDT(DETACH);
							}
							return;
						}


						
						
					}

					
					if(parts[0].equals("rail")) {
						if(nb>1 && parts[1].equals("?")) {
							dout.write(
									"rail list     : list the different rail configs\n\r" +
									"rail select x : select rail config number x\n\r"+
									"rail new stackshot : create a new stackshot rail\n\r"+
									"rail new virtual   : create a new virtual rail\n\r"+
									"rail delete x      : delete rail named x\n\r"+
									"rail name x     : set the current rail name to x\n\r"+
									"rail speed x    : set rail speed to x (mm/s)\n\r"+
									"rail ramptime x : set rail ramp time to x (s)\n\r"+
									"rail torque x   : set rail motor torque to x\n\r"+
									"rail backlash x : set rail backlash to x (mm)\n\r"+
									"rail distperrev x  : set rail dist/rev to x (mm)\n\r"+
									"rail stepsperrev x : set rail steps/rev to x (step)\n\r"+
									"rail hiprecision x : set rail high precision to x (on/off)\n\r"+
									"rail lcd x         : set rail lcd backlight to x (on/off)\n\r"+
									"rail config : get rail config\n\r"
									);
									
							dout.flush();
							return;
						}
						
						if(nb>1 && parts[1].equals("list")) {
							String[] list = gb.railList();
							for (int i = 0; i < list.length; i++) {
								if(i==gb.lastRailIndex) {
									dout.write(i+"* : "+list[i]+"\n\r");
								} else {
									dout.write(i+"  : "+list[i]+"\n\r");	
								}
								
							}
							dout.flush();
							return;
						}
						
						if(nb>1 && parts[1].equals("config")) {
							UtesTelnet.broadcastMsg("RAILNAME : "+gb.rh.getRAILNAME()+"\n\r");
							UtesTelnet.broadcastMsg("RAILTYPE : "+gb.rh.getRAILTYPE()+"\n\r");
							UtesTelnet.broadcastMsg("RAILSPEED : "+gb.rh.getMOTORSPEED()+"\n\r");
							UtesTelnet.broadcastMsg("RAILRAMPTIME : "+gb.rh.getTRAMP()+"\n\r");
							UtesTelnet.broadcastMsg("RAILTORQUE : "+gb.rh.getTORQUE()+"\n\r");
							UtesTelnet.broadcastMsg("RAILBACKLASH : "+gb.rh.getBACKLASH()+"\n\r");
							UtesTelnet.broadcastMsg("RAILDISTPERREV : "+gb.rh.getMMPERREV()+"\n\r");
							UtesTelnet.broadcastMsg("RAILSTEPSPERREV : "+gb.rh.getSTEPSPERREV()+"\n\r");
							UtesTelnet.broadcastMsg("RAILHIPRECISION : "+gb.rh.getHIPRECISION()+"\n\r");
							UtesTelnet.broadcastMsg("RAILLCD : "+gb.rh.getLCD()+"\n\r");
							
							return;
						}
						
						if(nb>2 && parts[1].equals("new")) {
							if (parts[2].equals("stackshot")) {
								railComboNewStackshot();
							}
							
							return;
						}
						
						if(nb>2 && parts[1].equals("new")) {
							if (parts[2].equals("virtual")) {
								railComboNewVirtual();
							}
							
							return;
						}

						
						if(nb>2 && parts[1].equals("delete")) {
							String str=parts[2];
							for(int i=3;i<parts.length;i++) {
								str=str+" "+parts[i];
							}
							UtesTelnet.broadcastMsg("RAIL à effacer : "+str+"\n\r");
							railComboDelete(str);
							return;
						}

						
						if(nb>2 && parts[1].equals("select")) {
							int val;
							try {
								val = gb.nf.parse(parts[2]).intValue();
							} catch (ParseException e1) {
								return;
							}
							if(val<0) return;
							if(val>=gb.railList().length) return;
							
							comboBoxRail.setSelectedIndex(val);
							railComboSelect();
							return;
						}
						
						if(nb>2 && parts[1].equals("name")) {
							if(gb.rh==null) return;
							gb.rh.setRAILNAME(parts[2]);
							railComboRefresh();
							return;
						}
						
						if(nb>2 && parts[1].equals("speed")) {
							if(gb.rh==null) return;
							gb.rh.setMOTORSPEED(parts[2]);
							railComboRefresh();
							return;
							
						}
						
						if(nb>2 && parts[1].equals("ramptime")) {
							if(gb.rh==null) return;
							gb.rh.setTRAMP(parts[2]);
							railComboRefresh();
							return;
						}
						if(nb>2 && parts[1].equals("torque")) {
							if(gb.rh==null) return;
							gb.rh.setTORQUE(parts[2]);
							railComboRefresh();
							return;
						}
						if(nb>2 && parts[1].equals("backlash")) {
							if(gb.rh==null) return;
							gb.rh.setBACKLASH(parts[2]);
							railComboRefresh();
							return;
						}
						if(nb>2 && parts[1].equals("distperrev")) {
							if(gb.rh==null) return;
							gb.rh.setMMPERREV(parts[2]);
							railComboRefresh();
							return;
						}
						if(nb>2 && parts[1].equals("stepsperrev")) {
							if(gb.rh==null) return;
							gb.rh.setSTEPSPERREV(parts[2]);
							railComboRefresh();
							return;
						}
						if(nb>2 && parts[1].equals("hiprecision")) {
							if(gb.rh==null) return;
							gb.rh.setHIPRECISION(parts[2]);
							railComboRefresh();
							return;
						}
						if(nb>2 && parts[1].equals("lcd")) {
							if(gb.rh==null) return;
							gb.rh.setLCD(parts[2]);
							railComboRefresh();
							return;
						}



					}
					
	
					dout.write("unknow command\n\r");
					dout.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return;

				
			}
		}
		
		
		Code code = new Code(clisock, dout, command);
		SwingUtilities.invokeLater(code);			
		
	}
	
	
	
	
	public void frameInitEDT(int detach){
		class Code implements Runnable {
			@Override
			public void run(){
				valuesInit();
				activateFields();
				setDistances();
				startTelnetServer();

				
			}
		}
		
		
		Code code = new Code();
		if(detach==DETACH){
			SwingUtilities.invokeLater(code);			
		} else {
			code.run();
		}
		return;
	}

	public boolean moveToEDTOUT(int detach, double val){
		class Code implements Runnable {
			private double offset1;
			public Code(double off){
				this.offset1=off;
			}
			
			@Override
			public void run(){
				if(gb.rh.moveOf(this.offset1)==RailBase.RESP_NOK) {
					gb.rh.setMoving(false);
				}
				return;
			}
		}
			
		boolean ret=true;
		//TODO controler si bonne position
		//System.out.println(val);
		if((gb.frameBellows!=null) && (val==gb.rh.getCurrentPosition())) {
			gb.frameBellows.tempDeactivateHyperfocusOUT();
			return true;
		}
		double pos=gb.rh.getCurrentPosition();
		if(val==pos) {
			return true;
		}
		double offset=0;
		
		if(pos>val){
			if (val<gb.LOWER_LIMIT){ 
				val=gb.LOWER_LIMIT;
				ret=false;
			}
			offset=val-pos;
			//System.out.println(offset);
		}
		if(pos<val){
			if(gb.OP_MODE==gb.MO_AdaptiveBellow){
				if (val>Math.min(gb.UPPER_LIMIT, gb.infinitePos)){
					val=Math.min(gb.UPPER_LIMIT, gb.infinitePos);
					ret=false;
				}
			}else{
				if (val>gb.UPPER_LIMIT){
					val=gb.UPPER_LIMIT;
					ret=false;
				}
			}
			offset=val-pos;
		}
		//TODO
		//System.out.println(offset);
		gb.rh.setMoving(true);
		UtesTelnet.broadcastMsg("MOVING\r\n");
		Code code = new Code(offset);		
		if (SwingUtilities.isEventDispatchThread()) {
			if(detach==DETACH){
				SwingUtilities.invokeLater(code);
			} else {
				code.run();	
			}
		} else {
			try {
				SwingUtilities.invokeAndWait(code);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (InvocationTargetException e) {
				//e.printStackTrace();
			}
		}
		return ret;
	}
	
	public void stopRailEDT(int detach) {
		class Code implements Runnable {
			@Override
			public void run(){
				//System.out.println("STOP");
				gb.rh.stopAll();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				gb.rh.stopAll();
			}
		}

		Code code = new Code();
		if (detach==DETACH){
			SwingUtilities.invokeLater(code);			
		} else {
			code.run();
		}
		return;
	}
	
	
	
	
	
	public void railComboSelect() {
		String result = (String) comboBoxRail.getSelectedItem();
		// System.out.println(result);
		int index;
	
		if (result == null)
			return;

		index = UtesArrays.findInArrayList(gb.railArray, result);
		if(index==gb.lastRailIndex) return;
		if (index == -1) {
			index = gb.lastRailIndex;
			if (gb.railArray.size() > 0) {
				comboBoxRail.setSelectedIndex(index);
				//lensEditor.setText(gb.LensArray.get(index)[0]);
			} else {
				//lensEditor.setText("");
			}
		} else {
			gb.lastRailIndex = index;
		}
		
        if (gb.frame!=null){
	       	comboSetRailEDT(DETACH, false);
        }
	}
	
	
	public void railComboRefresh() {
		for(int i=0;i<gb.rh.getSettings().length;i++) {
			gb.railArray.get(gb.lastRailIndex)[i] = gb.rh.getSettings()[i];	
		}
		String name = gb.railArray.get(gb.lastRailIndex)[0];
		UtesArrays.sort(gb.railArray);
		comboBoxRail.removeAllItems();
		String[] list = gb.railList();
		for (int i = 0; i < list.length; i++) {
			comboBoxRail.addItem(list[i]);
		}
		int index = UtesArrays.findInArrayList(gb.railArray, name);
		comboBoxRail.setSelectedIndex(index);
		gb.lastRailIndex = index;
		UtesTelnet.broadcastMsg("RAILNAME : "+gb.rh.getRAILNAME()+"\n\r");
		UtesTelnet.broadcastMsg("RAILSPEED : "+gb.rh.getMOTORSPEED()+"\n\r");
		UtesTelnet.broadcastMsg("RAILRAMPTIME : "+gb.rh.getTRAMP()+"\n\r");
		UtesTelnet.broadcastMsg("RAILTORQUE : "+gb.rh.getTORQUE()+"\n\r");
		UtesTelnet.broadcastMsg("RAILBACKLASH : "+gb.rh.getBACKLASH()+"\n\r");
		UtesTelnet.broadcastMsg("RAILDISTPERREV : "+gb.rh.getMMPERREV()+"\n\r");
		UtesTelnet.broadcastMsg("RAILSTEPSPERREV : "+gb.rh.getSTEPSPERREV()+"\n\r");
		UtesTelnet.broadcastMsg("RAILHIPRECISION : "+gb.rh.getHIPRECISION()+"\n\r");
		UtesTelnet.broadcastMsg("RAILLCD : "+gb.rh.getLCD()+"\n\r");
	}
	
	public void railComboNewStackshot() {
		String baseName= "Stackshot - New ";
		String newName = baseName+gb.findFirstFreeNewRail(baseName);
		gb.railArray.add(new String[] { newName, gb.RAIL_STACKSHOT, "", "", "", 
				"", "", "", "", "", "" });
		UtesArrays.sort(gb.railArray);
		comboBoxRail.removeAllItems();
		String[] list = gb.railList();
		for (int i = 0; i < list.length; i++) {
			comboBoxRail.addItem(list[i]);
		}
		int index = UtesArrays.findInArrayList(gb.railArray, newName);
		comboBoxRail.setSelectedIndex(index);
		gb.lastRailIndex = index;
		UtesTelnet.broadcastMsg("NEWRAIL : "+newName+"\n\r");
		comboSetRailEDT(DETACH, true);
	}
	
	public void railComboNewVirtual() {
		String baseName= "Virtual - New ";
		String newName = baseName+gb.findFirstFreeNewRail(baseName);

		gb.railArray.add(new String[] { newName, gb.RAIL_VIRTUAL, "", "", "", 
				"", "", "", "", "", "" });
		UtesArrays.sort(gb.railArray);
		comboBoxRail.removeAllItems();
		String[] list = gb.railList();
		for (int i = 0; i < list.length; i++) {
			comboBoxRail.addItem(list[i]);
		}
		int index = UtesArrays.findInArrayList(gb.railArray, newName);
		comboBoxRail.setSelectedIndex(index);
		gb.lastRailIndex = index;
		UtesTelnet.broadcastMsg("NEWRAIL : "+newName+"\n\r");
		comboSetRailEDT(DETACH, true);
	}
	
	public void railComboDelete(String str){
		int index;
		index = UtesArrays.findInArrayList(gb.railArray, str);
		if (index == -1) {
		} else {
			gb.railArray.remove(index);
			comboBoxRail.removeAllItems();
			String[] list = gb.railList();
			for (int i = 0; i < list.length; i++) {
				comboBoxRail.addItem(list[i]);
			}
		}
		gb.lastRailIndex = 0;
		if (gb.railArray.size() > 0) {
			comboBoxRail.setSelectedIndex(0);
		} else {
		}
		UtesTelnet.broadcastMsg("RAILDELETED : "+str+"\n\r");
		comboSetRailEDT(DETACH, false);
		
	}

	
	public void comboSetEDT(int detach){
		class Code implements Runnable {
			@Override
			public void run(){
				valuesValidateEDT(NOPE);
				activateFields();
				if(gb.frameBellows!=null) gb.frameBellows.activateBellowsFieldsOUT();
				setDistances();
				setUpperLimitEDT(DETACH, gb.UPPER_LIMIT);
				//if(gb.frameBellows!=null) gb.frameBellows.setUpperLimitColorOUT();
				if (gb.frameBellows!=null) {
					gb.frameBellows.valuesCalculate();
					if(gb.rh!=null) gb.frameBellows.setBellowsPositionOUT(gb.rh.getCurrentPosition());
					gb.frame.setUpperLimitEDT(NOPE, gb.UPPER_LIMIT);
					gb.frameBellows.setUpperLimitColorOUT();
				}
				UtesTelnet.broadcastMsg("OPMODE : "+gb.OP_MODE_STR[gb.OP_MODE]+"\n\r");
			}
		}

		Code code = new Code();
		if(detach==DETACH) {
			SwingUtilities.invokeLater(code);
		} else {
			code.run();
		}
		return;
	}
	
	public void comboSetRailEDT(int detach, boolean newRail){
		class Code implements Runnable {
			private boolean newRail;
			public Code(boolean newRail){
				this.newRail=newRail;
			}
			@Override
			public void run(){
				//if(gb.rail!=sel){
				activateAll(false);
				String result = (String) comboBoxRail.getSelectedItem();
				gb.frame.statuslog("Rail selected : "+result);
				UtesTelnet.broadcastMsg("RAIL : "+result+"\n\r");
				int index;
				index = UtesArrays.findInArrayList(gb.railArray, result);
				//System.out.println(index);
								
				if(gb.rh!=null) {
					gb.rh.close();
					gb.rh=null;
				}
				activateAll(false);
				if(index!=-1) {
					if(gb.railArray.get(index)[1].equals(gb.RAIL_VIRTUAL)){
						System.out.println("VIRTUAL selected");
						gb.rh = new RailVirtual(gb.nf);
					} else {
						gb.rh = new RailStackshot(gb.nf);
						System.out.println("STACKSHOT selected");
					}
					//si new rail alors on efface le type de rail afin de permettre au 
					//helper de savoir qu'il faut mettre les parametres par defaut 
					if(newRail) gb.railArray.get(index)[1]="";
					String [] settings = gb.rh.open(gb.railArray.get(index));
					if(settings==null) {
						gb.rh=null;
						gb.frame.statuslog(gb.railArray.get(index)[1].equals(gb.RAIL_STACKSHOT)
								?"StackShot rail not opened, please, select Virtual rail"
								:"Virtual rail not opened, please, select StackShot rail");
					} else {
						//TODO
						//inutile???
						for(int i=0;i<settings.length;i++) {
							gb.railArray.get(index)[i] = settings[i];	
						}
						
					}
				}
				valuesInit();
				if(gb.rh!=null){
					gb.rh.addStateNotifier(new StateNotifier() {
						@Override
						public void stateNotifier(boolean state){
							//System.out.println(state);
							if(state!=laststate){
								
								if(!gb.LOCKEDSEQUENCE) {
									lockInterface(state);
								}
								laststate=state;
							}
							showRailPosition();
						}
					}
							);
					setPositionOUT(gb.rh.getCurrentPosition());
					gb.setBellowsPosition(gb.rh.getCurrentPosition());
					activateAll(true);
					valuesValidateEDT(NOPE);
					activateFields();
					setDistances();
					setUpperLimitEDT(DETACH, gb.UPPER_LIMIT);
					if(gb.frameBellows!=null) gb.frameBellows.setUpperLimitColorOUT();
				}

					
			}
		}

		Code code = new Code(newRail);

		if(detach==DETACH) {
			SwingUtilities.invokeLater(code);
		} else {
			code.run();
		}
		return;
	}
	
	public void activateAll(boolean flag){
		if(gb.rh==null) btnConfig.setEnabled(true);
		else  btnConfig.setEnabled(flag);
		textFieldStepSize.setEditable(flag);
		textFieldStepNumber.setEditable(flag);
		textFieldDistance.setEditable(flag);

		comboBoxOpMode.setEnabled(flag);
		btnStartL.setEnabled(flag);
		btnStartR.setEnabled(flag);
		btnSetZero.setEnabled(flag);
		btnShutter.setEnabled(flag);
		btnBck.setEnabled(flag);
		btnFwd.setEnabled(flag);
		btnBckStep.setEnabled(flag);
		btnFwdStep.setEnabled(flag);
		btnBckXSteps.setEnabled(flag);
		btnFwdXSteps.setEnabled(flag);
		btnSetLower.setEnabled(flag);
		btnSetUpper.setEnabled(flag);
		btnClearLower.setEnabled(flag);
		btnClearUpper.setEnabled(flag);
		btnSetStart.setEnabled(flag);
		btnSetEnd.setEnabled(flag);
		btnGotoStart.setEnabled(flag);
		btnGotoEnd.setEnabled(flag);
		if(gb.frameBellows!=null) gb.frameBellows.tempBellowsDeactivateFieldsOUT(flag);
	}
	
	public void rangeValidateEDT(int detach){
		class Code implements Runnable {
			@Override
			public void run(){
				//setDistances();
				valuesValidateEDT(NOPE);
			}
		}

		Code code = new Code();
		if(detach==DETACH) {
			SwingUtilities.invokeLater(code);
		} else {
			code.run();
		}
		return;
	}
	
	//validation des saisies
	public void valuesInit(){
		if(gb.rh!=null) {
			double pos = gb.rh.getCurrentPosition();
			//System.out.println(pos);
			textFieldPosition.setText(gb.nf.format(gb.rh.getCurrentPosition()));
			textFieldStepNumber.setText(gb.nf.format(gb.STEP_NUMBER));
			textFieldStepSize.setText(gb.nf.format(gb.STEP_SIZE));
			textFieldDistance.setText(gb.nf.format(gb.STEP_NUMBER*gb.STEP_SIZE));

			textFieldLowerLimit.setText(gb.nf.format(gb.LOWER_LIMIT));
			textFieldRangeStart.setText(gb.nf.format(gb.RANGE_START));
			textFieldRangeEnd.setText(gb.nf.format(gb.RANGE_END));
			textFieldUpperLimit.setText(gb.nf.format(gb.UPPER_LIMIT));	
		} else {
			textFieldPosition.setText(gb.nf.format(Double.NaN));
			textFieldStepNumber.setText(gb.nf.format(Double.NaN));
			textFieldStepSize.setText(gb.nf.format(Double.NaN));
			textFieldDistance.setText(gb.nf.format(Double.NaN));

			textFieldLowerLimit.setText(gb.nf.format(Double.NaN));
			textFieldRangeStart.setText(gb.nf.format(Double.NaN));
			textFieldRangeEnd.setText(gb.nf.format(Double.NaN));
			textFieldUpperLimit.setText(gb.nf.format(Double.NaN));	

		}
		
		lblAlert.setVisible(false);
	}
	
	public void valuesValidateEDT(int detach){
		class Code implements Runnable {
			@Override
			public void run(){
				if(gb.OP_MODE==gb.MO_AutoStep) {
					autoStepValidate();
				}
				if(gb.OP_MODE==gb.MO_AutoDist) {
					autoDistValidate();
				}
				if(gb.OP_MODE==gb.MO_ManualDist) {
					manualDistValidate();
				}
				if(gb.OP_MODE==gb.MO_TotalDist) {
					totalDistValidate();
				}
				if(gb.OP_MODE==gb.MO_DistStep) {
					distStepValidate();
				}
				if(gb.OP_MODE==gb.MO_Manual) {
					manualValidate();
				}
				if(gb.OP_MODE==gb.MO_Continuous) {
					continuousValidate();
				}
				if(gb.OP_MODE==gb.MO_AdaptiveBellow) {
					bellowsValidate();
				}
			}
		}
		Code code = new Code();
		if(detach==DETACH){
			SwingUtilities.invokeLater(code);
		} else {
			code.run();
		}
		return;
	}
	
	public void setStartTextOUT(String str){
		class Code implements Runnable {
			private String str;
			public Code(String str){
				this.str=str;
			}
			@Override
			public void run(){
				btnStartL.setText(this.str);
				btnStartR.setText(this.str);
			}
		}
		Code code = new Code(str);
		SwingUtilities.invokeLater(code);
		return;
	}
	
	public void activateFields(){
		if(gb.OP_MODE==gb.MO_AutoStep) {
			textFieldDistance.setEditable(false);
			textFieldStepSize.setEditable(false);
			textFieldStepNumber.setEditable(true);
			btnStartL.setText("<html>Start (<a style='text-decoration:underline'>f</a>)</html>");
			btnStartL.getActionMap().put("btnStartLD", null);
			btnStartL.getActionMap().put("btnStartLF", null);
			btnStartR.getActionMap().put("btnStartRG", null);
			btnStartL.getActionMap().put("btnStartLF", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (gb.rh==null) return;
					btnStartL_actionPerformed(arg0);
				}
			});
			btnStartR.setVisible(false);
		}
		if(gb.OP_MODE==gb.MO_AutoDist) {
			textFieldDistance.setEditable(false);
			textFieldStepSize.setEditable(true);
			textFieldStepNumber.setEditable(false);
			btnStartL.setText("<html>Start (<a style='text-decoration:underline'>f</a>)</html>");
			btnStartL.getActionMap().put("btnStartLD", null);
			btnStartL.getActionMap().put("btnStartLF", null);
			btnStartR.getActionMap().put("btnStartRG", null);
			btnStartL.getActionMap().put("btnStartLF", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (gb.rh==null) return;
					btnStartL_actionPerformed(arg0);
				}
			});

			btnStartR.setVisible(false);
		}
		if(gb.OP_MODE==gb.MO_ManualDist) {
			textFieldDistance.setEditable(false);
			textFieldStepSize.setEditable(true);
			textFieldStepNumber.setEditable(false);
			//textFieldStepNumber.setText("N/A");
			btnStartL.setText("<html>Start (<a style='text-decoration:underline'>f</a>)</html>");
			btnStartL.getActionMap().put("btnStartLD", null);
			btnStartL.getActionMap().put("btnStartLF", null);
			btnStartR.getActionMap().put("btnStartRG", null);
			btnStartL.getActionMap().put("btnStartLF", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (gb.rh==null) return;
					btnStartL_actionPerformed(arg0);
				}
			});

			btnStartR.setVisible(false);
		}
		if(gb.OP_MODE==gb.MO_TotalDist) {
			textFieldDistance.setEditable(true);
			textFieldStepSize.setEditable(false);
			textFieldStepNumber.setEditable(true);
			btnStartL.setText("<html>< Start (<a style='text-decoration:underline'>d</a>)</html>");
			btnStartL.getActionMap().put("btnStartLD", null);
			btnStartL.getActionMap().put("btnStartLF", null);
			btnStartR.getActionMap().put("btnStartRG", null);
			btnStartL.getActionMap().put("btnStartLD", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (gb.rh==null) return;
					btnStartL_actionPerformed(arg0);
				}
			});


			btnStartR.setVisible(true);
			btnStartR.setText("<html>Start > (<a style='text-decoration:underline'>g</a>)</html>");
			btnStartR.getActionMap().put("btnStartRG", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (gb.rh==null) return;
					btnStartR_actionPerformed(arg0);
				}
			});

		}
		if(gb.OP_MODE==gb.MO_DistStep) {
			textFieldDistance.setEditable(false);
			textFieldStepSize.setEditable(true);
			textFieldStepNumber.setEditable(true);
			btnStartL.setText("<html>< Start (<a style='text-decoration:underline'>d</a>)</html>");
			btnStartL.getActionMap().put("btnStartLD", null);
			btnStartL.getActionMap().put("btnStartLF", null);
			btnStartR.getActionMap().put("btnStartRG", null);
			btnStartL.getActionMap().put("btnStartLD", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (gb.rh==null) return;
					btnStartL_actionPerformed(arg0);
				}
			});


			btnStartR.setVisible(true);
			btnStartR.setText("<html>Start > (<a style='text-decoration:underline'>g</a>)</html>");
			btnStartR.getActionMap().put("btnStartRG", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (gb.rh==null) return;
					btnStartR_actionPerformed(arg0);
				}
			});

		}
		if(gb.OP_MODE==gb.MO_Manual) {
			textFieldDistance.setEditable(false);
			textFieldStepSize.setEditable(true);
			textFieldStepNumber.setEditable(false);
			btnStartL.setText("<html>< Start (<a style='text-decoration:underline'>d</a>)</html>");
			btnStartL.getActionMap().put("btnStartLD", null);
			btnStartL.getActionMap().put("btnStartLF", null);
			btnStartR.getActionMap().put("btnStartRG", null);
			btnStartL.getActionMap().put("btnStartLD", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (gb.rh==null) return;
					btnStartL_actionPerformed(arg0);
				}
			});


			btnStartR.setVisible(true);
			btnStartR.setText("<html>Start > (<a style='text-decoration:underline'>g</a>)</html>");
			btnStartR.getActionMap().put("btnStartRG", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (gb.rh==null) return;
					btnStartR_actionPerformed(arg0);
				}
			});

		}
		if(gb.OP_MODE==gb.MO_Continuous) {
			textFieldDistance.setEditable(true);
			textFieldStepSize.setEditable(false);
			textFieldStepNumber.setEditable(false);
			btnStartL.setText("<html>< Start (<a style='text-decoration:underline'>d</a>)</html>");
			btnStartL.getActionMap().put("btnStartLD", null);
			btnStartL.getActionMap().put("btnStartLF", null);
			btnStartR.getActionMap().put("btnStartRG", null);
			btnStartL.getActionMap().put("btnStartLD", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (gb.rh==null) return;
					btnStartL_actionPerformed(arg0);
				}
			});


			btnStartR.setVisible(true);
			btnStartR.setText("<html>Start > (<a style='text-decoration:underline'>g</a>)</html>");
			btnStartR.getActionMap().put("btnStartRG", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (gb.rh==null) return;
					btnStartR_actionPerformed(arg0);
				}
			});

		}
		
		if(gb.OP_MODE==gb.MO_AdaptiveBellow) {
			textFieldDistance.setEditable(false);
			textFieldStepSize.setEditable(true);
			textFieldStepNumber.setEditable(true);
			btnStartL.setText("<html>Start (<a style='text-decoration:underline'>f</a>)</html>");
			btnStartL.getActionMap().put("btnStartLD", null);
			btnStartL.getActionMap().put("btnStartLF", null);
			btnStartR.getActionMap().put("btnStartRG", null);
			btnStartL.getActionMap().put("btnStartLF", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (gb.rh==null) return;
					btnStartL_actionPerformed(arg0);
				}
			});

			btnStartR.setVisible(false);
			if(gb.frameBellows==null){
				try {
					gb.frameBellows = new frameBellows();
					gb.frameBellows.setName(gb.SSCBELLOWS);
					gb.frameBellows.setVisible(true);
					if(gb.DOCKED) gb.docker.registerDockee(gb.frameBellows, gb.SSCBELLOWS);
				//gb.frame.add(gb.docker.getDockToolbar(), BorderLayout.EAST);
				} catch (Exception e) {
					System.out.println("Exception in frame-new frame operations");
					e.printStackTrace();
				}
			}
		}
	}
	
	public void tempDeactivateFieldsOUT(boolean on) {			
		class Code implements Runnable {
			private boolean on;
			public Code(boolean on){
				this.on=on;
			}
			
			@Override
			public void run(){
				gb.frame.textFieldPosition.setEditable(this.on);
				mnHelp.setEnabled(this.on);
				if(this.on==true) {
					activateFields();
				} else {
					gb.frame.textFieldStepSize.setEditable(this.on);
					gb.frame.textFieldStepNumber.setEditable(this.on);
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
	
	public void activateAlertOUT(boolean on) {
		class Code implements Runnable {
			private boolean on;
			public Code(boolean on){
				this.on=on;
			}
			
			@Override
			public void run(){
				lblAlert.setVisible(this.on);
			}		
		}
		Code code = new Code(on);
		if (SwingUtilities.isEventDispatchThread()) {
			code.run();	
		} else {
			SwingUtilities.invokeLater(code);
		}
	}

	
	public void validateNewPositionEDT(int detach){
		class Code implements Runnable {
			@Override
			public void run(){
				double val=0;
				try {
					val = gb.nf.parse(textFieldPosition.getText()).doubleValue();
				} catch (ParseException e1) {
					val=gb.rh.getCurrentPosition();
					//e1.printStackTrace();
				}
				textFieldPosition.setText(gb.nf.format(val));
				if(Math.abs(val-gb.rh.getCurrentPosition())<0.001) {
					return;
				}
				moveToEDTOUT(DETACH,val);
			}
		}
		
		Code code = new Code();
		if(detach==DETACH){
			SwingUtilities.invokeLater(code);
		} else {
			code.run();
		}
		return;
	}
	

	public void autoDistValidate(){
		try {
			if(UtesNumbers.isNumber(textFieldStepSize.getText())==false){
				setStepSize((gb.STEP_SIZE));
				return;
			}
			double val = gb.nf.parse(textFieldStepSize.getText()).doubleValue();
			//gb.frame.statuslog("val : "+val);
			if((gb.RANGE_END-gb.RANGE_START)!=0){
				if (val<0.001) val = 0.001;				
			} else {
				if (val<0.0) val = 0.0;
			}
			if (val>200.0) val = 200.0;
			//gb.frame.statuslog("val : "+val);
			setStepSize(val);
			//if (gb.STEP_SIZE!=val) gb.frame.statuslog("STEP_SIZE sets to : "+gb.nf.format(gb.stepsToMm(val)));
			gb.STEP_SIZE=val;
			gb.STEP_NUMBER = (int)Math.floor(Math.abs((gb.RANGE_END-gb.RANGE_START)/gb.STEP_SIZE));
			if(gb.STEP_NUMBER==0) gb.STEP_NUMBER=1;
			setStepNumber(gb.STEP_NUMBER);
			setDistances();

		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			System.out.println("ParseException in autoDistValidate");
			e1.printStackTrace();
		}
	}
	
	public void autoStepValidate() {
		int val=0;
		try {
			val = gb.nf.parse(textFieldStepNumber.getText()).intValue();
		} catch (ParseException e1) {
			val=gb.STEP_NUMBER;
		}
		if((gb.RANGE_END-gb.RANGE_START)!=0){
			if(val<1) val=1;
		} else {
			if(val<0) val=0;
		}
		
		textFieldStepNumber.setText(gb.nf.format(val));
		//if (gb.STEP_NUMBER!=val) gb.frame.statuslog("STEP_NUMBER sets to : "+val);
		gb.STEP_NUMBER=val;
		gb.STEP_SIZE = Math.abs((gb.RANGE_END-gb.RANGE_START)/gb.STEP_NUMBER);
		setStepSize((gb.STEP_SIZE));
		setDistances();
	}
	
	public void manualDistValidate(){
		double val=0;
		try {
			val = gb.nf.parse(textFieldStepSize.getText()).doubleValue();
		} catch (ParseException e1) {
			val=gb.STEP_SIZE;
		}
		//gb.frame.statuslog("val : "+val);
		if((gb.RANGE_END-gb.RANGE_START)!=0){
			if (val<0.001) val = 0.001;				
		} else {
			if (val<0.0) val = 0.0;
		}
		if (val>200.0) val = 200.0;
		
		textFieldStepSize.setText(gb.nf.format(val));
		//if (gb.STEP_NUMBER!=val) gb.frame.statuslog("STEP_NUMBER sets to : "+val);
		gb.STEP_SIZE=val;
		gb.STEP_NUMBER = (int)Math.floor(Math.abs((gb.RANGE_END-gb.RANGE_START)/gb.STEP_SIZE));
		if(gb.STEP_NUMBER==0) gb.STEP_NUMBER=1;
		setStepNumber(gb.STEP_NUMBER);
		setDistances();
	}
	
	public void totalDistValidate() {
		double dist=0;
		try {
			dist = gb.nf.parse(textFieldDistance.getText()).doubleValue();
		} catch (ParseException e1) {
			dist = 	gb.STEP_NUMBER*gb.STEP_SIZE;
		}
		if (dist<0.0) dist = 0.0;
		if (dist>200.0) dist = 200.0;
		textFieldDistance.setText(gb.nf.format(dist));
		
		int nb=0;
		try {
			nb = gb.nf.parse(textFieldStepNumber.getText()).intValue();
		} catch (ParseException e) {
			nb=gb.STEP_NUMBER;
		}
		if((dist)!=0){
			if(nb<1) nb=1;
		} else {
			if(nb<0) nb=0;
		}
		textFieldStepNumber.setText(gb.nf.format(nb));
		
		gb.STEP_NUMBER=nb;
		gb.STEP_SIZE = Math.abs(dist/gb.STEP_NUMBER);
		setStepSize((gb.STEP_SIZE));
		setDistances();
	}
	
	public void distStepValidate() {
		int nb=0;
		try {
			nb = gb.nf.parse(textFieldStepNumber.getText()).intValue();
		} catch (ParseException e1) {
			nb=gb.STEP_NUMBER;
		}
		if(nb<0) nb=0;
		textFieldStepNumber.setText(gb.nf.format(nb));
		gb.STEP_NUMBER=nb;
		
		double size=0;
		try {
			size = gb.nf.parse(textFieldStepSize.getText()).doubleValue();
		} catch (ParseException e) {
			size=gb.STEP_SIZE;
		}
		if(size<0) size=0;
		textFieldStepSize.setText(gb.nf.format(size));
		gb.STEP_SIZE = size;

		setDistances();
	}
	
	public void manualValidate() {
		double size=0;
		try {
			size = gb.nf.parse(textFieldStepSize.getText()).doubleValue();
		} catch (ParseException e1) {
			size=gb.STEP_SIZE;
		}
		if(size<0) size=0;
		textFieldStepSize.setText(gb.nf.format(size));
		gb.STEP_SIZE = size;
		gb.STEP_NUMBER=1;
		setStepNumber(gb.STEP_NUMBER);
		setDistances();
	}
	
	public void continuousValidate() {
		double dist=0;
		try {
			dist = gb.nf.parse(textFieldDistance.getText()).doubleValue();
		} catch (ParseException e1) {
			dist=gb.STEP_SIZE*gb.STEP_NUMBER;
		}
		if (dist<0.0) dist = 0.0;
		if (dist>200.0) dist = 200.0;
		textFieldDistance.setText(gb.nf.format(dist));
		gb.STEP_NUMBER=1;
		setStepNumber(gb.STEP_NUMBER);
		gb.STEP_SIZE = dist;
		setStepSize(dist);
		setDistances();
	}
	
	public void bellowsValidate(){
		int nb=0;
		try {
			nb = gb.nf.parse(textFieldStepNumber.getText()).intValue();
		} catch (ParseException e) {
			nb=gb.STEP_NUMBER;
		}
		if(nb<0) nb=0;
		textFieldStepNumber.setText(gb.nf.format(nb));
		gb.STEP_NUMBER=nb;
		
		if(UtesNumbers.isNumber(textFieldStepSize.getText())==false){
			setStepSize(gb.STEP_SIZE);
			return;
		}
		double size=0;
		try {
			size = gb.nf.parse(textFieldStepSize.getText()).doubleValue();
		} catch (ParseException e) {
			size=gb.STEP_SIZE;
		}
		if(size<0) size=0;
		textFieldStepSize.setText(gb.nf.format(size));
		gb.STEP_SIZE = size;
		
		setDistances();
	}
	
	//fonctions helper pour les modifications des chambres textes
	public synchronized void statuslog(String str) {			
		class Code implements Runnable {
			private String str;
			public Code(String str){
				this.str=str;
			}
			
			@Override
			public void run(){
				//if (SwingUtilities.isEventDispatchThread()) {
				//	System.out.println("----OK");
				//} else {
				//	System.out.println("----NOK");
				//}
				textAreaLog.append(this.str+"\n");
				try {
					textAreaLog.scrollRectToVisible(textAreaLog.modelToView(textAreaLog.getDocument().getLength ()));
					} catch (javax.swing.text.BadLocationException err) {
						System.out.println("BadLocation in statuslog");
				}
			}
				
		}
		
		Code code = new Code(str);

		//if (SwingUtilities.isEventDispatchThread()) {
		//	System.out.println("OK");
		//	code.run();
		//} else {
		//	System.out.println("NOK");
			SwingUtilities.invokeLater(code);
		//}
	}
	
	public synchronized void setPositionOUT(double val) {
		class Code implements Runnable {
			private double val;
			public Code(double val){
				this.val=val;
			}
			
			@Override
			public void run(){
				textFieldPosition.setText(gb.nf.format(this.val));
				UtesTelnet.broadcastMsg("POSITION : "+gb.nf.format(this.val)+"     \r");
				if(!gb.rh.isMoving()){
					UtesTelnet.broadcastMsg("\nSTOPPED\r\n");
				}
			}
		}
		Code code = new Code(val);
		if (SwingUtilities.isEventDispatchThread()) {
			code.run();
		} else {
			SwingUtilities.invokeLater(code);
		}
	}

	public void setLowerLimitEDT(int detach, double val) {
		class Code implements Runnable {
			private double val;
			public Code(double val){
				this.val=val;
			}
			
			@Override
			public void run(){
				gb.LOWER_LIMIT= this.val;
				textFieldLowerLimit.setText(gb.nf.format(gb.LOWER_LIMIT));
				UtesTelnet.broadcastMsg("LOWERLIMIT : "+gb.nf.format(this.val)+"\n\r");
				validateColors();
			}
		}
		Code code = new Code(val);
		if(detach==DETACH){
			SwingUtilities.invokeLater(code);
		} else {
			code.run();
		}
	}

	public void setUpperLimitEDT(int detach, double val) {
		class Code implements Runnable {
			private double val;
			public Code(double val){
				this.val=val;
			}
			
			@Override
			public void run(){
				gb.UPPER_LIMIT= this.val;
				textFieldUpperLimit.setText(gb.nf.format(gb.UPPER_LIMIT));
				UtesTelnet.broadcastMsg("UPPERLIMIT : "+gb.nf.format(this.val)+"\n\r");
				validateColors();
			}
		}
		/*Code code = new Code(val);
		if(detach==DETACH){
			SwingUtilities.invokeLater(code);
		} else {
			code.run();
		}*/
		Code code = new Code(val);		
		if (SwingUtilities.isEventDispatchThread()) {
			if(detach==DETACH){
				SwingUtilities.invokeLater(code);
			} else {
				code.run();	
			}
		} else {
			SwingUtilities.invokeLater(code);
		}
	}

	public void setRangeEndEDT(int detach, double val) {
		class Code implements Runnable {
			private double val;
			public Code(double val){
				this.val=val;
			}
			
			@Override
			public void run(){
				gb.RANGE_END=this.val;
				textFieldRangeEnd.setText(gb.nf.format(this.val));
				UtesTelnet.broadcastMsg("RANGEEND : "+gb.nf.format(this.val)+"\n\r");
				validateColors();
			}
		}
		Code code = new Code(val);
		if(detach==DETACH){
			SwingUtilities.invokeLater(code);
		} else {
			code.run();
		}
	}
	
	public void setRangeStartEDT(int detach, double val) {
		class Code implements Runnable {
			private double val;
			public Code(double val){
				this.val=val;
			}
			
			@Override
			public void run(){
				gb.RANGE_START=this.val;
				textFieldRangeStart.setText(gb.nf.format(this.val));
				UtesTelnet.broadcastMsg("RANGESTART : "+gb.nf.format(this.val)+"\n\r");
				validateColors();
			}
		}
		Code code = new Code(val);
		if(detach==DETACH) {
			SwingUtilities.invokeLater(code);
		} else {
			code.run();
		}
	}

	public void setStepSize(double val) {
		textFieldStepSize.setText(gb.nf.format(val));
		UtesTelnet.broadcastMsg("STEPSIZE : "+gb.nf.format(val)+"\n\r");
	}

	public void setStepNumber(int val) {
		textFieldStepNumber.setText(gb.nf.format(val));
		UtesTelnet.broadcastMsg("STEPNUMBER : "+gb.nf.format(val)+"\n\r");
	}

	public void setDistances() {
		if((gb.OP_MODE==gb.MO_TotalDist)) {
			//textFieldDistance.setText(gb.nf.format(gb.stepsToMm(gb.STEP_SIZE)*gb.STEP_NUMBER));
			lblRealdistance.setVisible(true);
			lblRealdistance.setText("("+gb.nf.format(gb.STEP_SIZE*gb.STEP_NUMBER)+")");
		} else {
			lblRealdistance.setVisible(false);
			textFieldDistance.setText(gb.nf.format(gb.STEP_SIZE*gb.STEP_NUMBER));
			UtesTelnet.broadcastMsg("DISTANCE : "+gb.nf.format(gb.STEP_SIZE*gb.STEP_NUMBER)+"\n\r");
		}
		
		if((gb.OP_MODE==gb.MO_AutoStep)||(gb.OP_MODE==gb.MO_AutoDist)||(gb.OP_MODE==gb.MO_ManualDist)
				||(gb.OP_MODE==gb.MO_AdaptiveBellow)) {
			textFieldRange.setText(gb.nf.format(Math.abs(gb.RANGE_END-gb.RANGE_START)));
			UtesTelnet.broadcastMsg("RANGE : "+gb.nf.format(Math.abs(gb.RANGE_END-gb.RANGE_START))+"\n\r");
		} else {
			textFieldRange.setText("N/A");
			UtesTelnet.broadcastMsg("RANGE : N/A\n\r");
		}
		validateColors();
	}
	
	public void validateColors() {
		textFieldUpperLimit.setForeground(gb.black);
		textFieldRangeStart.setForeground(gb.black);
		textFieldLowerLimit.setForeground(gb.black);
		textFieldRangeEnd.setForeground(gb.black);
		textFieldPosition.setForeground(gb.black);
		textFieldDistance.setForeground(gb.black);

		//ranged
		if((gb.OP_MODE==gb.MO_AutoStep)||(gb.OP_MODE==gb.MO_AutoDist)||(gb.OP_MODE==gb.MO_ManualDist)
				||(gb.OP_MODE==gb.MO_AdaptiveBellow)) {
			if(gb.RANGE_START>gb.UPPER_LIMIT) {
				textFieldUpperLimit.setForeground(gb.red);
				textFieldRangeStart.setForeground(gb.red);
			}
			if(gb.RANGE_START<gb.LOWER_LIMIT) {
				textFieldLowerLimit.setForeground(gb.red);
				textFieldRangeStart.setForeground(gb.red);
			}
			if(gb.RANGE_END>gb.UPPER_LIMIT) {
				textFieldUpperLimit.setForeground(gb.red);
				textFieldRangeEnd.setForeground(gb.red);
			}
			if(gb.RANGE_END<gb.LOWER_LIMIT) {
				textFieldLowerLimit.setForeground(gb.red);
				textFieldRangeEnd.setForeground(gb.red);
			}
		} else {
			if(gb.rh!=null && (gb.rh.getCurrentPosition()+gb.STEP_SIZE*gb.STEP_NUMBER)>gb.UPPER_LIMIT) {
				textFieldUpperLimit.setForeground(gb.red);
				textFieldPosition.setForeground(gb.red);
				textFieldDistance.setForeground(gb.red);
			}
			if(gb.rh!=null && (gb.rh.getCurrentPosition()-gb.STEP_SIZE*gb.STEP_NUMBER)<gb.LOWER_LIMIT) {
				textFieldLowerLimit.setForeground(gb.red);
				textFieldPosition.setForeground(gb.red);
				textFieldDistance.setForeground(gb.red);
			}
		}
		if(gb.OP_MODE==gb.MO_AdaptiveBellow) {
			if (gb.UPPER_LIMIT>gb.infinitePos){
				textFieldUpperLimit.setForeground(gb.blue);
			}
			if(gb.RANGE_START>gb.infinitePos) {
				textFieldRangeStart.setForeground(gb.red);
			}
			if(gb.RANGE_END>gb.infinitePos) {
				textFieldRangeEnd.setForeground(gb.red);
			}
		}

	}
	
	public void setZeroEDT(int detach){
		class Code implements Runnable {
			public Code(){
			}
			
			@Override
			public void run(){
				gb.RANGE_START-= gb.rh.getCurrentPosition();
				setRangeStartEDT(NOPE, gb.RANGE_START);
				gb.RANGE_END-=gb.rh.getCurrentPosition();
				setRangeEndEDT(NOPE, gb.RANGE_END);
				if(gb.LOWER_LIMIT>-200){
					gb.LOWER_LIMIT-=gb.rh.getCurrentPosition();
					setLowerLimitEDT(NOPE, gb.LOWER_LIMIT);
				}
				if(gb.UPPER_LIMIT<200){
					gb.UPPER_LIMIT-=gb.rh.getCurrentPosition();
					setUpperLimitEDT(NOPE, gb.UPPER_LIMIT);
				}
				setPositionOUT(0);
				gb.rh.setPositionZero();
				rangeValidateEDT(NOPE);
			}
		}
		Code code = new Code();
		if(detach==DETACH){
			SwingUtilities.invokeLater(code);
		} else {
			code.run();
		}

	}
	
	public void doSequenceEDT(int dir){
		if (!cancel(gb.TEST_CANCEL_ON)){
			Y y = new Y(dir);
			gb.yThread = new Thread(y);
			gb.yThread.start();
		}
	}

	public synchronized void pauseOUT() {
		class Code implements Runnable {
			@Override
			public void run(){
				btnStartL.setText("Restart");
				btnStartR.setText("Restart");
			}
		}
		Code code = new Code();
		if (SwingUtilities.isEventDispatchThread()) {
			code.run();
		} else {
			SwingUtilities.invokeLater(code);
		}
	}
	public synchronized boolean cancel(int val){
		if (val==gb.TEST_CANCEL_ON){
			//System.out.println("TEST_CANCEL_ON");
			if(gb.LOCKEDSEQUENCE||gb.rh.isMoving()||gb.rh.isShutting()){
				gb.PAUSE=false;
				//System.out.println("Something to cancel");
				if (gb.LOCKEDSEQUENCE) {
					// System.out.println("sequence");
					gb.yThread.interrupt();
				}
				gb.rh.stopAll();
				return true;
			} else {
				return false;
			}
		} else if (val==gb.PAUSE_ONOFF){
			if(gb.LOCKEDSEQUENCE){
				if(gb.PAUSE) 
					gb.PAUSE=false;
				else 
					gb.PAUSE=true;
				return true;
			} else
				return false;
		}
		return true;
	}

	
	
	
}



class Y implements Runnable {
	private int dir;
	
	public Y(int dir){
		this.dir=dir;
	}
	
	
	private void waitIfPause() throws InterruptedException{
		boolean paused=false;
		if(gb.PAUSE) {
			paused=true;
			gb.frame.statuslog("PAUSED");
		}
		while(gb.PAUSE){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				throw new InterruptedException();
			}
		}
		if(paused) {
			gb.frame.statuslog("RESTARTED");
		}
	}
	
	private void waitForNext() throws InterruptedException{
		while(gb.PAUSE){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				throw new InterruptedException();
			}
		}
	}
	
	@Override
	public void run() {
		boolean finish=false;
		gb.LOCKEDSEQUENCE=true;
		gb.frame.lockInterface(true);
		//positionnement en début de sequence
		
		double val=gb.rh.getCurrentPosition();
		int actualdir=dir;
		int stepnumber=0;
		int tlstepnumber=1;
		int tlstepstodo=gb.TIMELAPSE_NUMBER>0?gb.TIMELAPSE_NUMBER:-1;
		int totalsteps=gb.STEP_NUMBER;
		try {
			if ((gb.OP_MODE==gb.MO_AutoStep)||(gb.OP_MODE==gb.MO_AutoDist)||(gb.OP_MODE==gb.MO_ManualDist)
					||(gb.OP_MODE==gb.MO_AdaptiveBellow)) {
					val = gb.RANGE_START;
					if(gb.RANGE_END>gb.RANGE_START) actualdir = 1;
					if(gb.RANGE_END<gb.RANGE_START) actualdir = -1;
					if(gb.RANGE_END==gb.RANGE_START) actualdir = 0;
					gb.frame.statuslog("Moving to Range Start");
			} else {
				val = gb.rh.getCurrentPosition();
				//if(dir==FTD2XXHelper.COMM_RAIL_DIR_FWD) actualdir = 1;
				//if(dir==FTD2XXHelper.COMM_RAIL_DIR_BACK) actualdir = -1;
			}
			double startpos=val;
			
			if(gb.OP_MODE==gb.MO_AdaptiveBellow) {
				totalsteps = gb.frameBellows.showAdaptiveBellowsStats(actualdir);
				if (gb.frame.cancel(gb.PAUSE_ONOFF)){
					if(gb.PAUSE){
						gb.frame.pauseOUT();
					}
				}
				waitIfPause();
			}
			
			//stepnumber=0;
			//finish=false;
			//val=gb.POSITION;
			if(gb.OP_MODE!=gb.MO_Continuous) {
				do {
					val=startpos;
					if(gb.TIMELAPSE_INTERVAL>0) {
						gb.frame.statuslog("######-----###### TL STEP "+tlstepnumber+((gb.TIMELAPSE_NUMBER!=0)?("/"+gb.TIMELAPSE_NUMBER+" "):"")+"at position "+gb.nf.format(val));
					}
					do {
						if(gb.frame.moveToEDTOUT(frame.OUT, val)==false) {
							finish=true;
							//System.out.println("finish");
						}
						do {
							Thread.sleep(50);
						} while (gb.rh.isMoving());//status!=gb.jd.RAIL_STATUS_IDLE);
						//System.out.println("out of moving loop");
						//if(gb.POSITION==gb.RANGE_END) finish=true;
						gb.frame.statuslog("DONE");
						if(gb.OP_MODE==gb.MO_AdaptiveBellow) {
							if(actualdir==-1) {
								if (gb.rh.getCurrentPosition()<=gb.RANGE_END) finish=true;
							} else {
								if (gb.rh.getCurrentPosition()>=gb.RANGE_END) finish=true;
							}
						}
						waitIfPause();
			
						//On attend le temps de settle
						gb.frame.statuslog("################# STEP "+stepnumber+"/"+totalsteps+" at position "+gb.nf.format(val));
						//gb.frame.statuslog("START "+gb.RANGE_START);
						//gb.frame.statuslog("END   "+gb.RANGE_END);
						//gb.frame.statuslog("SIZE   "+gb.STEP_SIZE);
						gb.frame.statuslog("Waiting Settle Time "+gb.SETTLE_TIME+"s");
						Thread.sleep((int)gb.SETTLE_TIME*1000);
						gb.frame.statuslog("DONE");
						waitIfPause();
						
						//on fait la 1ere serie de photos
						int nb=gb.PULSE_NUMBER;
						if(nb>0){	
							do {
								gb.processShoot(gb.PULSE_NUMBER-nb+1);
								nb-=1;
								waitIfPause();
							}while (nb>0);
						}
			
						//let's move
						if(gb.OP_MODE!=gb.MO_AdaptiveBellow) {
							val=actualdir*gb.STEP_SIZE+gb.rh.getCurrentPosition();					
						} else {
							double offset;
							double frontfocus = gb.focalLens*(1+1/gb.theoricMag);
							//System.out.println("frontfocus : "+frontfocus);
							if(actualdir==-1) {
								offset=-Math.abs(gb.dofn*gb.DOF_OVERLAP);
							} else {
								offset=Math.abs(gb.doff*gb.DOF_OVERLAP);
							}
							//System.out.println("offset : "+offset);
							double targetfocus = offset+frontfocus;
							//System.out.println("targetfocus : "+targetfocus);
							double targetmag=1/(targetfocus/gb.focalLens-1);
							//System.out.println("targetmag : "+targetmag);
							double ext = (gb.theoricMag-targetmag)*gb.focalLens;
							//System.out.println("ext : "+ext);
							val=ext+gb.rh.getCurrentPosition();
							//System.out.println("val : "+val);
							if(actualdir==-1) {
								if (val<gb.RANGE_END) {
									val=gb.RANGE_END;
								}
							} else {
								if (val>gb.RANGE_END) {
									val=gb.RANGE_END;
								}
							}
						}
		
						stepnumber+=1;
						if((gb.OP_MODE!=gb.MO_Manual) && (gb.OP_MODE!=gb.MO_AdaptiveBellow) && (stepnumber>gb.STEP_NUMBER)) finish=true;
						if((!finish) && ((gb.OP_MODE==gb.MO_ManualDist) || (gb.OP_MODE==gb.MO_Manual))){
							gb.frame.statuslog("Press 'Next Step' Button to continue");
							gb.frame.cancel(gb.PAUSE_ONOFF);
							gb.frame.setStartTextOUT("<html>Next Step (<a style='text-decoration:underline'>f</a>)</html>");
							waitForNext();
						}
						if(!finish)	gb.frame.statuslog("Let's move to next pos. Step Size : " + gb.nf.format(val-gb.rh.getCurrentPosition())+"mm");
			
					} while (!finish);
					System.out.println("finished");
					if(gb.TIMELAPSE_INTERVAL>0) {
						finish=false;
						stepnumber=0;
						tlstepnumber+=1;
						if(tlstepstodo>0) tlstepstodo-=1;
						if(tlstepstodo!=0) {
							gb.frame.statuslog("Waiting Time Lapse Interval");
							Thread.sleep((int)gb.TIMELAPSE_INTERVAL*1000);
							gb.frame.statuslog("DONE");
						} else {
							gb.frame.statuslog("TIMPELAPSE COMPLETED");
						}
					}
				} while(gb.TIMELAPSE_INTERVAL>0 && tlstepstodo!=0);
			} else {
				val=actualdir*gb.STEP_SIZE+gb.rh.getCurrentPosition();	
				gb.frame.moveToEDTOUT(frame.OUT, val);
				do {
					//Thread.sleep(1000);
					//on fait la 1ere serie de photos
					gb.processShoot(stepnumber);
					stepnumber+=1;
				} while (gb.rh.isMoving());
			}
			gb.frame.statuslog("FINISH");
		} catch (InterruptedException e) {
			gb.frame.statuslog("CANCELED");
		}

		gb.LOCKEDSEQUENCE=false;
		gb.frame.lockInterface(false);
	}	
}


