// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: GameInfo.java
//                  $Date: 2004/09/13 15:22:33 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client;

import at.einspiel.simme.nanoxml.XMLElement;

/**
 * Represents game meta information.
 * 
 * @author kariem
 */
public class GameInfo {

	private static final String TAG_GAME_INFO = "gameinfo";

	// meta information
	private String id;
	private String p1Name;
	private String p2Name;
	private String p1Info;
	private String p2Info;

	/**
	 * Creates a new instance of <code>GameInfo</code>.
	 * 
	 * @param id
	 * @param nameP1
	 * @param nameP2
	 * @param infoP1
	 * @param infoP2
	 */
	public GameInfo(String id, String nameP1, String nameP2, String infoP1, String infoP2) {
		this.id = id == null ? "" : id;
		p1Name = nameP1 == null ? "Player 1" : nameP1;
		p2Name = nameP2 == null ? "Player 2" : nameP2;
		p1Info = infoP1 == null ? "AT" : infoP1;
		p2Info = infoP2 == null ? "AT" : infoP2;
	}

	/**
	 * Creates a new instance of <code>GameInfo</code> with default values for
	 * the fields.
	 */
	public GameInfo() {
		this(null, null, null, null, null);
	}

	/**
	 * Creates a new instance of <code>GameInfo</code> with the meta
	 * information found in <code>xmlInfo</code>.
	 * 
	 * @param xmlInfo
	 *            holds game meta information.
	 * @throws NullPointerException
	 *             if <code>xmlInfo</code> is <code>null</code>.
	 */
	public GameInfo(XMLElement xmlInfo) throws NullPointerException {
		this(xmlInfo.getAttribute("gameid"), xmlInfo.getAttribute("p1"), xmlInfo
				.getAttribute("p2"), xmlInfo.getAttribute("info1"), xmlInfo
				.getAttribute("info2"));
	}

	/**
	 * Returns an xml representation of the game information.
	 * @return an xml representation showing id and player names. Additional
	 *         player information is optional and ommitted if not existant.
	 */
	public XMLElement toXml() {
		XMLElement info = new XMLElement();
		info.setName(TAG_GAME_INFO);
		info.setAttribute("gameid", id);
		info.setAttribute("p1", p1Name);
		info.setAttribute("p2", p2Name);
		// optional info fields
		if (p1Info != null) {
			info.setAttribute("info1", p1Info);
		}
		if (p2Info != null) {
			info.setAttribute("info2", p2Info);
		}
		return info;
	}

	//
	// getters and setters
	//

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the p1Info.
	 */
	public String getP1Info() {
		return p1Info;
	}

	/**
	 * @param info
	 *            The p1Info to set.
	 */
	public void setP1Info(String info) {
		p1Info = info;
	}

	/**
	 * @return Returns the p1Name.
	 */
	public String getP1Name() {
		return p1Name;
	}

	/**
	 * @param name
	 *            The p1Name to set.
	 */
	public void setP1Name(String name) {
		p1Name = name;
	}

	/**
	 * @return Returns the p2Info.
	 */
	public String getP2Info() {
		return p2Info;
	}

	/**
	 * @param info
	 *            The p2Info to set.
	 */
	public void setP2Info(String info) {
		p2Info = info;
	}

	/**
	 * @return Returns the p2Name.
	 */
	public String getP2Name() {
		return p2Name;
	}

	/**
	 * @param name
	 *            The p2Name to set.
	 */
	public void setP2Name(String name) {
		p2Name = name;
	}

	//
	// overidden java.lang.Object methods
	//

	/** @see java.lang.Object#equals(java.lang.Object) */
	public boolean equals(Object obj) {
		if (obj instanceof GameInfo) {
			return equals((GameInfo) obj);
		}
		return false;
	}

	/**
	 * Indicates whether some other GameInfo is equal to this one.
	 * @param info
	 *            the other GamInfo object.
	 * @return <code>true</code> if both GameInfo objects share the same id,
	 *         and the names of player1 and player2 are equal between the
	 *         GameInfo objects.
	 */
	public boolean equals(GameInfo info) {
		return info.id.equals(this.id) && info.p1Name.equals(this.p1Name)
				&& info.p2Name.equals(this.p2Name);
	}
}