package mtb.swamp.utes;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UtesTasksScheduler {


/**
 * This class should be the easiest way to use the canon sdk. 
 * Please note that you _can_ use the sdk directly or also 
 * use this class to get the basic communication running, and then 
 * communicate with the edsdk directly. 
 * 
 * Either way, one of the most important things to remember is that 
 * edsdk is not multithreaded so your vm might crash if you just call functions
 * from the library. 
 * Instead I suggest you use the static method SLR.invoke( Runnable r ); 
 * or the method canonCamera.invoke( CanonTask task ); 
 * 
 * The later is basically the same, but allows you to easily get a return integer value, 
 * like int result = SLR.invoke( new CanonTask(){ public int run(){ return ...; } } );
 * 
 * 
 * This class also automatically processes and forwards all windows-style messages. 
 * This is required to forward camera events into the edsdk. Currently there is no 
 * way to disable this if it conflicts with your software. 
 * 
 * @author hansi
 */

  
	// The queue of commands that need to be run. 
	private static ConcurrentLinkedQueue<UtesTask<?>> queue = new ConcurrentLinkedQueue<UtesTask<?>>(); 
	
	
	private static Thread dispatcherThread; 
	static{
		// Tells the app to throw an error instead of crashing entirely. 
		// Native.setProtected( true ); 
		// We actually want our apps to crash, because something very dramatic 
		// is going on when the user receives this kind of crash message from 
		// the os and it puts the developer under pressure to fix the issue. 
		// If we enable Native.setProtected the app might just freeze, 
		// which is imho more annoying than a proper crash. 
		// Anyways, if you want the exception-throwing-instead-crashing behaviour
		// just call the above code as early as possible in your main method. 
		
		// Start the dispatch thread
		dispatcherThread = new Thread(){
			public void run(){
				dispatchMessages(); 
			}
		}; 
		dispatcherThread.start(); 
		
		// people are sloppy! 
		// so we add a shutdown hook to close camera connections
		// TODO: doesn't seem to work
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				//CanonCamera.close(); 
			}
		}); 
	}
	
	
	
	
	public UtesTasksScheduler(){
	}
	
	

	
	public void execute(UtesTask<?> cmd){
		queue.add(cmd);
	}
	
	public <T> T executeNow(UtesTask<T> cmd){
		execute(cmd); 
		return cmd.result(); 
	}
	

	/**
	 * executes tasks
	 */
	private static void dispatchMessages() {
		// Do some initializing
	
		UtesTask<?> task = null; 
		
		while(!Thread.currentThread().isInterrupted()){
			// is there a command we're currently working on? 
			if(task != null){
				if(task.finished()){
					System.out.println("Command finished"); 
					// great! 
					task = null; 
				}
			}
			
			// are we free to do new work, and is there even new work to be done? 
			if(!queue.isEmpty() && task == null){
				System.out.println("Received new command, processing " + queue.peek().getClass().toString()); 
				task = queue.poll(); 
				task.run(); 
				task.ran(); 
			}
			
			try {
				Thread.sleep(10);
			}
			catch(InterruptedException e){
				// we don't mind being interrupted
				//e.printStackTrace();
				break; 
			}
		}
		
		System.out.println("Dispatcher thread says bye!"); 
	}
	
	
	public static void close() {
		if(dispatcherThread != null && dispatcherThread.isAlive()){
			dispatcherThread.interrupt(); 
			try {
				dispatcherThread.join();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


}