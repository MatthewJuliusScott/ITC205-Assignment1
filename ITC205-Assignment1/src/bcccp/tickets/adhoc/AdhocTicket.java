
package bcccp.tickets.adhoc;

import java.util.Date;

/**
 * The Class AdhocTicket.
 */
public class AdhocTicket
implements IAdhocTicket {

	/** The carpark id. */
	private String	carparkId_;

	/** The ticket no. */
	private int		ticketNo_;

	/** The entry date time. */
	private long	entryDateTime_;

	/** The paid date time. */
	private long	paidDateTime_;

	/** The exit date time. */
	private long	exitDateTime_;

	/** The charge. */
	private float	charge_;

	/** The barcode. */
	private String	barcode_;

	/** The state. */
	private STATE	state_;

	/**
	 * The Enum STATE.
	 */
	private enum STATE {ISSUED, CURRENT, PAID, EXITED}

	/**
	 * Instantiates a new adhoc ticket.
	 *
	 * @param carparkId
	 *            the carpark id
	 * @param ticketNo
	 *            the ticket no
	 * @param barcode
	 *            the barcode
	 */
	public AdhocTicket(String carparkId, int ticketNo, String barcode) {
		this.carparkId_ = carparkId;
		this.ticketNo_ = ticketNo;
		this.barcode_ = barcode;
		this.state_ = STATE.ISSUED;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.tickets.adhoc.IAdhocTicket#getBarcode()
	@Override
	public String getBarcode() {
		return barcode_;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.tickets.adhoc.IAdhocTicket#getCarparkId()
	@Override
	public String getCarparkId() {
		return carparkId_;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.tickets.adhoc.IAdhocTicket#getTicketNo()
	@Override
	public int getTicketNo() {
		return ticketNo_;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.tickets.adhoc.IAdhocTicket#enter(long)
	@Override
	public void enter(long entryDateTime) {
		this.entryDateTime_ = entryDateTime;
		this.state_ = STATE.CURRENT;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.tickets.adhoc.IAdhocTicket#getEntryDateTime()
	@Override
	public long getEntryDateTime() {
		return entryDateTime_;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.tickets.adhoc.IAdhocTicket#pay(long, float)
	@Override
	public void pay(long paidDateTime, float charge) {
		this.paidDateTime_ = paidDateTime;
		this.charge_ = charge;
		state_ = STATE.PAID;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.tickets.adhoc.IAdhocTicket#getPaidDateTime()
	@Override
	public long getPaidDateTime() {
		return paidDateTime_;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.tickets.adhoc.IAdhocTicket#getCharge()
	@Override
	public float getCharge() {
		return charge_;
	}

	
	
	// (non-Javadoc)
	// @see java.lang.Object#toString()
	public String toString() {
		Date entryDate = new Date( entryDateTime_);
		Date paidDate = new Date( paidDateTime_);
		Date exitDate = new Date( exitDateTime_);

		return "Carpark    : " + carparkId_ + "\n" + "Ticket No  : " + ticketNo_
		        + "\n" + "Entry Time : " + entryDate + "\n" + "Paid Time  : "
		        + paidDate + "\n" + "Exit Time  : " + exitDate + "\n"
		        + "State      : " + state_ + "\n" + "Barcode    : " + barcode_;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.tickets.adhoc.IAdhocTicket#isCurrent()
	@Override
	public boolean isCurrent() {
		return state_ == STATE.CURRENT;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.tickets.adhoc.IAdhocTicket#isPaid()
	@Override
	public boolean isPaid() {
		return state_ == STATE.PAID;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.tickets.adhoc.IAdhocTicket#exit(long)
	@Override
	public void exit(long dateTime) {
		exitDateTime_ = dateTime;
		state_ = STATE.EXITED;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.tickets.adhoc.IAdhocTicket#getExitDateTime()
	@Override
	public long getExitDateTime() {
		return exitDateTime_;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.tickets.adhoc.IAdhocTicket#hasExited()
	@Override
	public boolean hasExited() {
		return state_ == STATE.EXITED;
	}

	
	
}
