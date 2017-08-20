package bcccp.tickets.season;

public class UsageRecordFactory implements IUsageRecordFactory {

	@Override
	public IUsageRecord newUsageRecord(String ticketId, long startDateTime) { 
		
		return new UsageRecord(ticketId, startDateTime); 
	}


}
