package at.einspiel.simme.server.db;

import java.util.*;

import java.sql.*;

/**
 * Represents the interface to the database.
 *
 * @author kariem
 *
 */
public class Database {

   private static final String DRIVER_CLASS = "org.postgresql.Driver";
   private static final String SERVER_URL =
      "jdbc:postgresql://localhost/einspiel";
   private static final String USER = "einspiel";
   private static final String PWD = "Weisahma";

   private Connection conn;

   static {
      // load the driver
      try {
         Class.forName(DRIVER_CLASS);
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }
   }

   /**
    * Creates a new connection with the server and returns a reference to it. If
    * this object already had a connection to the database that connection is
    * returned.
    *
    * @return a reference to a newly created connection
    *
    * @throws SQLException if a database access error occurs
    *
    * @see DriverManager#getConnection(String, String, String)
    *
    */
   public Connection getConnection() throws SQLException {
      if (conn == null) {
         conn = DriverManager.getConnection(SERVER_URL, USER, PWD);
      }
      return conn;
   }

   /*
    *
    * Updates, inserts, deletes
    *
    */

   /**
    * Executes the given SQL statement, which may be an <code>INSERT</code>,
    * <code>UPDATE</code>, or <code>DELETE</code> statement or an
    * SQL statement that returns nothing, such as an SQL DDL statement.
    *
    * @param text an SQL <code>INSERT</code>, <code>UPDATE</code> or
    *        <code>DELETE</code> statement or an SQL statement that returns
    *        nothing
    * @return either the row count for <code>INSERT</code>,
    *         <code>UPDATE</code> or <code>DELETE</code> statements, or
    *         <code>0</code> for SQL statements that return nothing
    * @throws SQLException if a database access error occurs or the given
    *         SQL statement produces a <code>ResultSet</code> object
    *
    * @see Statement#executeUpdate(java.lang.String)
    */
   public int executeUpdate(String text) throws SQLException {
      Statement stmt = getConnection().createStatement();
      System.out.println("text=" + text);
      return stmt.executeUpdate(text);
   }

   /**
    * Executes an insert statement with the given parameters.
    *
    * @param tableName the name of the table.
    * @param params a list of parameters to this insert statement
    * @return the row count
    *
    *  @throws SQLException in addition to {@link #executeUpdate(String)} if
    *         <code>params</code> is either <code>null</code> or empty.
    *
    * @see #executeUpdate(String).
    */
   public int insert(String tableName, List params) throws SQLException {
      if ((params == null) || (params.size() == 0)) {
         throw new SQLException("Bad parameters in parameters list.");
      }

      // write beginning of insert statement
      StringBuffer bufInsert = new StringBuffer("INSERT INTO ");
      bufInsert.append(tableName);
      bufInsert.append(" VALUES(");

      for (Iterator i = params.iterator(); i.hasNext();) {
         Object element = i.next();
         if (element instanceof String) {
            bufInsert.append("'");
            bufInsert.append(element);
            bufInsert.append("'");
         } else {
            bufInsert.append(element);
         }
         if (i.hasNext()) {
            bufInsert.append(",");
         }
      }

      // write end of insert statement
      bufInsert.append(")");

      return executeUpdate(bufInsert.toString());
   }

