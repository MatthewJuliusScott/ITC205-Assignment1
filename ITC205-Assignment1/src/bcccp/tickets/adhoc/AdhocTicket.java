package bcccp.tickets.adhoc;

import java.util.Date;

public class AdhocTicket implements IAdhocTicket {
	
	private String carparkId_;
	private int ticketNo_;
	private long entryDateTime;
	private long paidDateTime;
	private long exitDateTime;
	private float charge;
	private String barcode;
	private STATE state_;
	
	private enum STATE { ISSUED, CURRENT, PAID, EXITED }

	
	
	public AdhocTicket(String carparkId, int ticketNo, String barcode) {
		this.carparkId_ = carparkId;
		this.ticketNo_ = ticketNo;
		this.barcode = barcode;
		this.state_ = STATE.ISSUED;		
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
		result = prime * result
		        + ((carparkId_ == null) ? 0 : carparkId_.hashCode());
		result = prime * result + Float.floatToIntBits(charge);
		result = prime * result
		        + (int) (entryDateTime ^ (entryDateTime >>> 32));
		result = prime * result + (int) (exitDateTime ^ (exitDateTime >>> 32));
		result = prime * result + (int) (paidDateTime ^ (paidDateTime >>> 32));
		result = prime * result + ((state_ == null) ? 0 : state_.hashCode());
		result = prime * result + ticketNo_;
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AdhocTicket)) {
			return false;
		}
		AdhocTicket other = (AdhocTicket) obj;
		if (barcode == null) {
			if (other.barcode != null) {
				return false;
			}
		} else if (!barcode.equals(other.barcode)) {
			return false;
		}
		if (carparkId_ == null) {
			if (other.carparkId_ != null) {
				return false;
			}
		} else if (!carparkId_.equals(other.carparkId_)) {
			return false;
		}
		if (Float.floatToIntBits(charge) != Float
		        .floatToIntBits(other.charge)) {
			return false;
		}
		if (entryDateTime != other.entryDateTime) {
			return false;
		}
		if (exitDateTime != other.exitDateTime) {
			return false;
		}
		if (paidDateTime != other.paidDateTime) {
			return false;
		}
		if (state_ != other.state_) {
			return false;
		}
		if (ticketNo_ != other.ticketNo_) {
			return false;
		}
		return true;
	}



	@Override
	public String getBarcode() {
		return barcode;
	}


	
	@Override
	public String getCarparkId() {
		return carparkId_;
	}

	
	
	@Override
	public int getTicketNo() {
		return ticketNo_;
	}
	

	
	@Override
	public void enter(long entryDateTime) {
		this.entryDateTime = entryDateTime;
		this.state_ = STATE.CURRENT;		
	}
	
	
	
	@Override
	public long getEntryDateTime() {
		return entryDateTime;
	}

	
	
	@Override
	public void pay(long paidDateTime, float charge) {
		this.paidDateTime = paidDateTime;
		this.charge = charge;
		state_ = STATE.PAID;
	}
	
	
	
	@Override
	public long getPaidDateTime() {
		return paidDateTime;
	}



	@Override
	public float getCharge() {
		return charge;
	}

	
	
	public String toString() {
		Date entryDate = new Date(entryDateTime);
		Date paidDate = new Date(paidDateTime);
		Date exitDate = new Date(exitDateTime);

		return "Carpark    : " + carparkId_ + "\n" +
		       "Ticket No  : " + ticketNo_ + "\n" +
		       "Entry Time : " + entryDate + "\n" + 
		       "Paid Time  : " + paidDate + "\n" + 
		       "Exit Time  : " + exitDate + "\n" +
		       "State      : " + state_ + "\n" +
		       "Barcode    : " + barcode;		
	}



	@Override
	public boolean isCurrent() {
		return state_ == STATE.CURRENT;
	}



	@Override
	public boolean isPaid() {
		return state_ == STATE.PAID;
	}



	@Override
	public void exit(long dateTime) {
		exitDateTime = dateTime;
		state_ = STATE.EXITED;
	}



	@Override
	public long getExitDateTime() {
		return exitDateTime;
	}



	@Override
	public boolean hasExited() {
		return state_ == STATE.EXITED;
	}


}
