// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SessionMgrServlet.java
//                  $Date: 2004/09/02 10:19:33 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.einspiel.messaging.IConstants;
import at.einspiel.simme.server.SessionManager;

/**
 * Servlet to interact with the session manager.
 * 
 * @author kariem
 */
public class SessionMgrServlet extends HttpServlet {
	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

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
		SessionManager manager = SessionManager.getInstance();

		// retrieve parameters
		String userNick = req.getParameter(IConstants.PARAM_USER);
		String menuId = req.getParameter(IConstants.PARAM_MENUID);
		String selection = req.getParameter(IConstants.PARAM_SEL);
		
		// answer through session manager
		resp.getOutputStream().print(manager.getAnswerFor(userNick, menuId, selection));
	}
}
