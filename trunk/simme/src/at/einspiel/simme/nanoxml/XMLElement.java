/* XMLElement.java
 *
 * $Revision: 1.8 $
 * $Date: 2004/09/28 21:06:08 $
 * $Name:  $
 *
 * This file is part of NanoXML 2 Lite.
 * Copyright (C) 2000-2002 Marc De Scheemaecker, All Rights Reserved.
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from the
 * use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
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
package at.einspiel.simme.nanoxml;

import at.einspiel.simme.client.util.StringReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * XMLElement is a representation of an XML object. The object is able to parse
 * XML code.
 * <P><DL>
 * <DT><B>Parsing XML Data</B></DT>
 * <DD>
 * You can parse XML data using the following code:
 * <UL><CODE>
 * XMLElement xml = new XMLElement();<BR>
 * FileReader reader = new FileReader("filename.xml");<BR>
 * xml.parseFromReader(reader);
 * </CODE></UL></DD></DL>
 * <DL><DT><B>Retrieving Attributes</B></DT>
 * <DD>
 * You can enumerate the attributes of an element using the method
 * {@link #enumerateAttributeNames() enumerateAttributeNames}.
 * The attribute values can be retrieved using the method
 * {@link #getAttribute(java.lang.String)}.
 * The following example shows how to list the attributes of an element:
 * <UL><CODE>
 * XMLElement element = ...;<BR>
 * Enumeration enum = element.getAttributeNames();<BR>
 * while (enum.hasMoreElements()) {<BR>
 * &nbsp;&nbsp;&nbsp;&nbsp;String key = (String) enum.nextElement();<BR>
 * &nbsp;&nbsp;&nbsp;&nbsp;String value = element.getStringAttribute(key);<BR>
 * &nbsp;&nbsp;&nbsp;&nbsp;System.out.println(key + " = " + value);<BR>
 * }
 * </CODE></UL></DD></DL>
 * <DL><DT><B>Retrieving Child Elements</B></DT>
 * <DD>
 * You can enumerate the children of an element using
 * {@link #enumerateChildren() enumerateChildren}.
 * The number of child elements can be retrieved using
 * {@link #countChildren() countChildren}.
 * </DD></DL>
 * <DL><DT><B>Elements Containing Character Data</B></DT>
 * <DD>
 * If an elements contains character data, like in the following example:
 * <UL><CODE>
 * &lt;title&gt;The Title&lt;/title&gt;
 * </CODE></UL>
 * you can retrieve that data using the method
 * {@link #getContent() getContent}.
 * </DD></DL>
 * <DL><DT><B>Subclassing XMLElement</B></DT>
 * <DD>
 * When subclassing XMLElement, you need to override the method
 * createAnotherElement()
 * which has to return a new copy of the receiver.
 * </DD></DL>
 * <P>
 *
 *
 * <p>Changes made by Kariem Hussein (april 2003):
 * <ul>
 *   <li>Removed all methods containing <code>double</code>s.</li>
 *   <li>Removed char array parsing.</li>
 *   <li>Changed getChildren() to return the children instead of a clone.</li>
 *   <li>Removed minor (was 2) and major (was 2) version numbers and UID.</li>
 *   <li>Shortened/changed some javadoc comments.</li>
 *   <li>Removed deprecated methods.</li>
 *   <li>Removed getStringAttr, setStringAttr.</li>
 *   <li>changed boolean handling.</li>
 *   <li>Removed linenr.</li>
 *   <li>Set all protected methods except exceptions to private.</li>
 *   <li>changed constructor calling chain (ignoreWhitespace defaults to
 *       <code>false</code>.</li>
 *   <li>Changed names of getAttribute methods for int and bool.</li>
 *   <li>Added String values for booleans values.</li>
 *   <li>Removed ignoreCase.</li>
 * </ul>
 * </p>
 *
 * @see XMLParseException
 *
 * @author Marc De Scheemaecker
 *         &lt;<A href="mailto:cyberelf@mac.com">cyberelf@mac.com</A>&gt;
 * @version 2.2
 */
public class XMLElement {

    /** Represents true as a string */
    public static final String TRUE = "true";
    /** Represents false as a string */
    public static final String FALSE = "false";

    /**
     * The attributes given to the element.
     *
     * <dl><dt><b>Invariants:</b></dt><dd>
     * <ul><li>The field can be empty.
     *     <li>The field is never <code>null</code>.
     *     <li>The keys and the values are strings.
     * </ul></dd></dl>
     */
    private final Hashtable attributes;

    /**
     * Child elements of the element.
     *
     * <dl><dt><b>Invariants:</b></dt><dd>
     * <ul><li>The field can be empty.
     *     <li>The field is never <code>null</code>.
     *     <li>The elements are instances of <code>XMLElement</code>
     *         or a subclass of <code>XMLElement</code>.
     * </ul></dd></dl>
     */
    private final Vector children;

    /**
     * The name of the element.
     *
     * <dl><dt><b>Invariants:</b></dt><dd>
     * <ul><li>The field is <code>null</code> iff the element is not
     *         initialized by either parse or setName.
     *     <li>If the field is not <code>null</code>, it's not empty.
     *     <li>If the field is not <code>null</code>, it contains a valid
     *         XML identifier.
     * </ul></dd></dl>
     */
    private String name;

