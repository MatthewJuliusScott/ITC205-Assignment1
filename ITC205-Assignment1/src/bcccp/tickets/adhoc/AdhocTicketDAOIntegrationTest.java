/**
 * 
 */

package bcccp.tickets.adhoc;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Matthew
 *
 */
public class AdhocTicketDAOIntegrationTest {

	private IAdhocTicketFactory	adhocTicketFactory;

	private AdhocTicketDAO		adhocTicketDAO;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		adhocTicketFactory = new AdhocTicketFactory();
		adhocTicketDAO = new AdhocTicketDAO(adhocTicketFactory); 
	}
	
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
		int ticketNo = 1;
		String barcode = "A" + Integer.toHexString(ticketNo);
		IAdhocTicket expected = new AdhocTicket("carparkId", ticketNo, barcode);
		IAdhocTicket actual = adhocTicketDAO.createTicket("carparkId");
		Assert.assertEquals(expected, actual);	
	}

	/**
	 * Test method for {@link bcccp.tickets.adhoc.AdhocTicketDAO#findTicketByBarcode(java.lang.String)}.
	 */
	@Test
	public final void testFindTicketByBarcode() throws Exception {
		int ticketNo = 1;
		String barcode = "A" + Integer.toHexString(ticketNo);
		IAdhocTicket expected = new AdhocTicket("carparkId", ticketNo, barcode);
		adhocTicketDAO.createTicket("carparkId");
		IAdhocTicket actual = adhocTicketDAO.findTicketByBarcode(expected.getBarcode());
		Assert.assertEquals(expected, actual);	
	}
	
	/**
	 * Test method for {@link bcccp.tickets.adhoc.AdhocTicketDAO#getCurrentTickets()}.
	 */
	@Test
	public final void testGetCurrentTickets() throws Exception {
		IAdhocTicket ticket = adhocTicketDAO.createTicket("carparkId");
		List<IAdhocTicket> currentTickets = adhocTicketDAO.getCurrentTickets();
		Assert.assertTrue(currentTickets.contains(ticket));
	}
}
