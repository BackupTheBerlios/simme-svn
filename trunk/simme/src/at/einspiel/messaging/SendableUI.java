package at.einspiel.messaging;

import java.util.Enumeration;
import java.util.Vector;

import at.einspiel.simme.nanoxml.XMLElement;
import at.einspiel.simme.nanoxml.XMLParseException;


/**
 * This class represents an easily createable and sendable user interface. It
 * provides two possibilities for creation:
 * <ol>
 *  <li>The <code>SendableUI</code> may be initialized with an xml string, by
 *      calling the method {@linkplain #initialize(String)}. See its doc for
 *      further information. Alternatively this string may be passed as a
 *      parameter to the constructor.</li>
 *  <li>The second possibility is provided by means of {@linkplain
 *      #setTitle(String)} and {@linkplain #setId(String)} to create a simple
 *      UI object. The methods {@linkplain #setText(String)} and {@linkplain
 *      #setListElements(Vector)} are used to fill in contents.
 * </ol>
 * <p>The xml string to create a new instance of SendableUI is directly
 * accessible through {@linkplain #getXmlString()}.</p>
 *   
 * @author kariem
 */
public class SendableUI {

    private byte id;
    private String title;
    private boolean list;
    private boolean xmlInfo;

    /** for simple uis: only text */
    private String text;
    /** enumeration of list elements */
    private Vector listElements;
    /** optional game information */
    private XMLElement xmlElement;

    /** Default Constructor to use as bean. */
    public SendableUI() {
        // default constructor
    }

    /**
     * Creates a new <code>SendableUI</code> from a string.
     * @param xmlString a string that contains information for the building
     *         a displayable component.
     * 
     * @see #initialize(String)
     */
    public SendableUI(String xmlString) {
        initialize(xmlString);
    }

    /**
     * Initializes the SendableUI with the given xml string.
     * @param xmlString should be for the format.
     * <p>
     * <pre>
     *    &lt;element title="title" id="ID" list="true" &gt;
     *        &lt;child name="name1" /&gt;
     *        &lt;child name="name2" /&gt;
     *        &lt;child name="name3" /&gt;
     *    &lt;element/&gt;
     * </pre>
     * </p>
     * or
     * <p>
     * <pre>
     *    &lt;element title="title"
     *                id="ID"
     *                list="false"
     *                msg="Text /&gt;
     * </pre>
     * </p>
     */
    public void initialize(String xmlString) {
        XMLElement xml = new XMLElement();

        try {
            xml.parseString(xmlString);
            makeXmlDisplayable(xml);
        } catch (XMLParseException xex) {
            title = "Information";
            text = xmlString;
        }
    }

    /**
     * Sets the id. This field is optional.
     * @param id the id.
     */
    public void setId(byte id) {
        this.id = id;
    }

    /**
     * Returns the id.
     * @return the id.
     */
    public byte getId() {
        return id;
    }

    /**
     * Sets the title.
     * @param title the new title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the title. 
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the message text.
     * @param text the new message text.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the message text.
     * @return the message text.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the elements of a list.
     * @param elements the new elements.
     */
    public void setListElements(Vector elements) {
        this.listElements = elements;
        list = true;
    }

    /**    
     * Returns the elements of the list, which may be rendered by this instance.
     * @return the list's elements.
     */
    public Vector getListElements() {
        return listElements;
    }

    /**
     * Whether this instance builds a list.
     * @return <code>true</code> if this instance may be rendered into a list,
     *          <code>false</code> otherwise.
     */
    public boolean isList() {
        return list;
    }
    
    /**
     * Returns whether this instance contains additional information as xml.
     * 
     * @return <code>true</code>, if this instance contains xml information,
     *          <code>false</code> otherwise.
     */
    public boolean hasXmlInfo() {
        return xmlInfo;
    }

    /**    
     * Returns the xml information.
     * @return the xml information.
     */
    public XMLElement getXmlInfo() {
        return xmlElement;
    }

    /**
     * Creates a displayable from an xml element.
     * @param xml the element.
     */
    private void makeXmlDisplayable(XMLElement xml) {
        System.out.println("xml-ing");

        // set title
        title = xml.getAttribute("title", "Auswahl");

        // set id, if available
        id = (byte) xml.getAttributeInt("id", 0);

        // show either list, or simple status message
        if (xml.getAttributeBoolean("list", false)) {
            list = true;
            // add children to vector
            listElements = new Vector();
            Enumeration enum = xml.enumerateChildren();
            while (enum.hasMoreElements()) {
                XMLElement element = (XMLElement) enum.nextElement();
                String name = element.getAttribute("name", null);

                if (name != null) {
                    listElements.addElement(name);
                }
            }
        } else if (xml.countChildren() == 1) {
            // single child ... must be information to create a game
            xmlElement = (XMLElement) xml.getChildren().elementAt(0);
            xmlInfo = true;
        } else {
            text = xml.getAttribute("msg", "Warte...");
        }
    }

    /**
     * Returns a string which can be used to create a new sendable UI. This
     * method may be used to send user interface information across a network
     * using xml.
     * 
     * @return a string that contains the information for rendering the user
     *          interface component.
     */
    public String getXmlString() {
        XMLElement xml = new XMLElement();
        xml.setName("sendable");
        xml.setAttribute("title", title);
        xml.setAttribute("id", Byte.toString(id));
        if ((list) && (listElements != null) && (!listElements.isEmpty())) {
            xml.setAttribute("list", XMLElement.TRUE);
            Enumeration enum = listElements.elements();
            while (enum.hasMoreElements()) {
                String el = (String) enum.nextElement();
                XMLElement child = new XMLElement();
                child.setAttribute("name", el);
                xml.addChild(child);
            }
        } else {
            xml.setAttribute("msg", text);
        }
        return xml.toString();
    }

}
