// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: Logger.java
//                  $Date: 2004/09/07 13:21:08 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.logging;

/**
 * This class provides a simple logging interface for the whole application. For
 * concurrently running applications this interface has to be changed.
 * 
 * @author kariem
 */
public class Logger {

	/** Debug level */
	public static final int LVL_DEBUG = 3;
	/**
	 * Information level, used for clear messages that could even be forwarded
	 * to an experienced user.
	 */
	public static final int LVL_INFO = 2;
	/** Warning level, used for non fatal warning messages */
	public static final int LVL_WARN = 1;
	/** Error level, used for fatal error messages */
	public static final int LVL_ERROR = 0;

	private static int verboseLevel = 2;
	private static LimitedLog log = new LimitedLog();

	/**
	 * <p>
	 * Logs the message according to the currently set verbose level and the log
	 * level of this message. If a debug or information messages is received and
	 * the verbose level is high enough, the message will be printed to stdout.
	 * Otherwise stderr is used.
	 * </p>
	 * <p>
	 * Irrespective the verbose level all messages will be appended to the
	 * current log.
	 * </p>
	 * 
	 * @param c
	 *            the class which has called the log method.
	 * 
	 * @param message
	 *            the message.
	 * @param lvl
	 *            the log level.
	 */
	static void log(Class c, String message, int lvl) {
		if (c != null) {
			message = addClassInfo(message, c);
		}
		message = addLevelInfo(message, lvl);
		if (lvl >= verboseLevel) {
			// log to stdout or stderr accordingly
			if (lvl == LVL_DEBUG || lvl == LVL_INFO) {
				System.out.println(message);
			} else {
				System.err.println(message);
			}
		}
		appendToLog(message);
	}

	static void log(String message, int lvl) {
		log(null, message, lvl);
	}

	/**
	 * Appends the string to the log.
	 * 
	 * @param msg
	 *            the message to be appended.
	 */
	private static void appendToLog(String msg) {
		log.append(msg);
	}

	/**
	 * Returns the log's most recent content.
	 * 
	 * @return the the content.
	 */
	public static String getContent() {
		return log.getContent();
	}

	/**
	 * Prepends the class information to the message
	 * 
	 * @param message
	 *            the message
	 * @param c
	 *            the class
	 * @return the message with the class information.
	 */
	private static String addClassInfo(String message, Class c) {
		String name = c.getName();
		int dotPos = name.lastIndexOf('.');
		return "#" + name.substring(dotPos + 1) + "# " + message + "  \t_"
				+ Thread.currentThread();
	}

	/**
	 * Prepends the log level to the message.
	 * 
	 * @param message
	 *            the message.
	 * @param lvl
	 *            the level
	 * @return the message with an additional log level.
	 */
	private static String addLevelInfo(String message, int lvl) {
		switch (lvl) {
			case LVL_DEBUG :
				return "[DBG] " + message;
			case LVL_INFO :
				return message; //"[INF]" + message;
			case LVL_WARN :
				return "[WRN] " + message;
			case LVL_ERROR :
				return "[ERR] " + message;
			default :
				return "[???] " + message;
		}
	}

	/**
	 * Reports a debug message to the logger.
	 * 
	 * @param message
	 *            the debug message.
	 */
	public static void debug(String message) {
		debug(null, message);
	}

	/**
	 * Reports a debug message to the logger including the generating class.
	 * 
	 * @param c
	 *            the class.
	 * @param message
	 *            the debug message.
	 */
	public static void debug(Class c, String message) {
		log(c, message, LVL_DEBUG);
	}

	/**
	 * Reports an information message to the logger.
	 * 
	 * @param message
	 *            the information message.
	 */
	public static void info(String message) {
		log(message, LVL_INFO);
	}

	/**
	 * Reports a warning message to the logger.
	 * 
	 * @param message
	 *            the warning message.
	 */
	public static void warn(String message) {
		log(message, LVL_WARN);
	}

	/**
	 * Reports an error message to the logger.
	 * 
	 * @param message
	 *            the error message.
	 */
	public static void error(String message) {
		log(message, LVL_ERROR);
	}

	/**
	 * Returns the current log level.
	 * 
	 * @return the current log level will be one of {@linkplain #LVL_DEBUG},
	 *         {@linkplain #LVL_INFO},{@linkplain #LVL_WARN},
	 *         {@linkplain #LVL_ERROR}
	 */
	public static int getVerboseLevel() {
		return verboseLevel;
	}

	/**
	 * Sets the log level.
	 * 
	 * @param loglevel
	 *            the log level to set. This can be one of
	 *            {@linkplain #LVL_DEBUG},{@linkplain #LVL_INFO},
	 *            {@linkplain #LVL_WARN},{@linkplain #LVL_ERROR}
	 */
	public static void setVerboseLevel(int loglevel) {
		Logger.verboseLevel = loglevel;
	}

	/**
	 * Sets the log level from a string parameter.
	 * 
	 * @param loglevel
	 *            one of
	 *            <ul>
	 *            <li><i>debug </i></li>
	 *            <li><i>info </i></li>
	 *            <li><i>warn </i></li>
	 *            <li><i>error </i></li>
	 *            </ul>
	 * 
	 * @see #setVerboseLevel(int)
	 */
	public static void setVerboseLevel(String loglevel) {
		if (loglevel.equals("debug")) {
			setVerboseLevel(LVL_DEBUG);
		} else if (loglevel.equals("info")) {
			setVerboseLevel(LVL_INFO);
		} else if (loglevel.equals("warn")) {
			setVerboseLevel(LVL_WARN);
		} else if (loglevel.equals("error")) {
			setVerboseLevel(LVL_ERROR);
		}
	}
}