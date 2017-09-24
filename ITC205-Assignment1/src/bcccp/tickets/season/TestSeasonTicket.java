package bcccp.tickets.season;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class TestSeasonTicket {

	private IUsageRecord usage;
	private SeasonTicket ticket;

	@Before
	public void setUp() {

		ticket = new SeasonTicket("A", "B", 1L, 2L);

		usage = mock(UsageRecord.class);
	}

	@Test
	public void testEndUsage() {
		ticket.recordUsage(usage);
		assertTrue(ticket.inUse());

		ticket.endUsage(System.currentTimeMillis());
		assertFalse(ticket.inUse());
		verify(usage).finalise(anyLong());
	}

	@Test
	public void testGetters() {

		assertEquals(1L, ticket.getStartValidPeriod());
		assertEquals(2L, ticket.getEndValidPeriod());
		assertEquals("A", ticket.getId());
		assertEquals("B", ticket.getCarparkId());

	}

	@Test
	public void testRecordUsage() {

		ticket.recordUsage(usage);

		assertEquals(usage, ticket.getCurrentUsageRecord());
		assertTrue(ticket.inUse());
		assertTrue(ticket.getUsageRecords().contains(usage));
		assertEquals(1, ticket.getUsageRecords().size());

	}
}