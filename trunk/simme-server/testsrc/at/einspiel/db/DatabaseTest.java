package at.einspiel.db;

import at.einspiel.base.User;
import at.einspiel.base.UserException;

import java.sql.SQLException;

import junit.framework.TestCase;

/**
 * Class to test database methods.
 * 
 * @author kariem
 */
public class DatabaseTest extends TestCase {

   private String[] nicks = { "one", "two", "three", "four", "five", "six" };
   private String[] pwds = { "pwd1", "pwd2", "pwd3", "pwd4", "pwd5", "pwd6" };

   /**
    * Tests adding and removal.
    * 
    * @throws SQLException error in db.
    */
   public void testAddingAndRemoval() throws SQLException {
      User u = new User(nicks[0], pwds[0]);
      assertEquals(1, u.saveToDB());
      assertEquals(1, u.delete());
   }

   /**
    * Tests login process.
    * 
    * @throws SQLException error in db.
    * @throws UserException error while creating user.
    */
   public void testLogin() throws SQLException, UserException {
      long millis1 = System.currentTimeMillis();
      for (int i = 0; i < nicks.length; i++) {
         User u = User.getUser(nicks[i], pwds[1]);
         assertEquals(User.LOCATION_DEFAULT, u.getLocation());
      }
      long millis2 = System.currentTimeMillis();
      System.out.println("adding 6 new users in " + (millis2 - millis1) + "ms.");
      for (int i = 0; i < nicks.length; i++) {
         User u = User.getUser(nicks[i], pwds[1]);
         assertEquals(1, u.delete());
      }
      long millis3 = System.currentTimeMillis();
      System.out.println("removing 6 users in " + (millis3 - millis2) + "ms.");
      System.out.println("adding and removing 6 users in " + (millis3 - millis1) + "ms.");
   }

   /** @see junit.framework.TestCase#tearDown() */
   protected void tearDown() throws Exception {
      for (int i = 0; i < nicks.length; i++) {
         User u = User.getUserByNick(nicks[i]);
         u.delete();
      }

   }
}