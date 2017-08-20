package bcccp.tickets.season;

public interface IUsageRecord {
	
	public void setEndDateTime(long endDateTime);
	public long getStartTime();
	public long getEndTime();
	public String getId();

}
