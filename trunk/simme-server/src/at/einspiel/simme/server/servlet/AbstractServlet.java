// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: AbstractServlet.java
//                  $Date: 2004/09/13 15:11:27 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.servlet;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.einspiel.messaging.IConstants;
import at.einspiel.simme.server.ManagedUser;
import at.einspiel.simme.server.SessionManager;

/**
 * Abstract class that forwards all calls from
 * {@linkplain #doGet(HttpServletRequest, HttpServletResponse)}to the method
 * {@linkplain #doPost(HttpServletRequest, HttpServletResponse)}. In addition
 * the response's content type is set to XML with UTF-8 encoding.
 * @author kariem
 */
public abstract class AbstractServlet extends HttpServlet {

	/** The locale of the requesting client. */
	protected Locale clientLocale;
	/** The user associated with this request. */
	ManagedUser u;
	
	
	/**
	 * Forwards the request to the method
	 * {@linkplain #doPost(HttpServletRequest, HttpServletResponse)}.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * The current implementation only sets the response to contain XML content
	 * type. The client locale from the request is saved in
	 * {@link #clientLocale}.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
	 * @see #setResponseXml(HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.clientLocale = req.getLocale();
		// find associated user
		String nick = req.getParameter(IConstants.PARAM_USER);
		if (nick != null) {
			u = SessionManager.getInstance().getOnlineUser(nick);
		}
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