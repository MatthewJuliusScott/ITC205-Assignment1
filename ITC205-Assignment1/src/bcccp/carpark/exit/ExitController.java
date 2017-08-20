
package bcccp.carpark.exit;

import java.util.Date;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.ICarSensorResponder;
import bcccp.carpark.ICarpark;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.IAdhocTicket;

public class ExitController
implements ICarSensorResponder, IExitController {

	/** The exit gate. */
	private IGate			exitGate_;
	
	/** The inside sensor. */
	private ICarSensor		insideSensor_;

	/** The outside sensor. */
	private ICarSensor		outsideSensor_;

	/** The user interface. */
	private IExitUI			ui_; 

	/** The carpark. */
	private ICarpark		carpark_;

	/** The adhoc ticket. */
	private IAdhocTicket	adhocTicket_		= null;

	/** The exit time. */
	private long			exitTime_;

	/** The season ticket id. */
	private String			seasonTicketId_	= null;

	/**
	 * Instantiates a new exit controller.
	 *
	 * @param carpark
	 *            the carpark
	 * @param exitGate
	 *            the exit gate
	 * @param is
	 *            the inside sensor
	 * @param os
	 *            the outside sensor
	 * @param ui
	 *            the user interface
	 */
	public ExitController(Carpark carpark, IGate exitGate, ICarSensor is,
	        ICarSensor os, IExitUI ui) {
		this.carpark_ = carpark;
		this.exitGate_ = exitGate;
		this.insideSensor_ = is;
		this.insideSensor_.registerResponder(this);
		this.outsideSensor_ = os;
		this.outsideSensor_.registerResponder(this);
		this.ui_ = ui;
		this.ui_.registerController(this);
	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.ICarSensorResponder#carEventDetected(java.lang.String,
	// boolean)
	@Override
	public void carEventDetected(String detectorId, boolean detected) {
		if (detectorId == insideSensor_.getId() && detected) { 
			ui_.display("Please insert ticket.");
		} else if (detectorId == outsideSensor_.getId() && detected) {
			exitGate_.lower();
			ui_.display("");
		}
	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.exit.IExitController#ticketInserted(java.lang.String)
	@Override
	public void ticketInserted(String ticketStr) {
		exitTime_ = new Date().getTime();
		adhocTicket_ = carpark_.getAdhocTicket( ticketStr);
		seasonTicketId_ = ticketStr;

		// adhoc ticket
		if (adhocTicket_ != null) {
			if (adhocTicket_.isPaid()) {
				ui_.display("Please take ticket.");
				exitGate_.raise();
			} else {
				ui_.display("Pay before exiting.");
				// eject ticket
			}

		} else if (carpark_.isSeasonTicketInUse( seasonTicketId_)
		        && carpark_.isSeasonTicketValid( seasonTicketId_)) { // season
			// ticket
			ui_.display("Please take ticket.");
			exitGate_.raise();
		} else {
			ui_.display("Invalid ticket.");
		}

	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.exit.IExitController#ticketTaken()
	@Override
	public void ticketTaken() {

		// if ticket valid
		if (adhocTicket_ != null && adhocTicket_.isPaid()) {
			adhocTicket_.exit( exitTime_);
			carpark_.recordAdhocTicketExit();
			ui_.discardTicket();
		} else if (seasonTicketId_ != null
		        && carpark_.isSeasonTicketValid( seasonTicketId_)) {
			carpark_.recordSeasonTicketExit( seasonTicketId_);
			ui_.discardTicket();
		}

		adhocTicket_ = null;
		seasonTicketId_ = null;
	}

	
	
}
