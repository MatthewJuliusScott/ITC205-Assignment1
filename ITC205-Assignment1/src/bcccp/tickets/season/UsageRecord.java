package bcccp.tickets.season;

 public class UsageRecord implements IUsageRecord {
	
	private String ticketId_;
	private long startDateTime_;
	private long endDateTime_;
	
	
	
	public UsageRecord(String ticketId, long startDateTime) {
		this.ticketId_ = ticketId;
		this.startDateTime_ = startDateTime;
	}





	public void setEndDateTime(long endDateTime) {
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
	public String getId() { 
		
		return ticketId_;
	}
	
	
	
}
