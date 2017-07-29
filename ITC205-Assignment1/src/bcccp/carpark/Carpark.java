package bcccp.carpark;

import java.util.ArrayList;
import java.util.List;

import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.ISeasonTicketDAO;

public class Carpark implements ICarpark {

	private List<ICarparkObserver> observers;
	private String carparkId;
	private int capacity;
	private int numberOfCarsParked;
	private IAdhocTicketDAO adhocTicketDAO;
	private ISeasonTicketDAO seasonTicketDAO;
	private String name;
	private boolean isFull;

	/**
	 * Instantiates a new carpark.
	 *
	 * @param name
	 *            the name
	 * @param capacity
	 *            the capacity
	 * @param adhocTicketDAO
	 *            the adhoc ticket DAO
	 * @param seasonTicketDAO
	 *            the season ticket DAO
	 */
	public Carpark(String name, int capacity, IAdhocTicketDAO adhocTicketDAO, ISeasonTicketDAO seasonTicketDAO) {
		this.name = name;
		this.capacity = capacity;
		this.adhocTicketDAO = adhocTicketDAO;
		this.seasonTicketDAO = seasonTicketDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bcccp.carpark.ICarpark#register(bcccp.carpark.ICarparkObserver)
	 */
	@Override
	public void register(ICarparkObserver observer) {
		if (observers == null) {
			observers = new ArrayList<ICarparkObserver>();
		}
		observers.add(observer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bcccp.carpark.ICarpark#deregister(bcccp.carpark.ICarparkObserver)
	 */
	@Override
	public void deregister(ICarparkObserver observer) {
		if (observers != null) {
			observers.remove(observer);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bcccp.carpark.ICarpark#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bcccp.carpark.ICarpark#isFull()
	 */
	@Override
	public boolean isFull() {
		return isFull;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bcccp.carpark.ICarpark#issueAdhocTicket()
	 */
	@Override
	public IAdhocTicket issueAdhocTicket() {
		return adhocTicketDAO.createTicket(carparkId);
	}

	/* (non-Javadoc)
	 * @see bcccp.carpark.ICarpark#recordAdhocTicketEntry()
	 */
	@Override
	public void recordAdhocTicketEntry() {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see bcccp.carpark.ICarpark#getAdhocTicket(java.lang.String)
	 */
	@Override
	public IAdhocTicket getAdhocTicket(String barcode) {
		// TODO check interface implementation, exceptions etc. what happens if ticket doesnt exist?
		return adhocTicketDAO.findTicketByBarcode(barcode);
	}

	@Override
	public float calculateAddHocTicketCharge(long entryDateTime) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void recordAdhocTicketExit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerSeasonTicket(ISeasonTicket seasonTicket) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deregisterSeasonTicket(ISeasonTicket seasonTicket) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSeasonTicketValid(String ticketId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSeasonTicketInUse(String ticketId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void recordSeasonTicketEntry(String ticketId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void recordSeasonTicketExit(String ticketId) {
		// TODO Auto-generated method stub

	}

}
