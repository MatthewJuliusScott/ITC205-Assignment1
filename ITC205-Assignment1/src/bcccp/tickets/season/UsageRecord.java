package bcccp.tickets.season;

 public class UsageRecord implements IUsageRecord {
	
	private String ticketId_;
	private long startDateTime_;
	private long endDateTime_;
	
	
	
	public UsageRecord(String ticketId, long startDateTime) {
		this.ticketId_ = ticketId;
		this.startDateTime_ = startDateTime;
	}



	@Override
	public void finalise(long endDateTime) { 
		this.setEndDateTime(endDateTime);
	}



	private void setEndDateTime(long endDateTime) {
		this.endDateTime_ = endDateTime;
	}



	@Override
	public long getStartTime() {
	
		return startDateTime_;
	}



	@Override
	public long getEndTime() {

		return endDateTime_;
	}



	@Override
	public String getSeasonTicketId() { //TODO The name of the object is implicit, and should be avoided in a method name.
		
		return ticketId_;
	}
	
	
	
}
