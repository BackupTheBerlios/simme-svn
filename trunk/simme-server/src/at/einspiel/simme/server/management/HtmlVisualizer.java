package at.einspiel.simme.server.management;

import java.util.Iterator;

/**
 * Is used to visualize data in different forms 
 */
public class HtmlVisualizer {

   /**
    * Shows the given SessionManager as HTML table.
    *
    * @param sMgr The SessionManager which should be shown as HTML table.
    * 
    * @return a String which shows <code>sMgr</code> as table.
    */
   public static String showAsTable(SessionManager sMgr) {
      StringBuffer buffer = new StringBuffer();

      buffer.append("<table border='0'>\n");
      buffer.append("<tr>\n");
      buffer.append("<td>Nickname</td>\n");
      buffer.append("<td>Model</td>\n");
      buffer.append("<td>Update</td>\n");
      buffer.append("<td>State</td>\n");
      buffer.append("</tr>\n");
      for (Iterator i = sMgr.userIterator(); i.hasNext();) {
         ManagedUser user = (ManagedUser) i.next();
         buffer.append("<tr>\n");

         buffer.append("\t<td>" + user.getNick() + "<td>\n");
         buffer.append("\t<td>" + user.getClientmodel() + "<td>\n");
         buffer.append("\t<td>" + user.secondsSinceLastUpdate() + "<td>\n");
         buffer.append("\t<td>" + user.getStateAsString() + "<td>\n");

         buffer.append("</tr>\n");
      }
      buffer.append("</table>");

      return buffer.toString();
   }
}
