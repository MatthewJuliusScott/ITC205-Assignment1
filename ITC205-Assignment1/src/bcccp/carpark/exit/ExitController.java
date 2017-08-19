
package bcccp.carpark.exit;

import java.util.Date;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.ICarSensorResponder;
import bcccp.carpark.ICarpark;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.IAdhocTicket;

<<<<<<< HEAD
public class ExitController implements ICarSensorResponder, IExitController { //TODO Rule 63 Interface declarations should be on next line
=======
public class ExitController implements ICarSensorResponder, IExitController {
>>>>>>> branch 'master' of https://github.com/MatthewJuliusScott/ITC205-Team-Repository.git

	private IGate			exitGate;
<<<<<<< HEAD
=======

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
		this.insideSensor.registerResponder(this);
		this.outsideSensor = os;
		this.outsideSensor.registerResponder(this);
		this.ui = ui;
		this.ui.registerController(this);
	}
>>>>>>> branch 'master' of https://github.com/MatthewJuliusScott/ITC205-Team-Repository.git

<<<<<<< HEAD
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
		this.insideSensor.registerResponder(this);
		this.outsideSensor = os;
		this.outsideSensor.registerResponder(this);
		this.ui = ui;
		this.ui.registerController(this);
	} //TODO Methods should be separated by three blank lines. 

	/*
	 * (non-Javadoc) //TODO Rule 83 Use // for non Java Doc comments 
	 *
	 * @see bcccp.carpark.ICarSensorResponder#carEventDetected(java.lang.String,
	 * boolean)
	 */  
=======
	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.ICarSensorResponder#carEventDetected(java.lang.String,
	 * boolean)
	 */
>>>>>>> branch 'master' of https://github.com/MatthewJuliusScott/ITC205-Team-Repository.git
	@Override
	public void carEventDetected(String detectorId, boolean detected) {
<<<<<<< HEAD
		if (detectorId == insideSensor.getId() && detected) { 
			ui.display("Please insert ticket."); //TODO First parenthesis in methods can have a space before them (Rule 74).
=======
		if (detectorId == insideSensor.getId() && detected) {
			ui.display("Please insert ticket.");
>>>>>>> branch 'master' of https://github.com/MatthewJuliusScott/ITC205-Team-Repository.git
		} else if (detectorId == outsideSensor.getId() && detected) {
			exitGate.lower();
			ui.display("");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.exit.IExitController#ticketInserted(java.lang.String)
	 */
	@Override
	public void ticketInserted(String ticketStr) {
		exitTime = new Date().getTime();
		adhocTicket = carpark.getAdhocTicket(ticketStr);
		seasonTicketId = ticketStr;

		// adhoc ticket
		if (adhocTicket != null) {
			if (adhocTicket.isPaid()) {
				ui.display("Please take ticket.");
				exitGate.raise();
			} else {
				ui.display("Pay before exiting.");
				// eject ticket
			}

		} else if (carpark.isSeasonTicketInUse(seasonTicketId)
		        && carpark.isSeasonTicketValid(seasonTicketId)) { // season
			// ticket
			ui.display("Please take ticket.");
			exitGate.raise();
		} else {
			ui.display("Invalid ticket.");
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.exit.IExitController#ticketTaken()
	 */
	@Override
	public void ticketTaken() {

		// if ticket valid
		if (adhocTicket != null && adhocTicket.isPaid()) {
			adhocTicket.exit(exitTime);
			carpark.recordAdhocTicketExit();
			ui.discardTicket();
		} else if (seasonTicketId != null
		        && carpark.isSeasonTicketValid(seasonTicketId)) {
			carpark.recordSeasonTicketExit(seasonTicketId);
			ui.discardTicket();
		}

		adhocTicket = null;
		seasonTicketId = null;
	}

}
