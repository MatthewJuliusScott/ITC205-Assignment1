package bcccp.tickets.season;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class TestSeasonTicketDAO {

	private IUsageRecord usage;
	private ISeasonTicket ticket;
	private ISeasonTicketDAO sut;
	private IUsageRecordFactory usageFactory;

	@Before
	public void setUp() {
		usageFactory = mock(UsageRecordFactory.class);
		usage = mock(UsageRecord.class);
		ticket = mock(SeasonTicket.class);

		when(ticket.getId()).thenReturn("id");
		when(ticket.getCurrentUsageRecord()).thenReturn(usage);
		when(usageFactory.make(eq("id"), anyLong())).thenReturn(usage);

		sut = new SeasonTicketDAO(usageFactory);
	}

	@Test
	public void testRegisterTicket() {

		when(ticket.inUse()).thenReturn(true);

		sut.registerTicket(ticket);
		assertEquals(1, sut.getNumberOfTickets());
		assertEquals(ticket, sut.findTicketById("id"));

		sut.recordTicketEntry("id");
		verify(ticket).recordUsage(usage);

		sut.recordTicketExit("id");
		verify(usage).finalise(anyLong());

		sut.deregisterTicket(ticket);
		assertEquals(0, sut.getNumberOfTickets());
	}
}