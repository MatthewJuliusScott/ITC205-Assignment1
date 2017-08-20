package bcccp.tickets.season;

 public class UsageRecord implements IUsageRecord {
	
	String ticketId; // TODO Class variables should never be declared public.
	long startDateTime;
	long endDateTime;
	
	
	
	public UsageRecord(String ticketId, long startDateTime) {
		this.ticketId = ticketId;
		this.startDateTime = startDateTime;
	}



	@Override
	public void finalise(long endDateTime) { //TODO The terms get/set must be used where an attribute is accessed directly.
		this.endDateTime = endDateTime;
		
	}



	@Override
	public long getStartTime() {
	
		return startDateTime;
	}



	@Override
	public long getEndTime() {

		return endDateTime;
	}



	@Override
	public String getSeasonTicketId() { //TODO The name of the object is implicit, and should be avoided in a method name.
		
		return ticketId;
	}
	
	
	
}
