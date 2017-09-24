/**
 * 
 */

package bcccp.carpark;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import bcccp.tickets.adhoc.AdhocTicketDAO;
import bcccp.tickets.adhoc.AdhocTicketFactory;
import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.ISeasonTicketDAO;
import bcccp.tickets.season.IUsageRecordFactory;
import bcccp.tickets.season.SeasonTicket;
import bcccp.tickets.season.SeasonTicketDAO;
import bcccp.tickets.season.UsageRecordFactory;

/**
 * @author Matthew
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CarparkIntegrationTest {

	private int					capacity	= 10;

	private String				name		= "Test Carpark";

	private IAdhocTicketDAO		adhocTicketDAO;

	private ISeasonTicketDAO	seasonTicketDAO;

	Carpark						carpark;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		AdhocTicketFactory adhocTicketFactory = new AdhocTicketFactory();
		adhocTicketDAO = new AdhocTicketDAO(adhocTicketFactory); 
		
		IUsageRecordFactory usageRecordFactory = new UsageRecordFactory();
		seasonTicketDAO = new SeasonTicketDAO(usageRecordFactory);
		
		carpark = new Carpark(name, capacity, adhocTicketDAO, seasonTicketDAO);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.Carpark#Carpark(java.lang.String, int, bcccp.tickets.adhoc.IAdhocTicketDAO, bcccp.tickets.season.ISeasonTicketDAO)}.
	 */
	@Test
	public final void testCarpark() throws Exception {
		carpark = new Carpark(name, capacity, adhocTicketDAO, seasonTicketDAO);
		Assert.assertNotNull(carpark);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.Carpark#getAdhocTicket(java.lang.String)}.
	 */
	@Test
	public final void testGetAdhocTicket() throws Exception {
		int ticketNo = 1;
		String barcode = "A" + Integer.toHexString(ticketNo);
		IAdhocTicket expected = adhocTicketDAO.createTicket("carparkId");
		IAdhocTicket actual = carpark.getAdhocTicket(barcode);
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.Carpark#isSeasonTicketValid(java.lang.String)}.
	 */
	@Test
	public final void testIsSeasonTicketValid() throws Exception { 
		ISeasonTicket seasonTicket = new SeasonTicket("S1111","Bathurst Chase", 0L, 0L);
		carpark.registerSeasonTicket(seasonTicket);
		Assert.assertTrue(carpark.isSeasonTicketValid(seasonTicket.getId()));
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#recordAdhocTicketExit()}.
	 */
	@Test
	public final void testRecordAdhocTicketExit() throws Exception {
		Integer before = carpark.getNParked();
		carpark.recordAdhocTicketExit();
		Integer after = carpark.getNParked();
		Assert.assertTrue(after.equals(before - 1));
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.Carpark#recordSeasonTicketExit(java.lang.String)}.
	 */
	@Test
	public final void testRecordSeasonTicketExit() throws Exception {
		try {
			ISeasonTicket seasonTicket = new SeasonTicket("S1111",carpark.getName(), 0L, 0L);
			carpark.registerSeasonTicket(seasonTicket);
			carpark.recordSeasonTicketEntry(seasonTicket.getId());
			carpark.recordSeasonTicketExit(seasonTicket.getId());
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.Carpark#isSeasonTicketInUse(java.lang.String)}.
	 */
	@Test
	public final void testIsSeasonTicketInUse() throws Exception {
		try {
			ISeasonTicket seasonTicket = new SeasonTicket("S1111",carpark.getName(), 0L, 0L);
			carpark.registerSeasonTicket(seasonTicket);
			carpark.recordSeasonTicketEntry(seasonTicket.getId());
			boolean expected = true;
			boolean actual = carpark.isSeasonTicketInUse(seasonTicket.getId());
			Assert.assertEquals(expected, actual);
		} catch (Exception e) {
			fail();
		}
	}
}
