// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: Messages.java
//                  $Date: $
//              $Revision: $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.messages;

/**
 * @author kariem
 */
public class Messages {
	private static final Bundle RESOURCE_BUNDLE = new Bundle();

	private Messages() {
		// private constructor for utility class
	}

	/**
	 * Returns the string value associated with the given <code>key</code>.
	 * 
	 * @param key
	 *            the key.
	 * @return an associated string value.
	 */
	public static String getString(String key) {
		return RESOURCE_BUNDLE.getString(key);
	}
}
