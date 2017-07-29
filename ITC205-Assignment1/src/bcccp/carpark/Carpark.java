package bcccp.carpark;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.ISeasonTicketDAO;
import bcccp.tickets.season.IUsageRecord;
import bcccp.tickets.season.UsageRecord;

/**
 * The Class Carpark.
 */
public class Carpark implements ICarpark {

	/** The observers. */
	private List<ICarparkObserver> observers;
	
	/** The Carpark id. */
	private String carparkId;
	
	/** The capacity. */
	private int capacity;
	
	/** The number of cars parked. */
	private int numberOfCarsParked;
	
	/** The adhoc ticket DAO. */
	private IAdhocTicketDAO adhocTicketDAO;
	
	/** The season ticket DAO. */
	private ISeasonTicketDAO seasonTicketDAO;
	
	/** The name. */
	private String name;
	
	/** The is full. */
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
		this.setCapacity(capacity);
		this.adhocTicketDAO = adhocTicketDAO;
		this.seasonTicketDAO = seasonTicketDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bcccp.carpark.ICarpark#calculateAddHocTicketCharge(long)
	 */
	@Override
	public float calculateAddHocTicketCharge(long entryDateTime) {
		// This doesn't calculate properly as yet, as each car park should have a
		// tarrif type (short or long stay), a day rate and night / weekend rate, and
		// needs to know the exit time.
		// for testing purposes it currently returns a generic value
		return 10.0f;
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

	/* (non-Javadoc)
	 * @see bcccp.carpark.ICarpark#deregisterSeasonTicket(bcccp.tickets.season.ISeasonTicket)
	 */
	@Override
	public void deregisterSeasonTicket(ISeasonTicket seasonTicket) {
		seasonTicketDAO.deregisterTicket(seasonTicket);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bcccp.carpark.ICarpark#getAdhocTicket(java.lang.String)
	 */
	@Override
	public IAdhocTicket getAdhocTicket(String barcode) {
		return adhocTicketDAO.findTicketByBarcode(barcode);
	}

	public int getCapacity() {
		return capacity;
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

	public int getNumberOfCarsParked() {
		return numberOfCarsParked;
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

	/* (non-Javadoc)
	 * @see bcccp.carpark.ICarpark#isSeasonTicketInUse(java.lang.String)
	 */
	@Override
	public boolean isSeasonTicketInUse(String ticketId) {
		ISeasonTicket ticket = seasonTicketDAO.findTicketById(ticketId);
		return ticket.inUse();
	}

	/* (non-Javadoc)
	 * @see bcccp.carpark.ICarpark#isSeasonTicketValid(java.lang.String)
	 */
	@Override
	public boolean isSeasonTicketValid(String ticketId) {
		ISeasonTicket ticket = seasonTicketDAO.findTicketById(ticketId);
		long now = new Date().getTime();
		return now >= ticket.getStartValidPeriod() && now < ticket.getEndValidPeriod();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see bcccp.carpark.ICarpark#recordAdhocTicketEntry()
	 */
	@Override
	public void recordAdhocTicketEntry() {
		setNumberOfCarsParked(getNumberOfCarsParked() + 1);
	}

	/* (non-Javadoc)
	 * @see bcccp.carpark.ICarpark#recordAdhocTicketExit()
	 */
	@Override
	public void recordAdhocTicketExit() {
		setNumberOfCarsParked(getNumberOfCarsParked() - 1);
	}

	/* (non-Javadoc)
	 * @see bcccp.carpark.ICarpark#recordSeasonTicketEntry(java.lang.String)
	 */
	@Override
	public void recordSeasonTicketEntry(String ticketId) {
		ISeasonTicket ticket = seasonTicketDAO.findTicketById(ticketId);
		// record the ticket usage as entering now
		IUsageRecord record = new UsageRecord(ticketId, new Date().getTime());
		ticket.recordUsage(record);
	}

	/* (non-Javadoc)
	 * @see bcccp.carpark.ICarpark#recordSeasonTicketExit(java.lang.String)
	 */
	@Override
	public void recordSeasonTicketExit(String ticketId) {
		ISeasonTicket ticket = seasonTicketDAO.findTicketById(ticketId);
		// record the ticket usage as exited now
		IUsageRecord record = ticket.getCurrentUsageRecord();
		record.finalise(new Date().getTime());
		ticket.recordUsage(record);
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

	/* (non-Javadoc)
	 * @see bcccp.carpark.ICarpark#registerSeasonTicket(bcccp.tickets.season.ISeasonTicket)
	 */
	@Override
	public void registerSeasonTicket(ISeasonTicket seasonTicket) {
		seasonTicketDAO.registerTicket(seasonTicket);
	}

	/**
	 * Sets the capacity.
	 *
	 * @param capacity the new capacity
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Sets the number of cars parked.
	 *
	 * @param numberOfCarsParked the new number of cars parked
	 */
	public void setNumberOfCarsParked(int numberOfCarsParked) {
		this.numberOfCarsParked = numberOfCarsParked;
	}

}
