package bcccp.carpark.entry;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import bcccp.carpark.CarSensor;
import bcccp.carpark.Carpark;
import bcccp.carpark.Gate;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.ICarpark;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.AdhocTicket;
import bcccp.tickets.adhoc.IAdhocTicket;

public class TestEntryController {

	private ICarpark carpark;
	private IGate gate;
	private ICarSensor outsideSensor;
	private ICarSensor insideSensor;
	private IEntryUI entryUI;
	private IAdhocTicket adhocTicket;

	private EntryController controller;

	@Before
	public void setUp() {

		carpark = mock(Carpark.class);
		gate = mock(Gate.class);
		outsideSensor = mock(CarSensor.class);
		insideSensor = mock(CarSensor.class);
		entryUI = mock(EntryUI.class);
		adhocTicket = mock(AdhocTicket.class);

		when(carpark.getAdhocTicket("barcode")).thenReturn(adhocTicket);
		when(carpark.issueAdhocTicket()).thenReturn(adhocTicket);
		when(adhocTicket.getCarparkId()).thenReturn("carparkId");
		when(adhocTicket.getBarcode()).thenReturn("barcode");
		when(adhocTicket.getTicketNo()).thenReturn(1);
		when(adhocTicket.getEntryDateTime()).thenReturn(1L);
		when(carpark.isFull()).thenReturn(false);
		when(outsideSensor.getId()).thenReturn("OutSensor");
		when(insideSensor.getId()).thenReturn("InSensor");

		controller = new EntryController((Carpark) carpark, gate, outsideSensor, insideSensor, entryUI);
	}

	@Test
	public void testConstructor() {

		assertEquals(EntryController.STATE.IDLE, controller.getState());
		verify(carpark).register(controller);
		verify(outsideSensor).registerResponder(controller);
		verify(insideSensor).registerResponder(controller);
		verify(entryUI).registerController(controller);

	}

	@Test
	public void testEnterCarparkWithAdhocTicket() {

		when(outsideSensor.carIsDetected()).thenReturn(true);
		when(gate.isRaised()).thenReturn(true);

		assertEquals(EntryController.STATE.IDLE, controller.getState());

		controller.carEventDetected("OutSensor", true);
		verify(entryUI).display("Push Button");
		assertEquals(EntryController.STATE.WAITING, controller.getState());

		controller.buttonPushed();
		verify(carpark).issueAdhocTicket();
		assertEquals(EntryController.STATE.ISSUED, controller.getState());

		controller.ticketTaken();
		verify(gate).raise();
		assertEquals(EntryController.STATE.TAKEN, controller.getState());

		controller.carEventDetected("InSensor", true);
		assertEquals(EntryController.STATE.ENTERING, controller.getState());

		controller.carEventDetected("OutSensor", false);
		assertEquals(EntryController.STATE.ENTERED, controller.getState());

		when(outsideSensor.carIsDetected()).thenReturn(false);
		controller.carEventDetected("InSensor", false);
		assertEquals(EntryController.STATE.IDLE, controller.getState());
		verify(carpark).recordAdhocTicketEntry();
		verify(gate).lower();
		verify(adhocTicket).enter(anyLong());

	}

	@Test
	public void testEnterCarparkWithSeasonTicket() {

		when(carpark.isSeasonTicketValid("barcode")).thenReturn(true);
		when(carpark.isSeasonTicketInUse("barcode")).thenReturn(false);
		when(outsideSensor.carIsDetected()).thenReturn(true);
		when(gate.isRaised()).thenReturn(true);

		assertEquals(EntryController.STATE.IDLE, controller.getState());

		controller.carEventDetected("OutSensor", true);
		verify(entryUI).display("Push Button");
		assertEquals(EntryController.STATE.WAITING, controller.getState());

		controller.ticketInserted("barcode");
		verify(entryUI).display("Ticket Validated");
		assertEquals(EntryController.STATE.VALIDATED, controller.getState());

		controller.ticketTaken();
		verify(gate).raise();
		assertEquals(EntryController.STATE.TAKEN, controller.getState());

		controller.carEventDetected("InSensor", true);
		assertEquals(EntryController.STATE.ENTERING, controller.getState());

		controller.carEventDetected("OutSensor", false);
		assertEquals(EntryController.STATE.ENTERED, controller.getState());

		when(outsideSensor.carIsDetected()).thenReturn(false);

		controller.carEventDetected("InSensor", false);
		assertEquals(EntryController.STATE.IDLE, controller.getState());
		verify(gate).lower();
		verify(carpark).recordSeasonTicketEntry("barcode");
	}
}