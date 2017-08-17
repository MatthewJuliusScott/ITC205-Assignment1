
package bcccp.carpark.entry;

import java.util.Date;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.ICarSensorResponder;
import bcccp.carpark.ICarpark;
import bcccp.carpark.ICarparkObserver;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.IAdhocTicket;

/**
 * The Class EntryController.
 */
public class EntryController
        implements
            ICarSensorResponder,
            ICarparkObserver,
            IEntryController {

	/** The entry gate. */
	private IGate			entryGate;

	/** The outside sensor. */
	private ICarSensor		outsideSensor;

	/** The inside sensor. */
	private ICarSensor		insideSensor;

	/** The ui. */
	private IEntryUI		ui;

	/** The carpark. */
	private ICarpark		carpark;

	/** The adhoc ticket. */
	private IAdhocTicket	adhocTicket		= null;

	/** The entry time. */
	private long			entryTime;

	/** The season ticket id. */
	private String			seasonTicketId	= null;

	/**
	 * Instantiates a new entry controller.
	 *
	 * @param carpark
	 *            the carpark
	 * @param entryGate
	 *            the entry gate
	 * @param os
	 *            the os
	 * @param is
	 *            the is
	 * @param ui
	 *            the ui
	 */
	public EntryController(Carpark carpark, IGate entryGate, ICarSensor os,
	        ICarSensor is, IEntryUI ui) {
		this.carpark = carpark;
		this.entryGate = entryGate;
		this.outsideSensor = os;
		this.outsideSensor.registerResponder(this);
		this.insideSensor = is;
		this.insideSensor.registerResponder(this);
		this.ui = ui;
		ui.registerController(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.entry.IEntryController#buttonPushed()
	 */
	@Override
	public void buttonPushed() {
		adhocTicket = carpark.issueAdhocTicket();
		ui.printTicket(adhocTicket.getCarparkId(), adhocTicket.getTicketNo(),
		        adhocTicket.getEntryDateTime(), adhocTicket.getBarcode());
		ui.display("Please take ticket");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.ICarSensorResponder#carEventDetected(java.lang.String,
	 * boolean)
	 */
	@Override
	public void carEventDetected(String detectorId, boolean detected) {
		if (detectorId == outsideSensor.getId() && detected) {
			ui.display("Please press button.");
		} else if (detectorId == insideSensor.getId() && detected) {
			entryGate.lower();
			ui.display("");
			ui.discardTicket();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.ICarparkObserver#notifyCarparkEvent()
	 */
	@Override
	public void notifyCarparkEvent() {
		carpark.notify();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * bcccp.carpark.entry.IEntryController#ticketInserted(java.lang.String)
	 */
	@Override
	public void ticketInserted(String barcode) {
		if (carpark.isSeasonTicketValid(barcode)
		        && !carpark.isSeasonTicketInUse(barcode)) {
			seasonTicketId = barcode;
			ui.display("Ticket Valid.");
		} else {
			ui.display("Invalid Ticket.");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.entry.IEntryController#ticketTaken()
	 */
	@Override
	public void ticketTaken() {
		entryTime = new Date().getTime();
		if (adhocTicket != null) {
			adhocTicket.enter(new Date().getTime());
			adhocTicket.enter(entryTime);
			carpark.recordAdhocTicketEntry();
			entryGate.raise();
			ui.display("Enter");
		} else if (seasonTicketId != null) {
			carpark.recordSeasonTicketEntry(seasonTicketId);
			ui.display("Enter");
		}

		adhocTicket = null;
		seasonTicketId = null;
	}

}
