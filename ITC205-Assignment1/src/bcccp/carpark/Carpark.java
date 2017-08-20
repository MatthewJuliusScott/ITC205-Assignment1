
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
import bcccp.tickets.season.ISeasonTicketDataAccessObject;
import bcccp.tickets.season.IUsageRecord;
import bcccp.tickets.season.UsageRecord;

/**
 * The Class Carpark.
 */
public class Carpark
implements ICarpark { 

	/** The Constant TARRIF_SHORT_STAY. */
	public static final int			TARRIF_SHORT_STAY	= 0;

	/** The Constant TARRIF_LONG_STAY. */
	public static final int			TARRIF_LONG_STAY	= 1;

	/** The Constant DAY_RATE. */
	private static final int		DAY_RATE_			= 0;

	/** The Constant NIGHT_RATE. */
	private static final int		NIGHT_RATE_			= 1;

	/**
	 * The rates. dimension 1 is the tarrif stay type, dimension 2 is the time
	 * (day||weekday / night||weekend)
	 */
	private static final float[][]	rates_				= new float[][]{
	        {8.0f, 6.0f}, {4.0f, 3.0f}};

	/** The current carpark id. */
	private static int				currentCarparkId_	= 0;

	/** The observers. */
	private List<ICarparkObserver>	observers_;

	/** The Carpark id. */
	private String					carparkId_;

	/** The capacity. */
	private int						capacity_;

	/** The number of cars parked. */
	private int						numberOfCarsParked_;

	/** The adhoc ticket DAO. */
	private final IAdhocTicketDAO	adhocTicketDao_;

	/** The season ticket DAO. */
	private final ISeasonTicketDataAccessObject	seasonTicketDao_;

	/** The name. */
	private final String			name_;

	/** The is full. */
	private boolean					isFull_;

	/** The tarrif type. */
	private int						tarrifType_; 

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
	        ISeasonTicketDataAccessObject seasonTicketDAO) {
		this.name_ = name;
		this.setCapacity( capacity);
		this.adhocTicketDao_ = adhocTicketDAO;
		this.seasonTicketDao_ = seasonTicketDAO;
		this.tarrifType_ = TARRIF_SHORT_STAY;
		this.carparkId_ = String.valueOf( currentCarparkId_++);
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
	        ISeasonTicketDataAccessObject seasonTicketDAO, int tarrif) {
		this.name_ = name;
		this.setCapacity( capacity);
		this.adhocTicketDao_ = adhocTicketDAO;
		this.seasonTicketDao_ = seasonTicketDAO;
		this.tarrifType_ = tarrif;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.ICarpark#calculateAddHocTicketCharge(long)
	@Override
	public float calculateAddHocTicketCharge(long entryDateTime) {

		float charge = 0.0f;

		LocalDateTime entryDateTime_ = LocalDateTime.ofInstant(
		        Instant.ofEpochMilli( entryDateTime), ZoneId.systemDefault());

		LocalDateTime now = LocalDateTime.now();

		LocalDateTime next = entryDateTime_.minusDays(1);
		while ((next = next.plusDays(1)).isBefore(now.plusDays(1))) { 

			Duration duration;
			float hours;
			LocalDateTime start;
			LocalDateTime end;

			// weekday rules
			boolean isSaturday = next.getDayOfWeek().equals(DayOfWeek.SATURDAY);
			boolean isSunday = next.getDayOfWeek().equals(DayOfWeek.SUNDAY);
			boolean isWeekend = isSaturday || isSunday;
			if (!isWeekend) {
				// add the time from 1:00:00:00 to 7:59:59:999999999 at the
				// NIGHT_RATE
				// starting no earlier than the entry time
				start = next.withHour(1).withMinute(0).withSecond(0)
				        .withNano(0);
				start = start.compareTo( entryDateTime_) > 0
				        ? start
				        : entryDateTime_;
				// finishing no later than the current time
				end = next.withHour(7).withMinute(59).withSecond(59)
				        .withNano(999999999);
				end = now.compareTo(end) < 0 ? now : end;

				duration = Duration.between(start, end);
				hours = duration.toHours();
				if (hours > 0f) {
					charge += hours * rates_[tarrifType_][NIGHT_RATE_];
				}

				// add the time from 8:00:00:00 to 18:00:00:00 at the DAY_RATE
				// starting no earlier than the entry time
				start = next.withHour(8).withMinute(0).withSecond(0)
				        .withNano(0);
				start = start.compareTo( entryDateTime_) > 0
				        ? start
				        : entryDateTime_;
				// finishing no later than the current time
				end = next.withHour(18).withMinute(0).withSecond(0).withNano(0);
				end = now.compareTo(end) < 0 ? now : end;

				duration = Duration.between( start, end);
				hours = duration.toHours();
				if (hours > 0f) {;   
					charge += hours * rates_[tarrifType_][DAY_RATE_];
				}

				// add the time from 18:00:00:000000001 to 23:59:59:999999999 at
				// the NIGHT_RATE
				// starting no earlier than the entry time
				start = next.withHour(18).withMinute(0).withSecond(0)
				        .withNano(1);
				start = start.compareTo( entryDateTime_) > 0
				        ? start
				        : entryDateTime_;
				// finishing no later than the current time
				end = next.withHour(23).withMinute(59).withSecond(59)
				        .withNano(999999999);
				end = now.compareTo(end) < 0 ? now : end;

				duration = Duration.between( start, end);
				hours = duration.toHours();
				if (hours > 0f) {
					charge += hours * rates_[tarrifType_][NIGHT_RATE_];
				}
			} else { // weekend rules

				// add the time from 1:00:00:00 to 23:59:59:999999999 at the
				// NIGHT_RATE
				// starting no earlier than the entry time
				start = next.withHour(1).withMinute(0).withSecond(0)
				        .withNano(0);
				start = start.compareTo( entryDateTime_) < 0
				        ? start
				        : entryDateTime_;
				// finishing no later than the current time
				end = next.withHour(23).withMinute(59).withSecond(59)
				        .withNano(999999999);
				end = now.compareTo(end) < 0 ? now : end;

				duration = Duration.between( start, end);
				hours = duration.toHours();
				if (hours > 0f) {
					charge += hours * rates_[tarrifType_][NIGHT_RATE_];
				}
			}
		}

		return charge;
	}

	
	
	//(non-Javadoc)
	// @see bcccp.carpark.ICarpark#deregister(bcccp.carpark.ICarparkObserver)
	@Override
	public void deregister(ICarparkObserver observer) {
		if (observers_ != null) {
			observers_.remove( observer);
		}
	}

	// (non-Javadoc)
	// @see bcccp.carpark.ICarpark#deregisterSeasonTicket(bcccp.tickets.season.
	// ISeasonTicket)
	@Override
	public void deregisterSeasonTicket(ISeasonTicket seasonTicket) {
		seasonTicketDao_.deregisterTicket( seasonTicket);
	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.ICarpark#getAdhocTicket(java.lang.String)
	@Override
	public IAdhocTicket getAdhocTicket(String barcode) {
		return adhocTicketDao_.findTicketByBarcode( barcode);
	}

	
	
	/**
	 * Gets the capacity.
	 *
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity_;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.ICarpark#getName()
	@Override
	public String getName() {
		return name_;
	}

	
	
	/**
	 * Gets the number of cars parked.
	 *
	 * @return the number of cars parked
	 */
	public int getNumberOfCarsParked() {
		return numberOfCarsParked_;
	}

	
	
	/**
	 * Gets the tarrif type.
	 *
	 * @return the tarrif type
	 */
	public int getTarrifType() {
		return tarrifType_;
	}

	
	
 	// (non-Javadoc)
	// @see bcccp.carpark.ICarpark#isFull()
	@Override
	public boolean isFull() {
		return isFull_;
	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.ICarpark#isSeasonTicketInUse(java.lang.String)
	@Override
	public boolean isSeasonTicketInUse(String ticketId) {
		final ISeasonTicket ticket = seasonTicketDao_.findTicketById( ticketId);
		if (ticket != null) {
			return ticket.inUse();
		} else {
			return false;
		}
	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.ICarpark#isSeasonTicketValid(java.lang.String)
	@Override
	public boolean isSeasonTicketValid(String ticketId) {
		final ISeasonTicket ticket = seasonTicketDao_.findTicketById( ticketId);
		if (ticket != null) {
			final long now = new Date().getTime();
			return now >= ticket.getStartValidPeriod()
			        && now < ticket.getEndValidPeriod();
		} else {
			return false;
		}
	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.ICarpark#issueAdhocTicket()
	@Override
	public IAdhocTicket issueAdhocTicket() {
		return adhocTicketDao_.createTicket( carparkId_);
	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.ICarpark#recordAdhocTicketEntry()
	@Override
	public void recordAdhocTicketEntry() {
		setNumberOfCarsParked(getNumberOfCarsParked() + 1);
	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.ICarpark#recordAdhocTicketExit()
	@Override
	public void recordAdhocTicketExit() {
		setNumberOfCarsParked(getNumberOfCarsParked() - 1);
	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.ICarpark#recordSeasonTicketEntry(java.lang.String)
	@Override
	public void recordSeasonTicketEntry(String ticketId) {
		final ISeasonTicket ticket = seasonTicketDao_.findTicketById( ticketId);
		// record the ticket usage as entering now
		final IUsageRecord record = new UsageRecord(ticketId,
		        new Date().getTime());
		ticket.recordUsage(record);
	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.ICarpark#recordSeasonTicketExit(java.lang.String)
	@Override
	public void recordSeasonTicketExit(String ticketId) {
		final ISeasonTicket ticket = seasonTicketDao_.findTicketById( ticketId);
		// record the ticket usage as exited now
		final IUsageRecord record = ticket.getCurrentUsageRecord();
		record.finalise(new Date().getTime());
		ticket.recordUsage(record);
	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.ICarpark#register(bcccp.carpark.ICarparkObserver)
	@Override
	public void register(ICarparkObserver observer) {
		if (observers_ == null) {
			observers_ = new ArrayList<ICarparkObserver>();
		}
		observers_.add(observer);
	}

	
	
	// (non-Javadoc)
	// @see bcccp.carpark.ICarpark#registerSeasonTicket(bcccp.tickets.season.
	// ISeasonTicket)
	@Override
	public void registerSeasonTicket(ISeasonTicket seasonTicket) {
		seasonTicketDao_.registerTicket( seasonTicket);
	}

	
	
	/**
	 * Sets the capacity.
	 *
	 * @param capacity
	 *            the new capacity
	 */
	public void setCapacity(int capacity) {
		this.capacity_ = capacity;
	}

	
	
	/**
	 * Sets the number of cars parked.
	 *
	 * @param numberOfCarsParked
	 *            the new number of cars parked
	 */
	public void setNumberOfCarsParked(int numberOfCarsParked) {
		this.numberOfCarsParked_ = numberOfCarsParked;
	}

	
	
	/**
	 * Sets the tarrif type.
	 *
	 * @param tarrifType
	 *            the new tarrif type
	 */
	public void setTarrifType(int tarrifType) {
		this.tarrifType_ = tarrifType;
	}

	
	
}