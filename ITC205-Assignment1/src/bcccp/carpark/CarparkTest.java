/**
 * 
 */

package bcccp.carpark;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.season.ISeasonTicketDAO;

/**
 * @author Matthew
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CarparkTest {

	@Mock
	private IAdhocTicketDAO		adhocTicketDAO;

	@Mock
	private int					capacity;

	@Mock
	private String				name;

	@Mock
	private ISeasonTicketDAO	seasonTicketDAO;

	@InjectMocks
	private Carpark				carpark;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#Carpark(java.lang.String, int, bcccp.tickets.adhoc.IAdhocTicketDAO, bcccp.tickets.season.ISeasonTicketDAO)}.
	 */
	@Test
	public final void testCarpark() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#register(bcccp.carpark.ICarparkObserver)}.
	 */
	@Test
	public final void testRegister() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#deregister(bcccp.carpark.ICarparkObserver)}.
	 */
	@Test
	public final void testDeregister() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#getName()}.
	 */
	@Test
	public final void testGetName() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#isFull()}.
	 */
	@Test
	public final void testIsFull() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#issueAdhocTicket()}.
	 */
	@Test
	public final void testIssueAdhocTicket() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#getAdhocTicket(java.lang.String)}.
	 */
	@Test
	public final void testGetAdhocTicket() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#calculateAddHocTicketCharge(long)}.
	 */
	@Test
	public final void testCalculateAddHocTicketCharge() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#isSeasonTicketValid(java.lang.String)}.
	 */
	@Test
	public final void testIsSeasonTicketValid() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#registerSeasonTicket(bcccp.tickets.season.ISeasonTicket)}.
	 */
	@Test
	public final void testRegisterSeasonTicket() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#deregisterSeasonTicket(bcccp.tickets.season.ISeasonTicket)}.
	 */
	@Test
	public final void testDeregisterSeasonTicket() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#recordSeasonTicketEntry(java.lang.String)}.
	 */
	@Test
	public final void testRecordSeasonTicketEntry() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#recordAdhocTicketEntry()}.
	 */
	@Test
	public final void testRecordAdhocTicketEntry() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#recordAdhocTicketExit()}.
	 */
	@Test
	public final void testRecordAdhocTicketExit() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#recordSeasonTicketExit(java.lang.String)}.
	 */
	@Test
	public final void testRecordSeasonTicketExit() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link bcccp.carpark.Carpark#isSeasonTicketInUse(java.lang.String)}.
	 */
	@Test
	public final void testIsSeasonTicketInUse() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#Object()}.
	 */
	@Test
	public final void testObject() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#getClass()}.
	 */
	@Test
	public final void testGetClass() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#hashCode()}.
	 */
	@Test
	public final void testHashCode() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#equals(java.lang.Object)}.
	 */
	@Test
	public final void testEquals() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#clone()}.
	 */
	@Test
	public final void testClone() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#toString()}.
	 */
	@Test
	public final void testToString() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#notify()}.
	 */
	@Test
	public final void testNotify() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#notifyAll()}.
	 */
	@Test
	public final void testNotifyAll() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#wait(long)}.
	 */
	@Test
	public final void testWait() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#finalize()}.
	 */
	@Test
	public final void testFinalize() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

}
