// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: XMLUtils.java
//                  $Date: 2004/04/03 23:38:36 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Provides methods for faster XML parsing.
 * @author kariem
 */
public class XMLUtils {

   private XMLUtils() {
      // private constructor for utility class
   }
   
   /**
    * Returns the child elements of a specified <code>Element</code>.
    * 
    * @param parentElement the element which will be searched.
    * 
    * @return a <code>List</code> of DOM elements, that are direct children of
    *          the given parent node.
    */
   public static List getDirectChildren(Element parentElement) {
      List directChildren = new ArrayList();
      Node child = parentElement.getFirstChild();
      while (child != null) {
         if (child.getNodeType() == Node.ELEMENT_NODE) {
            directChildren.add(child);
         }
         child = child.getNextSibling();
      }
      return directChildren;
   }
   
   /**
    * Returns the text contained in the given element.
    * @param e the element.
    * @return the text in the first textnode within the element, or
    *          <code>null</code>, if the element does not contain any text
    *          nodes.
    */
   public static String getText(Element e) {
       Node child = e.getFirstChild();
       while (child != null) {
           if (child.getNodeType() == Node.TEXT_NODE) {
               // return the text of the text node
               return child.getNodeValue();
           }
       }
       return null;
   }
}
