// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SessionMgrServlet.java
//                  $Date: 2004/09/07 13:30:36 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.einspiel.simme.server.SessionManager;

/**
 * Servlet to interact with the session manager.
 * 
 * @author kariem
 */
public class SessionMgrServlet extends AbstractServlet {

	/**
	 * This method checks, if the client's menu has already been loaded. If not
	 * loaded yet, the menu's URL local to the server is resolved, and the menu
	 * is loaded. The request is then forwarded to the login JSP page.
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
		SessionManager manager = SessionManager.getInstance();

		// answer through session manager
		resp.getOutputStream().print(manager.getAnswerFor(req.getParameterMap()));
	}
}
