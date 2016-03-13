import java.util.*;
import java.sql.*; // Java package for accessing Oracle
import java.io.*; // Java package includes Console for getting password from user and printing to screen

public class Main
{
	public static void main(String[] args) {
		Connection cnxn;
		cnxn = loginToDataBase();
	}

	private static Connection loginToDataBase() {
		// get username
		System.out.print("Username: ");
		Console co = System.console();
		String m_userName = co.readLine();

		// obtain password
		char[] passwordArray = co.readPassword("Password: ");
		String m_password = new String(passwordArray);

		// The URL we are connecting to
		String m_url = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";

		// The driver to use for connection
		String m_driverName = "oracle.jdbc.driver.OracleDriver";

		Connection m_con;

		try {
			Class drvClass = Class.forName(m_driverName);
			// DriverManager.registerDriver((Driver)drvClass.newInstance());- not needed.
			// This is automatically done by Class.forName().
		} catch(Exception e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try	{
			// Establish a connection
			m_con = DriverManager.getConnection(m_url, m_userName, m_password);
			return m_con;
		} catch(SQLException ex) {
   			System.err.println("SQLException: " +
          	ex.getMessage());
		}
		return null;
	}
}
