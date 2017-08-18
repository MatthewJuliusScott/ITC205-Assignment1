
package bcccp.tickets.season;

import java.util.HashMap;

public class SeasonTicketDAO implements ISeasonTicketDAO {

	private IUsageRecordFactory				factory; //TODO Private class variables should have underscore suffix.

	// need to keep track of all tickets
	private HashMap<String, ISeasonTicket>	tickets;

	public SeasonTicketDAO(IUsageRecordFactory factory) {
		this.factory = factory;
		tickets = new HashMap<String, ISeasonTicket>();
	}

	@Override
	public void deregisterTicket(ISeasonTicket ticket) {

		tickets.remove(ticket.getId());
	}

	@Override
	public ISeasonTicket findTicketById(String ticketId) {

		return tickets.get(ticketId);
	}

	@Override
	public int getNumberOfTickets() {

		return tickets.size();
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

	@Override
	public void registerTicket(ISeasonTicket ticket) {
		tickets.put(ticket.getId(), ticket);
	}

}
