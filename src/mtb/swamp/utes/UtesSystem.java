package mtb.swamp.utes;

import java.util.concurrent.atomic.AtomicInteger;


public class UtesSystem {

	public static String getDebugInfos(){
		 String s="Device infos:";
	        /*s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
	        s += "\n OS API Level: " + android.os.Build.VERSION.SDK;
	        s += "\n Device: " + android.os.Build.DEVICE;
	        s += "\n Model: " + android.os.Build.MODEL;
	        s += "\n Product: " + android.os.Build.PRODUCT;

	        
	        boolean keyboardPresent = (ctx.getResources().getConfiguration().keyboard != Configuration.KEYBOARD_NOKEYS);
	        s += "\n Hardware Keyboard: " + keyboardPresent;
	        
	        //Display display = getWindowManager().getDefaultDisplay();
	        //Point size = new Point();
	        //display.getSize(size);
	        //int width = size.x;
	        //int height = size.y;
	        
	        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	        int width = display.getWidth();  // deprecated
	        int height = display.getHeight();  // deprecated
	        s += "\nScreen Size: "+width+"x"+height;*/
	        return s;
	}
	
	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

	/**
	 * Generate a value suitable for use in {@link #setId(int)}.
	 * This value will not collide with ID values generated at build time by aapt for R.id.
	 *
	 * @return a generated ID value
	 */
	public static int generateViewId() {
	    for (;;) {
	        final int result = sNextGeneratedId.get();
	        // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
	        int newValue = result + 1;
	        if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
	        if (sNextGeneratedId.compareAndSet(result, newValue)) {
	            return result;
	        }
	    }
	}
}
