/**
 * 
 */

package bcccp.carpark.exit;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import bcccp.carpark.CarSensor;
import bcccp.carpark.Carpark;
import bcccp.carpark.Gate;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.AdhocTicket;
import bcccp.tickets.adhoc.AdhocTicketDAO;
import bcccp.tickets.adhoc.AdhocTicketFactory;
import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.season.IUsageRecordFactory;
import bcccp.tickets.season.SeasonTicket;
import bcccp.tickets.season.SeasonTicketDAO;
import bcccp.tickets.season.UsageRecordFactory;

/**
 * @author Matthew
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ExitControllerIntegrationTest {

	private Carpark			carpark;

	private IGate			exitGate;

	private CarSensor		is;

	private CarSensor		os;

	private IExitUI			ui;

	IAdhocTicket			adhocTicket;

	SeasonTicket			seasonTicket;

	private ExitController	exitController;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// create the Carpark and its supporting classes
		int capacity = 10;
		String name = "Test Carpark";
		AdhocTicketFactory adhocTicketFactory = new AdhocTicketFactory();
		AdhocTicketDAO adhocTicketDAO = new AdhocTicketDAO(adhocTicketFactory);

		IUsageRecordFactory usageRecordFactory = new UsageRecordFactory();
		SeasonTicketDAO seasonTicketDAO = new SeasonTicketDAO(
		        usageRecordFactory);

		carpark = new Carpark(name, capacity, adhocTicketDAO, seasonTicketDAO);

		exitGate = new Gate(1330, 320);
		is = new CarSensor("Exit Inside Sensor", 1330, 100);
		is.registerResponder(exitController);
		os = new CarSensor("Exit Outside Sensor", 1330, 440);
		os.registerResponder(exitController);
		ui = new ExitUI(1000, 100);

		exitController = new ExitController(carpark, exitGate, is, os, ui);

		seasonTicket = new SeasonTicket("S1111", name, 0L, 0L);
		
		int ticketNo = 1;
		String barcode = "A" + Integer.toHexString(ticketNo);
		adhocTicket = new AdhocTicket("carparkId", ticketNo, barcode);
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
		ExitController.STATE expected = ExitController.STATE.WAITING;
		is.setCarIsDetected(true);
		exitController.carEventDetected(is.getId(), true);
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
		ExitController.STATE expected = ExitController.STATE.BLOCKED;
		os.setCarIsDetected(true);
		exitController.carEventDetected(os.getId(), true);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedPrevState() throws Exception {
		// tests that when the exit is BLOCKED and no car is detected inside it
		// returns to its previous State
		ExitController.STATE expected = exitController.getPrevState();
		is.setCarIsDetected(false);
		exitController.carEventDetected(os.getId(), false);
		testCarEventDetectedBLOCKED();
		exitController.carEventDetected("Exit Inside Sensor", false);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedIDLE() throws Exception {
		// tests that when the state is PROCESSED and no car is detected inside
		// it becomes IDLE
		ExitController.STATE expected = ExitController.STATE.IDLE;

		testTicketInsertedSeasonPROCESSED();

		is.setCarIsDetected(false);
		exitController.carEventDetected("Exit Inside Sensor", false);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedBlocked() throws Exception {
		// tests that when the state is PROCESSED but a car is detected outside
		// it becomes BLOCKED
		ExitController.STATE expected = ExitController.STATE.BLOCKED;

		testTicketInsertedSeasonPROCESSED();

		os.setCarIsDetected(true);

		exitController.carEventDetected("Exit Outside Sensor", true);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedAdhocTAKENtoIDLE() throws Exception {
		// tests that when the ticket is TAKEN and no car is detected inside the
		// state changes to IDLE
		ExitController.STATE expected = ExitController.STATE.IDLE;

		testTicketTakenAdhocTAKEN();

		is.setCarIsDetected(false);

		exitController.carEventDetected("Exit Inside Sensor", false);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedAdhocTAKENtoWAITING()
	        throws Exception {
		// tests that when the ticket is TAKEN and a car is detected inside the
		// state changes to WAITING
		ExitController.STATE expected = ExitController.STATE.WAITING;

		testTicketTakenAdhocTAKEN();

		is.setCarIsDetected(true);

		exitController.carEventDetected("Exit Inside Sensor", false);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedSeasonTAKENtoIDLE() throws Exception {
		// tests that when the ticket is TAKEN and no car is detected inside the
		// state changes to IDLE
		ExitController.STATE expected = ExitController.STATE.IDLE;

		testTicketTakenSeasonTAKEN();

		is.setCarIsDetected(false);

		exitController.carEventDetected("Exit Inside Sensor", false);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedAdhocTAKENtoEXITING()
	        throws Exception {
		// tests that when the ticket is TAKEN and a car is detected outside the
		// state changes to EXITING
		ExitController.STATE expected = ExitController.STATE.EXITING;

		testTicketTakenAdhocTAKEN();

		exitController.carEventDetected("Exit Outside Sensor", true);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedSeasonTAKENtoEXITING()
	        throws Exception {
		// tests that when the ticket is TAKEN and a car is detected outside the
		// state changes to EXITING
		ExitController.STATE expected = ExitController.STATE.EXITING;

		testTicketTakenSeasonTAKEN();

		exitController.carEventDetected("Exit Outside Sensor", true);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedAdhocEXITINGtoEXITED()
	        throws Exception {
		// tests that when the State is EXITING and there is no car detected
		// inside the State changes to EXITED
		ExitController.STATE expected = ExitController.STATE.EXITED;

		testCarEventDetectedAdhocTAKENtoEXITING();

		exitController.carEventDetected("Exit Inside Sensor", false);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedSeasonEXITINGtoEXITED()
	        throws Exception {
		// tests that when the State is EXITING and there is no car detected
		// inside the State changes to EXITED
		ExitController.STATE expected = ExitController.STATE.EXITED;

		testCarEventDetectedSeasonTAKENtoEXITING();

		exitController.carEventDetected("Exit Inside Sensor", false);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedAdhocEXITINGtoTAKEN()
	        throws Exception {
		// tests that when the State is EXITING and there is no car detected
		// outside the State changes to TAKEN
		ExitController.STATE expected = ExitController.STATE.TAKEN;

		testCarEventDetectedAdhocTAKENtoEXITING();

		exitController.carEventDetected("Exit Outside Sensor", false);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedSeasonEXITINGtoTAKEN()
	        throws Exception {
		// tests that when the State is EXITING and there is no car detected
		// outside the State changes to TAKEN
		ExitController.STATE expected = ExitController.STATE.TAKEN;

		testCarEventDetectedSeasonTAKENtoEXITING();

		exitController.carEventDetected("Exit Outside Sensor", false);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedAdhocEXITEDtoEXITING()
	        throws Exception {
		// tests that when the State is EXITED and there is a car detected
		// inside the State changes to EXITING
		ExitController.STATE expected = ExitController.STATE.EXITING;

		testCarEventDetectedAdhocEXITINGtoEXITED();

		exitController.carEventDetected("Exit Inside Sensor", true);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedSeasonEXITEDtoEXITING()
	        throws Exception {
		// tests that when the State is EXITED and there is a car detected
		// inside the State changes to EXITING
		ExitController.STATE expected = ExitController.STATE.EXITING;

		testCarEventDetectedSeasonEXITINGtoEXITED();

		exitController.carEventDetected("Exit Inside Sensor", true);
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedAdhocEXITEDtoIDLE() throws Exception {
		// tests that when the State is EXITED and there is no car detected
		// outside the State changes to IDLE and then to WAITING
		ExitController.STATE expected = ExitController.STATE.IDLE;

		testCarEventDetectedAdhocEXITINGtoEXITED();


		is.setCarIsDetected(false);

		exitController.carEventDetected("Exit Outside Sensor", false);
		ExitController.STATE actual = exitController.getPrevState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#carEventDetected(java.lang.String, boolean)}.
	 */
	@Test
	public final void testCarEventDetectedSeasonEXITEDtoIDLE()
	        throws Exception {
		// tests that when the State is EXITED and there is no car detected
		// outside the State changes to IDLE and then to WAITING
		ExitController.STATE expected = ExitController.STATE.IDLE;

		testCarEventDetectedSeasonEXITINGtoEXITED();

		is.setCarIsDetected(false);

		exitController.carEventDetected("Exit Outside Sensor", false);
		ExitController.STATE actual = exitController.getPrevState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#ticketInserted(java.lang.String)}.
	 */
	@Test
	public final void testTicketInsertedAdhocPROCESSED() throws Exception {
		adhocTicket = carpark.issueAdhocTicket();
		adhocTicket.pay(0L, 0.0f);

		// cause state to be WAITING
		testCarEventDetectedWAITING();

		// insert the ticket, STATE should be PROCESSED
		ExitController.STATE expected = ExitController.STATE.PROCESSED;
		exitController.ticketInserted(adhocTicket.getBarcode());
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#ticketInserted(java.lang.String)}.
	 */
	@Test
	public final void testTicketInsertedAdhocREJECTED() throws Exception {
		
		// cause state to be WAITING
		testCarEventDetectedWAITING();

		// insert the ticket, STATE should be REJECTED
		ExitController.STATE expected = ExitController.STATE.REJECTED;
		exitController.ticketInserted(adhocTicket.getBarcode());
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#ticketInserted(java.lang.String)}.
	 */
	@Test
	public final void testTicketInsertedSeasonPROCESSED() throws Exception {
		// cause state to be WAITING
		testCarEventDetectedWAITING();
		
		carpark.registerSeasonTicket(seasonTicket);
		carpark.recordSeasonTicketEntry(seasonTicket.getId());

		// insert the ticket, STATE should be PROCESSED
		ExitController.STATE expected = ExitController.STATE.PROCESSED;
		exitController.ticketInserted(seasonTicket.getId());
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#ticketInserted(java.lang.String)}.
	 */
	@Test
	public final void testTicketInsertedSeasonIDLE() throws Exception {
		carpark.registerSeasonTicket(seasonTicket);
		carpark.recordSeasonTicketEntry(seasonTicket.getId());

		// cause state to be WAITING
		testCarEventDetectedWAITING();

		// insert the ticket, STATE should be PROCESSED
		is.setCarIsDetected(false);
		ExitController.STATE expected = ExitController.STATE.IDLE;
		exitController.ticketInserted("1");
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#ticketInserted(java.lang.String)}.
	 */
	@Test
	public final void testTicketInsertedAdhocIDLE() throws Exception {
		// cause state to be WAITING
		testCarEventDetectedWAITING();

		// insert the ticket, STATE should be IDLE
		is.setCarIsDetected(false);
		ExitController.STATE expected = ExitController.STATE.IDLE;
		exitController.ticketInserted("1");
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#ticketInserted(java.lang.String)}.
	 */
	@Test
	public final void testTicketInsertedSeasonREJECTED() throws Exception {
		is.setCarIsDetected(false);

		// cause state to be WAITING
		testCarEventDetectedWAITING();

		// insert the ticket, STATE should be REJECTED
		ExitController.STATE expected = ExitController.STATE.REJECTED;
		exitController.ticketInserted(seasonTicket.getId());
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link bcccp.carpark.exit.ExitController#ticketInserted(java.lang.String)}.
	 */
	@Test
	public final void testTicketInsertedIncorrectStateIDLE() throws Exception {
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
	public final void testTicketTakenAdhocTAKEN() throws Exception {
		// 'take' the ticket, STATE should be TAKEN
		testTicketInsertedAdhocPROCESSED();
		exitController.ticketTaken();
		ExitController.STATE expected = ExitController.STATE.TAKEN;
		exitController.ticketTaken();
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link bcccp.carpark.exit.ExitController#ticketTaken()}.
	 */
	@Test
	public final void testTicketTakenSeasonTAKEN() throws Exception {
		// 'take' the ticket, STATE should be TAKEN
		testTicketInsertedSeasonPROCESSED();
		exitController.ticketTaken();
		ExitController.STATE expected = ExitController.STATE.TAKEN;
		exitController.ticketTaken();
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link bcccp.carpark.exit.ExitController#ticketTaken()}.
	 */
	@Test
	public final void testTicketTakenStateIDLE() throws Exception {
		// 'take' the ticket, STATE should be IDLE
		testTicketInsertedAdhocREJECTED();
		is.setCarIsDetected(false);
		exitGate.raise();
		exitController.ticketTaken();
		ExitController.STATE expected = ExitController.STATE.IDLE;
		exitController.ticketTaken();
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link bcccp.carpark.exit.ExitController#ticketTaken()}.
	 */
	@Test
	public final void testTicketTakenStateWAITING() throws Exception {
		// 'take' the ticket, STATE should be WAITING
		testTicketInsertedAdhocREJECTED();
		is.setCarIsDetected(true);
		exitController.ticketTaken();
		ExitController.STATE expected = ExitController.STATE.WAITING;
		ExitController.STATE actual = exitController.getState();
		Assert.assertEquals(expected, actual);
	}
}
