// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: AbstractXMLTestCase.java
//                  $Date: 2004/09/07 13:30:36 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.util;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.custommonkey.xmlunit.XMLTestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Provides means for testing xml classes. As of the current implementation
 * this class uses a single document builder and a single transformer.
 * @author kariem
 */
public class AbstractXMLTestCase extends XMLTestCase {

   protected PrintStream output = null;
   
   private static DocumentBuilder builder;
   static {
      try {
         builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   private static Transformer transformer;
   static {
      try {
         transformer = TransformerFactory.newInstance().newTransformer();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   /**
    * Sets the default output for verbose comparison of xml nodes with xml
    * strings.
    * 
    * @param outputStream the output stream used to verbose.
    */
   protected void setOutput(PrintStream outputStream) {
      output = outputStream;
   }

   /**
    * (copied from DocumentBuilder.parse(String))
    * Parse the content of the given URI as an XML document
    * and return a new DOM {@link org.w3c.dom.Document} object.
    *
    * @param uri The location of the content to be parsed.
    * @exception IOException If any IO errors occur.
    * @exception SAXException If any parse errors occur.
    * @exception IllegalArgumentException If the URI is null.
    * 
    * @return A new DOM Document object.
    */
   protected synchronized Document parse(String uri) throws SAXException, IOException,
         IllegalArgumentException {
      return builder.parse(uri);
   }

   /**
    * Adds node list elements to a list.
    * @param nodeList the node list.
    * @param list the list.
    */
   protected void addElements(NodeList nodeList, List list) {
      for (int i = 0; i < nodeList.getLength(); i++) {
         list.add(nodeList.item(i));
      }
   }

   /**
    * Compares the xml representation of a node with a given string.
    * 
    * @param xmlNode the node.
    * @param xmlString the string.
    * @param outputStream the output print stream.
    * 
    * @throws TransformerException If an unrecoverable error occurs during the
    *          course of the transformation.
    * @throws IOException If any IO errors occur.
    * @throws SAXException If any parse errors occur.
    * @throws ParserConfigurationException if a DocumentBuilder cannot be
    *          created which satisfies the configuration requested.
    * @throws IllegalArgumentException If the InputSource is null.
    */
   protected void assertXMLEqual(Node xmlNode, String xmlString, PrintStream outputStream)
         throws TransformerException, SAXException, IOException,
         ParserConfigurationException {
      
	      StringWriter writer = new StringWriter();
	      synchronized (transformer) {
	         transformer.transform(new DOMSource(xmlNode), new StreamResult(writer));
	      }
      if (outputStream != null) {
	      outputStream.println("comparing");
	      outputStream.print("\t");
	      outputStream.println(writer.toString());
	      outputStream.println(xmlString);
      }
      assertXMLEqual(writer.toString(), xmlString);
   }
   
   /**
    * Compares the xml representation of a node with a given string.
    * 
    * @param xmlNode the node.
    * @param xmlString the string.
    * 
    * @throws TransformerException If an unrecoverable error occurs during the
    *          course of the transformation.
    * @throws IOException If any IO errors occur.
    * @throws SAXException If any parse errors occur.
    * @throws ParserConfigurationException if a DocumentBuilder cannot be
    *          created which satisfies the configuration requested.
    * @throws IllegalArgumentException If the InputSource is null.
    */
   protected void assertXMLEqual(Node xmlNode, String xmlString)
   throws TransformerException, SAXException, IOException,
   ParserConfigurationException {
      assertXMLEqual(xmlNode, xmlString, output);
   }
}
