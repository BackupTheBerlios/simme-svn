// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: MenuTester.java
//                  $Date: 2004/04/03 23:40:00 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import at.einspiel.simme.nanoxml.XMLElement;

/**
 * This class is used to visually test menus.
 * @author kariem
 */
public class MenuTester extends JFrame {

    private static final URL MENU_URL = MenuTester.class.getResource("test-menumanager-simplenavigation.xml");

    private MenuManager mgr;
    private JPanel contentPane;
    private JPanel centerPane;
    private JButton backButton;

    private JPanel oldPanel;
    
    /**
     * Creates a new instance of <code>MenuTester</code>.
     * @param menuUrl the menu's URL.
     * @throws MenuCreateException if an error occurs while creating the menu.
     */
    public MenuTester(URL menuUrl) throws MenuCreateException, IOException {
        super("Menu Tester");
        mgr = MenuManager.getMenuManager(menuUrl);

        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);
        
        // editor pane
        JEditorPane textPane = new JEditorPane(menuUrl);
        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setPreferredSize(new Dimension(300, 500));
        scroll.setBorder(new TitledBorder("Source"));
        contentPane.add(scroll, BorderLayout.WEST);
        
        // center pane
        centerPane = new JPanel();
        contentPane.add(centerPane, BorderLayout.CENTER);
        
        // meta controls
        JPanel controls = new JPanel();
        backButton = new JButton("Go to last");
        backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});
        backButton.setEnabled(false);
        controls.add(backButton);
        contentPane.add(controls, BorderLayout.NORTH);
		
		
		// load interior menu
        loadMenu(mgr.getMenu());
        
    }

	void goBack() {
		if (oldPanel != null) {
			loadPanel(oldPanel);
		}
		backButton.setEnabled(false);
	}


	private void loadMenu(IMenu menu) {
		loadPanel(createPanel(menu));
    }

	private void loadPanel(JPanel panel) {
		oldPanel = centerPane;
    	contentPane.remove(centerPane);
    	centerPane = panel;
		contentPane.add(centerPane, BorderLayout.CENTER);
        pack();
	}

	/**
     * Creates a clickable panel from a menu.
     * @param menu the menu.
     * @return the newly created panel.
     */
    private JPanel createPanel(IMenu menu) {
        return new MenuPanel(menu);
    }

    void load(byte currentMenu, int selectedPosition) {
        IMenu newMenu = mgr.getMenu(currentMenu, selectedPosition);
        loadMenu(newMenu);
    }

    /**
     * Runs the menu tester.
     * @param args command line arguments.
     * @throws MenuCreateException if an error occurs while creating the frame.
     */
    public static void main(String[] args) throws MenuCreateException, IOException {
        MenuTester frame = new MenuTester(MENU_URL);
        frame.addWindowListener(new WindowAdapter() {
            /** @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent) */
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.show();
    }

    class MenuPanel extends JPanel {

        byte menuId;
        private String name;

        MenuPanel(IMenu menu) {
            super(new BorderLayout());
            XMLElement xml = new XMLElement();
            xml.parseString(menu.getXml());

            // id
            menuId = (byte) xml.getAttributeInt("id", 0);

            // name
            name = xml.getName();

            // title label
            JLabel lblTitle = new JLabel(xml.getAttribute("title", "## no title ##"));
            add(lblTitle, BorderLayout.NORTH);

            if (xml.getAttributeBoolean("list")) {
                JPanel interiorPanel = new JPanel(new GridLayout(0, 1));
                Enumeration enum = xml.enumerateChildren();
                int itemPos = 0;
                while (enum.hasMoreElements()) {
                    XMLElement element = (XMLElement) enum.nextElement();
                    String childName = element.getContent();
                    JButton btnChild = new JButton(childName);
                    btnChild.addActionListener(new ButtonListener(itemPos));
                    interiorPanel.add(btnChild);
                    itemPos++;
                }
                add(interiorPanel, BorderLayout.CENTER);
            } else if (name.equals("text")) {
                String text = (String) xml.getAttribute("msg");
                if (text == null || text.length() == 0) {
                    if (xml.countChildren() > 0) {
                        text = ((XMLElement) xml.getChildren().elementAt(0)).getContent();
                    }
                    // fall-back instead of showing nothing
                    text = "## no text ##";
                }
                JLabel lblText = new JLabel(text);
                add(lblText, BorderLayout.CENTER);
                backButton.setEnabled(true);
            }
        }

        private final class ButtonListener implements ActionListener {
            
            private int itemPos;

            ButtonListener(int itemPos) {
                this.itemPos = itemPos;
            }
            
            /** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent) */
            public void actionPerformed(ActionEvent e) {
                load(menuId, itemPos);
            }
        }
    }
}