    /**
     * The #PCDATA content of the object.
     *
     * <dl><dt><b>Invariants:</b></dt><dd>
     * <ul><li>The field is <code>null</code> iff the element is not a
     *         #PCDATA element.
     *     <li>The field can be any string, including the empty string.
     * </ul></dd></dl>
     */
    private String contents;

    /**
     * Conversion table for &amp;...; entities. The keys are the entity names
     * without the &amp; and ; delimiters.
     *
     * <dl><dt><b>Invariants:</b></dt><dd>
     * <ul><li>The field is never <code>null</code>.
     *     <li>The field always contains the following associations:
     *         "lt"&nbsp;=&gt;&nbsp;"&lt;", "gt"&nbsp;=&gt;&nbsp;"&gt;",
     *         "quot"&nbsp;=&gt;&nbsp;"\"", "apos"&nbsp;=&gt;&nbsp;"'",
     *         "amp"&nbsp;=&gt;&nbsp;"&amp;"
     *     <li>The keys are strings
     *     <li>The values are char arrays
     * </ul></dd></dl>
     */
    private Hashtable entities;

    /**
     * <code>true</code> if the leading and trailing whitespace of #PCDATA
     * sections have to be ignored.
     */
    private boolean ignoreWhitespace;

    /**
     * Character read too much.
     * This character provides push-back functionality to the input reader
     * without having to use a PushbackReader.
     * If there is no such character, this field is '\0'.
     */
    private char charReadTooMuch;

    /**
     * The reader provided by the caller of the parse method.
     *
     * <dl><dt><b>Invariants:</b></dt><dd>
     * <ul><li>The field is not <code>null</code> while the parse method
     *         is running.
     * </ul></dd></dl>
     */
    private Reader reader;

    /**
     * The current line number in the source content.
     *
     * <dl><dt><b>Invariants:</b></dt><dd>
     * <ul><li>parserLineNr &gt; 0 while the parse method is running.
     * </ul></dd></dl>
     */
    private int parserLineNr;

    /**
     * Creates and initializes a new XML element with an empty hash table.
     */
    public XMLElement() {
        this(new Hashtable(), false, true);
    }

    /**
     * Creates and initializes a new XML element.
     *
     * @param entities The entity conversion table.
     * @param skipLeadingWhitespace <code>true</code> if leading and trailing
     *        whitespace in PCDATA content has to be removed.
     * @param fillBasicConversionTable <code>true</code> if the basic entities
     *        need to be added to the entity list.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>entities != null</code>
     *     <li>if <code>fillBasicConversionTable == false</code>
     *         then <code>entities</code> contains at least the following
     *         entries: <code>amp</code>, <code>lt</code>, <code>gt</code>,
     *         <code>apos</code> and <code>quot</code>
     * </ul></dd></dl>
     *
     */
    protected XMLElement(
        Hashtable entities,
        boolean skipLeadingWhitespace,
        boolean fillBasicConversionTable) {
        this.ignoreWhitespace = skipLeadingWhitespace;
        this.name = null;
        this.contents = "";
        this.attributes = new Hashtable();
        this.children = new Vector();
        this.entities = entities;

        Enumeration e = this.entities.keys();

        while (e.hasMoreElements()) {
            Object key = e.nextElement();
            Object value = this.entities.get(key);

            if (value instanceof String) {
                value = ((String) value).toCharArray();
                this.entities.put(key, value);
            }
        }

        if (fillBasicConversionTable) {
            this.entities.put("amp", new char[] { '&' });
            this.entities.put("quot", new char[] { '"' });
            this.entities.put("apos", new char[] { '\'' });
            this.entities.put("lt", new char[] { '<' });
            this.entities.put("gt", new char[] { '>' });
        }
    }

    /**
     * Adds a child element.
     *
     * @param child
     *     The child element to add.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>child != null</code>
     *     <li><code>child.getName() != null</code>
     *     <li><code>child</code> does not have a parent element
     * </ul></dd></dl>
     *
     */
    public void addChild(XMLElement child) {
        this.children.addElement(child);
    }

    /**
     * Adds or modifies an attribute.
     *
     * @param name
     *     The name of the attribute.
     * @param value
     *     The value of the attribute.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>name != null</code>
     *     <li><code>name</code> is a valid XML identifier
     *     <li><code>value != null</code>
     * </ul></dd></dl>
     *
     */
    public void setAttribute(String name, Object value) {
        if (value != null) {
            this.attributes.put(name, value.toString());
        }
    }

    /**
     * Adds or modifies an attribute.
     *
     * @param name
     *     The name of the attribute.
     * @param value
     *     The value of the attribute.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>name != null</code>
     *     <li><code>name</code> is a valid XML identifier
     * </ul></dd></dl>
     *
     */
    public void setIntAttribute(String name, int value) {
        this.attributes.put(name, Integer.toString(value));
    }

    /**
     * Returns the number of child elements of the element.
     *
     * @return number of children elements.
     */
    public int countChildren() {
        return this.children.size();
    }

