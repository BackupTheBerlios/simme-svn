// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: Locale.java
//                  $Date: $
//              $Revision: $
// ----------------------------------------------------------------------------
package at.einspiel.midp.i18n;

/**
 * Implementation of locale class.
 * 
 * Copied from http://java.sun.com/developer/J2METechTips/2001/tt0129.html
 * 
 * @author kariem
 */
public class Locale {

	private static Locale defaultLocale = new Locale(System.getProperty("microedition.locale"));

	private static final String DEFAULT_LANG = "en";
	private static final String DEFAULT_COUNTRY = "US";

	private final String language;
	private final String country;

	/**
	 * Creates a new instance of <code>Locale</code>.
	 * @param language
	 *            the language to use.
	 * @param country
	 *            the country.
	 */
	public Locale(String language, String country) {
		this.language = language;
		this.country = country;
	}

	/**
	 * Creates a new instance of <code>Locale</code> from the information in
	 * the parameter.
	 * 
	 * @param locale
	 *            the locale in the format
	 *            <code>&lt;language&gt;-&lt;country&gt;</code>.
	 * @throws NullPointerException
	 *             if locale is <code>null</code>
	 */
	public Locale(String locale) throws NullPointerException {
		int pos = locale.indexOf('-');
		if (pos != -1) {
			language = locale.substring(0, pos);
			locale = locale.substring(pos + 1);

			pos = locale.indexOf('-');
			if (pos == -1) {
				country = locale;
			} else {
				country = locale.substring(0, pos);
			}
		} else {
			language = DEFAULT_LANG;
			country = DEFAULT_COUNTRY;
		}
	}

	/**
	 * Returns the locale's language.
	 * @return should be an uppercase ISO 3166 2-letter code.
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Returns the locale's country.
	 * @return should be a lowercase ISO 639 code.
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Returns the default locale as identifyed by the system property
	 * <code>microedition.locael</code>.
	 * @return the locale that corresponds to the default locale for this
	 *         system.
	 */
	public static Locale getDefaultLocale() {
		return defaultLocale;
	}
}