package com.macro_toolbox.stackshotctrl;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFrame;

import mtb.swamp.utes.UtesProps;

public class WindowSaver implements AWTEventListener {

    private static WindowSaver saver;
    private Map<String, JFrame> framemap;
    
	private WindowSaver( ) {
        framemap = new HashMap<String, JFrame>( );
    }

    public static WindowSaver getInstance( ) {
        if(saver == null) {
            saver = new WindowSaver( );
            }
        return saver;   
    }

    @Override
	public void eventDispatched(AWTEvent evt) {
        try {
            if(evt.getID( ) == WindowEvent.WINDOW_OPENED) {
                ComponentEvent cev = (ComponentEvent)evt;
                if(cev.getComponent( ) instanceof JFrame) {
                    JFrame frame = (JFrame)cev.getComponent( );
                    loadSettings(frame);
                }
            }
        }catch(Exception ex) {
        	System.out.println((ex.toString( )));
        }
    }
    
    public static void loadSettings(JFrame frame) throws IOException {
        String name = frame.getName( );
        if( (name.compareTo(gb.SSCMAIN)!=0)&&(name.compareTo(gb.SSCBELLOWS)!=0)&&
        	(name.compareTo(gb.SSCDIAGNOSE)!=0)&&
        	(name.compareTo(gb.SSCLIVEVIEW)!=0)){
        	return;
        }
        
        Properties parameters = new Properties();
		UtesProps.loadXMLProperties(parameters, "configuration.props");

 
   		int x = 0;
		int y = 0;
		int w = 0;
		int h = 0;
		if(name.compareTo(gb.SSCMAIN)==0) {
			x=gb.SSCMAINX;
			y=gb.SSCMAINY;
			w=gb.SSCMAINW;
			h=gb.SSCMAINH;
		}
		if(name.compareTo(gb.SSCBELLOWS)==0) {
			x=gb.SSCBELLOWSX;
			y=gb.SSCBELLOWSY;
			w=gb.SSCBELLOWSW;
			h=gb.SSCBELLOWSH;
		}
		if(name.compareTo(gb.SSCLIVEVIEW)==0) {
			x=gb.SSCLIVEVIEWX;
			y=gb.SSCLIVEVIEWY;
			w=gb.SSCLIVEVIEWW;
			h=gb.SSCLIVEVIEWH;
		}
		if(name.compareTo(gb.SSCDIAGNOSE)==0) {
			x=100;
			y=50;
			w=375;
			h=256;
		}
        
		
        String v=UtesProps.propGetString(null, parameters, name);
      //System.out.println(name+"-"+v);
    	if(v!=null){
        	String[] array = new String[4];
       		array=v.split("\t");
       		try {
    			x = gb.nf.parse(array[0]).intValue();
    			y = gb.nf.parse(array[1]).intValue();
    			w = gb.nf.parse(array[2]).intValue();
    			h = gb.nf.parse(array[3]).intValue();
    		} catch (ParseException e) {
    			
    		}
    	}
        frame.setLocation(x,y);
        frame.setSize(new Dimension(w,h));
        saver.framemap.put(name,frame);
        frame.validate( );
    }
    
   
    
    

    
    public static void saveSettings( ) throws IOException {
    	//System.out.println("prop");
    	Properties parameters = new Properties();
		UtesProps.loadXMLProperties(parameters, "configuration.props");
        
        
        Iterator<String> it = saver.framemap.keySet( ).iterator( );
        while(it.hasNext( )) {    
            String name = it.next( ); 
            JFrame frame = saver.framemap.get(name);
            String param="";
            param+=""+frame.getX()+"\t";
            param+=""+frame.getY()+"\t";
            param+=""+frame.getWidth()+"\t";
            param+=""+frame.getHeight();
            UtesProps.propPutString(parameters, name, param);   
        }
        UtesProps.saveXMLProperties(parameters, "configuration.props");
        
 
    }
}