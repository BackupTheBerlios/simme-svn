/*
 * Created on 20.04.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package test.sim;

import java.util.Enumeration;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import nanoxml.XMLElement;

/**
 * @author jorge
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DynamicUI extends List implements CommandListener {

   /**
    * @param arg0
    * @param arg1
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

      // TODO Auto-generated constructor stub
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
