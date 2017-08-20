package bcccp.tickets.season;

public class UsageRecordFactory implements IUsageRecordFactory {

	@Override
	public IUsageRecord newUsageRecord(String ticketId, long startDateTime) { //TODO Classes that creates instances on behalf of others (factories) can do so through method new[ClassName].
		
		return new UsageRecord(ticketId, startDateTime); 
	}


}
