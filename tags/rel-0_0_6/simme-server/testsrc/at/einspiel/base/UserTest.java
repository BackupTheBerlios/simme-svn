// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: UserTest.java
//                  $Date: 2004/02/23 09:10:36 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.base;

import junit.framework.TestCase;
import at.einspiel.db.UserDB;

/**
 * Class to test user methods.
 * @author kariem
 */
public class UserTest extends TestCase {

   private String[] nicks = { "one", "two", "three", "four", "five", "six" };
   private String[] pwds = { "pwd1", "pwd2", "pwd3", "pwd4", "pwd5", "pwd6" };

   /**
    * Tests adding and removal.
    * 
    * @throws UserException error while trying to save / delete.
    */
   public void testAddingAndRemoval() throws UserException {
      User u = new User(nicks[0], pwds[0]);
      assertEquals(1, u.saveToDB());
      assertEquals(1, u.delete());
   }

   /**
    * Tests login process. Additionally a short benchmark is performed.
    * 
    * @throws UserException error while creating user.
    */
   public void testLogin() throws UserException {
      long millis1 = System.currentTimeMillis();
      for (int i = 0; i < nicks.length; i++) {
         User u = UserDB.getUser(nicks[i], pwds[1]);
         assertEquals(User.LOCATION_DEFAULT, u.getLocation());
      }
      long millis2 = System.currentTimeMillis();
      System.out.println("adding 6 new users in " + (millis2 - millis1) + "ms.");
      for (int i = 0; i < nicks.length; i++) {
         User u = UserDB.getUser(nicks[i], pwds[1]);
         assertEquals(1, u.delete());
      }
      long millis3 = System.currentTimeMillis();
      System.out.println("removing 6 users in " + (millis3 - millis2) + "ms.");
      System.out.println("adding and removing 6 users in " + (millis3 - millis1) + "ms.");
   }

   /**
    * Tests getting all users 
    * @throws UserException
    */
   public final void testGetAllUsers() throws UserException {
      int count = UserDB.getAllUsers().length;
      for (int i = 0; i < nicks.length; i++) {
         User u = new User(nicks[i], pwds[i]);
         u.saveToDB();
      }
      assertTrue(count + nicks.length == UserDB.getAllUsers().length);
   }

   /** @see junit.framework.TestCase#tearDown() */
   protected void tearDown() {
      for (int i = 0; i < nicks.length; i++) {
         try {
         User u;
            u = UserDB.getUserByNick(nicks[i]);
            u.delete();
         } catch (NoSuchUserException e) {
            // ignore this
         } catch (UserException e) {
            // ignore this
         }
      }
   }
}
