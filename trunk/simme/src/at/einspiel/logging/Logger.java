// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: Logger.java
//                  $Date: 2004/09/14 22:28:56 $
//              $Revision: 1.6 $
// ----------------------------------------------------------------------------
package at.einspiel.logging;

import java.util.Hashtable;

/**
 * A simple logging class loosely adapted from the java.util.logging.Logger
 * class. Adapted from Eric Giguere (April 29, 2002):
 * http://developers.sun.com/techtopics/mobility/midp/ttips/logging/
 * 
 * @author Kariem
 */
public class Logger {

	private Level lvl;
	private String name;
	private Logger parent;
	private int version;

	// root logger
	private static Logger root = new Logger("");
	// set of loggers
	private static Hashtable loggers = new Hashtable();

	static { // Initialize the root level
		loggers.put("", root);
		root.setParent(null);
		root.setLevel(Level.FINE);
	}

	private Logger(String name) {
		this.name = name;
	}

	/**
	 * Finds the logger's parent by parsing the name into parts separated by '.'
	 * @param childName
	 *            the name of the child logger in question.
	 * @return the parent logger
	 */
	private Logger findParent(String childName) {
		if (childName == null || childName.length() == 0) {
			return null;
		}

		Logger p;

		synchronized (loggers) {
			while (true) {
				int pos = childName.lastIndexOf('.');
				if (pos < 0) {
					p = root;
					break;
				}

				String pname = childName.substring(0, pos);
				p = (Logger) loggers.get(pname);
				if (p != null)
					break;

				childName = pname;
			}
		}

		return p;
	}

	/**
	 * Get this logger's level.
	 * @return the logger's level, or <code>null</code> if no level has been
	 *         set.
	 */
	public Level getLevel() {
		return lvl;
	}

	/**
	 * Find the logger registered with a specific name.
	 * @param name
	 *            the logger's name
	 * @return the logger registered with <code>name</code>. If there is no
	 *         logger registered with <code>name</code> a new logger will be
	 *         created and registered accordingly.
	 */
	public static synchronized Logger getLogger(String name) {
		Logger l = (Logger) loggers.get(name);

		if (l == null) {
			l = new Logger(name);
			loggers.put(name, l);
		}

		return l;
	}

	/**
	 * Return the logger's name.
	 * @return the logger's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the logger's parent. The parent is reset if other loggers were
	 * created, in case children are created after their parents.
	 * 
	 * @return the logger's parent.
	 */
	public Logger getParent() {
		if (loggers.size() != version) {
			setParent(findParent(name));
		}

		return parent;
	}

	/**
	 * Test whether the given level is loggable according to this Logger.
	 * @param test
	 *            the level to test.
	 * @return whether the given level is loggable for this logger.
	 */
	public boolean isLoggable(Level test) {
		if (test == null)
			return false;

		Logger curr = this;
		while (curr != null) {
			Level actual = curr.getLevel();
			if (actual != null) {
				if (actual == Level.OFF) {
					return false;
				}
				return (test.intVal() >= actual.intVal());
			}

			curr = curr.getParent();
		}

		return false;
	}

	/**
	 * Log a message using the given level.
	 * @param level
	 *            the log level to use for logging.
	 * @param msg
	 *            the message to log.
	 */
	public void log(Level level, String msg) {
		log(level, msg, null);
	}

	/**
	 * Log a message using the given level.
	 * @param level
	 *            the log level to use for logging.
	 * @param msg
	 *            the message to log.
	 * @param t
	 *            the throwable to log.
	 */
	public void log(Level level, String msg, Throwable t) {
		log(null, level, msg, t);
	}

	/**
	 * Log a message using the given level.
	 * @param c
	 *            the class to log.
	 * @param level
	 *            the log level to use for logging.
	 * @param msg
	 *            the message to log.
	 */
	public void log(Class c, Level level, String msg) {
		log(c, level, msg, null);
	}

	/**
	 * Log a message using the given level, including optional exception
	 * information.
	 * @param c
	 *            the class to log.
	 * @param level
	 *            the log level
	 * @param msg
	 *            the message to log.
	 * @param t
	 *            the throwable object,t that should be logged.
	 */
	public void log(Class c, Level level, String msg, Throwable t) {
		if (level == null) {
			// no level => set to fine
			level = Level.FINE;
		}
		if (!isLoggable(level)) {
			return;
		}

		long time = System.currentTimeMillis();
		String tname = Thread.currentThread().toString();

		if (t != null) {
			msg += " " + t;
		}

		if (c != null) {
			final String cName = c.getName();
			int dotPos = cName.lastIndexOf('.');
			msg += "#" + cName.substring(dotPos + 1) + "#";
		}

		System.out.println(time + " " + tname + " " + level + " " + msg);
	}

	/**
	 * Sets the logger's level.
	 * @param level
	 *            the new level. This parameter may be <code>null</code>.
	 */
	public void setLevel(Level level) {
		lvl = level;
	}

	/**
	 * Sets the logger's parent.
	 * @param parent
	 *            the new parent.
	 */
	public void setParent(Logger parent) {
		this.parent = parent;
		version = loggers.size();
	}

	//
	// static logging methods
	//

	/**
	 * Log a message to the root logger with the FINE level.
	 * @param c
	 *            the class to log.
	 * @param msg
	 *            the message to log.
	 */
	public static void debug(Class c, String msg) {
		root.fine(c, msg);
	}

	/**
	 * Log a message to the root logger with the FINE level.
	 * @param msg
	 *            the message to log.
	 */
	public static void debug(String msg) {
		debug(null, msg);
	}

	/**
	 * Log a message to the root logger with the WARNING level.
	 * @param msg
	 *            the message to log.
	 */
	public static void warn(String msg) {
		root.warning(msg);
	}

	/**
	 * Log a message to the root logger with the SEVERE level.
	 * @param msg
	 *            the message to log.
	 * @param t
	 *            the throwable to log.
	 */
	public static void error(String msg, Throwable t) {
		root.severe(msg, t);
	}

	//
	// logging methods
	//

	/**
	 * Log a message with the FINE level.
	 * @param c
	 *            the class to log.
	 * @param msg
	 *            the message to log.
	 */
	public void fine(Class c, String msg) {
		log(c, Level.FINE, msg);
	}

	/**
	 * Log a message with the FINE level.
	 * @param msg
	 *            the message to log.
	 */
	public void fine(String msg) {
		log(Level.FINE, msg);
	}

	/**
	 * Log a message with the INFO level. *
	 * @param msg
	 *            the message to log.
	 */
	public void info(String msg) {
		log(Level.INFO, msg);
	}

	/**
	 * Log a message using the WARNING level.
	 * @param msg
	 *            the message to log.
	 */
	public void warning(String msg) {
		log(Level.WARNING, msg);
	}

	/**
	 * Log a message using the WARNING level.
	 * @param msg
	 *            the message to log.
	 * @param t
	 *            the throwable to log.
	 */
	public void warning(String msg, Throwable t) {
		log(Level.WARNING, msg, t);
	}

	/**
	 * Log a message using the SEVERE level.
	 * @param msg
	 *            the message to log.
	 */
	public void severe(String msg) {
		log(Level.SEVERE, msg);
	}

	/**
	 * Log a message using the SEVERE level.
	 * @param msg
	 *            the message to log.
	 * @param t
	 *            the throwable to log.
	 */
	public void severe(String msg, Throwable t) {
		log(Level.WARNING, msg, t);
	}
}