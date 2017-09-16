/**
 * 
 */

package bcccp.carpark.exit;

import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.junit.MockitoJUnitRunner;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.AdhocTicket;

/**
 * @author Matthew
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ExitControllerTest {

	@Mock
	private Carpark			carpark;

	@Mock
	private IGate			exitGate;

	@Mock
	private ICarSensor		is;

	@Mock
	private ICarSensor		os;

	@Mock
	private IExitUI			ui;

	@InjectMocks
	private ExitController	exitController;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#ExitController(bcccp.carpark.Carpark, bcccp.carpark.IGate, bcccp.carpark.ICarSensor, bcccp.carpark.ICarSensor, bcccp.carpark.exit.IExitUI)}.
	 */
	@Test
	public final void testExitController() throws Exception {
		exitController = new ExitController(carpark, exitGate, is, is, ui);
		Assert.assertNotNull(exitController);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedWAITING() throws Exception {
		// tests that when a car approaches the inside sensor
		// the state is set to waiting
		exitController = new ExitController(carpark, exitGate, is, os, ui);

		ExitController.STATE expected = ExitController.STATE.WAITING;
		when(is.getId()).thenReturn("Exit Inside Sensor");
		when(is.carIsDetected()).thenReturn(true);
		exitController.carEventDetected("Exit Inside Sensor", true);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedBLOCKED() throws Exception {
		// tests that when a car is at the outside sensor
		// the state is set to blocked
		exitController = new ExitController(carpark, exitGate, is, os, ui);
		ExitController.STATE expected = ExitController.STATE.BLOCKED;
		when(os.getId()).thenReturn("Exit Outside Sensor");
		when(os.carIsDetected()).thenReturn(true);
		exitController.carEventDetected("Exit Outside Sensor", true);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#ticketInserted(java.lang.String)}.
	 */
	@Test
	public final void testTicketInsertedAdhocPROCESSED() throws Exception {
		exitController = new ExitController(carpark, exitGate, is, os, ui);

		AdhocTicket adhocTicket = org.mockito.Mockito.mock(AdhocTicket.class);
		when(adhocTicket.equals(null)).thenReturn(false);
		when(adhocTicket.isPaid()).thenReturn(true);

		when(carpark.getAdhocTicket("A")).thenReturn(adhocTicket);

		// cause state to be WAITING
		when(is.getId()).thenReturn("Exit Inside Sensor");
		when(is.carIsDetected()).thenReturn(true);
		exitController.carEventDetected("Exit Inside Sensor", true);
		ExitController.STATE expected = ExitController.STATE.WAITING;
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);

		// insert the ticket, STATE should be PROCESSED
		expected = ExitController.STATE.PROCESSED;
		exitController.ticketInserted("A");
		actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#ticketInserted(java.lang.String)}.
	 */
	@Test
	public final void testTicketInsertedAdhocREJECTED() throws Exception {
		exitController = new ExitController(carpark, exitGate, is, os, ui);

		AdhocTicket adhocTicket = org.mockito.Mockito.mock(AdhocTicket.class);
		when(adhocTicket.equals(null)).thenReturn(false);
		when(adhocTicket.isPaid()).thenReturn(false);

		when(carpark.getAdhocTicket("A")).thenReturn(adhocTicket);

		// cause state to be WAITING
		when(is.getId()).thenReturn("Exit Inside Sensor");
		when(is.carIsDetected()).thenReturn(true);
		exitController.carEventDetected("Exit Inside Sensor", true);
		ExitController.STATE expected = ExitController.STATE.WAITING;
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);

		// insert the ticket, STATE should be REJECTED
		expected = ExitController.STATE.REJECTED;
		exitController.ticketInserted("A");
		actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#ticketInserted(java.lang.String)}.
	 */
	@Test
	public final void testTicketInsertedSeasonPROCESSED() throws Exception {
		exitController = new ExitController(carpark, exitGate, is, os, ui);

		AdhocTicket adhocTicket = org.mockito.Mockito.mock(AdhocTicket.class);
		when(adhocTicket.equals(null)).thenReturn(false);
		when(adhocTicket.isPaid()).thenReturn(true);

		when(carpark.isSeasonTicketValid("1")).thenReturn(true);
		when(carpark.isSeasonTicketInUse("1")).thenReturn(true);

		// cause state to be WAITING
		when(is.getId()).thenReturn("Exit Inside Sensor");
		when(is.carIsDetected()).thenReturn(true);
		exitController.carEventDetected("Exit Inside Sensor", true);
		ExitController.STATE expected = ExitController.STATE.WAITING;
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);

		// insert the ticket, STATE should be PROCESSED
		expected = ExitController.STATE.PROCESSED;
		exitController.ticketInserted("1");
		actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#ticketInserted(java.lang.String)}.
	 */
	@Test
	public final void testTicketInsertedSeasonREJECTED() throws Exception {
		exitController = new ExitController(carpark, exitGate, is, os, ui);

		AdhocTicket adhocTicket = org.mockito.Mockito.mock(AdhocTicket.class);
		when(adhocTicket.equals(null)).thenReturn(false);
		when(adhocTicket.isPaid()).thenReturn(false);

		when(carpark.isSeasonTicketValid("1")).thenReturn(false);

		// cause state to be WAITING
		when(is.getId()).thenReturn("Exit Inside Sensor");
		when(is.carIsDetected()).thenReturn(true);
		exitController.carEventDetected("Exit Inside Sensor", true);
		ExitController.STATE expected = ExitController.STATE.WAITING;
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);

		// insert the ticket, STATE should be REJECTED
		expected = ExitController.STATE.REJECTED;
		exitController.ticketInserted("1");
		actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#ticketInserted(java.lang.String)}.
	 */
	@Test
	public final void testTicketInsertedIncorrectStateIDLE() throws Exception {
		exitController = new ExitController(carpark, exitGate, is, os, ui);

		// insert the ticket, STATE should be IDLE
		ExitController.STATE expected = ExitController.STATE.IDLE;
		exitController.ticketInserted("1");
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link bcccp.carpark.exit.ExitController#ticketTaken()}.
	 */
	@Test
	public final void testTicketTakenIncorrectState() throws Exception {
		exitController = new ExitController(carpark, exitGate, is, os, ui);

		// 'take' the ticket, STATE should be IDLE
		ExitController.STATE expected = ExitController.STATE.IDLE;
		exitController.ticketTaken();
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);

	}

	/**
	 * Test method for {@link bcccp.carpark.exit.ExitController#ticketTaken()}.
	 */
	@Test
	public final void testTicketTaken() throws Exception {
		exitController = new ExitController(carpark, exitGate, is, os, ui);
		exitController.ticketTaken();
	}

	/**
	 * Test method for {@link java.lang.Object#Object()}.
	 */
	@Test
	public final void testObject() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#getClass()}.
	 */
	@Test
	public final void testGetClass() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#hashCode()}.
	 */
	@Test
	public final void testHashCode() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#equals(java.lang.Object)}.
	 */
	@Test
	public final void testEquals() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#clone()}.
	 */
	@Test
	public final void testClone() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#toString()}.
	 */
	@Test
	public final void testToString() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#notify()}.
	 */
	@Test
	public final void testNotify() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#notifyAll()}.
	 */
	@Test
	public final void testNotifyAll() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#wait(long)}.
	 */
	@Test
	public final void testWait() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#finalize()}.
	 */
	@Test
	public final void testFinalize() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

}
