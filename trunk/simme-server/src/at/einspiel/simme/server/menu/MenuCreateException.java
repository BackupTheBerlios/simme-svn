// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: MenuCreateException.java
//                  $Date: 2004/02/21 23:04:20 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

/**
 * An exception to indicate the failure of a menu creation.
 * @author kariem
 */
public class MenuCreateException extends Exception {

    /**
     * Creates a new instance of <code>MenuCreateException</code>.
     * @param message
     */
    public MenuCreateException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of <code>MenuCreateException</code>.
     * @param cause
     */
    public MenuCreateException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance of <code>MenuCreateException</code>.
     * @param message
     * @param cause
     */
    public MenuCreateException(String message, Throwable cause) {
        super(message, cause);
    }

}
