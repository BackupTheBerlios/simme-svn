// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: Level.java
//                  $Date: 2004/09/14 22:28:56 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.logging;

/**
 * Define the logging levels we need. Adapted from the java.util.logging.Level
 * class. Adapted from Eric Giguere (April 29, 2002):
 * http://developers.sun.com/techtopics/mobility/midp/ttips/logging/
 * 
 * @author Kariem
 */
public class Level {

	private int lvl;
	private String toString;

	
	/** Fine logging level. */
	public static final Level FINE = new Level(1, "FINE");
	/** Info logging level. */
	public static final Level INFO = new Level(2, "INFO");
	/** Warning logging level. */
	public static final Level WARNING = new Level(3, "WARNING");
	/** Severe logging level. */
	public static final Level SEVERE = new Level(4, "SEVERE");
	/** Logging turned off. */ 
	public static final Level OFF = new Level(100, "OFF");

	private Level(int level, String name) {
		lvl = level;
		this.toString = "[" + name + "]";
	}

	/**
	 * Returns the integer value of the level.
	 * @return a numeric representation of the log level.
	 */
	public int intVal() {
		return lvl;
	}

	/** @see java.lang.Object#toString() */
	public String toString() {
		return toString;
	}

}