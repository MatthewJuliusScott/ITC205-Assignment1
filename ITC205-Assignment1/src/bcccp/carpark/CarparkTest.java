/**
 * 
 */

package bcccp.carpark;

import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.IGate;
import bcccp.carpark.exit.ExitController;
import bcccp.tickets.adhoc.AdhocTicket;
import bcccp.tickets.adhoc.AdhocTicketDAO;
import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.ISeasonTicketDAO;
import bcccp.tickets.season.SeasonTicketDAO;

/**
 * @author Matthew
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CarparkTest {

	private int					capacity	= 1;

	private String				name		= "Test Carpark";

	@Mock
	private IAdhocTicketDAO		adhocTicketDAO;

	@Mock
	private ISeasonTicketDAO	seasonTicketDAO;

	Carpark						carpark;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
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
		IAdhocTicket expected = org.mockito.Mockito.mock(IAdhocTicket.class);
		when(adhocTicketDAO.findTicketByBarcode("BARCODE"))
		        .thenReturn(expected);
		IAdhocTicket actual = carpark.getAdhocTicket("BARCODE");
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.Carpark#isSeasonTicketValid(java.lang.String)}.
	 */
	@Test
	public final void testIsSeasonTicketValid() throws Exception {
		ISeasonTicket ticket = org.mockito.Mockito.mock(ISeasonTicket.class);
		when(seasonTicketDAO.findTicketById("BARCODE")).thenReturn(ticket);
		boolean isValid = carpark.isSeasonTicketValid("BARCODE");
		Assert.assertTrue(isValid);
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
			ISeasonTicket ticket = org.mockito.Mockito.mock(ISeasonTicket.class);
			when(seasonTicketDAO.findTicketById("BARCODE")).thenReturn(ticket);
			doNothing().when(seasonTicketDAO).recordTicketExit("BARCODE");
			carpark.recordSeasonTicketExit("BARCODE");
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
			ISeasonTicket ticket = org.mockito.Mockito.mock(ISeasonTicket.class);
			when(seasonTicketDAO.findTicketById("BARCODE")).thenReturn(ticket);
			when(ticket.inUse()).thenReturn(true);
			boolean expected = true;
			boolean actual = carpark.isSeasonTicketInUse("BARCODE");
			Assert.assertEquals(expected, actual);
		} catch (Exception e) {
			fail();
		}
	}
}
