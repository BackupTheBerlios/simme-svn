/* ParsedObject.java
 *
 * Copyright (C) 2003 Kariem Hussein
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from the
 * use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose in
 * non-commercial applications, and to alter it and redistribute it freely,
 * subject to the following restrictions:
 *
 *  1. The origin of this software must not be misrepresented; you must not
 *     claim that you wrote the original software. If you use this software in
 *     a product, an acknowledgment in the product documentation would be
 *     appreciated but is not required.
 *
 *  2. Altered source versions must be plainly marked as such, and must not be
 *     misrepresented as being the original software.
 *
 *  3. This notice may not be removed or altered from any source distribution.
 *****************************************************************************/

package at.einspiel.messaging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * This Object is used to easily convert from XML DOM to a simple data structure
 * using Collection classes. DOM Elements are represented by a
 * <code>ParsedObject</code> and their associated information is saved in
 * structures from the collection classes. Child elements are represented in a
 * <code>List</code> of <code>ParsedObject</code>s. Attributes are saved in a
 * <code>Map</code> conaining a name-value pair for each Attribute.
 *
 * @author kariem
 * @version 10.03.2003
 */
public class ParsedObject {

   /** Owner document of this instance's XML representation */
   private Document document;

   /**
    * List of <code>ParsedObject</code>s representing children of this
    * <code>Node</code> in the DOM tree.
    */
   private List children;

   /**
    * <code>Map</code> that holds information on the attributes of this Object
    */
   private Map attributes;

   /** String representing the unique id of this Object */
   private String id = null;

   /** Name of this <code>ParsedObject</code> */
   private String name;

   /** Text this <code>ParsedObject</code> holds */
   private String text;

   /** Parent of the current node  */
   private ParsedObject parent;

   /**
    * Builds a new <code>ParsedObject</code> from an XML element. This
    * constructor builds the fields name, text, attributes and children from
    * the information conained in the XML element. An attribute with the name
    * <code>id</code> will not be added to the list of attributes. Instead the
    * field id will be set with the adequate information.
    * 
    * @param parentObject sets a parent that may differ from the DOM Element's
    *         parent
    * @param element the element that holds the data for this ParsedObject
    */
   public ParsedObject(ParsedObject parentObject, Element element) {
      document = element.getOwnerDocument();
      //xmlElement = element;
      // set name of this Object to the tagname of the element
      name = element.getNodeName();
      //initialize
      init(parentObject);
      // read attributes and map them and their values accordingly
      NamedNodeMap nodeMap = element.getAttributes();
      if (nodeMap.getLength() > 0) {
         for (int i = 0; i < nodeMap.getLength(); i++) {
            Node current = nodeMap.item(i);
            if (current.getNodeName().equals("id")) {
               id = current.getNodeValue();
            } else {
               attributes.put(current.getNodeName(), current.getNodeValue());
            }
         }
      }
      // read child elements and add them as ParsedObjects
      List childElements = XmlParser.getAllChildElements(element);
      if (childElements.size() > 0) {
         for (Iterator i = childElements.iterator(); i.hasNext(); ) {
            Element child = (Element) i.next();
            children.add(new ParsedObject(this, child));
         }
      }
      // set Text to possible value of a containing text node
      NodeList elementNodes = element.getChildNodes();
      for (int j = 0; j < elementNodes.getLength(); j++) {
         Node n = elementNodes.item(j);
         if (n.getNodeType() == Node.TEXT_NODE) {
            String content = n.getNodeValue().trim();
            if (content.length() > 0) {
               text = content;
            }
            break; // if content.length < 1 then text will remain null;
         }
      }
   }

   /**
    * Builds a new <code>ParsedObject</code> from an DOM Element. The parent is
    * set to <code>null</code>.
    * @param element the element that holds the data for this ParsedObject
    */
   public ParsedObject(Element element) {
      this(null, element);
   }

