package com.macro_toolbox.stackshotctrl;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class frameDiagnose extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JLabel lblCount;
	public static JProgressBar progressBar;
	public frameDiagnose() {
		setTitle("Testing the Stackshot");
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblTest = new JLabel("Testing : GET_SOFTWARE_STRING");
		getContentPane().add(lblTest, "3, 3, 9, 1");
		
		progressBar = new JProgressBar(SwingConstants.HORIZONTAL,0, 255);
		progressBar.setStringPainted(true);
		getContentPane().add(progressBar, "5, 7, 5, 1, fill, fill");
		
		lblCount = new JLabel("Count");
		getContentPane().add(lblCount, "5, 5, 5, 1, fill, bottom");
		
		JButton btnAbort = new JButton("Abort");
		btnAbort.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(gb.diagnoseThread!=null) gb.diagnoseThread.interrupt();
			}
		});
		getContentPane().add(btnAbort, "7, 9");
		
		// create some css from the label's font
		Font font = lblCount.getFont();
	    StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
	    style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
	    style.append("font-size:" + font.getSize() + "pt;");
		JEditorPane dtrpn = new JEditorPane("text/html", "<html><body style=\"" + style + "\">" 
	            + "When the tests are over, " +
	            "please send the file <a href=\"#\">output.txt</a>" +
	            " to <a href=\"mailto:support@macro-toolbox.com?subject=Diagnose%20Result&" +
	            "body=insert%20your%20message%20here\">support@macro-toolbox.com</a>" 
	            + "</body></html>");
		dtrpn.setEditable(false);
		dtrpn.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent arg0) {
	            if (arg0.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
					try {
						URL url = arg0.getURL();
						//System.out.println(url);
						if(url!=null){	
							Desktop.getDesktop().browse(new URI(url.toString()));
						} else {
							File file=new File(System.getProperty("user.dir"));
							//Process p = new ProcessBuilder("explorer.exe", "/select," + file.getAbsolutePath()).start();
							Desktop.getDesktop().open(file);
						}
						
					} catch ( URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		getContentPane().add(dtrpn, "3, 11, 9, 1, fill, fill");
		setIconImages(gb.icons);
		diagnose y = new diagnose();
		gb.diagnoseThread = new Thread(y);
		gb.diagnoseThread.start();
	}
	
	//fonctions helper pour les modifications des chambres textes
	public synchronized void update(int count) {			
		class Code implements Runnable {
			private int count;
			public Code(int count){
				this.count=count;
			}
			
			@Override
			public void run(){
				lblCount.setText("Count : " + this.count);
				progressBar.setValue(this.count);
				try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
			}
				
		}
		
		Code code = new Code(count);

		SwingUtilities.invokeLater(code);
	}
	
	class diagnose implements Runnable {
		@Override
		public void run() {
			System.out.println("SCANNING STACKSHOT");
	
			update(0);
			
	        //dlg.setVisible(false);
		    JOptionPane.showMessageDialog(null, "Scan complete");
		    gb.rh.close();
			gb.rh=null;
			if(gb.ps!=null) gb.ps.close();
			WindowEvent wev = new WindowEvent(gb.frameDiagnose, WindowEvent.WINDOW_CLOSING);
			Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
			//System.exit(0);
		}
	}
}
