// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: AbstractServlet.java
//                  $Date: 2004/09/07 13:30:36 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract class that forwards all calls from
 * {@linkplain #doGet(HttpServletRequest, HttpServletResponse)}to the method
 * {@linkplain #doPost(HttpServletRequest, HttpServletResponse)}. In addition
 * the response's content type is set to XML with UTF-8 encoding.
 * @author kariem
 */
public abstract class AbstractServlet extends HttpServlet {

	/**
	 * Forwards the request to the
	 * {@linkplain #doPost(HttpServletRequest, HttpServletResponse)}method.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * The current implementation only sets the response to contain XML content
	 * type.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
	 * @see #setResponseXml(HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		setResponseXml(resp);
	}

	/**
	 * Sets the content type of the given response object to
	 * <code>text/xml</code> with <code>UTF-8</code> encoding.
	 * 
	 * @param response
	 *            the response.
	 */
	protected void setResponseXml(HttpServletResponse response) {
		response.setContentType("text/xml");//; charset=UTF-8");
	}
}