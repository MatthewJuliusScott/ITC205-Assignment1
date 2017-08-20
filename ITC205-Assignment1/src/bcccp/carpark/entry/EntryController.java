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
	private IGate			entryGate_ = null; 

	/** The outside sensor. */
	private ICarSensor		outsideSensor_ = null;

	/** The inside sensor. */
	private ICarSensor		insideSensor_ = null;

	/** The ui. */
	private IEntryUI		ui_ = null;

	/** The carpark. */
	private ICarpark		carpark_ = null;

	/** The adhoc ticket. */
	private IAdhocTicket	adhocTicket_ = null;

	/** The entry time. */
	private long			entryTime_ = 0l;

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
						adhocTicket_.getEntryDateTime(), adhocTicket_.getBarcode()); 
		
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
		if (adhocTicket_ != null) {
			adhocTicket_.enter(new Date().getTime());
			entryTime_ = new Date().getTime(); 
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
