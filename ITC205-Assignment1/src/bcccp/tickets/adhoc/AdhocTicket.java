package bcccp.tickets.adhoc;

import java.util.Date;

/**
 * The Class AdhocTicket.
 */
public class AdhocTicket implements IAdhocTicket {
	
	/** The Carpark id. */
	private String carparkId;
	
	/** The ticket no. */
	private int ticketNo;
	
	/** The entry date time. */
	private long entryDateTime;
	
	/** The paid date time. */
	private long paidDateTime;
	
	/** The exit date time. */
	private long exitDateTime = 0l;
	
	/** The charge. */
	private float charge;
	
	/** The barcode. */
	private String barcode;

	
	
	/**
	 * Instantiates a new adhoc ticket.
	 *
	 * @param carparkId the carpark id
	 * @param ticketNo the ticket no
	 * @param barcode the barcode
	 */
	public AdhocTicket(String carparkId, int ticketNo, String barcode) {
		this.carparkId = carparkId;
		this.ticketNo = ticketNo;
		this.barcode = barcode;
	}


	/* (non-Javadoc)
	 * @see bcccp.tickets.adhoc.IAdhocTicket#getTicketNo()
	 */
	@Override
	public int getTicketNo() {
		return ticketNo;
	}


	/* (non-Javadoc)
	 * @see bcccp.tickets.adhoc.IAdhocTicket#getBarcode()
	 */
	@Override
	public String getBarcode() {
		return barcode;
	}


	/* (non-Javadoc)
	 * @see bcccp.tickets.adhoc.IAdhocTicket#getCarparkId()
	 */
	@Override
	public String getCarparkId() {
		return carparkId;
	}


	/* (non-Javadoc)
	 * @see bcccp.tickets.adhoc.IAdhocTicket#enter(long)
	 */
	@Override
	public void enter(long dateTime) {
		entryDateTime = dateTime;
	}


	/* (non-Javadoc)
	 * @see bcccp.tickets.adhoc.IAdhocTicket#getEntryDateTime()
	 */
	@Override
	public long getEntryDateTime() {
		return entryDateTime;
	}


	/* (non-Javadoc)
	 * @see bcccp.tickets.adhoc.IAdhocTicket#isCurrent()
	 */
	@Override
	public boolean isCurrent() {
		long now = new Date().getTime();
		return now >= getEntryDateTime() && now < getExitDateTime();
	}


	/* (non-Javadoc)
	 * @see bcccp.tickets.adhoc.IAdhocTicket#pay(long, float)
	 */
	@Override
	public void pay(long dateTime, float charge) {
		paidDateTime = dateTime;
		this.charge -= charge;
	}

	/* (non-Javadoc)
	 * @see bcccp.tickets.adhoc.IAdhocTicket#getPaidDateTime()
	 */
	@Override
	public long getPaidDateTime() {
		return paidDateTime;
	}


	/* (non-Javadoc)
	 * @see bcccp.tickets.adhoc.IAdhocTicket#isPaid()
	 */
	@Override
	public boolean isPaid() {
		return charge <= 0f;
	}


	/* (non-Javadoc)
	 * @see bcccp.tickets.adhoc.IAdhocTicket#getCharge()
	 */
	@Override
	public float getCharge() {
		return charge;
	}


	/* (non-Javadoc)
	 * @see bcccp.tickets.adhoc.IAdhocTicket#exit(long)
	 */
	@Override
	public void exit(long dateTime) {
		exitDateTime = dateTime;
	}


	/* (non-Javadoc)
	 * @see bcccp.tickets.adhoc.IAdhocTicket#getExitDateTime()
	 */
	@Override
	public long getExitDateTime() {
		return exitDateTime;
	}


	/* (non-Javadoc)
	 * @see bcccp.tickets.adhoc.IAdhocTicket#hasExited()
	 */
	@Override
	public boolean hasExited() {
		return  exitDateTime != 0l;
	}
}
