package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple class to create a database.
 */
public class CreateDatabase {

	static final String DRIVER = "org.postgresql.Driver";
	static final String DB = "jdbc:postgresql://localhost/";

	static final String USER = "postgres";
	static final String PASS = "admin";

	public static void main(String[] args) throws SQLException {

		Connection conn = null;
		Statement st = null;

		try {
			// loading driver
			Class.forName(DRIVER);
			System.out.println("Driver loaded successfully.");
			// getting connection
			conn = DriverManager.getConnection(DB, USER, PASS);
			System.out.println("PostgreSQL connection obtained.");

			// creating statement
			st = conn.createStatement();
			System.out.println("Statement created.");

			// creating test database
			String dbCreate = "CREATE DATABASE test_db";
			st.executeUpdate(dbCreate);
			System.out.println("Database created.");
			System.out.println("Exiting.");
			
		} catch (ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			ex.printStackTrace();
		} catch (SQLException e) {
			System.out.println("SQL error!");
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

}
