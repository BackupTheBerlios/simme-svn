package test.sim;

import nanoxml.XMLElement;
import nanoxml.XMLParseException;

import test.sim.net.Request;

import java.io.IOException;

import java.util.Enumeration;

import javax.microedition.lcdui.*;


/**
 * This class is intended to create dynamically a user interface from the
 * information found in an xml string.
 *
 * @author jorge
 */
public class DynamicUI implements CommandListener
{
  private static final String COMM_PATH = "dynamicState.jsp";
  String title;
  private String stateID;
  private Displayable displayable;
  private boolean list;

  /**
   * Creates a new instance of this object with list entries built from the
   * data found in the given xml string.
   *
   * @param xmlString information to build the user interface.
   */
  public DynamicUI(String xmlString)
  {
    System.out.println("xmlString=" + xmlString);

    init(xmlString);

    displayable.addCommand(new Command("Ende", Command.EXIT, 0));
    displayable.setCommandListener(this);
  }

  /**
   * Initialization.
   *
   * @param xmlString the message to build this <code>DynamicUI</code>
   */
  private void init(String xmlString)
  {
    XMLElement xml = new XMLElement();

    try
    {
      xml.parseString(xmlString);
      makeXmlDisplayable(xml);
    }
    catch (XMLParseException xex)
    {
      title = "Information";
      makeTextDisplayable(xmlString);
    }
  }

  /**
   * Creates a displayable from an xml string.
   *
   * @param xml a string of the form:
   * <p>
   * <pre>
   *    &lt;element title="Title" id="ID" list="true" &gt;
   *        &lt;child name="Name1" /&gt;
   *        &lt;child name="Name2" /&gt;
   *        &lt;child name="Name3" /&gt;
   *    /&gt;
   * </pre>
   * </p>
   * or
   * <p>
   * <pre>
   *    &lt;element title="Title"
   *                id="ID"
   *                list="false"
   *                msg="Text /&gt;
   * </pre>
   * </p>
   */
  private void makeXmlDisplayable(XMLElement xml)
  {
    System.out.println("xmlling");

    // set title
    title = xml.getAttribute("title", "Auswahl");

    // set id, if available
    String id = xml.getAttribute("id", null);

    if (id != null)
    {
      stateID = id;
    }

    // show either list, or simple status message
    if (xml.getBooleanAttribute("list", false))
    {
      list = true;
      displayable = new List(title, List.IMPLICIT);

      Enumeration enumeration = xml.enumerateChildren();

      while (enumeration.hasMoreElements())
      {
        XMLElement element = (XMLElement) enumeration.nextElement();
        String name = element.getAttribute("name", null);

        if (name != null)
        {
          ((List) displayable).append(name, null);
        }
      }
    }
    else
    {
      makeTextDisplayable(xml.getAttribute("msg", "Warten"));
    }
  }

  private void makeTextDisplayable(String text)
  {
    System.out.println("texting");
    displayable = new Form(title);
    ((Form) displayable).append(new StringItem("Status: ", text));
  }

  /** @see CommandListener#commandAction(Command, Displayable) */
  public void commandAction(Command cmd, Displayable disp)
  {
    Display d = Sim.getDisplay();

    if (cmd.getCommandType() == Command.EXIT)
    {
      d.setCurrent(Sim.getMainScreen());
    }
    else
    {
      if (list)
      {
        // find selected index
        int selected = ((List) displayable).getSelectedIndex();

        // send index to server
        Request r = new Request();
        r.setParam("selected", Integer.toString(selected));

        if (stateID != null)
        {
          // attach id if possible
          r.setParam("id", stateID);
        }

        r.sendRequest(COMM_PATH);

        String response = null;

        try
        {
          response = new String(r.getResponse());
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }

        // load response into UI
        init(response);

        // show new UI
        d.setCurrent(getDisplayable());
      }
    }
  }

  /**
   * Returns the current displayable object of the dynamic user interface.
   *
   * @return a displayable.
   */
  protected Displayable getDisplayable()
  {
    return displayable;
  }
}