    /**
     * Enumerates the attribute names.
     *
     * <dl><dt><b>Postconditions:</b></dt><dd>
     * <ul><li><code>result != null</code>
     * </ul></dd></dl>
     * @return enumeration of attribute names
     */
    public Enumeration enumerateAttributeNames() {
        return this.attributes.keys();
    }

    /**
     * Enumerates the child elements.
     *
     * <dl><dt><b>Postconditions:</b></dt><dd>
     * <ul><li><code>result != null</code>
     * </ul></dd></dl>
     *
     * @return enumeration of children
     */
    public Enumeration enumerateChildren() {
        return this.children.elements();
    }

    /**
     * Returns the child elements as a Vector. It is not safe to modify this
     * Vector.
     *
     * <dl><dt><b>Postconditions:</b></dt><dd>
     * <ul><li><code>result != null</code>
     * </ul></dd></dl>
     *
     * @return children.
     */
    public Vector getChildren() {
        return this.children;
    }

    /**
     * Returns the PCDATA content of the object. If there is no such content,
     * <CODE>null</CODE> is returned.
     *
     * @return contents
     */
    public String getContent() {
        return this.contents;
    }

    /**
     * Returns an attribute of the element.
     * If the attribute doesn't exist, <code>null</code> is returned.
     *
     * @param attrName The name of the attribute.
     *
     * @return the attribute identified by <code>name</code>.
     *
     */
    public String getAttribute(String attrName) {
        return this.getAttribute(attrName, null);
    }

    /**
     * Returns an attribute of the element.
     * If the attribute doesn't exist, <code>defaultValue</code> is returned.
     *
     * @param attrName The name of the attribute.
     * @param defaultValue Key to use if the attribute is missing.
     *
     * @return the attribute.
     */
    public String getAttribute(String attrName, String defaultValue) {
        Object value = this.attributes.get(attrName);

        if (value == null) {
            value = defaultValue;
        }

        return (String) value;
    }

    /**
      * Returns an attribute of the element.
      * If the attribute doesn't exist, <code>0</code> is returned.
      *
      * @param attrName The name of the attribute.
      *
      * @return the value of the attribute identified by <code>name</code>, or
      *         <code>0</code> if the attribute is missing.
      *
      */
    public int getAttributeInt(String attrName) {
        return this.getAttributeInt(attrName, 0);
    }

