package test.sim.net;

import java.util.Enumeration;
import java.util.Vector;

import nanoxml.XMLElement;
import nanoxml.XMLParseException;

/**
 * This class represents a easily createable and sendable user interface. It
 * provides two possibilities for creation:
 * <ol>
 *  <li>The <code>SendableUI</code> may be initialized with a xml string, by
 *      calling the method {@linkplain #initialize(String)}. See its doc for
 *      further information. </li>
 *  <li>The second possibility is provided by means of {@linkplain
 *      #createSimpleForm(String)} and {@linkplain #createSimpleList(Vector)},
 *      which both create the displayable with the information found in the
 *      properties <i>title</i> and <i>id</i>, and the paremeters.</li>
 * </ol>
 * <p>After creating an instance of SendableUI the selected property may be 
 * packet into a request and sent. The request is accessible via {@linkplain
 * #handleCommand(Command, Displayable)}, because usually a certain command
 * has to be executed in order to send a request.</p>
 * <p>The xml string to create a new instance of SendableUI is directly
 * accessible throught {@linkplain #getXmlString()}.</p>
 *   
 * @author kariem
 */
public class SendableUI {

    private String id;
    private String title;
    private boolean list;

    /** for simple uis: only text */
    private String text;
    /** enumeration of list elements */
    private Vector listElements;

    /** Default Constructor to use as bean. */
    public SendableUI() {
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
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the id.
     * @return the id.
     */
    public String getId() {
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
     * Creates a displayable from an xml element.
     * @param xml the element.
     */
    private void makeXmlDisplayable(XMLElement xml) {
        System.out.println("xml-ing");

        // set title
        title = xml.getAttribute("title", "Auswahl");

        // set id, if available
        id = xml.getAttribute("id", null);

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
        xml.setAttribute("id", id);
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
