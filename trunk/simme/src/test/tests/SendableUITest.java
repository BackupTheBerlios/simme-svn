/************************************************************************
** PROJECT:   Simme
** $Workfile: SendableUITest.java $
** $Revision: 1.1 $
** LANGUAGE:  Java, JDK version 1.4
**
** COPYRIGHT: Frequentis Nachrichtentechnik Ges.m.b.H.
**            Spittelbreitengasse 34
**            A-1120 VIENNA
**            AUSTRIA
**            tel +43 1 811 50-0
**
** The copyright to the computer program(s) herein
** is the property of Frequentis Nachrichtentechnik
** Ges.m.b.H., Austria.
** The program(s) shall not be used and/or copied without
** the written permission of Frequentis Nachrichtentechnik.
**
** $Log: SendableUITest.java,v $
** Revision 1.1  2003/09/17 16:45:05  kariem
** corrected sendable UI, several small updates
**
**
************************************************************************/

package test.tests;

import junit.framework.TestCase;
import test.sim.net.SendableUI;

/**
 * Tests methods in {@linkplain net.SendableUI}
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