   /**
    * Builds a new <code>ParsedObject</code> with the given name. Now new
    * document is being created for this instance, which saves some resources.
    * @param objectName The name of the new <code>ParsedObject</code>.
    * @throws ParserConfigurationException If there is a problem in the XML
    * configuration
    * @see ParsedObject#ParsedObject(String, boolean)
    */
   public ParsedObject(String objectName) throws ParserConfigurationException {
      this(objectName, true);
   }

   /**
    * Builds a new <code>ParsedObject</code> with the given name.
    * @param objectName The name of the new <code>ParsedObject</code>.
    * @param reuseOldDocument Indicates if a new <code>Document</code> should
    * be created or not.
    * @throws ParserConfigurationException If there is a problem in the XML
    * configuration
    */
   public ParsedObject(String objectName, boolean reuseOldDocument)
         throws ParserConfigurationException {
      this(null, reuseOldDocument ? XmlParser.getReuseDocument() : XmlParser
            .newDocument(), objectName);
   }

   /**
    * Builds a new <code>ParsedObject</code> with the given name, parent and
    * owner document.
    * @param parentObject The parent of this instance.
    * @param ownerDocument The document in which this instance is included
    * @param objectName The name of this object.
    */
   private ParsedObject(ParsedObject parentObject, Document ownerDocument,
         String objectName) {
      init(parentObject);
      document = ownerDocument;
      this.name = objectName;
   }

   /**
    * Creates a new <code>ParsedObject</code> from the information found in an
    * XML file. This method does not validate the XML content.
    * @param filename The filename that contains the information for the new
    * instance.
    * @return ParsedObject A newly created <code>Object</code> representing the
    * same information found in the given XML file.
    * @throws SAXException If the XML data contains syntactic or semantic
    * errors. These are related to non-wellformedness, because validation is
    * disabled for this method.
    * @throws ParserConfigurationException If the underlying XML implementation
    * and/or configuration throws errors.
    * @throws IOException If the file does not exist or is not readable.
    * @see #createNewInstance(String, boolean)
    */
   public static ParsedObject createNewInstance(String filename)
         throws SAXException, ParserConfigurationException, IOException {

      return new ParsedObject(XmlParser.loadDocument(filename)
            .getDocumentElement());
   }

   /**
    * Initialization code for <code>ParsedObject</code>.
    * @param parentObject Parent of this <code>ParsedObject</code>
    */
   private void init(ParsedObject parentObject) {
      this.parent = parentObject;
      // intialize attributes
      attributes = new HashMap();
      // initialize children
      children = new ArrayList();
   }

   /**
    * Creates a new <code>ParsedObject</code> from the information found in an
    * XML file.
    * 
    * @param filename The filename that contains the information for the new
    *         instance.
    * @param validation Indicates, if the content should be validated 
    * @return A newly created <code>Object</code> representing the
    *          same information found in the given XML file.
    * @throws SAXException If the XML data contains syntactic or semantic
    *          errors. These are related to non-wellformedness, because 
    *          validation is disabled for this method.
    * @throws IOException If the file does not exist or is not readable.
    */
   public static ParsedObject createNewInstance(String filename,
         boolean validation) throws SAXException, IOException {

      return new ParsedObject(XmlParser.loadDocument(filename, validation)
            .getDocumentElement());
   }

   /**
    * Creates a new <code>ParsedObject</code> from the information found in a
    * string.
    * 
    * @param string the String that holds the data.
    * @return A newly created <code>Object</code> holding the same
    *         information found in the given XML file.
    * @throws SAXException If the XML data contains syntactic or semantic
    *         errors. These are related to non-wellformedness, because
    *         validation is disabled for this method.
    * @throws ParserConfigurationException If the underlying XML implementation
    *         and/or configuration throws errors.
    * @throws IOException If the file does not exist or is not readable.
    * 
    * @see #parse(InputStream)
    */
   public static ParsedObject parse(String string) throws SAXException,
         IOException, ParserConfigurationException {
      InputStream is = new ByteArrayInputStream(string.getBytes());
      return parse(is);
   }

