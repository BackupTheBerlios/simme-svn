// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SpecialMenu.java
//                  $Date: 2004/09/15 23:37:50 $
//              $Revision: 1.7 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

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

	private final IReqHandler handler;

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
		String idNext = e.getAttribute(ATTR_NEXT);

		if (type.equals(SPECIAL_WAIT)) {
			handler = new ReqHandlerWait(idNext);
		} else if (type.equals(SPECIAL_START)) {
			handler = new ReqHandlerStart(id, idNext);
		} else {
			if (!type.equals(SPECIAL_READY)) {
				throw new RuntimeException("Assertion: Wrong type specified: " + type);
			}
			handler = new ReqHandlerReady();
		}
	}

	/** @see IMenu#getXml() */
	public String getXml() {
		return handler.answer(mu);
	}
}