
package integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import bcccp.carpark.CarSensor;
import bcccp.carpark.Carpark;
import bcccp.carpark.Gate;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.IGate;
import bcccp.carpark.entry.EntryController;
import bcccp.carpark.entry.EntryUI;
import bcccp.carpark.entry.IEntryUI;
import bcccp.tickets.adhoc.AdhocTicketDAO;
import bcccp.tickets.adhoc.AdhocTicketFactory;
import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.ISeasonTicketDAO;
import bcccp.tickets.season.SeasonTicket;
import bcccp.tickets.season.SeasonTicketDAO;
import bcccp.tickets.season.UsageRecordFactory;

public class EnterCarparkIntegrationTest {

	private Carpark				carpark;

	private ISeasonTicket		seasonTicket;

	private IAdhocTicketDAO		adhocTicketDAO;

	private ISeasonTicketDAO	seasonTicketDAO;

	private EntryController		entryController;

	private ICarSensor			entryOutsideSensor	= mock(CarSensor.class);

	private ICarSensor			entryInsideSensor	= mock(CarSensor.class);

	private IEntryUI			entryUI				= mock(EntryUI.class);

	private IGate				gate				= mock(Gate.class);

	@Test
	public void carparkFullTest() {

		// make carpark full
		carpark.recordAdhocTicketEntry();
		carpark.recordAdhocTicketEntry();
		carpark.recordAdhocTicketEntry();
		carpark.recordAdhocTicketEntry();
		carpark.recordAdhocTicketEntry();
		carpark.recordAdhocTicketEntry();
		carpark.recordAdhocTicketEntry();
		carpark.recordAdhocTicketEntry();
		carpark.recordAdhocTicketEntry();
		carpark.recordAdhocTicketEntry();

		when(entryOutsideSensor.getId()).thenReturn("Out sensor");
		when(entryOutsideSensor.carIsDetected()).thenReturn(true);
		when(entryInsideSensor.getId()).thenReturn("In sensor");

		entryController.carEventDetected(entryOutsideSensor.getId(), true);

		verify(entryUI).display("Push Button");
		assertEquals(EntryController.STATE.WAITING, entryController.getState());

		entryController.buttonPushed();

		assertTrue(carpark.isFull());

		verify(entryUI).display("Carpark Full");
		assertEquals(EntryController.STATE.FULL, entryController.getState());
	}

	@Test
	public void enterCarparkSeason() {

		seasonTicket = new SeasonTicket("Season Id", "Carpark Id", 1, 2);
		carpark.registerSeasonTicket(seasonTicket);

		when(entryOutsideSensor.getId()).thenReturn("Out sensor");
		when(entryOutsideSensor.carIsDetected()).thenReturn(true);
		when(entryInsideSensor.getId()).thenReturn("In sensor");
		when(gate.isRaised()).thenReturn(true);

		entryController.carEventDetected(entryOutsideSensor.getId(), true);

		verify(entryUI).display("Push Button");
		assertEquals(EntryController.STATE.WAITING, entryController.getState());

		entryController.ticketInserted(seasonTicket.getId());
		carpark.recordSeasonTicketEntry(seasonTicket.getId());

		assertTrue(carpark.isSeasonTicketValid(seasonTicket.getId()));
		assertTrue(carpark.isSeasonTicketInUse(seasonTicket.getId()));
		verify(entryUI).display("Ticket Validated");

		entryController.ticketTaken();
		assertEquals(EntryController.STATE.TAKEN, entryController.getState());

		verify(gate).raise();

		entryController.carEventDetected(entryInsideSensor.getId(), true);
		entryController.carEventDetected(entryOutsideSensor.getId(), false);
		verify(entryUI).display("Idle");
		verify(entryUI).discardTicket();

		assertEquals(EntryController.STATE.ENTERED, entryController.getState());

		entryController.carEventDetected(entryInsideSensor.getId(), false);
		verify(gate).lower();

		assertTrue(carpark.isSeasonTicketInUse(seasonTicket.getId()));
		verify(carpark, times(2)).recordSeasonTicketEntry(seasonTicket.getId());
	}

	@Test
	public void enterCarparkTest() {

		when(entryOutsideSensor.getId()).thenReturn("Out sensor");
		when(gate.isRaised()).thenReturn(true);
		when(entryOutsideSensor.carIsDetected()).thenReturn(true);
		when(entryInsideSensor.getId()).thenReturn("In sensor");

		entryController.carEventDetected(entryOutsideSensor.getId(), true);

		assertEquals(EntryController.STATE.WAITING, entryController.getState());
		verify(entryUI).display("Push Button");

		entryController.buttonPushed();
		assertTrue(carpark.isFull() == false);

		verify(carpark).issueAdhocTicket();
		verify(entryUI).printTicket(anyString(), anyInt(), anyLong(),
		        anyString());
		assertTrue(adhocTicketDAO.getCurrentTickets().size() == 1);

		assertEquals(EntryController.STATE.ISSUED, entryController.getState());
		verify(entryUI).display("Take Ticket");

		entryController.ticketTaken();
		assertEquals(EntryController.STATE.TAKEN, entryController.getState());

		verify(gate).raise();

		entryController.carEventDetected(entryInsideSensor.getId(), true);
		entryController.carEventDetected(entryOutsideSensor.getId(), false);
		verify(entryUI).display("Idle");
		verify(entryUI).discardTicket();

		assertTrue(EntryController.STATE.ENTERED == entryController.getState());

		entryController.carEventDetected(entryInsideSensor.getId(), false);
		verify(carpark).recordAdhocTicketEntry();
		verify(gate).lower();
	}

	@Before
	public void setUp() {

		adhocTicketDAO = new AdhocTicketDAO(new AdhocTicketFactory());

		seasonTicketDAO = new SeasonTicketDAO(new UsageRecordFactory());

		carpark = spy(
		        new Carpark("Carpark Id", 10, adhocTicketDAO, seasonTicketDAO));

		entryController = new EntryController(carpark, gate, entryOutsideSensor,
		        entryInsideSensor, entryUI);
	}
}