// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: MenuTester.java
//                  $Date: 2004/06/07 09:25:45 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import at.einspiel.simme.nanoxml.XMLElement;

/**
 * This class is used to visually test menus.
 * @author kariem
 */
public class MenuTester extends JFrame {

    static final URL MENU_URL = MenuTester.class
            .getResource("test-menumanager-simplenavigation.xml");

    private MenuManager mgr;
    private JSplitPane splitPane;
    private JPanel centerPane;
    // back button on top
    JButton backButton;
    JFileChooser fileChooser;
    JEditorPane textPane;

    private JPanel oldPanel;

    /**
     * Creates a new instance of <code>MenuTester</code>.
     * @param menuUrl the menu's URL.
     * @throws MenuCreateException if an error occurs while creating the menu.
     * @throws IOException if the URL does not resolve to a readable text file.
     */
    public MenuTester(URL menuUrl) throws MenuCreateException, IOException {
        super("Menu Tester");

        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // editor pane
        textPane = new JEditorPane();
        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setPreferredSize(new Dimension(300, 500));
        scroll.setMinimumSize(new Dimension(300, 500));
        scroll.setBorder(new TitledBorder("Source"));
//        contentPane.add(scroll, BorderLayout.WEST);

        // center pane
        centerPane = new JPanel();
//        contentPane.add(centerPane, BorderLayout.CENTER);
        
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, centerPane);
        contentPane.add(splitPane, BorderLayout.CENTER);

        // meta controls
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.LINE_AXIS));
        JButton btnLoadMenu = new JButton("Load menu...");
        btnLoadMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser(new File(MENU_URL.getPath()));
                }
                fileChooser.showOpenDialog(MenuTester.this);
                File f = fileChooser.getSelectedFile();
                if (f != null) {
                    try {
                        loadMenuFile(f.toURL());
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(MenuTester.this, "Menu not found");
                        e1.printStackTrace();
                    } catch (MenuCreateException e1) {
                        JOptionPane.showMessageDialog(MenuTester.this, "Menu could not be created");
                        e1.printStackTrace();
                    }
                }
            }
        });
        controls.add(btnLoadMenu);
        controls.add(Box.createHorizontalGlue());
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
        loadMenuFile(menuUrl);
    }

    void loadMenuFile(URL menuUrl) throws IOException, MenuCreateException {
        textPane.setPage(menuUrl);
        mgr = MenuManager.getMenuManager(menuUrl);
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
        splitPane.remove(centerPane);
        centerPane = panel;
        splitPane.setRightComponent(centerPane);
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
     * @throws IOException if the default URL does not work.
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
                String text = xml.getAttribute("msg");
                if (text == null || text.length() == 0) {
                    if (xml.countChildren() > 0) {
                        text = ((XMLElement) xml.getChildren().elementAt(0)).getContent();
                    } else {
                        // fall-back instead of showing nothing
                        text = "## no text ##";
                    }
                }
                JTextArea taText = new JTextArea();
                taText.setEditable(false);
                taText.setWrapStyleWord(true);
                taText.setLineWrap(true);
                taText.setText(text);
                add(taText, BorderLayout.CENTER);
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