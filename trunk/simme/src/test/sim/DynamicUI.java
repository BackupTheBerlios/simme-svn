package test.sim;

import java.util.Enumeration;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import nanoxml.XMLElement;

/**
 * This class is intended to create dynamically a user interface from the
 * information found in an xml string.
 * 
 * @author jorge
 */
public class DynamicUI extends List implements CommandListener {

   /**
    * Creates a new instance of this object with list entries built from the
    * data found in the given xml string.
    * 
    * @param xmlString information to build the user interface.
    */
   public DynamicUI(String xmlString) {
      super("", List.EXCLUSIVE);
      addCommand(new Command("Ausloggen", Command.EXIT, 0));
      setCommandListener(this);
      XMLElement xml = new XMLElement();

      xml.parseString(xmlString);
      String title = xml.getAttribute("title", "Auswahl");
      setTitle(title);

      Enumeration enumeration = xml.enumerateChildren();

      while (enumeration.hasMoreElements()) {
         XMLElement element = (XMLElement) enumeration.nextElement();
         String name = element.getAttribute("name", null);
         if (name != null) {
            append(name, null);
         }

      }
   }

   /** @see CommandListener#commandAction(Command, Displayable) */
   public void commandAction(Command cmd, Displayable d) {

      if (cmd.getCommandType() == Command.EXIT) {
         Display display = Display.getDisplay(Sim.getInstance());
         display.setCurrent(Sim.getMainScreen());
      } else {
         // TODO selektierten index auswählen
         // TODO index an server schicken
         // TODO antwort von server in DynamicUI stecken
         // TODO neue DynamicUI anzeigen.
      }
   }
}
