package com.macro_toolbox.stackshotctrl;

import java.awt.AWTEvent;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import mtb.devices.cameras.CameraConfigs;
import mtb.devices.cameras.CameraList;
import mtb.swamp.utes.UtesFiles;
import mtb.swamp.utes.UtesTelnet;
import mtb.swamp.windows.Docker;

import com.sun.jna.Platform;

public class stackshotctrl extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws Exception {
		
		EventQueue.invokeLater(new Runnable()  {
			@Override
			public void run() {

				gb.cleanOldInstalls();
				gb.nf = (DecimalFormat)NumberFormat.getInstance();
				gb.nf.setMaximumFractionDigits(3); //arrondi à 3 chiffres apres la virgule 
				gb.dfs = new DecimalFormatSymbols();
		        gb.dfs.setDecimalSeparator('.');
		        gb.nf.setDecimalFormatSymbols( gb.dfs );
		        gb.nf.setGroupingUsed(false);
		        
		        File imgs = new File("icons/32x32.png");
		        File imgm = new File("icons/64x64.png");
		        File imgl = new File("icons/256x256.png");
		        
		        // Lets set the icon for the application
		        
		        try {        
		            gb.icons.add(ImageIO.read(imgs));
		            gb.icons.add(ImageIO.read(imgm));
		            gb.icons.add(ImageIO.read(imgl));
		        } catch (IOException ex) {
		            //logger.log(Level.INFO,"Failed to read icons");
		        }
				
				try {
					   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					   //force chaque composant de la fenêtre à appeler sa méthode updateUI
					} catch (InstantiationException e) {
					} catch (ClassNotFoundException e) {
					} catch (UnsupportedLookAndFeelException e) {
					} catch (IllegalAccessException e) {}
		        
		     
		        gb.cl = new CameraList(gb.nf);
		        gb.cc = new CameraConfigs(gb.nf);
		        
		        gb.propLoadGlobals();
		        
		       		        
		        boolean output=true;
		        if(gb.OUTPUTTXT) {
			        try {
						gb.ps = new PrintStream("output.txt");
					} catch (FileNotFoundException e2) {
						System.out.println("Could not open output");
						output=false;
						e2.printStackTrace();
					}
					if(output){
						System.setOut(gb.ps);
						System.setErr(gb.ps);
					}
		        }
			
				System.out.println((Platform.isWindows() ? 
								"Windows Platform " :
							Platform.isMac() ?
								"Mac Platform " :
							Platform.isLinux() ?
								"Linux Platform " :
								"Unknow Platform ")
							+" with Bitness "+ System.getProperty("os.arch"));
				
				
				
		        

		       
						

		        //create IHM
				Toolkit tk = Toolkit.getDefaultToolkit( );
			    tk.addAWTEventListener(WindowSaver.getInstance( ),
			        AWTEvent.WINDOW_EVENT_MASK);
			    if(gb.scan){
			    	gb.frameDiagnose=new frameDiagnose();
			    	gb.frameDiagnose.setName(gb.SSCDIAGNOSE);
					gb.frameDiagnose.setVisible(true);
			    } else {
					try {
						gb.frame = new frame();
						gb.docker = new Docker();
						gb.docker.registerDock(gb.frame);
						//gb.docker.setLayout(Docker.STICKY_LAYOUT);
						//gb.docker.setMagneticFieldSize(250);//default is 150
						gb.docker.setComponentMovedReactTime(20);//default is 100
						gb.frame.setName(gb.SSCMAIN);
						gb.frame.setVisible(true);
						
					} catch (Exception e) {
						System.out.println("Exception in Init New frame");
						e.printStackTrace();
					}
					if(gb.openFrameBellows){
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
				
			    
				if(gb.scan) {
					SwingUtilities.updateComponentTreeUI(gb.frameDiagnose);	    
				} else {
					SwingUtilities.updateComponentTreeUI(gb.frame);
					
					//gb.frame.statuslog("Stackshot Version : "+version);
					
					//X x = new X();
					//Thread t = new Thread(x);
					//t.start();
					
				}
				
				
				//on detecte si il y a une mise à jour
				System.out.println("Current version : "+gb.version);
				if(gb.CHECKVERSION) {
					try {
						  URL url = new URL("http://macro-toolbox.com/download/SimpleStackshotCtrl/mtb_getversion.php");
						  InputStream ins = url.openStream();
						  String appversion = UtesFiles.streamToString(ins);
						  ins.close();
						  if (!appversion.equalsIgnoreCase(gb.version)) {
							  System.out.println("New version : "+appversion);
							// for copying style
							    JLabel label = new JLabel();
							    Font font = label.getFont();

							    // create some css from the label's font
							    StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
							    style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
							    style.append("font-size:" + font.getSize() + "pt;");

							    // html content
							    JEditorPane ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">" //
							            + "New version available <a href=\"http://macro-toolbox.com/download/SimpleStackshotCtrl/\">here</a>" //
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
											} catch ( URISyntaxException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											} catch (IOException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
							        }
							    });
							    ep.setEditable(false);
							    ep.setBackground(label.getBackground());

							    // show
							    JOptionPane.showMessageDialog(null, ep);
						  } else System.out.println("No new version");
						  
					} catch (IOException e) {}
				}
			}
		});
		
	}
	

}