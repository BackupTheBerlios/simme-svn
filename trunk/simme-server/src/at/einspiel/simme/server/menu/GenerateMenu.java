// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: GenerateMenu.java
//                  $Date: 2004/08/25 15:43:11 $
//              $Revision: 1.4 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import java.util.Collection;

import org.w3c.dom.Element;

import at.einspiel.base.User;
import at.einspiel.base.UserException;
import at.einspiel.db.UserDB;
import at.einspiel.simme.server.SessionManager;

/**
 * A menu that generates its contents from the database.
 * 
 * @author kariem
 */
class GenerateMenu extends AbstractListMenu implements IMenu {

	static final String TAG_NAME = "generate";
	private static final String ATTR_TYPE = "type";

	static final String USERS_ALL = "all";
	static final String USERS_WAITING = "waiting";
	static final String USERS_ONLINE = "online";

	private String type;
	private final String prefix, suffix;

	/**
	 * Creates a new instance of <code>GenerateMenu</code>.
	 * 
	 * @param e
	 *            should have the following syntax: <br>
	 *            <code>
	 * 	     &lt;generate title=&quot;[title]&quot;
	 * 						 id=&quot;[id]&quot; 
	 * 					   type=&quot;[all|waiting|online]&quot;&gt;
	 * 			</code>
	 */
	GenerateMenu(Element e) {
		super(e);

		// parse type
		this.type = e.getAttribute(ATTR_TYPE);
		//
		// create prefix and suffix of xml source automatically
		StringBuffer buf = new StringBuffer();
		// prefix
		buf.append(createXMLStart(TAG_NAME, true));
		buf.append('>');
		prefix = buf.toString();
		// suffix
		buf = new StringBuffer("</");
		buf.append(TAG_NAME);
		buf.append('>');
		suffix = buf.toString();
	}

	/**
	 * This method newly creates its result with every call. No additional
	 * update is needed.
	 * 
	 * @see at.einspiel.simme.server.menu.IMenu#getXml()
	 */
	public String getXml() {
		User[] users;

		if (type.equals(USERS_ALL)) {
			try {
				// retrieve users from database
				users = UserDB.getAllUsers();
			} catch (UserException e) {
				// empty user array
				users = new User[0];
				e.printStackTrace();
			}
		} else {
			// only look for online users
			SessionManager sessionMgr = SessionManager.getInstance();
			Collection foundUsers;
			if (type.equals(USERS_ONLINE)) {
				foundUsers = sessionMgr.getUsers();
			} else {
				foundUsers= sessionMgr.getUsersAvailable();
			}
			users = (User[]) foundUsers.toArray(new User[foundUsers.size()]);
		}
		
		// prefix
		StringBuffer buf = new StringBuffer(prefix);
		// add each user's nick name to the XML listing
		for (int i = 0; i < users.length; i++) {
			MenuItem.appendXmlTo(buf, users[i].getNick());
		}
		
		// add suffix
		buf.append(suffix);
		
		return buf.toString();
	}
}