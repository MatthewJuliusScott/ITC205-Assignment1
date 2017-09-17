/**
 * 
 */

package bcccp.tickets.adhoc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author Matthew
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AdhocTicketDAOTest {

	@Mock
	private IAdhocTicketFactory	adhocTicketFactory;

	@InjectMocks
	private AdhocTicketDAO		adhocTicketDAO;

	/**
	 * Test method for {@link bcccp.tickets.adhoc.AdhocTicketDAO#AdhocTicketDAO(bcccp.tickets.adhoc.IAdhocTicketFactory)}.
	 */
	@Test
	public final void testAdhocTicketDAO() throws Exception {
		adhocTicketDAO = new AdhocTicketDAO(adhocTicketFactory);
		Assert.assertNotNull(adhocTicketDAO);
	}
	
	/**
	 * Test method for {@link bcccp.tickets.adhoc.AdhocTicketDAO#createTicket(java.lang.String)}.
	 */
	@Test
	public final void testCreateTicket() throws Exception {
		IAdhocTicket expected = org.mockito.Mockito.mock(IAdhocTicket.class);
		when(adhocTicketFactory.make(any(String.class), anyInt()))
        .thenReturn(expected);
		IAdhocTicket actual = adhocTicketDAO.createTicket("carparkId");
		Assert.assertEquals(expected, actual);	
	}

	/**
	 * Test method for {@link bcccp.tickets.adhoc.AdhocTicketDAO#findTicketByBarcode(java.lang.String)}.
	 */
	@Test
	public final void testFindTicketByBarcode() throws Exception {
		IAdhocTicket ticketMock = org.mockito.Mockito.mock(IAdhocTicket.class);
		when(adhocTicketFactory.make(any(String.class), anyInt()))
        .thenReturn(ticketMock);
		IAdhocTicket ticket = adhocTicketDAO.createTicket("carparkId");
		IAdhocTicket findTicket = adhocTicketDAO.findTicketByBarcode(ticket.getBarcode());
		Assert.assertEquals(ticket.getBarcode(), findTicket.getBarcode());	
	}
	
	/**
	 * Test method for {@link bcccp.tickets.adhoc.AdhocTicketDAO#getCurrentTickets()}.
	 */
	@Test
	public final void testGetCurrentTickets() throws Exception {
		IAdhocTicket ticketMock = org.mockito.Mockito.mock(IAdhocTicket.class);
		when(adhocTicketFactory.make(any(String.class), anyInt()))
        .thenReturn(ticketMock);
		IAdhocTicket ticket = adhocTicketDAO.createTicket("carparkId");
		List<IAdhocTicket> currentTickets = adhocTicketDAO.getCurrentTickets();
		Assert.assertTrue(currentTickets.contains(ticket));
	}
}