   /**
    * Executes an insert statement with the given parameters into the given
    * columns. The two lists <code>columns</code> and <code>params</code> have
    * to be of same size.
    *
    * @param tableName the name of the table.
    * @param columns a list of columns to this insert statement
    * @param params a list of parameters to this insert statement
    * @return the row count
    *
    * @throws SQLException in addition to {@link #executeUpdate(String)} if
    *         <code>params</code> is either <code>null</code> or empty.
    *
    * @see #executeUpdate(String).
    */
   public int insert(String tableName, Collection columns, Collection params)
      throws SQLException {
      if ((params == null) || (params.size() == 0)) {
         throw new SQLException("Bad parameters in parameters list.");
      }

      // write beginning of insert statement
      StringBuffer bufInsert = new StringBuffer("INSERT INTO ");
      bufInsert.append(tableName);

      // write columns
      bufInsert.append(" (");
      for (Iterator i = columns.iterator(); i.hasNext();) {
         bufInsert.append(i.next());
         if (i.hasNext()) {
            bufInsert.append(",");
         }
      }
      bufInsert.append(" )");

      // write values
      bufInsert.append(" VALUES(");
      for (Iterator i = params.iterator(); i.hasNext();) {
         Object element = i.next();
         if (element instanceof String) {
            bufInsert.append("'");
            bufInsert.append(element);
            bufInsert.append("'");
         } else {
            bufInsert.append(element);
         }
         if (i.hasNext()) {
            bufInsert.append(",");
         }
      }
      bufInsert.append(")");

      return executeUpdate(bufInsert.toString());
   }

   /**
    * Executes an delete statement with the given parameters.
    *
    * @param tableName the name of the table.
    * @param params a list of parameters to this delete statement
    * @return the row count
    * @throws SQLException in addition to {@link #executeUpdate(String)} if
    *         <code>params</code> is either <code>null</code> or empty.
    *
    * @see #executeUpdate(String).
    */
   public int delete(String tableName, Map params) throws SQLException {
      if ((params == null) || (params.size() == 0)) {
         throw new SQLException("Bad parameters in parameters list.");
      }

      // write beginning of delete statement
      StringBuffer bufDelete = new StringBuffer("DELETE FROM ");
      bufDelete.append(tableName);

      // write where thingies
      bufDelete.append(" WHERE ");
      for (Iterator i = params.entrySet().iterator(); i.hasNext();) {
         Map.Entry entry = (Map.Entry) i.next();
         bufDelete.append(entry.getKey());
         Object value = entry.getValue();
         if (value instanceof String) {
            bufDelete.append(" like ");
            bufDelete.append("'");
            bufDelete.append(value);
            bufDelete.append("'");
         } else {
            bufDelete.append("=");
            bufDelete.append(value);
         }
         if (i.hasNext()) {
            bufDelete.append(" AND ");
         }
      }

      return executeUpdate(bufDelete.toString());
   }

   /**
    * Executes an delete statement with the given selection.
    *
    * @param tableName the name of the table.
    * @param tName the name of the type
    * @param tValue the value to match
    * @return the row count
    *
    * @throws SQLException see {@link #executeUpdate(String)}
    *
    * @see #executeUpdate(String).
    */
   public int delete(String tableName, String tName, String tValue)
      throws SQLException {
      StringBuffer bufDelete = new StringBuffer("DELETE FROM ");
      bufDelete.append(tableName);
      bufDelete.append(" WHERE ");
      bufDelete.append(tName);
      bufDelete.append(" like ");
      bufDelete.append("'");
      bufDelete.append(tValue);
      bufDelete.append("'");

      return executeUpdate(bufDelete.toString());
   }

   /**
    * Executes an delete statement with the given selection.
    *
    * @param tableName the name of the table.
    * @param tName the name of the type
    * @param tValue the value to match
    * @return the row count
    *
    * @throws SQLException see {@link #executeUpdate(String)}
    *
    * @see #executeUpdate(String).
    */
   public int delete(String tableName, String tName, double tValue)
      throws SQLException {
      StringBuffer bufDelete = new StringBuffer("DELETE FROM ");
      bufDelete.append(tableName);
      bufDelete.append(" WHERE ");
      bufDelete.append(tName);
      bufDelete.append("=");
      bufDelete.append(tValue);

      return executeUpdate(bufDelete.toString());
   }

