// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: ITestRequest.java
//                  $Date: 2004/09/13 15:22:00 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.messaging;

/**
 * Simple interface that allows setting constants for multiple test requests
 * implementing this interface.
 * @author kariem
 */
interface ITestRequest {

	/** The test server. */
	String TEST_SERVER = "http://localhost:8080/simme/";
	//private static final String TEST_SERVER =
	// "http://128.131.111.157:8080/simme/";

}