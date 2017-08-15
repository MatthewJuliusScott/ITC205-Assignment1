
package bcccp.carpark.entry;

import java.util.Date;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.ICarSensorResponder;
import bcccp.carpark.ICarpark;
import bcccp.carpark.ICarparkObserver;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.IAdhocTicket;

public class EntryController
        implements
            ICarSensorResponder,
            ICarparkObserver,
            IEntryController {

	private IGate			entryGate;

	private ICarSensor		outsideSensor;

	private ICarSensor		insideSensor;

	private IEntryUI		ui;

	private ICarpark		carpark;

	private IAdhocTicket	adhocTicket		= null;

	private long			entryTime;

	private String			seasonTicketId	= null;

	public EntryController(Carpark carpark, IGate entryGate, ICarSensor os,
	        ICarSensor is, IEntryUI ui) {
		this.carpark = carpark;
		this.entryGate = entryGate;
		this.outsideSensor = os;
		this.insideSensor = is;
		this.ui = ui;
		ui.registerController(this);
	}

	@Override
	public void buttonPushed() {
		adhocTicket = carpark.issueAdhocTicket();
		ui.display(adhocTicket.getBarcode());
	}

	@Override
	public void carEventDetected(String detectorId, boolean detected) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyCarparkEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ticketInserted(String barcode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ticketTaken() {
		if (adhocTicket != null) {
			adhocTicket.enter(new Date().getTime());
			carpark.recordAdhocTicketEntry();
			ui.display("Thank-you. Drive safely.");
		} else if (seasonTicketId != null) {
			carpark.recordSeasonTicketEntry(seasonTicketId);
			ui.display("Thank-you. Drive safely.");
		}

		adhocTicket = null;
		seasonTicketId = null;
	}

}