   /**
    * Creates a new <code>ParsedObject</code> from the information found in an
    * input stream.
    * 
    * @param is the input stream used to retrieve data.
    * @return A newly created <code>Object</code> holding the same
    *         information found in the given XML file.
    * @throws SAXException If the XML data contains syntactic or semantic
    *         errors. These are related to non-wellformedness, because
    *         validation is disabled for this method.
    * @throws ParserConfigurationException If the underlying XML implementation
    *         and/or configuration throws errors.
    * @throws IOException If the file does not exist or is not readable.
    */
   public static ParsedObject parse(InputStream is) throws SAXException,
         IOException, ParserConfigurationException {
      return new ParsedObject(XmlParser.loadDocument(is).getDocumentElement());
   }

   /**
    * May be used to detect empty children lists.
    * @return <code>false</code> if the children list is
    * <code>null</code> or empty, otherwise <code>true</code>.
    */
   public boolean hasChildren() {
      return children == null ? false : children.size() > 0 ? true : false;
   }

   /**
    * Shows the existence of attributes.
    * @return <code>false</code>, if the attribute map is <code>null</code> or
    * empty, otherwise <code>true</code>.
    */
   public boolean hasAttributes() {
      return attributes == null ? false : attributes.size() > 0 ? false : true;
   }

   /**
    * This method returns the attributes of this Object as a <code>map</code>.
    * The attribute names are in the keyset and their values map to them
    * accordingly. <br>
    * <em>Note:</em> The attribute <code>id</code> is not included in this
    * <code>Map</code>. It may be accessed via the method {@link #getId()}.
    * @return the attributes of this Object.
    */
   public Map getAttributes() {
      return attributes;
   }

   /**
    * Returns the value of an attribute identified by its <code>name</code>.
    * @param attrName The name of the attribute.
    * @return A String containing the value of the attribute identified by
    * <code>attrName</code>. Returns <code>null</code> if no attribute by this
    * name can be found.
    */
   public String getAttribute(String attrName) {
      return attributes == null ? null : (String) attributes.get(attrName);
   }

   /**
    * Adds an attribute to this instance of <code>ParsedObject</code>.
    * @param attrName The name of the newly created attribute.
    * @param value The value of the attribute.
    */
   public void addAttribute(String attrName, String value) {
      attributes.put(attrName, value);
   }

   /**
    * Returns the name of this <code>ParsedObject</code>. This name corresponds
    * to the tagname of the DOM Element.
    * @return the name of this Object.
    */
   public String getName() {
      return name;
   }

   /**
    * Sets the name of this <code>ParsedObject</code>.
    * @param newName The new name.
    */
   public void setName(String newName) {
      name = newName;
   }

   /**
    * Provides a list of <code>ParsedObject</code> that correspond to the child
    * DOM Elements of the DOM Element that is described by this
    * <code>ParsedObject</code>
    * @return a list of <code>ParsedObject</code> representing the children
    * of this Object.
    */
   public List getChildren() {
      return children;
   }

   /**
    * Adds a new child to the list of children.
    * @param newChild The new child to be added.
    */
   public void addChild(ParsedObject newChild) {
      newChild.setParent(this);
      children.add(newChild);
   }

   /**
    * Creates an XML <code>Element</code> from a given <code>ParsedObject</code>
    * and an additional document.
    * @param o The <code>ParsedObject containing the information</code>.
    * @param d The document to use for conversion.
    * @return A newly created Element holding information found in 
    * <code>o</code>.
    */
   private static Element createXml(ParsedObject o, Document d) {
      Element e = d.createElement(o.getName());
      Map attrs = o.getAttributes();
      // set attributes
      for (Iterator i = attrs.entrySet().iterator(); i.hasNext(); ) {
         Map.Entry entry = (Map.Entry) i.next();
         e.setAttribute((String) entry.getKey(), (String) entry.getValue());
      }
      // set children
      for (Iterator i = o.getChildren().iterator(); i.hasNext(); ) {
         ParsedObject child = (ParsedObject) i.next();
         e.appendChild(createXml(child, d));
      }
      String t = o.getText();
      if ((t != null) && (!t.equals(""))) {
         e.appendChild(d.createTextNode(t));
      }
      return e;
   }

