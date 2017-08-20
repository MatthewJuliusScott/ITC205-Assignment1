
package bcccp.carpark.entry; // TODO The package statement must be the first statement of the file. All files should belong to a specific package.

import java.util.Date; //TODO The import statements must follow the package statement. import statements should be sorted with the most fundamental packages first, and grouped with associated packages together and one blank line between groups.


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
	private IGate			entryGate_; //TODO Variables should be initialized where they are declared and they should be declared in the smallest scope possible.

	/** The outside sensor. */
	private ICarSensor		outsideSensor_;

	/** The inside sensor. */
	private ICarSensor		insideSensor_;

	/** The ui. */
	private IEntryUI		ui_;

	/** The carpark. */
	private ICarpark		carpark_;

	/** The adhoc ticket. */
	private IAdhocTicket	adhocTicket_		= null;

	/** The entry time. */
	private long			entryTime_;

	/** The season ticket id. */
	private String			seasonTicketId_	= null;

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
		this.carpark_ = carpark;
		this.entryGate_ = entryGate;
		this.outsideSensor_ = os;
		this.outsideSensor_.registerResponder(this);
		this.insideSensor_ = is;
		this.insideSensor_.registerResponder(this);
		this.ui_ = ui;
		ui.registerController(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.entry.IEntryController#buttonPushed()
	 */
	@Override
	public void buttonPushed() {
		adhocTicket_ = carpark_.issueAdhocTicket();
		ui_.printTicket(adhocTicket_.getCarparkId(), adhocTicket_.getTicketNo(), 
		        adhocTicket_.getEntryDateTime(), adhocTicket_.getBarcode()); //TODO The incompleteness of split lines must be made obvious [1].
		ui_.display("Please take ticket");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.ICarSensorResponder#carEventDetected(java.lang.String,
	 * boolean)
	 */
	@Override
	public void carEventDetected(String detectorId, boolean detected) {
		if (detectorId == outsideSensor_.getId() && detected) {
			ui_.display("Please press button.");
		} else if (detectorId == insideSensor_.getId() && detected) {
			entryGate_.lower();
			ui_.display("");
			ui_.discardTicket();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.ICarparkObserver#notifyCarparkEvent()
	 */
	@Override
	public void notifyCarparkEvent() {
		carpark_.notify();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * bcccp.carpark.entry.IEntryController#ticketInserted(java.lang.String)
	 */
	@Override
	public void ticketInserted(String barcode) {
		if (carpark_.isSeasonTicketValid(barcode)
		        && !carpark_.isSeasonTicketInUse(barcode)) {
			seasonTicketId_ = barcode;
			ui_.display("Ticket Valid.");
		} else {
			ui_.display("Invalid Ticket.");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.entry.IEntryController#ticketTaken()
	 */
	@Override
	public void ticketTaken() {
		entryTime_ = new Date().getTime(); //TODO Variables should be kept alive for as short a time as possible.
		if (adhocTicket_ != null) {
			adhocTicket_.enter(new Date().getTime());
			adhocTicket_.enter(entryTime_);
			carpark_.recordAdhocTicketEntry();
			entryGate_.raise();
			ui_.display("Enter");
		} else if (seasonTicketId_ != null) {
			carpark_.recordSeasonTicketEntry(seasonTicketId_);
			ui_.display("Enter");
		}

		adhocTicket_ = null;
		seasonTicketId_ = null;
	}

}
