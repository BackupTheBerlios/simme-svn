package at.einspiel.simme.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Represents the interface to the database.
 * 
 * @author kariem
 *
 */
public class Database {

   private static final String SERVER_URL = "jdbc:postgresql://localhost/einspiel";
   private static final String USER = "einspiel";
   private static final String PWD = "weisahma";

   static {
      // load the driver
      try {
         Class.forName("org.postgresql.Driver");
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }
   }

   /**
    * Creates a new connection with the server and returns a reference to it
    * 
    * @return a reference to a newly created connection
    * 
    * @throws SQLException if a database access error occurs
    * 
    * @see DriverManager#getConnection(String, String, String)
    * 
    */
   public Connection getConnection() throws SQLException {
      return DriverManager.getConnection(SERVER_URL, USER, PWD);
   }

}
