
package bcccp.carpark.exit;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.ICarSensorResponder;
import bcccp.carpark.ICarpark;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.IAdhocTicket;

public class ExitController implements ICarSensorResponder, IExitController {

	private IGate			exitGate;

	private ICarSensor		insideSensor;

	private ICarSensor		outsideSensor;

	private IExitUI			ui;

	private ICarpark		carpark;

	private IAdhocTicket	adhocTicket		= null;

	private long			exitTime;

	private String			seasonTicketId	= null;

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
		this.carpark = carpark;
		this.exitGate = exitGate;
		this.insideSensor = is;
		this.outsideSensor = os;
		this.ui = ui;
		this.ui.registerController(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.ICarSensorResponder#carEventDetected(java.lang.String,
	 * boolean)
	 */
	@Override
	public void carEventDetected(String detectorId, boolean detected) {
		if (detectorId == insideSensor.getId() && detected) {
			ui.display("Please insert ticket.");
		} else if (detectorId == outsideSensor.getId() && detected) {
			exitGate.lower();
			if (adhocTicket != null) {
				carpark.recordAdhocTicketExit();
			} else {
				carpark.recordSeasonTicketExit(seasonTicketId);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.exit.IExitController#ticketInserted(java.lang.String)
	 */
	@Override
	public void ticketInserted(String ticketStr) {
		// adhoc ticket
		if (adhocTicket.getBarcode().equals(ticketStr)) {
			if (adhocTicket.isPaid()) {
				ui.discardTicket();
				ui.display("Please take ticket and exit car park.");
			} else {
				ui.display(
				        "Please return to a Paystation and pay ticket before leaving.");
				// eject ticket
			}

		} else if (seasonTicketId.equals(ticketStr)) { // season ticket
			if (carpark.isSeasonTicketValid(seasonTicketId)) {
				ui.display("Please take season ticket and exit car park.");
			} else {
				ui.display(
				        "Season ticket is invalid. Please see carpark office.");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.exit.IExitController#ticketTaken()
	 */
	@Override
	public void ticketTaken() {
		ui.display("Thank-you. Drive safely.");
	}

}