   /**
    * Creates a new, empty child with the given name and the calling object
    * as parent and returns it.
    * @param childName The name of the new child.
    * @return A newly created, empty child.
    */
   public ParsedObject addChild(String childName) {
      // create new ParsedObject
      ParsedObject newChild = new ParsedObject(this, document, childName);
      children.add(newChild);
      return newChild;
   }

   /**
    * Updates an existing child, overwriting its position with a new one.
    * @param pos The position of the child to be overwritten.
    * @param updatedChild The new child to insert at this position.
    */
   public void setChild(int pos, ParsedObject updatedChild) {
      if ((pos < children.size() - 1) || (pos > -1)) {
         // update ParsedObject information
         updatedChild.setParent(this);
         // better than delete + add for ArrayList implementation
         children.set(pos, updatedChild);
      }
   }

   /**
    * Creates an empty child of this <code>ParsedObject</code>.
    * @param childName The name of the newly created child.
    * @return A new child of this <code>ParsedObject</code> with the given
    * name.
    */
   public ParsedObject getEmptyChild(String childName) {
      return new ParsedObject(this, document, childName);
   }

   /**
    * Returns the object saved in the <code>parent</code> field of this
    * <code>ParsedObject</code>.
    * @return The parent of this <code>ParsedObject</code>.
    */
   public ParsedObject getParent() {
      return parent;
   }

   /**
    * Sets the <code>parent</code> field of this object
    * @param newParent The new parent.
    */
   public void setParent(ParsedObject newParent) {
      parent = newParent;
   }

   /**
    * Returns the unique id of this object.
    * @return The unique id of this object. Returns <code>null</code>, if the id
    * was not set.
    */
   public String getId() {
      return id;
   }

   /**
    * This method returns the text between the enclosing opening and closing tag
    * elements of an Element. This is the text that this
    * <code>ParsedObject</code> holds.<br>
    * Example:<br>
    *   <pre>
    *       &lt;strip&gt;
    *          &lt;A&gt;value 1&lt;/A&gt;
    *          &lt;B&gt;value 2&lt;/B&gt;
    *          &lt;C&gt;value 3&lt;/C&gt;
    *          &lt;D&gt;&lt;/D&gt;
    *       &lt;/strip>
    *   </pre>
    *
    * <ul>
    * <li> A <code>ParsedObject</code> of element <code>&lt;A&gt;</code> returns
    * "value 1" as a result of calling this method.
    * <li> A <code>ParsedObject</code> of element <code>&lt;D&gt;</code> returns
    * <code>null</code>.
    * <li> A <code>ParsedObject</code> of element <code>&lt;strip&gt;</code>
    * returns <code>null</code>.
    * </ul>
    *
    * @return the text of this <code>ParsedObject</code>. If this ParsedObject
    * does not hold any text, <code>null</code> is returned.
    */
   public String getText() {
      return text;
   }

   /**
    * Sets the text of this <code>ParsedObject</code>
    * @param newText The new text.
    */
   public void setText(String newText) {
      text = newText;
   }

   /**
    * Returns the XML representation of this <code>ParsedObejct</code>.
    * @return The XML representation.
    */
   public Element getXmlRepresentation() {
      return createXml(this, document);
   }

   /*
    * Methods to visually test internals of this object
    */

   /**
    * @see #toString()
    */
   private String toString(String prefix) {
      StringBuffer b = new StringBuffer(prefix + "<" + name);
      if (attributes.size() > 0) {
         for (Iterator i = attributes.keySet().iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            String value = (String) attributes.get(key);
            b.append(" " + key + "=\"" + value + "\"");
         }
      }
      b.append(">\n");
      for (Iterator i = children.iterator(); i.hasNext(); ) {
         ParsedObject p = (ParsedObject) i.next();
         b.append(p.toString(prefix + "\t"));
      }
      if (text != null) {
         b.append(prefix + "\t" + text + "\n");
      }
      b.append(prefix + "</" + name + ">\n");
      return b.toString();
   }

   /**
    * @see Object#toString()
    */
   public String toString() {
      return toString("");
   }
}
