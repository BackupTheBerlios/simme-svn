// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: GenerateMenu.java
//                  $Date: 2004/09/13 15:11:53 $
//              $Revision: 1.6 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import java.util.Collection;

import org.w3c.dom.Element;

import at.einspiel.base.NoSuchUserException;
import at.einspiel.base.User;
import at.einspiel.base.UserException;
import at.einspiel.db.UserDB;
import at.einspiel.messaging.ISimpleInfo;
import at.einspiel.simme.nanoxml.XMLElement;
import at.einspiel.simme.server.ManagedUser;
import at.einspiel.simme.server.SessionManager;
import at.einspiel.util.XMLUtils;

/**
 * A menu that generates its contents from the database.
 * 
 * @author kariem
 */
class GenerateMenu extends AbstractListMenu {

	static final String TAG_NAME = "generate";

	/** Shows all users. */
	static final String USERS_ALL = "all";
	/**
	 * Shows all users currently not playing a game. The user calling this menu
	 * via {@link #setUser(ManagedUser)}is not in the result list.
	 */
	static final String USERS_WAITING = "waiting";
	/**
	 * Shows all users currently online, i.e. either playing a game, browsing
	 * the tables or waiting for the next game. The user calling this menu via
	 * {@link #setUser(ManagedUser)}is not in the result list.
	 */
	static final String USERS_ONLINE = "online";

	private final String prefix, suffix;

	private String type;
	private ManagedUser u;

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
		this.type = e.getAttribute(IMenu.ATTR_TYPE);
		//
		// create prefix and suffix of xml source automatically
		StringBuffer buf = new StringBuffer();
		// prefix
		buf.append(createXMLStart(TAG_NAME, true));
		// meta-info
		XMLUtils.addAttrToBuf(ISimpleInfo.ATTR_METAINFO, XMLElement.TRUE, buf);
		// end of start tag
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
			// look for online users only
			SessionManager sessionMgr = SessionManager.getInstance();
			Collection foundUsers;
			if (type.equals(USERS_ONLINE)) {
				foundUsers = sessionMgr.getUsers();
			} else {
				foundUsers = sessionMgr.getUsersAvailable();
			}
			// remove user which has called this menu
			if (u != null) {
				foundUsers.remove(u);
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

	/**
	 * Returns detailed information about the user identified by
	 * <code>meta</code>.
	 * 
	 * @see IMenu#getXml(String)
	 */
	public String getXml(String meta) {
		if (meta != null) {

			try {
				// find user with the name as in "meta"
				User foundUser = UserDB.getUserByNick(meta);

				String messageTitle = meta;
				StringBuffer info = new StringBuffer();
				// append user information to string buffer
				foundUser.toString(info);
				// create short textual info
				return TextXml.createTextXml(messageTitle, id, info.toString(), true);
			} catch (NoSuchUserException e) {
				e.printStackTrace();
			}
		}
		// no meta info or any error: return default
		return getXml();
	}

	/**
	 * This menu only refers to itself.
	 * @see IMenu#getOptions()
	 */
	public String[] getOptions() {
		return new String[]{id};
	}

	/**
	 * This method always returns its own id for every selection.
	 * @see at.einspiel.simme.server.menu.IMenu#getIdFor(java.lang.String)
	 */
	public String getIdFor(String selection) {
		return id;
	}

	//
	// UserMenu implementation
	// 

	/** @see AbstractMenu#setUser(ManagedUser) */
	public void setUser(ManagedUser mu) {
		this.u = mu;
	}

	/**
	 * Returns a real clone (new object) of this menu.
	 * 
	 * @see at.einspiel.simme.server.menu.AbstractMenu#clone()
	 */
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}