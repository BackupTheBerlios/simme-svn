// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: XMLUtils.java
//                  $Date: 2004/02/21 23:04:20 $
//              $Revision: 1.1 $
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
   public static List getDirectChildren(Element parentElement)
   {
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
}
