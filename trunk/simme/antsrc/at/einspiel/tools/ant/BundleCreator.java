// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: BundleCreator.java
//                  $Date: $
//              $Revision: $
// ----------------------------------------------------------------------------
package at.einspiel.tools.ant;

import java.io.*;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * This class creates custom Bundle classes out of properties files.
 * 
 * @author kariem
 */
public class BundleCreator extends Task {

	File from;
	File to;
	String prefix;
	String suffix;

	boolean convertToUpperCase = true;

	/** @see org.apache.tools.ant.Task#execute() */
	public void execute() throws BuildException {
		checkIfValid(from);
		if (to == null) {
			to = from;
		}
		checkIfValid(to);

		if (prefix == null) {
			prefix = "bundle";
		}

		final String firstChar = prefix.substring(0, 1);
		final String outputPrefix = firstChar.toUpperCase() + prefix.substring(1);

		if (suffix == null) {
			suffix = ".properties";
		}

		System.out.println(from);

		// filter files in directory and retain all valid files
		File[] files = from.listFiles(new PrefixFilter());

		// Iterate over files and create new class for each file in target
		// directory
		Properties props = new Properties();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];

			// construct output filename
			String fileName = f.getName();
			final String middleString = fileName.substring(prefix.length(), fileName.length()
					- suffix.length());
			String className = outputPrefix + middleString;
			File output = new File(to, className + ".java");

			try {
				props.load(new FileInputStream(f));

				FileWriter fw = new FileWriter(output);

				StringBuffer buf = new StringBuffer();
				// package declaration
				buf.append("package at.einspiel.simme.client.messages;\n");
				// import statements
				buf.append("import at.einspiel.midp.i18n.ResourceBundle;\n");
				// class declaration
				buf.append("public class ");
				buf.append(className);
				buf.append(" extends ResourceBundle {\n");
				// constructor
				buf.append("public ");
				buf.append(className);
				buf.append("() {");

				// add keys
				for (Iterator it = props.entrySet().iterator(); it.hasNext();) {
					Map.Entry e = (Map.Entry) it.next();
					buf.append("resources.put(\"");
					buf.append(e.getKey());
					buf.append("\", \"");
					buf.append(getValue((String)e.getValue()));
					buf.append("\");\n");
				}
				props.clear();

				// close constructor and class
				buf.append("}}");

				fw.write(buf.toString());
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	private String getValue(String value) {
			value = value.replaceAll("\"", "\\\\\"");
			value = value.replaceAll("\n", "\\\\n");
			value = value.replaceAll("\r", "\\\\r");
			value = value.replaceAll("\t", "\\\\t");
			
			return value;
	}

	private void checkIfValid(File f) {
		if (f == null) {
			throw new BuildException("No directory specified");
		}

		if (!f.isDirectory()) {
			throw new BuildException(from + " is no valid directory. ");
		}
	}

	/**
	 * @param to
	 *            a directory.
	 */
	public void setTo(File to) {
		this.to = to;
	}

	/**
	 * @param from
	 *            a directory.
	 */
	public void setFrom(File from) {
		this.from = from;
	}

	/**
	 * @param prefix
	 *            The prefix to set.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	class PrefixFilter implements FilenameFilter {

		/**
		 * Accepts non-directory files with names that start with
		 * <code>filePrefix</code> and end with <i>.properties </i>.
		 * 
		 * @see FilenameFilter#accept(File, String)
		 */
		public boolean accept(File dir, String name) {
			File f = new File(dir, name);
			if (f.isFile()) {
				if (name.startsWith(BundleCreator.this.prefix)) {
					if (name.endsWith(BundleCreator.this.suffix)) {
						return true;
					}
				}
			}
			return false;
		}
	}

}
