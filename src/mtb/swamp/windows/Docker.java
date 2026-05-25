package mtb.swamp.windows;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Timer;



public class Docker {

    //public static final String NORTH_DOCKED = "dock north", SOUTH_DOCKED = "dock south", WEST_DOCKED = " dock west", EAST_DOCKED = "dock east";
    //public static final String FOLLOW_LAYOUT = "layout follow", STICKY_LAYOUT = "layout sticky", MAGNETIC_LAYOUT = "layout magnetic";
    private HashMap<Component, String> dockees = new HashMap<Component, String>();
    private Timer dockeeMoveTimer;
    private ComponentAdapter caDock, caDockee;
    private Component dock;
    //private String layout = STICKY_LAYOUT;
    //private int MAGNETIC_FIELD_SIZE = 150, 
    private int movedReactTime = 100;

    public Docker() {
        initTimers();
        initComponentAdapters();
    }

    //public void setLayout(String layout) {
     //   this.layout = layout;
    //}

    public void setComponentMovedReactTime(int milis) {
        movedReactTime = milis;
    }

    private void initComponentAdapters() {
        caDockee = new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent ce) {
                super.componentMoved(ce);
                //if (layout.equals(MAGNETIC_LAYOUT)) {
                //    createDockeeMovedTimer();
                //} else {
                    iterateDockables();
                //}
            }

            /*private void createDockeeMovedTimer() {
                if (dockeeMoveTimer.isRunning()) {
                    dockeeMoveTimer.restart();
                } else {
                    dockeeMoveTimer.start();
                }
            }*/
        };
        caDock = new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent ce) {
                super.componentMoved(ce);
                iterateDockables();
            }

            @Override
            public void componentResized(ComponentEvent ce) {
                super.componentResized(ce);
                iterateDockables();
            }
        };
    }

    private void initTimers() {
        dockeeMoveTimer = new Timer(movedReactTime, new AbstractAction() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent ae) {
                iterateDockables();
            }
        });
        dockeeMoveTimer.setRepeats(false);
    }

    private void iterateDockables() {
        //System.out.println("Dock will call for Dockees to come");
        for (Map.Entry<Component, String> entry : dockees.entrySet()) {
            Component component = entry.getKey();
            String pos = entry.getValue();
            if (!isDocked(component, pos)) {
                dock(component, pos);
            }
        }
    }

    public void registerDock(Component dock) {
        this.dock = dock;
        dock.addComponentListener(caDock);
    }

    public void registerDockee(Component dockee, String frame) {
        dockee.addComponentListener(caDockee);
        dockees.put(dockee, frame);
        caDock.componentResized(new ComponentEvent(dock, 1));//not sure about the int but w dont use it so its fine for now
    }

    public void deregisterDockee(Component dockee) {
        dockee.removeComponentListener(caDockee);
        dockees.remove(dockee);
    }

    private boolean isDocked(Component comp, String pos) {
        int eastDockedX = dock.getX() + dock.getWidth();
        int eastDockedY = dock.getY();
        if (comp.getX() == eastDockedX && comp.getY() == eastDockedY) {
            // System.out.println("is eastly docked");
            return true;
        }
        return false;
    }
    private Timer eastTimer = null;//, southTimer = null, westTimer = null, northTimer = null;

    private void dock(final Component comp, String pos) {
        //System.out.println("Snapping Dockee back to the dock");
        int eastDockedX = dock.getX() + dock.getWidth();
        int eastDockedY = dock.getY();
        if (eastTimer == null) {
            eastTimer = getTimer(comp, eastDockedX, eastDockedY);
            eastTimer.start();
        } else {
            if (!eastTimer.isRunning()) {
                eastTimer = getTimer(comp, eastDockedX, eastDockedY);
                eastTimer.start();
            }
        }
}

    private Timer getTimer(final Component comp, int finalX, int finalY) {
        Timer t = null;
        t = stickyDockableTimer(comp, finalX, finalY);
        return t;
    }

  
  
    private Timer stickyDockableTimer(final Component comp, final int finalX, final int finalY) {
        Timer t = new Timer(1, new AbstractAction() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent ae) {
                comp.setLocation(finalX, finalY);
            }
        });
        t.setRepeats(false);
        return t;
    }

  
}