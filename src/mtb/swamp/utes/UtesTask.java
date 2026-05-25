package mtb.swamp.utes;


public abstract class UtesTask<T> {

	private boolean finished = false; 
	private boolean waitForFinish = false;
	private boolean ran = false; 
	private T result; 
	
	public UtesTask(){
	}
	
	
	/**
	 * This should be short and sweet! 
	 * If your command needs to wait for events 
	 */
	public abstract void run();
	
	/**
	 * Sets the result
	 */
	public void setResult(T result){
		this.result = result; 
	}
	
	/**
	 * By default a SLRCommand will be marked as finished as soon as 
	 * the run method completed. If you attached listens inside run 
	 * and are waiting for a special event to happen before you're done 
	 * please call the notYetFinished() at the end of run(). 
	 * 
	 * This will tell the dispatcher that it should start forwarding event 
	 * messages again, and also it'll wait with the execution of further commands 
	 * until your command somehow calls finish() on itself to let the dispatcher
	 * know that it's done. 
<	 */
	public void notYetFinished(){
		waitForFinish = true; 
	}
	
	/**
	 * Only used in combination with notYetFinished. 
	 * Call this when your commands work is done (e.g. you successfully 
	 * shot and downloaded an image). 
	 * @see CanonTask#notYetFinished()
	 */
	public void finish(){
		finished = true; 
	}
	
	/**
	 * Don't _ever_ call this, promise! 
	 */
	protected void ran(){
		ran = true; 
	}
	
	/**
	 * Checks if this command finished it's work. Only useful in combination with 
	 * finish() and notYetFinished(). 
	 * @see CanonTask#notYetFinished()
	 * @return
	 */
	protected boolean finished(){
		return waitForFinish? finished : ran; 
	}
	
	public T result(){
		while(!finished()){
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				return null; 
			} 
		}
		
		return result; 
	}
}