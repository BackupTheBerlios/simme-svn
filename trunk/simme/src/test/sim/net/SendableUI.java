package test.sim.net;

import java.io.Serializable;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;

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
public class SendableUI implements Serializable {

    private Displayable displayable;
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
    
    public SendableUI(String xmlString) {
        initialize(xmlString);
    }
    

    /**
     * Initializes the SendableUI with the given xml string.
     * 
     * @param xmlString the input string. 
     */
    public void initialize(String xmlString) {
        XMLElement xml = new XMLElement();

        try {
           xml.parseString(xmlString);
           makeXmlDisplayable(xml);
        } catch (XMLParseException xex) {
           title = "Information";
           createSimpleForm(xmlString);
        }
    }
    
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Creates a list with the information found in the enumeration. The
     * enumeration should only contain strings, which will be used as names
     * for the list elements.
     * 
     * @param enum the enumeration.
     */
    public void createSimpleList(Vector v) {
        listElements = v;
        createSimpleList();
    }
    
    private void createSimpleList() {
        displayable = new List(title, List.IMPLICIT);

        Enumeration enum = listElements.elements();
        while (enum.hasMoreElements()) {
           String name = (String) enum.nextElement();

           if (name != null) {
               appendToList(name, null);
           }
        }
    }
    
    void createXmlList(Enumeration enum) {
        displayable = new List(title, List.IMPLICIT);

        while (enum.hasMoreElements()) {
           XMLElement element = (XMLElement) enum.nextElement();
           String name = element.getAttribute("name", null);

           if (name != null) {
               appendToList(name, null);
           }
        }
    }
    
    private void appendToList(String name, Image img) {
        ((List) displayable).append(name, img);
    }
    
    /**
     * Creates a simple form with <code>text</code> as the only item.
     * 
     * @param text the text to be displayed.
     */
    public void createSimpleForm(String message) {
        System.out.println("text-ing");
        displayable = new Form(title);
        ((Form) displayable).append(new StringItem("Status: ", message));
    }
    
    /**
     * Creates a displayable from an xml string.
     *
     * @param xml a string of the form:
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
                   listElements.add(name);
               }
           }
           
           createSimpleList();
       } else {
           text = xml.getAttribute("msg", "Warte...");
           createSimpleForm(text);
       }
    }
    
    public String getXmlString() {
        XMLElement xml = new XMLElement();
        xml.setAttribute("title", title);
        xml.setAttribute("id", id);
        if ((list) && (listElements != null) && (!listElements.isEmpty())) {
            xml.setAttribute("list", Boolean.TRUE);
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

    /**
     * A request that is generated, if a certain command has been executed.
     * 
     * @param cmd the command.
     * @param disp the displayable prior to the command.
     * @return a request.
     */
    public Request handleCommand(Command cmd, Displayable disp) {
        if (list) {
           // find selected index
           int selected = ((List) displayable).getSelectedIndex();

           // send index to server
           Request r = new Request();
           r.setParam("selected", Integer.toString(selected));

           // attach id if possible
           if (id != null) {
              r.setParam("id", id);
           }

            return r;

        }
        return null;
    }

    public Displayable getDisplayable() {
        return displayable;
    }
}
