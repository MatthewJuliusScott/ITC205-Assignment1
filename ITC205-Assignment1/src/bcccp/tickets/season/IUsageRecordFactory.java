package bcccp.tickets.season;

public interface IUsageRecordFactory {

	public IUsageRecord newUsageRecord(String ticketId, long startDateTime);
}
