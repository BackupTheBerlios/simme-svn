// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SpecialMenu.java
//                  $Date: 2004/09/07 13:30:36 $
//              $Revision: 1.5 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

import at.einspiel.base.User;

/**
 * Is used to deliver specialised menu information. This includes means to start
 * a game, or set a user into waiting state.
 * 
 * @author kariem
 */
class SpecialMenu extends AbstractUserMenu {

	static final String TAG_NAME = "special";

	private static final String SPECIAL_WAIT = "wait";
	private static final String SPECIAL_START = "start";
	private static final String SPECIAL_READY = "ready";

	private final IRequestHandler handler;

	/**
	 * The given element should contain a type attribute, which can be one of
	 * {@linkplain #SPECIAL_WAIT},{@linkplain #SPECIAL_START}, or
	 * {@linkplain #SPECIAL_READY}.
	 * 
	 * @see AbstractUserMenu#AbstractUserMenu(Element)
	 */
	SpecialMenu(Element e) {
		super(e);

		String type = e.getAttribute(ATTR_TYPE);

		if (type.equals(SPECIAL_WAIT)) {
			handler = new WaitRequestHandler();
		} else if (type.equals(SPECIAL_START)) {
			handler = new StartRequestHandler();
		} else {
			assert type.equals(SPECIAL_READY) : "Wrong type specified: " + type;
			handler = new ReadyRequestHandler();
		}
	}

	/** @see IMenu#getXml() */
	public String getXml() {
		return handler.answer(u);
	}

	interface IRequestHandler {
		/**
		 * Answers the given user.
		 * 
		 * @param u
		 *            the user, which will be the recipient of the message.
		 * @return a String that can be interpreted by the user.
		 */
		String answer(User u);
	}

	class WaitRequestHandler implements IRequestHandler {

		/** @see IRequestHandler#answer(User) */
		public String answer(User u) {
			// TODO Auto-generated method stub (kariem)
			return null;
		}

	}
	
	class StartRequestHandler implements IRequestHandler {

		/** @see IRequestHandler#answer(User) */
		public String answer(User u) {
			// TODO Auto-generated method stub (kariem)
			return null;
		}
		
	}
	
	class ReadyRequestHandler implements IRequestHandler {

		/** @see IRequestHandler#answer(User) */
		public String answer(User u) {
			// TODO Auto-generated method stub (kariem)
			return null;
		}
		
	}
}