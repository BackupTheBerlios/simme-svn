// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: ResourceBundle.java
//                  $Date: $
//              $Revision: $
// ----------------------------------------------------------------------------
package at.einspiel.midp.i18n;

import java.util.Hashtable;

/**
 * Implementation of a resource bundle for internationalized strings.
 * 
 * Copied from http://java.sun.com/developer/J2METechTips/2001/tt0129.html
 * 
 * @author kariem
 */
public class ResourceBundle {

	private static Hashtable groups = new Hashtable();

	/** Holds the keys and values. */
	protected Hashtable resources = new Hashtable();

	private ResourceBundle parent;
	private String groupName;

	/**
	 * Protected constructor.
	 */
	protected ResourceBundle() {
		// empty and only used by subclasses
	}

	/**
	 * Loads the bundle with the given name for the default locale.
	 * @param name
	 *            the name that identifies the bundle. This is the class name.
	 * @return a resource bundle that corresponds to <code>name</code>, or a
	 *         newly created resource bundle.
	 */
	public static ResourceBundle loadBundle(String name) {
		ResourceBundle bundle = null;
		Locale locale = Locale.getDefaultLocale();
		String language = locale.getLanguage();
		String country = locale.getCountry();

		try {
			bundle = (ResourceBundle) Class.forName(name).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (language != null) {
			// find child bundles and attach to parent
			ResourceBundle child;

			try {
				child = (ResourceBundle) Class.forName(name + '_' + language).newInstance();
				child.setParent(bundle);
				bundle = child;
			} catch (Exception e) {
				// no need to be catched
			}

			if (country != null) {
				try {
					child = (ResourceBundle) Class.forName(
							name + '_' + language + '_' + country).newInstance();
					child.setParent(bundle);
					bundle = child;
				} catch (Exception e) {
					// no need to be catched
				}
			}
		}

		if (bundle == null) {
			bundle = new ResourceBundle();
		}

		groups.put(name, bundle);
		return bundle;
	}

	/**
	 * Returns an object for <code>group</code> which is associated with
	 * <code>key</code>.
	 * @param group
	 *            the group which corresponds to the name of the class.
	 * @param key
	 *            the key.
	 * @return an object identified by <code>key</code>.
	 */
	public static Object getObject(String group, String key) {
		ResourceBundle bundle;

		synchronized (groups) {
			bundle = (ResourceBundle) groups.get(group);
			if (bundle == null) {
				bundle = loadBundle(group);
			}
		}

		return bundle.getResource(key);
	}

	/**
	 * Returns a string for <code>group</code> which is associated with
	 * <code>key</code>.
	 * @param group
	 *            the group which corresponds to the name of the class.
	 * @param key
	 *            the key.
	 * @return a string value identified by <code>key</code>.
	 * @see #getObject(String, String)
	 */
	public static String getString(String group, String key) {
		return (String) getObject(group, key);
	}

	/**
	 * Returns a string which is associated with <code>key</code>.
	 * @param key
	 *            the key.
	 * @return a string value identified by <code>key</code>.
	 * @see #getString(String, String)
	 */
	public String getString(String key) {
		return getString(getGroupName(), key);
	}

	/**
	 * Returns the group name. This is used for the method
	 * {@linkplain #getString(String)}.
	 * @return this object's group name.
	 */
	protected String getGroupName() {
		if (groupName == null) {
			groupName = getClass().getName();
		}
		return groupName;
	}

	/**
	 * Returns the resource associated with <code>key</code>. If the resource
	 * is not found in the current resource bundle, the parent resource bundle
	 * is queried.
	 * @param key
	 *            the key.
	 * @return the resource identified by <code>key</code>.
	 */
	protected Object getResource(String key) {
		Object obj = null;

		if (resources != null) {
			obj = resources.get(key);
		}

		if (obj == null && parent != null) {
			obj = parent.getResource(key);
		}

		return obj;
	}

	/**
	 * Sets the parent for this object.
	 * @param parent
	 *            the new parent.
	 */
	protected void setParent(ResourceBundle parent) {
		this.parent = parent;
	}
}