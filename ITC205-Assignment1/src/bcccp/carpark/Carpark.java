
package bcccp.carpark;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

	/** The Constant TARRIF_SHORT_STAY. */
	public static final int			TARRIF_SHORT_STAY	= 0;

	/** The Constant TARRIF_LONG_STAY. */
	public static final int			TARRIF_LONG_STAY	= 1;

	/** The Constant DAY_RATE. */
	private static final int		DAY_RATE			= 0;

	/** The Constant NIGHT_RATE. */
	private static final int		NIGHT_RATE			= 1;

	/**
	 * The rates. dimension 1 is the tarrif stay type, dimension 2 is the time
	 * (day||weekday / night||weekend)
	 */
	private static final float[][]	rates				= new float[][]{
	        {8.0f, 6.0f}, {4.0f, 3.0f}};

	/** The current carpark id. */
	private static int				currentCarparkId	= 0;

	/** The observers. */
	private List<ICarparkObserver>	observers;

	/** The Carpark id. */
	private String					carparkId;

	/** The capacity. */
	private int						capacity;

	/** The number of cars parked. */
	private int						numberOfCarsParked;

	/** The adhoc ticket DAO. */
	private final IAdhocTicketDAO	adhocTicketDAO;

	/** The season ticket DAO. */
	private final ISeasonTicketDAO	seasonTicketDAO;

	/** The name. */
	private final String			name;

	/** The is full. */
	private boolean					isFull;

	/** The tarrif type. */
	private int						tarrifType;

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
	public Carpark(String name, int capacity, IAdhocTicketDAO adhocTicketDAO,
	        ISeasonTicketDAO seasonTicketDAO) {
		this.name = name;
		this.setCapacity(capacity);
		this.adhocTicketDAO = adhocTicketDAO;
		this.seasonTicketDAO = seasonTicketDAO;
		this.tarrifType = TARRIF_SHORT_STAY;
		this.carparkId = String.valueOf(currentCarparkId++);
	}

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
	public Carpark(String name, int capacity, IAdhocTicketDAO adhocTicketDAO,
	        ISeasonTicketDAO seasonTicketDAO, int tarrif) {
		this.name = name;
		this.setCapacity(capacity);
		this.adhocTicketDAO = adhocTicketDAO;
		this.seasonTicketDAO = seasonTicketDAO;
		this.tarrifType = tarrif;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.ICarpark#calculateAddHocTicketCharge(long)
	 */
	@Override
	public float calculateAddHocTicketCharge(long entryDateTime) {

		float charge = 0.0f;

		LocalDateTime entryDateTime_ = LocalDateTime.ofInstant(
		        Instant.ofEpochMilli(entryDateTime), ZoneId.systemDefault());

		LocalDateTime now = LocalDateTime.now();

		LocalDateTime next = entryDateTime_.minusDays(1);
		while ((next = next.plusDays(1)).isBefore(now.plusDays(1))) {

			Duration duration;
			float hours;
			LocalDateTime start;
			LocalDateTime end;

			// weekday rules
			if (!next.getDayOfWeek().equals(DayOfWeek.SATURDAY)
			        && !next.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {

				// add the time from 1:00:00:00 to 7:59:59:999999999 at the
				// NIGHT_RATE
				// starting no earlier than the entry time
				start = next.withHour(1).withMinute(0).withSecond(0)
				        .withNano(0);
				start = start.compareTo(entryDateTime_) > 0
				        ? start
				        : entryDateTime_;
				// finishing no later than the current time
				end = next.withHour(7).withMinute(59).withSecond(59)
				        .withNano(999999999);
				end = now.compareTo(end) < 0 ? now : end;

				duration = Duration.between(start, end);
				hours = duration.toHours();
				if (hours > 0f) {
					charge += hours * rates[tarrifType][NIGHT_RATE];
				}

				// add the time from 8:00:00:00 to 18:00:00:00 at the DAY_RATE
				// starting no earlier than the entry time
				start = next.withHour(8).withMinute(0).withSecond(0)
				        .withNano(0);
				start = start.compareTo(entryDateTime_) > 0
				        ? start
				        : entryDateTime_;
				// finishing no later than the current time
				end = next.withHour(18).withMinute(0).withSecond(0).withNano(0);
				end = now.compareTo(end) < 0 ? now : end;

				duration = Duration.between(start, end);
				hours = duration.toHours();
				if (hours > 0f) {
					charge += hours * rates[tarrifType][DAY_RATE];
				}

				// add the time from 18:00:00:000000001 to 23:59:59:999999999 at
				// the NIGHT_RATE
				// starting no earlier than the entry time
				start = next.withHour(18).withMinute(0).withSecond(0)
				        .withNano(1);
				start = start.compareTo(entryDateTime_) > 0
				        ? start
				        : entryDateTime_;
				// finishing no later than the current time
				end = next.withHour(23).withMinute(59).withSecond(59)
				        .withNano(999999999);
				end = now.compareTo(end) < 0 ? now : end;

				duration = Duration.between(start, end);
				hours = duration.toHours();
				if (hours > 0f) {
					charge += hours * rates[tarrifType][NIGHT_RATE];
				}
			} else { // weekend rules

				// add the time from 1:00:00:00 to 23:59:59:999999999 at the
				// NIGHT_RATE
				// starting no earlier than the entry time
				start = next.withHour(1).withMinute(0).withSecond(0)
				        .withNano(0);
				start = start.compareTo(entryDateTime_) < 0
				        ? start
				        : entryDateTime_;
				// finishing no later than the current time
				end = next.withHour(23).withMinute(59).withSecond(59)
				        .withNano(999999999);
				end = now.compareTo(end) < 0 ? now : end;

				duration = Duration.between(start, end);
				hours = duration.toHours();
				if (hours > 0f) {
					charge += hours * rates[tarrifType][NIGHT_RATE];
				}
			}
		}

		return charge;
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
	 * @see bcccp.carpark.ICarpark#deregisterSeasonTicket(bcccp.tickets.season.
	 * ISeasonTicket)
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

	/**
	 * Gets the tarrif type.
	 *
	 * @return the tarrif type
	 */
	public int getTarrifType() {
		return tarrifType;
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
	 * @see bcccp.carpark.ICarpark#isSeasonTicketInUse(java.lang.String)
	 */
	@Override
	public boolean isSeasonTicketInUse(String ticketId) {
		final ISeasonTicket ticket = seasonTicketDAO.findTicketById(ticketId);
		if (ticket != null) {
			return ticket.inUse();
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.ICarpark#isSeasonTicketValid(java.lang.String)
	 */
	@Override
	public boolean isSeasonTicketValid(String ticketId) {
		final ISeasonTicket ticket = seasonTicketDAO.findTicketById(ticketId);
		if (ticket != null) {
			final long now = new Date().getTime();
			return now >= ticket.getStartValidPeriod()
			        && now < ticket.getEndValidPeriod();
		} else {
			return false;
		}
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

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.ICarpark#recordAdhocTicketExit()
	 */
	@Override
	public void recordAdhocTicketExit() {
		setNumberOfCarsParked(getNumberOfCarsParked() - 1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.ICarpark#recordSeasonTicketEntry(java.lang.String)
	 */
	@Override
	public void recordSeasonTicketEntry(String ticketId) {
		final ISeasonTicket ticket = seasonTicketDAO.findTicketById(ticketId);
		// record the ticket usage as entering now
		final IUsageRecord record = new UsageRecord(ticketId,
		        new Date().getTime());
		ticket.recordUsage(record);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.ICarpark#recordSeasonTicketExit(java.lang.String)
	 */
	@Override
	public void recordSeasonTicketExit(String ticketId) {
		final ISeasonTicket ticket = seasonTicketDAO.findTicketById(ticketId);
		// record the ticket usage as exited now
		final IUsageRecord record = ticket.getCurrentUsageRecord();
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

	/*
	 * (non-Javadoc)
	 *
	 * @see bcccp.carpark.ICarpark#registerSeasonTicket(bcccp.tickets.season.
	 * ISeasonTicket)
	 */
	@Override
	public void registerSeasonTicket(ISeasonTicket seasonTicket) {
		seasonTicketDAO.registerTicket(seasonTicket);
	}

	/**
	 * Sets the capacity.
	 *
	 * @param capacity
	 *            the new capacity
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Sets the number of cars parked.
	 *
	 * @param numberOfCarsParked
	 *            the new number of cars parked
	 */
	public void setNumberOfCarsParked(int numberOfCarsParked) {
		this.numberOfCarsParked = numberOfCarsParked;
	}

	/**
	 * Sets the tarrif type.
	 *
	 * @param tarrifType
	 *            the new tarrif type
	 */
	public void setTarrifType(int tarrifType) {
		this.tarrifType = tarrifType;
	}

}