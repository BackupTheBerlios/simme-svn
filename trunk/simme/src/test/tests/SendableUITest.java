package test.tests;

import junit.framework.TestCase;
import test.sim.net.SendableUI;

/**
 * Tests methods in {@linkplain SendableUI}
 * @author kariem
 */
public class SendableUITest extends TestCase {

    /** Constructor */
    public SendableUITest() {
        super("SendableUITest");
    }

    /** Tests for makeMessage(String,String) */
    public void testMessages() {
        String title = "Title";
        String message = "Test message";
        SendableUI sui = new SendableUI();
        sui.setTitle(title);
        sui.setText(message);
        System.out.println(sui.getXmlString());
    }
}
