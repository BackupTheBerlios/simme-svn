// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: LimitedLog.java
//                  $Date: 2004/08/12 21:56:08 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.logging;

/**
 * Represents a log which can be limited in size. The default size is
 * {@linkplain #DEFAULT_LOG_SIZE}characters.
 * 
 * @author kariem
 */
public class LimitedLog {

	protected static final int DEFAULT_LOG_SIZE = 1000;
	private final int maxLogSize;
	private StringBuffer logContent;

	/**
	 * Creates a new instance of <code>LimitedLog</code>.
	 */
	public LimitedLog() {
		this(DEFAULT_LOG_SIZE);
	}

	/**
	 * Creates a new instance of <code>LimitedLog</code>.
	 * 
	 * @param maxLogSize
	 *            the maximum size of the log.
	 */
	public LimitedLog(int maxLogSize) {
		this.maxLogSize = maxLogSize;
		logContent = new StringBuffer(maxLogSize);
	}

	/**
	 * Appends the given message to the log, regardless of the current log size.
	 * 
	 * @param msg
	 *            the message to be logged.
	 * @see #append(String)
	 */
	public void appendNoResize(String msg) {
		logContent.append(msg);
	}

	/**
	 * Appends the given message to the log and corrects the log size, if it
	 * surpasses the maximum limit.
	 * 
	 * @param msg
	 *            the message to be logged.
	 * @see #appendNoResize(String)
	 */
	public void append(String msg) {
		appendNoResize(msg);
		resize();
	}

	/**
	 * Returns the current content.
	 * @return the current content.
	 */
	public String getContent() {
		final int currentLength = logContent.length();
		
		if (currentLength > maxLogSize) {
			char[] mostCurrent = new char[maxLogSize];
			logContent.getChars(currentLength - maxLogSize, currentLength, mostCurrent, 0);
			return new String(mostCurrent);
		}
		
		// not too long, return the content
		return logContent.toString();
	}
	
	/**
	 * Resizes the log's content to the maximum of the log size set in the
	 * constructor.
	 */
	private void resize() {
		final int currentLength = logContent.length();
		// resize if buffer takes twice the space of the maximum log size.
		if (currentLength > maxLogSize * 2) {
			// content has to be resized
			char[] copyOfMostCurrent = new char[maxLogSize];
			logContent.getChars(currentLength - maxLogSize, currentLength, copyOfMostCurrent, 0);
			logContent = new StringBuffer(new String(copyOfMostCurrent));
		}
	}
}