   /**
    * Updates a given table with the attributes.
    *
    * @param tableName the name of the table.
    * @param attrMap the attributes to set.
    * @param whereMap the filter.
    * @return the row count
    *
    * @throws SQLException see {@link #executeUpdate(String)}
    *
    * @see #executeUpdate(String).
    */
   public int update(String tableName, Map attrMap, Map whereMap) throws SQLException {
      StringBuffer buf = new StringBuffer("UPDATE ");
      buf.append(tableName);

      // set
      buf.append(" SET ");
      for (Iterator i = attrMap.entrySet().iterator(); i.hasNext();) {
         Map.Entry entry = (Map.Entry) i.next();
         buf.append(entry.getKey());
         buf.append("=");
         Object value = entry.getValue();
         if (value instanceof String) {
            buf.append("'");
            buf.append(value);
            buf.append("'");
         } else {
            buf.append(value);
         }
         if (i.hasNext()) {
            buf.append(", ");
         }
      }

      // where clause
      buf.append(" WHERE ");
      for (Iterator i = whereMap.entrySet().iterator(); i.hasNext();) {
         Map.Entry entry = (Map.Entry) i.next();
         buf.append(entry.getKey());
         Object value = entry.getValue();
         if (value instanceof String) {
            buf.append(" like ");
            buf.append("'");
            buf.append(value);
            buf.append("'");
         } else {
            buf.append("=");
            buf.append(value);
         }
         if (i.hasNext()) {
            buf.append(" AND ");
         }
      }

      return executeUpdate(buf.toString());

   }


   /*
    *
    * queries
    *
    */

   /**
    * Executes the given SQL statement, which returns a single
    * <code>ResultSet</code> object.
    *
    * @param text an SQL statement to be sent to the database, typically a
    *        static SQL <code>SELECT</code> statement
    * @return a <code>ResultSet</code> object that contains the data produced
    *         by the given query; never <code>null</code>
    * @throws SQLException if a database access error occurs or the given
    *         SQL statement produces anything other than a single
    *         <code>ResultSet</code> object
    */
   public ResultSet executeQuery(String text) throws SQLException {
      Statement stmt = getConnection().createStatement();
      System.out.println("text=" + text);
      return stmt.executeQuery(text);
   }

   /**
    * Executes a simple <code>SELECT</code> query on the table
    * <code>table</code>.
    *
    * @param table the table.
    * @param columns the columns to return.
    * @param whereParams the filter which constructs the <code>WHERE</code>
    *        part of the query.
    * @return a <code>ResultSet</code> that shows the result of the constructed
    *         sql query.
    * @throws SQLException see {@link #executeQuery(String)}
    */
   public ResultSet executeQuery(
      String table,
      Collection columns,
      Map whereParams)
      throws SQLException {
      StringBuffer buf = new StringBuffer("SELECT ");

      if (columns == null) {
         buf.append("*");
      } else {
         for (Iterator i = columns.iterator(); i.hasNext();) {
            buf.append(i.next());
            if (i.hasNext()) {
               buf.append(",");
            }
         }
      }

      buf.append(" FROM ");
      buf.append(table);

      // write where thingies
      buf.append(" WHERE ");
      for (Iterator i = whereParams.entrySet().iterator(); i.hasNext();) {
         Map.Entry entry = (Map.Entry) i.next();
         buf.append(entry.getKey());
         Object value = entry.getValue();
         if (value instanceof String) {
            buf.append(" like ");
            buf.append("'");
            buf.append(value);
            buf.append("'");
         } else {
            buf.append("=");
            buf.append(value);
         }
         if (i.hasNext()) {
            buf.append(" AND ");
         }
      }

      return executeQuery(buf.toString());
   }

   /**
    * Executes a simple <code>SELECT</code> query on the table
    * <code>table</code>. All columns of the result will be returned.
    *
    * @param table the table.
    * @param whereParams the filter which constructs the <code>WHERE</code>
    *        part of the query.
    * @return a <code>ResultSet</code> that shows the result of the constructed
    *         sql query.
    * @throws SQLException see {@link #executeQuery(String)}
    */
   public ResultSet executeQuery(String table, Map whereParams)
      throws SQLException {
      return executeQuery(table, null, whereParams);
   }

}
