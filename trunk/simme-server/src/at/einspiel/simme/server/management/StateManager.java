/*
 * Created on 04.05.2003
 */
package at.einspiel.simme.server.management;

import java.util.Map;

/**
 * 
 * @author kariem
 */
public class StateManager {

   private Map users;

   private static StateManager instance;

   /**
    * Ensures singleton
    * 
    * @return The existing instance if a <code>SessionManager</code> already
    * exists. Otherwise a new object of type <code>SessionManager</code> will
    * be instantiated.
    */
   public static StateManager getInstance() {
      if (instance == null) {
         instance = new StateManager(SessionManager.getInstance().getUsers());
      }
      return instance;
   }

   private StateManager(Map users) {
      this.users = users;
      init();
   }

   /** Initializes this object. */
   private void init() {
   }
}
