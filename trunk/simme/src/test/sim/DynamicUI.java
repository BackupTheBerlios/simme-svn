package test.sim;

import java.io.IOException;

import java.util.Enumeration;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;

import test.sim.net.Request;
import test.sim.net.SendableUI;

/**
 * This class is intended to create dynamically a user interface from the
 * information found in an xml string. For this purpose it uses {@link
 * SendableUI}.
 *
 * @author jorge
 */
public class DynamicUI implements CommandListener {
    private static final String COMM_PATH = "dynamicState";

    SendableUI ui;
    Displayable disp;

    /**
     * Creates a new instance of this object with list entries built from the
     * data found in the given xml string.
     *
     * @param xmlString information to build the user interface.
     */
    public DynamicUI(String xmlString) {
        System.out.println("xmlString=" + xmlString);

        ui = new SendableUI(xmlString);

        getDisplayable();

        disp.addCommand(new Command("Exit", Command.EXIT, 0));
        disp.setCommandListener(this);
    }

    /**
     * Creates a new dynamic user interface.
     * 
     * @param message the message to display.
     * @param url the address to connect to.
     */
    public DynamicUI(String message, String url) {
        System.out.println("message=" + message);
        ui = new SendableUI(message);

        getDisplayable();

        disp.addCommand(new Command("Abort", Command.CANCEL, 0));
        disp.setCommandListener(this);
        connect(url);
    }

    /**
     * Connects to the specified address and updates the user interface with
     * the information found in the given URL.
     * 
     * @param url the url.
     */
    private void connect(String url) {
        ConnectorThread connector = new ConnectorThread(url);
        connector.start();
    }

    /** @see CommandListener#commandAction(Command, Displayable) */
    public void commandAction(Command cmd, Displayable displayable) {
        Display d = Sim.getDisplay();

        if (cmd.getCommandType() == Command.EXIT) {
            d.setCurrent(Sim.getMainScreen());
        } else {
            Request r = handleCommand();
            if (r == null) {
                return;
            }
            r.sendRequest(COMM_PATH);

            String response = null;

            try {
                response = new String(r.getResponse());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // load response into UI
            ui.initialize(response);

            // show new UI
            d.setCurrent(getDisplayable());
        }
    }

    /**
     * A request that is generated, if a certain command has been executed.
     * 
     * @return a request.
     */
    private Request handleCommand() {
        if (ui.isList()) {
            // find selected index
            int selected = ((List) disp).getSelectedIndex();

            // send index to server
            Request r = new Request();
            r.setParam("selected", Integer.toString(selected));

            // attach id if possible
            r.setParam("id", ui.getId());

            return r;

        }
        return null;
    }

    /**
     * Returns the current displayable object of the dynamic user interface.
     *
     * @return a displayable.
     */
    protected Displayable getDisplayable() {
        disp = makeDisplayable(ui);
        return disp;
    }

    Displayable makeDisplayable(SendableUI sui) {
        Displayable d;
        String title = sui.getTitle();
        if (sui.isList()) {
            d = new List(title, List.IMPLICIT);

            Enumeration enum = sui.getListElements().elements();
            while (enum.hasMoreElements()) {
                String name = (String) enum.nextElement();

                if (name != null) {
                    appendToList((List) d, name, null);
                }
            }
        } else if (sui.hasXmlInfo()) {
            // build game with xml information
            Game g = new Game(sui.getXmlInfo());
            d = new Zeichenblatt(false); // TODO Georg start game, create Zeichenblatt, ...
        } else {
            d = new Form(title);
            ((Form) d).append(new StringItem("Status:", sui.getText()));
        }
        return d;
    }

    private void appendToList(List l, String name, Image img) {
        (l).append(name, img);
    }

    /** Establishes a connection and updates the user interface. */
    class ConnectorThread extends Thread {

        String url;

        /**
         * Creates a new thread that connects to <code>url</code>.
         * @param url the address to connect to
         */
        public ConnectorThread(String url) {
            this.url = url;
            // TODO implement connection and error handling - update UI accordingly
        }

        /** @see java.lang.Thread#run() */
        public void run() {
            // send the request
            Request r = new Request();
            r.sendRequest(url);

            try {
                String response = new String(r.getResponse());
                ui = new SendableUI(response);
            } catch (IOException e) {
                // error occured
                ui = new SendableUI("connection broken");
            }
        }
    }
}