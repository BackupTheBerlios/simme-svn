// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: XMLElementTest.java
//                  $Date: 2004/09/13 15:18:31 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.nanoxml;

import junit.framework.TestCase;

/**
 * Class to test XMLElement.
 * 
 * @author kariem
 */
public class XMLElementTest extends TestCase {

	private static final String TEST_STRING = "<gameinfo url=\"gameplay.jsp\" p2=\"Player 2\" info2=\"AT\" p1=\"Player 1\" info1=\"AT\" id=\"1094655158000---MAMI---Kariem\"/>";
	
	/** Test parseStringMethod */
	public void testParseString() {
		XMLElement xml = new XMLElement();
		xml.parseString(TEST_STRING);
	}
}
