package test.sim;

import java.util.Enumeration;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;

import nanoxml.XMLElement;
import nanoxml.XMLParseException;

/**
 * This class is intended to create dynamically a user interface from the
 * information found in an xml string.
 * 
 * @author jorge
 */
public class DynamicUI implements CommandListener {

   String title;
   private Displayable displayable;

   /**
    * Creates a new instance of this object with list entries built from the
    * data found in the given xml string.
    * 
    * @param xmlString information to build the user interface.
    */
   public DynamicUI(String xmlString) {
      System.out.println("xmlString=" + xmlString);
      XMLElement xml = new XMLElement();

      try {
         xml.parseString(xmlString);
         makeXmlDisplayable(xml);
      } catch (XMLParseException xex) {
         title = "Information";
         makeTextDisplayable(xmlString);
      }

      displayable.addCommand(new Command("Ausloggen", Command.EXIT, 0));
      displayable.setCommandListener(this);
   }

   private void makeXmlDisplayable(XMLElement xml) {
      System.out.println("xmlling");
      title = xml.getAttribute("title", "Auswahl");
      displayable = new List(title, List.IMPLICIT);

      Enumeration enumeration = xml.enumerateChildren();

      while (enumeration.hasMoreElements()) {
         XMLElement element = (XMLElement) enumeration.nextElement();
         String name = element.getAttribute("name", null);
         if (name != null) {
            ((List) displayable).append(name, null);
         }
      }
   }

   private void makeTextDisplayable(String text) {
      System.out.println("texting");
      displayable = new Form(title);
      ((Form) displayable).append(new StringItem("Status: ", text));
   }

   /** @see CommandListener#commandAction(Command, Displayable) */
   public void commandAction(Command cmd, Displayable d) {
      Display display = Sim.getDisplay();

      if (cmd.getCommandType() == Command.EXIT) {
         display.setCurrent(Sim.getMainScreen());
      } else {
         // TODO selektierten index ausw�hlen
         // TODO index an server schicken
         // TODO antwort von server in DynamicUI stecken
         // TODO neue DynamicUI anzeigen.
      }
   }

   /**
    * Returns the current displayable object of the dynamic user interface.
    * 
    * @return a displayable.
    */
   protected Displayable getDisplayable() {
      return displayable;
   }
}
