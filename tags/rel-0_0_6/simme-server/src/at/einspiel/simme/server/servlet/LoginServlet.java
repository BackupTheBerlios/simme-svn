// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: LoginServlet.java
//                  $Date: 2004/09/13 15:11:27 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.einspiel.simme.server.SessionManager;

/**
 * Servlet that is used as the first entrance to the application.
 * 
 * @author kariem
 */
public class LoginServlet extends AbstractServlet {

	private static final String LOGIN_JSP = "/doLogin.jsp";
	private static final String MENU_URL = "/menu.xml";

	/**
	 * This method checks, if the client's menu has already been loaded. If not
	 * loaded yet, the menu's URL local to the server is resolved, and the menu
	 * is loaded. The request is then forwarded to the login JSP page.
	 * 
	 * @see AbstractServlet#doPost(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// see if session manager's menu has already been initialised
		ServletContext sc = getServletContext();
		SessionManager mgr = SessionManager.getInstance();
		if (!mgr.menuLoaded()) {
			final String menuXmlPath = sc.getRealPath(MENU_URL);
			final File realFile = new File(menuXmlPath);
			try {
				mgr.loadMenu(realFile.toURL());
			} catch (Exception e) {
				log("Menu could not be loaded into SessionManager.", e);
			}
		}

		// forward request to login page
		RequestDispatcher rd = sc.getRequestDispatcher(LOGIN_JSP);
		rd.forward(req, resp);
	}
}