    /**
     * Returns an attribute of the element.
     * If the attribute doesn't exist, <code>defaultValue</code> is returned.
     *
     * @param attrName         The name of the attribute.
     * @param defaultValue Key to use if the attribute is missing.
     *
     * @return the value of the attribute identified by <code>name</code>, or
     *         <code>defaultValue</code> if the attribute is missing.
     */
    public int getAttributeInt(String attrName, int defaultValue) {
        String value = (String) this.attributes.get(attrName);

        if (value == null) {
            return defaultValue;
        }
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw this.invalidValue(attrName, value);
        }
    }

    /**
     * Returns an attribute of the element.
     * If the attribute doesn't exist, <code>defaultValue</code> is returned.
     *
     * @param attrName     The name of the attribute.
     * @param trueValue    The value associated with <code>true</code>.
     * @param falseValue   The value associated with <code>true</code>.
     * @param defaultValue Value to use if the attribute is missing.
     *
     * @return <code>true</code> If the value of the attribute is equal to
     *         <code>trueValue</code>. If the value of the attribute is equal to
     *         <code>falseValue</code>., <code>false</code> is returned. If the
     *         attribute is missing, <code>defaultValue</code> is returned.
     *
     * @throws XMLParseException if the value neither is equal to
     * <code>trueValue</code> nor to <code>falseValue</code>.
     *
     */
    public boolean getAttributeBoolean(
        String attrName,
        String trueValue,
        String falseValue,
        boolean defaultValue)
        throws XMLParseException {
        Object value = this.attributes.get(attrName);

        if (value == null) {
            return defaultValue;
        } else if (value.equals(trueValue)) {
            return true;
        } else if (value.equals(falseValue)) {
            return false;
        } else {
            throw this.invalidValue(attrName, (String) value);
        }
    }

    /**
     * Returns an attribute of the element.
     *
     * @param attrName Name of the attribute.
     * @param defaultValue Value to use if the attribute is missing.
     * @return see {@link #getAttributeBoolean(String, String, String, boolean)}.
     */
    public boolean getAttributeBoolean(String attrName, boolean defaultValue) {
        return getAttributeBoolean(attrName, TRUE, FALSE, defaultValue);
    }

    /**
     * Returns an attribute of the element.
     *
     * @param attrName Name of the attribute.
     *
     * @return the value of the attribute <code>attrName</code> or
     *          <code>false</code>, if the attribute could not be found.
     */
    public boolean getAttributeBoolean(String attrName) {
        return getAttributeBoolean(attrName, false);
    }

    /**
     * Returns the name of the element.
     *
     * @return element's name.
     *
     * @see XMLElement#setName(java.lang.String) setName(String)
     */
    public String getName() {
        return this.name;
    }

    /**
     * Reads one XML element from a java.io.Reader and parses it.
     *
     * @param r The reader from which to retrieve the XML data.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>r = null</code>
     *     <li><code>r</code> is not closed
     * </ul></dd></dl>
     *
     * <dl><dt><b>Postconditions:</b></dt><dd>
     * <ul><li>the state of the receiver is updated to reflect the XML element
     *         parsed from the reader
     *     <li>the reader points to the first character following the last
     *         '&gt;' character of the XML element
     * </ul></dd></dl><dl>
     *
     * @throws IOException
     *     If an error occured while reading the input.
     * @throws XMLParseException
     *     If an error occured while parsing the read data.
     */
    public void parseFromReader(Reader r) throws IOException, XMLParseException {
        this.parseFromReader(r, /*startingLineNr*/
        1);
    }

    /**
     * Reads one XML element from a java.io.Reader and parses it.
     *
     * @param r The reader from which to retrieve the XML data.
     * @param startingLineNr
     *     The line number of the first line in the data.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>r != null</code>
     *     <li><code>r</code> is not closed
     * </ul></dd></dl>
     *
     * <dl><dt><b>Postconditions:</b></dt><dd>
     * <ul><li>the state of the receiver is updated to reflect the XML element
     *         parsed from the reader
     *     <li>the reader points to the first character following the last
     *         '&gt;' character of the XML element
     * </ul></dd></dl><dl>
     *
     * @throws IOException If an error occured while reading the input.
     * @throws XMLParseException If an error occured while parsing the
     *         read data.
     */
    public void parseFromReader(Reader r, int startingLineNr)
        throws IOException, XMLParseException {
        this.charReadTooMuch = '\0';
        this.reader = r;
        this.parserLineNr = startingLineNr;

        while (true) {
            char ch = this.scanWhitespace();

            if (ch != '<') {
                throw this.expectedInput("<");
            }

            ch = this.readChar();

            if ((ch == '!') || (ch == '?')) {
                this.skipSpecialTag(0);
            } else {
                this.unreadChar(ch);
                this.scanElement(this);

                return;
            }
        }
    }

    /**
     * Reads one XML element from a String and parses it.
     *
     * @param string The string that holds the data
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>string != null</code>
     *     <li><code>string.length() &gt; 0</code>
     * </ul></dd></dl>
     *
     * <dl><dt><b>Postconditions:</b></dt><dd>
     * <ul><li>the state of the receiver is updated to reflect the XML element
     *         parsed from the reader
     * </ul></dd></dl><dl>
     *
     * @throws XMLParseException If an error occured while parsing the string.
     */
    public void parseString(String string) throws XMLParseException {
        try {
            this.parseFromReader(new StringReader(string), 1);
        } catch (IOException e) {
            throw new XMLParseException("IOException while parsing", e.getMessage());
        }
    }

    /**
     * Reads one XML element from a String and parses it.
     *
     * @param string The string that holds the data
     * @param offset The first character in <code>string</code> to scan.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>string != null</code>
     *     <li><code>offset &lt; string.length()</code>
     *     <li><code>offset &gt;= 0</code>
     * </ul></dd></dl>
     *
     * <dl><dt><b>Postconditions:</b></dt><dd>
     * <ul><li>the state of the receiver is updated to reflect the XML element
     *         parsed from the reader
     * </ul></dd></dl><dl>
     *
     * @throws XMLParseException
     *     If an error occured while parsing the string.
     */
    public void parseString(String string, int offset) throws XMLParseException {
        this.parseString(string.substring(offset));
    }

    /**
     * Reads one XML element from a String and parses it.
     *
     * @param string The string that holds the data
     * @param offset
     *     The first character in <code>string</code> to scan.
     * @param end
     *     The character where to stop scanning.
     *     This character is not scanned.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>string != null</code>
     *     <li><code>end &lt;= string.length()</code>
     *     <li><code>offset &lt; end</code>
     *     <li><code>offset &gt;= 0</code>
     * </ul></dd></dl>
     *
     * <dl><dt><b>Postconditions:</b></dt><dd>
     * <ul><li>the state of the receiver is updated to reflect the XML element
     *         parsed from the reader
     * </ul></dd></dl><dl>
     *
     * @throws XMLParseException If an error occured while parsing the string.
     */
    public void parseString(String string, int offset, int end) throws XMLParseException {
        this.parseString(string.substring(offset, end));
    }

    /**
     * Reads one XML element from a String and parses it.
     *
     * @param string The string that holds the data
     * @param offset
     *     The first character in <code>string</code> to scan.
     * @param end
     *     The character where to stop scanning.
     *     This character is not scanned.
     * @param startingLineNr
     *     The line number of the first line in the data.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>string != null</code>
     *     <li><code>end &lt;= string.length()</code>
     *     <li><code>offset &lt; end</code>
     *     <li><code>offset &gt;= 0</code>
     * </ul></dd></dl>
     *
     * <dl><dt><b>Postconditions:</b></dt><dd>
     * <ul><li>the state of the receiver is updated to reflect the XML element
     *         parsed from the reader
     * </ul></dd></dl><dl>
     *
     * @throws XMLParseException If an error occured while parsing the string.
     */
    public void parseString(String string, int offset, int end, int startingLineNr)
        throws XMLParseException {
        string = string.substring(offset, end);

        try {
            this.parseFromReader(new StringReader(string), startingLineNr);
        } catch (IOException e) {
            throw new XMLParseException("IOException while parsing", e.getMessage());
        }
    }

    /**
     * Removes a child element.
     *
     * @param child
     *     The child element to remove.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>child != null</code>
     *     <li><code>child</code> is a child element of the receiver
     * </ul></dd></dl>
     *
     * <dl><dt><b>Postconditions:</b></dt><dd>
     * <ul><li>countChildren() => old.countChildren() - 1
     *     <li>enumerateChildren() => old.enumerateChildren() - child
     *     <li>getChildren() => old.enumerateChildren() - child
     * </ul></dd></dl><dl>
     *
     */
    public void removeChild(XMLElement child) {
        this.children.removeElement(child);
    }

    /**
     * Removes an attribute.
     *
     * @param attrName
     *     The name of the attribute.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>attrName != null</code>
     *     <li><code>attrName</code> is a valid XML identifier
     * </ul></dd></dl>
     *
     * <dl><dt><b>Postconditions:</b></dt><dd>
     * <ul><li>enumerateAttributeNames()
     *         => old.enumerateAttributeNames() - attrName
     *     <li>getAttribute(name) => <code>null</code>
     * </ul></dd></dl><dl>
     *
     */
    public void removeAttribute(String attrName) {
        this.attributes.remove(attrName);
    }

    /**
     * Creates a new similar XML element.
     * <P>
     * You should override this method when subclassing XMLElement.
     *
     * @return a new element.
     */
    protected XMLElement createAnotherElement() {
        return new XMLElement(this.entities, this.ignoreWhitespace, false);
    }

    /**
     * Changes the content string.
     *
     * @param content
     *     The new content string.
     */
    public void setContent(String content) {
        this.contents = content;
    }

    /**
     * Changes the name of the element.
     *
     * @param name
     *     The new name.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>name != null</code>
     *     <li><code>name</code> is a valid XML identifier
     * </ul></dd></dl>
     *
     * @see XMLElement#getName()
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Writes the XML element to a string.
     *
     * @return the string representation.
     * 
     * @see Object#toString()
     * @see XMLElement#write(java.io.Writer) write(Writer)
     */
    public String toString() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            this.write(writer);
            writer.flush();

            return new String(out.toByteArray());
        } catch (IOException e) {
            // Java exception handling suxx
            return super.toString();
        }
    }

    /**
     * Writes the XML element to a writer.
     *
     * @param writer
     *     The writer to write the XML data to.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>writer != null</code>
     *     <li><code>writer</code> is not closed
     * </ul></dd></dl>
     *
     * @throws IOException If the data could not be written to the writer.
     *
     * @see XMLElement#toString()
     */
    public void write(Writer writer) throws IOException {
        if (this.name == null) {
            this.writeEncoded(writer, this.contents);

            return;
        }

        writer.write('<');
        writer.write(this.name);

        if (!this.attributes.isEmpty()) {
            Enumeration e = this.attributes.keys();

            while (e.hasMoreElements()) {
                writer.write(' ');

                String key = (String) e.nextElement();
                String value = (String) this.attributes.get(key);
                writer.write(key);
                writer.write('=');
                writer.write('"');
                this.writeEncoded(writer, value);
                writer.write('"');
            }
        }

        if ((this.contents != null) && (this.contents.length() > 0)) {
            writer.write('>');
            this.writeEncoded(writer, this.contents);
            writer.write('<');
            writer.write('/');
            writer.write(this.name);
            writer.write('>');
        } else if (this.children.isEmpty()) {
            writer.write('/');
            writer.write('>');
        } else {
            writer.write('>');

            Enumeration e = this.enumerateChildren();

            while (e.hasMoreElements()) {
                XMLElement child = (XMLElement) e.nextElement();
                child.write(writer);
            }

            writer.write('<');
            writer.write('/');
            writer.write(this.name);
            writer.write('>');
        }
    }

    /**
     * Writes a string encoded to a writer.
     *
     * @param writer The writer to write the XML data to.
     * @param str The string to write encoded.
     *
     * @throws IOException if an error occurs while reading.
     *
     */
    protected void writeEncoded(Writer writer, String str) throws IOException {
        for (int i = 0; i < str.length(); i += 1) {
            char ch = str.charAt(i);

            switch (ch) {
                case '<' :
                    writer.write('&');
                    writer.write('l');
                    writer.write('t');
                    writer.write(';');

                    break;

                case '>' :
                    writer.write('&');
                    writer.write('g');
                    writer.write('t');
                    writer.write(';');

                    break;

                case '&' :
                    writer.write('&');
                    writer.write('a');
                    writer.write('m');
                    writer.write('p');
                    writer.write(';');

                    break;

                case '"' :
                    writer.write('&');
                    writer.write('q');
                    writer.write('u');
                    writer.write('o');
                    writer.write('t');
                    writer.write(';');

                    break;

                case '\'' :
                    writer.write('&');
                    writer.write('a');
                    writer.write('p');
                    writer.write('o');
                    writer.write('s');
                    writer.write(';');

                    break;

                default :

                    int unicode = ch;

                    if ((unicode < 32) || (unicode > 126)) {
                        writer.write('&');
                        writer.write('#');
                        writer.write('x');
                        writer.write(Integer.toString(unicode, 16));
                        writer.write(';');
                    } else {
                        writer.write(ch);
                    }
            }
        }
    }

    /**
     * Scans an identifier from the current reader.
     * The scanned identifier is appended to <code>result</code>.
     *
     * @param result
     *     The buffer in which the scanned identifier will be put.
     *
     * @throws IOException if an error occurs while reading.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>result != null</code>
     *     <li>The next character read from the reader is a valid first
     *         character of an XML identifier.
     * </ul></dd></dl>
     *
     * <dl><dt><b>Postconditions:</b></dt><dd>
     * <ul><li>The next character read from the reader won't be an identifier
     *         character.
     * </ul></dd></dl><dl>
     */
    private void scanIdentifier(StringBuffer result) throws IOException {
        while (true) {
            char ch = this.readChar();

            if (((ch < 'A') || (ch > 'Z'))
                && ((ch < 'a') || (ch > 'z'))
                && ((ch < '0') || (ch > '9'))
                && (ch != '_')
                && (ch != '.')
                && (ch != ':')
                && (ch != '-')
                && (ch <= '\u007E')) {
                this.unreadChar(ch);

                return;
            }

            result.append(ch);
        }
    }

    /**
     * This method scans an identifier from the current reader.
     *
     * @return the next character following the whitespace.
     * @throws IOException if an error occurs while reading.
     */
    private char scanWhitespace() throws IOException {
        while (true) {
            char ch = this.readChar();

            switch (ch) {
                case ' ' :
                case '\t' :
                case '\n' :
                case '\r' :
                    break;

                default :
                    return ch;
            }
        }
    }

    /**
     * This method scans an identifier from the current reader.
     * The scanned whitespace is appended to <code>result</code>.
     *
     * @return the next character following the whitespace.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>result != null</code>
     * </ul></dd></dl>
     *
     * @param result StringBuffer
     * @throws IOException if an error occurs while reading.
     */
    private char scanWhitespace(StringBuffer result) throws IOException {
        while (true) {
            char ch = this.readChar();

            switch (ch) {
                case ' ' :
                case '\t' :
                case '\n' :
                    result.append(ch);

                case '\r' :
                    break;

                default :
                    return ch;
            }
        }
    }

    /**
     * This method scans a delimited string from the current reader.
     * The scanned string without delimiters is appended to
     * <code>string</code>.
     *
     * @param string string to scan.
     *
     * @throws IOException if an error occurs while reading.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>string != null</code>
     *     <li>the next char read is the string delimiter
     * </ul></dd></dl>
     */
    private void scanString(StringBuffer string) throws IOException {
        char delimiter = this.readChar();

        if ((delimiter != '\'') && (delimiter != '"')) {
            throw this.expectedInput("' or \"");
        }

        while (true) {
            char ch = this.readChar();

            if (ch == delimiter) {
                return;
            } else if (ch == '&') {
                this.resolveEntity(string);
            } else {
                string.append(ch);
            }
        }
    }

    /**
     * Scans a #PCDATA element. CDATA sections and entities are resolved.
     * The next &lt; char is skipped.
     * The scanned data is appended to <code>data</code>.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>data != null</code>
     * </ul></dd></dl>
     *
     * @param data buffer.
     * @throws IOException if an error occurs while reading.
     */
    private void scanPCData(StringBuffer data) throws IOException {
        while (true) {
            char ch = this.readChar();

            if (ch == '<') {
                ch = this.readChar();

                if (ch == '!') {
                    this.checkCDATA(data);
                } else {
                    this.unreadChar(ch);

                    return;
                }
            } else if (ch == '&') {
                this.resolveEntity(data);
            } else {
                data.append(ch);
            }
        }
    }

    /**
     * Scans a special tag and if the tag is a CDATA section, append its
     * content to <code>buf</code>.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>buf != null</code>
     *     <li>The first &lt; has already been read.
     * </ul></dd></dl>
     *
     * @param buf buffer.
     *
     * @return some boolean
     * @throws IOException if an error occurs while reading.
     */
    private boolean checkCDATA(StringBuffer buf) throws IOException {
        char ch = this.readChar();

        if (ch != '[') {
            this.unreadChar(ch);
            this.skipSpecialTag(0);

            return false;
        } else if (!this.checkLiteral("CDATA[")) {
            this.skipSpecialTag(1); // one [ has already been read

            return false;
        } else {
            int delimiterCharsSkipped = 0;

            while (delimiterCharsSkipped < 3) {
                ch = this.readChar();

                switch (ch) {
                    case ']' :

                        if (delimiterCharsSkipped < 2) {
                            delimiterCharsSkipped += 1;
                        } else {
                            buf.append(']');
                            buf.append(']');
                            delimiterCharsSkipped = 0;
                        }

                        break;

                    case '>' :

                        if (delimiterCharsSkipped < 2) {
                            for (int i = 0; i < delimiterCharsSkipped; i++) {
                                buf.append(']');
                            }

                            delimiterCharsSkipped = 0;
                            buf.append('>');
                        } else {
                            delimiterCharsSkipped = 3;
                        }

                        break;

                    default :

                        for (int i = 0; i < delimiterCharsSkipped; i += 1) {
                            buf.append(']');
                        }

                        buf.append(ch);
                        delimiterCharsSkipped = 0;
                }
            }

            return true;
        }
    }

    /**
     * Skips a comment.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li>The first &lt;!-- has already been read.
     * </ul></dd></dl>
     * @throws IOException if an error occurs while reading.
     */
    private void skipComment() throws IOException {
        int dashesToRead = 2;

        while (dashesToRead > 0) {
            char ch = this.readChar();

            if (ch == '-') {
                dashesToRead -= 1;
            } else {
                dashesToRead = 2;
            }
        }

        if (this.readChar() != '>') {
            throw this.expectedInput(">");
        }
    }

    /**
     * Skips a special tag or comment.
     *
     * @param bracketLevel The number of open square brackets ([) that have
     *                     already been read.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li>The first &lt;! has already been read.
     *     <li><code>bracketLevel >= 0</code>
     * </ul></dd></dl>
     * @throws IOException if an error occurs while reading.
     */
    private void skipSpecialTag(int bracketLevel) throws IOException {
        int tagLevel = 1; // <
        char stringDelimiter = '\0';

        if (bracketLevel == 0) {
            char ch = this.readChar();

            if (ch == '[') {
                bracketLevel += 1;
            } else if (ch == '-') {
                ch = this.readChar();

                if (ch == '[') {
                    bracketLevel += 1;
                } else if (ch == ']') {
                    bracketLevel -= 1;
                } else if (ch == '-') {
                    this.skipComment();

                    return;
                }
            }
        }

        while (tagLevel > 0) {
            char ch = this.readChar();

            if (stringDelimiter == '\0') {
                if ((ch == '"') || (ch == '\'')) {
                    stringDelimiter = ch;
                } else if (bracketLevel <= 0) {
                    if (ch == '<') {
                        tagLevel += 1;
                    } else if (ch == '>') {
                        tagLevel -= 1;
                    }
                }

                if (ch == '[') {
                    bracketLevel += 1;
                } else if (ch == ']') {
                    bracketLevel -= 1;
                }
            } else {
                if (ch == stringDelimiter) {
                    stringDelimiter = '\0';
                }
            }
        }
    }

    /**
     * Scans the data for literal text.
     * Scanning stops when a character does not match or after the complete
     * text has been checked, whichever comes first.
     *
     * @param literal the literal to check.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li><code>literal != null</code>
     * </ul></dd></dl>
     *
     * @throws IOException if an error occurs while reading.
     *
     * @return some boolean
     *
     */
    private boolean checkLiteral(String literal) throws IOException {
        int length = literal.length();

        for (int i = 0; i < length; i += 1) {
            if (this.readChar() != literal.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Reads a character from a reader.
     *
     * @throws IOException if an error occurs while reading.
     *
     * @return some char
     */
    private char readChar() throws IOException {
        if (this.charReadTooMuch != '\0') {
            char ch = this.charReadTooMuch;
            this.charReadTooMuch = '\0';

            return ch;
        }
        int i = this.reader.read();
        
        if (i < 0) {
            throw this.unexpectedEndOfData();
        } else if (i == 10) {
            this.parserLineNr += 1;
            
            return '\n';
        } else {
            return (char) i;
        }
    }

    /**
     * Scans an XML element.
     *
     * @param elt The element that will contain the result.
     *
     * @throws IOException if an error occurs while reading.
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li>The first &lt; has already been read.
     *     <li><code>elt != null</code>
     * </ul></dd></dl>
     */
    private void scanElement(XMLElement elt) throws IOException {
        StringBuffer buf = new StringBuffer();
        this.scanIdentifier(buf);

        String n = buf.toString();
        elt.setName(n);

        char ch = this.scanWhitespace();

        while ((ch != '>') && (ch != '/')) {
            buf.setLength(0);
            this.unreadChar(ch);
            this.scanIdentifier(buf);

            String key = buf.toString();
            ch = this.scanWhitespace();

            if (ch != '=') {
                throw this.expectedInput("=");
            }

            this.unreadChar(this.scanWhitespace());
            buf.setLength(0);
            this.scanString(buf);
            elt.setAttribute(key, buf);
            ch = this.scanWhitespace();
        }

        if (ch == '/') {
            ch = this.readChar();

            if (ch != '>') {
                throw this.expectedInput(">");
            }

            return;
        }

        buf.setLength(0);
        ch = this.scanWhitespace(buf);

        if (ch != '<') {
            this.unreadChar(ch);
            this.scanPCData(buf);
        } else {
            while (true) {
                ch = this.readChar();

                if (ch == '!') {
                    if (this.checkCDATA(buf)) {
                        this.scanPCData(buf);

                        break;
                    } 
                    ch = this.scanWhitespace(buf);

                    if (ch != '<') {
                        this.unreadChar(ch);
                        this.scanPCData(buf);

                        break;
                    }
                } else {
                    buf.setLength(0);

                    break;
                }
            }
        }

        if (buf.length() == 0) {
            while (ch != '/') {
                if (ch == '!') {
                    ch = this.readChar();

                    if (ch != '-') {
                        throw this.expectedInput("Comment or Element");
                    }

                    ch = this.readChar();

                    if (ch != '-') {
                        throw this.expectedInput("Comment or Element");
                    }

                    this.skipComment();
                } else {
                    this.unreadChar(ch);

                    XMLElement child = this.createAnotherElement();
                    this.scanElement(child);
                    elt.addChild(child);
                }

                ch = this.scanWhitespace();

                if (ch != '<') {
                    throw this.expectedInput("<");
                }

                ch = this.readChar();
            }

            this.unreadChar(ch);
        } else {
            if (this.ignoreWhitespace) {
                elt.setContent(buf.toString().trim());
            } else {
                elt.setContent(buf.toString());
            }
        }

        ch = this.readChar();

        if (ch != '/') {
            throw this.expectedInput("/");
        }

        this.unreadChar(this.scanWhitespace());

        if (!this.checkLiteral(n)) {
            throw this.expectedInput(n);
        }

        if (this.scanWhitespace() != '>') {
            throw this.expectedInput(">");
        }
    }

    /**
     * Resolves an entity. The name of the entity is read from the reader.
     * The value of the entity is appended to <code>buf</code>.
     *
     * @param buf Where to put the entity value.
     *
     * @throws IOException if an error occurs while reading.
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li>The first &amp; has already been read.
     *     <li><code>buf != null</code>
     * </ul></dd></dl>
     */
    private void resolveEntity(StringBuffer buf) throws IOException {
        char ch = '\0';
        StringBuffer keyBuf = new StringBuffer();

        while (true) {
            ch = this.readChar();

            if (ch == ';') {
                break;
            }

            keyBuf.append(ch);
        }

        String key = keyBuf.toString();

        if (key.charAt(0) == '#') {
            try {
                if (key.charAt(1) == 'x') {
                    ch = (char) Integer.parseInt(key.substring(2), 16);
                } else {
                    ch = (char) Integer.parseInt(key.substring(1), 10);
                }
            } catch (NumberFormatException e) {
                throw this.unknownEntity(key);
            }

            buf.append(ch);
        } else {
            char[] value = (char[]) this.entities.get(key);

            if (value == null) {
                throw this.unknownEntity(key);
            }

            buf.append(value);
        }
    }

    /**
     * Pushes a character back to the read-back buffer.
     *
     * @param ch The character to push back.
     *
     * </dl><dl><dt><b>Preconditions:</b></dt><dd>
     * <ul><li>The read-back buffer is empty.
     *     <li><code>ch != '\0'</code>
     * </ul></dd></dl>
     */
    private void unreadChar(char ch) {
        this.charReadTooMuch = ch;
    }

    /**
     * Creates a new <code>XMLParseException</code>.
     *
     * @param attrName name of invalid value
     * @return new XMLParseException.
     */
    protected XMLParseException invalidValueSet(String attrName) {
        String msg = "Invalid value set (entity name = \"" + attrName + "\")";

        return new XMLParseException(this.getName(), this.parserLineNr, msg);
    }

    /**
     * Creates a new <code>XMLParseException</code>.
     *
     * @param attrName name of invalid value
     * @param value the value
     * @return new XMLParseException.
     */
    protected XMLParseException invalidValue(String attrName, String value) {
        String msg =
            "Attribute \""
                + attrName
                + "\" does not contain a valid "
                + "value (\""
                + value
                + "\")";

        return new XMLParseException(this.getName(), this.parserLineNr, msg);
    }

    /**
     * Creates a new <code>XMLParseException</code>.
     *
     * @return new XMLParseException.
     */
    protected XMLParseException unexpectedEndOfData() {
        String msg = "Unexpected end of data reached";

        return new XMLParseException(this.getName(), this.parserLineNr, msg);
    }

    /**
     * Creates a new <code>XMLParseException</code>.
     *
     * @param context name of context
     * @return new XMLParseException.
     */
    protected XMLParseException syntaxError(String context) {
        String msg = "Syntax error while parsing " + context;

        return new XMLParseException(this.getName(), this.parserLineNr, msg);
    }

    /**
     * Creates a new <code>XMLParseException</code>.
     *
     * @param charSet name of expected input
     * @return new XMLParseException.
     */
    protected XMLParseException expectedInput(String charSet) {
        String msg = "Expected: " + charSet;

        return new XMLParseException(this.getName(), this.parserLineNr, msg);
    }

    /**
     * Creates a new <code>XMLParseException</code>.
     *
     * @param attrName name of unknown entity
     * @return new XMLParseException.
     */
    protected XMLParseException unknownEntity(String attrName) {
        String msg = "Unknown or invalid entity: &" + attrName + ";";

        return new XMLParseException(this.getName(), this.parserLineNr, msg);
    }
}
