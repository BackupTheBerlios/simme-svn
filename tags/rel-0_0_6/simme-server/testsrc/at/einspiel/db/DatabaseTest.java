package at.einspiel.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import junit.framework.TestCase;

/**
 * Class to test database methods.
 * 
 * @author kariem
 */
public class DatabaseTest extends TestCase {

   Database db;
   
   /** @see junit.framework.TestCase#setUp() */
   protected void setUp() {
      db = new Database();
   }
   
   /** 
    * Tests simple query execution 
    * @throws SQLException if an error occurs while executing the query.
    */
   public void testCreateConnection() throws SQLException {
      ResultSet rs = db.executeQuery("SELECT * FROM " + UserDB.TABLE_USER);
      ResultSetMetaData rsMeta= rs.getMetaData();
      assertTrue(rsMeta.getColumnCount() > 3);
   }
}