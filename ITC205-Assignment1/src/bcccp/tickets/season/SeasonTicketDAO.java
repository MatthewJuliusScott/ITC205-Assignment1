package bcccp.tickets.season;

import java.util.HashMap;

import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.IUsageRecordFactory;

public class SeasonTicketDAO implements ISeasonTicketDAO {

	private IUsageRecordFactory factory;

	// need to keep track of all tickets
	private HashMap<String, ISeasonTicket> tickets;
	
	public SeasonTicketDAO(IUsageRecordFactory factory) {
		this.factory = factory;
	}



	@Override
	public void registerTicket(ISeasonTicket ticket) {
		
		tickets.put(ticket.getId(), ticket);
	}



	@Override
	public void deregisterTicket(ISeasonTicket ticket) {
		
		tickets.remove(ticket.getId());
	}



	@Override
	public int getNumberOfTickets() {
		
		return tickets.size(); 
	}



	@Override
	public ISeasonTicket findTicketById(String ticketId) {
		
		return tickets.get(ticketId);
	}



	@Override
	public void recordTicketEntry(String ticketId) {
		
		ISeasonTicket newTicket = tickets.get(ticketId);
		
		long time = System.currentTimeMillis();
		IUsageRecord usage = factory.make(ticketId, time);
		
		newTicket.recordUsage(usage);
	}



	@Override
	public void recordTicketExit(String ticketId) {
		
		ISeasonTicket oldTicket = tickets.get(ticketId);
		
		long time = System.currentTimeMillis();
		oldTicket.getCurrentUsageRecord().finalise(time);
	
	}
	
	
	
}
