package bcccp.carpark;

import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.season.ISeasonTicket;

/**
 * The Interface ICarpark.
 */
public interface ICarpark {
	
	/**
	 * Register.
	 *
	 * @param observer the observer
	 */
	public void register(ICarparkObserver observer);
	
	/**
	 * Deregister.
	 *
	 * @param observer the observer
	 */
	public void deregister(ICarparkObserver observer);
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Checks if is full.
	 *
	 * @return true, if is full
	 */
	public boolean isFull();
	
	/**
	 * Issue adhoc ticket.
	 *
	 * @return the i adhoc ticket
	 */
	public IAdhocTicket issueAdhocTicket();
	
	/**
	 * Record adhoc ticket entry.
	 */
	public void recordAdhocTicketEntry();
	
	/**
	 * Gets the adhoc ticket.
	 *
	 * @param barcode the barcode
	 * @return the adhoc ticket
	 */
	public IAdhocTicket getAdhocTicket(String barcode);
	
	/**
	 * Calculate add hoc ticket charge.
	 *
	 * @param entryDateTime the entry date time
	 * @return the float
	 */
	public float calculateAddHocTicketCharge(long entryDateTime);
	
	/**
	 * Record adhoc ticket exit.
	 */
	public void recordAdhocTicketExit();
	
	/**
	 * Register season ticket.
	 *
	 * @param seasonTicket the season ticket
	 */
	public void registerSeasonTicket(ISeasonTicket seasonTicket);
	
	/**
	 * Deregister season ticket.
	 *
	 * @param seasonTicket the season ticket
	 */
	public void deregisterSeasonTicket(ISeasonTicket seasonTicket);

	/**
	 * Checks if is season ticket valid.
	 *
	 * @param ticketId the ticket id
	 * @return true, if is season ticket valid
	 */
	public boolean isSeasonTicketValid(String ticketId);
	
	/**
	 * Checks if season ticket is in use.
	 *
	 * @param ticketId the ticket id
	 * @return true, if is season ticket in use
	 */
	public boolean isSeasonTicketInUse(String ticketId);
	
	/**
	 * Record season ticket entry.
	 *
	 * @param ticketId the ticket id
	 */
	public void recordSeasonTicketEntry(String ticketId);
	
	/**
	 * Record season ticket exit.
	 *
	 * @param ticketId the ticket id
	 */
	public void recordSeasonTicketExit(String ticketId);


}
