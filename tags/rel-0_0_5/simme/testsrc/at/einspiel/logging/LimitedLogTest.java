// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: LimitedLogTest.java
//                  $Date: 2004/08/12 21:56:08 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.logging;

import java.util.Random;

import junit.framework.TestCase;

/**
 * Class to test {@linkplain at.einspiel.logging.LimitedLog}.
 * 
 * @author kariem
 */
public class LimitedLogTest extends TestCase {

	private static final int LENGTH = 1000;

	private LimitedLog log;
	private Random random;

	/** @see junit.framework.TestCase#setUp() */
	protected void setUp() throws Exception {
		log = new LimitedLog(LENGTH);
		random = new Random();
	}

	/** Test append with automatic resize */
	public final void testAppend() {
		// add some information to the log
		for (int i = 0; i < LENGTH * 3; i++) {
			// append to log
			log.append(getSingleLetter().toString());
		}

		// add the final messages to the log, only these should remain (most
		// current)
		StringBuffer comparisonBuffer = new StringBuffer();
		for (int i = 0; i < LENGTH; i++) {
			String current = getSingleLetter().toString();
			// append to log
			log.append(current);
			// append to comparison buffer
			comparisonBuffer.append(current);
		}

		// content of comparison buffer and log should be equal
		assertEquals(log.getContent(), comparisonBuffer.toString());
	}

	/**
	 * Returns a single byte.
	 * 
	 * @return a randomly generated single byte.
	 */
	private Character getSingleLetter() {
		return new Character((char) (random.nextDouble() * 26 + 'A'));
	}
}