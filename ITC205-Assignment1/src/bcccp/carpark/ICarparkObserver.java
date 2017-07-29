package bcccp.carpark;

/**
 * An asynchronous update interface for receiving notifications
 * about ICarpark information as the ICarpark is constructed.
 */
public interface ICarparkObserver {
	
	/**
	 * This method is called when information about an ICarpark
	 * which was previously requested using an asynchronous
	 * interface becomes available.
	 */
	public void notifyCarparkEvent();
	

}
