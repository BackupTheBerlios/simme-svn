/* XmlParser.java
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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class provides convenience methods to access XML content. The xerces
 * classes are required to use this class.
 * 
 * @author kariem
 */
public class XmlParser {
   /** Document used by this XML parser */
   private Document document;
   /** Globally reusable document */
   private static Document reusableDocument;
   /** Globally reusable document builder */
   private static DocumentBuilder reusableDocBuilder;

   /**
    * Constructor for this Parser.
    * @param filename name of the file to be parsed.
    * @throws IOException if an error occurs when accessing the file system.
    * @throws SAXException if an error occurs in the process of parsing the
    * file.
    */
   public XmlParser(String filename) throws IOException, SAXException {
      this(filename, true);
   }

   /**
    * Creates a new <code>XmlParser</code> from a document with a possible 
    * validation
    * @param filename The filename this parser should read.
    * @param validation Indicates validation or no validation
    * @throws IOException if an error occurs when accessing the file system.
    * @throws SAXException if an error occurs in the process of parsing the
    * file.
    */
   public XmlParser(String filename, boolean validation) throws IOException, SAXException {

      DOMParser parser = new DOMParser();
      if (validation) {
         parser.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", true);
         parser.setFeature("http://xml.org/sax/features/validation", true);
         parser.setFeature("http://xml.org/sax/features/namespaces", true);
         parser.setFeature("http://apache.org/xml/features/validation/schema", true);
         parser.setFeature(
            "http://apache.org/xml/features/dom/include-ignorable-whitespace",
            false);
      }
      parser.setErrorHandler(new CustomizedErrorHandler());

      parser.parse(new InputSource(new FileInputStream(new File(filename))));
      document = parser.getDocument();
   }

   /**
    * Creates a new <code>Document</code> without using validation.
    * @return An instance of <code>Document</code>.
    * @throws ParserConfigurationException if the underlying XML implementation
    * throws errors.
    */
   public static Document newDocument() throws ParserConfigurationException {
      return getReusableBuilder().newDocument();
   }

   /**
    * Loads the document from a specified filename. This method does not
    * validate the contents of the xml file.
    * @param filename The filename of the file to load.
    * @return Document An instance of the Document representing the contents
    * of the file.
    * @throws ParserConfigurationException if the underlying XML implementation
    * throws errors.
    * @throws SAXException if a parsing error has occured in the process of
    * reading the file.
    * @throws IOException if the file cannot be found, is not accessible or
    * cannot be read.
    */
   public static Document loadDocument(String filename)
      throws ParserConfigurationException, SAXException, IOException {
      return getReusableBuilder().parse(new File(filename));
   }

   /**
    * Loads the document from a specified input stream. This method does not
    * validate the contents of the xml file.
    * 
    * @param is the input stream with the data
    * 
    * @return Document An instance of the Document representing the contents
    *         of the file.
    * @throws ParserConfigurationException if the underlying XML implementation
    *         throws errors.
    * @throws SAXException if a parsing error has occured in the process of
    *         reading the file.
    * @throws IOException if the file cannot be found, is not accessible or
    *         cannot be read.
    */
   public static Document loadDocument(InputStream is)
      throws SAXException, IOException, ParserConfigurationException {
      return getReusableBuilder().parse(is);
   }

   /**
    * Loads the document from a specified filename. This method validates the
    * contents of the xml file, if the parameter <code>validation</code> is set
    * to <code>true</code>.
    * @param filename The filename of the file to load.
    * @param validation A boolean indicating the request for validation.
    * <code>true</code> enables validation, while <code>false</code> may be
    * compared to the method {@link #loadDocument(String)}.
    * @return Document An instance of the Document representing the contents
    * of the file.
    * @throws SAXException if a parsing error has occured in the process of
    * reading the file.
    * @throws IOException if the file cannot be found, is not accessible or
    * cannot be read.
    */
   public static Document loadDocument(String filename, boolean validation)
      throws SAXException, IOException {
      XmlParser p = new XmlParser(filename, validation);
      return p.getDocument();
   }

   /**
    * A simple class that handles minor errors by printing them including
    * their line number.
    *
    * @author kariem
    */
   class CustomizedErrorHandler extends DefaultHandler {
      /** @see DefaultHandler#warning(SAXParseException) */
      public void warning(SAXParseException ex) {
         System.err.println("Warning: (line " + ex.getLineNumber() + ") " + ex.getMessage());
      }
      /** @see DefaultHandler#error(SAXParseException) */
      public void error(SAXParseException ex) {
         System.err.println("Error: (line " + ex.getLineNumber() + ") " + ex.getMessage());
      }
      /** @see DefaultHandler#fatalError(SAXParseException) */
      public void fatalError(SAXParseException ex) {
         System.err.println(
            "Fatal Error: (line " + ex.getLineNumber() + ") " + ex.getMessage());
      }
   }

   /**
    * Returns the current XML document.
    * @return the <code>Document</code> of this parser.
    */
   public Document getDocument() {
      return document;
   }

   /**
    * Returns a reusable XML document.
    * @return A reusable <code>Document</code>.
    * @throws ParserConfigurationException If an error has occured within
    * the configuration of the used XML parser.
    */
   public static Document getReuseDocument() throws ParserConfigurationException {
      if (reusableDocument == null) {
         reusableDocument = newDocument();
      }
      return reusableDocument;
   }

   /**
    * Returns a reusable <code>DocumentBuilder</code>. If no instance exists yet
    * a new object will be instantiated.
    * @return A new <code>DocumentBuilder</code> or a reference to an already
    * existing.
    * @throws ParserConfigurationException If there is an error with the
    * underlying XML configuration.
    */
   private static DocumentBuilder getReusableBuilder() throws ParserConfigurationException {
      if (reusableDocBuilder == null) {
         reusableDocBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      }
      return reusableDocBuilder;
   }

   /** Returns the child elements of a specified <code>Node</code> with a
    * given name.
    * @param parentNode the node which will be searched.
    * @param nodeName the name of the nodes to search for.
    * @return a <code>List</code> of DOM elements, that are direct children of
    * the given parent node.
    */
   public static List getChildElements(Node parentNode, String nodeName) {
      NodeList nodes = parentNode.getChildNodes();
      List children = new ArrayList();
      for (int i = 0; i < nodes.getLength(); i++) {
         Node n = nodes.item(i);
         if (n instanceof Element) {
            Element e = (Element) n;
            if (e.getNodeName().equals(nodeName)) {
               children.add(e);
            }
         }
      }
      return children;
   }

   /** Returns all child elements of a specified <code>Node</code> that are
    * direct children of this <code>Node</code>.
    * @return a <code>List</code> of DOM elements, that are direct children of
    * the given parent node.
    * @param parentNode the node which will be searched.
    */
   public static List getAllChildElements(Node parentNode) {
      NodeList nodes = parentNode.getChildNodes();
      List children = new ArrayList();
      for (int i = 0; i < nodes.getLength(); i++) {
         Node n = nodes.item(i);
         if (n instanceof Element) {
            children.add(n);
         }
      }
      return children;
   }

}
