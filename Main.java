import java.util.*;
import java.sql.*; // Java package for accessing Oracle
import java.io.*; // Java package includes Console for getting password from user and printing to screen

public class Main
{
	public static void main(String[] args) {
		Connection cnxn;
		cnxn = loginToDataBase();
		try {
			Statement stmt = cnxn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			Boolean connected = true;
			// Create main menu
			while (connected) {
				System.out.print("To register a new vehicle enter 'R'\n" +
								"To enter a new auto transaction enter 'A'\n" +
								"To register a new driver's licence enter 'L'\n" +
								"To enter a new violation record enter 'V'\n" +
								"To perform a search enter 'S'\n" +
								"To exit the system press 'E'\n");
				Console co = System.console();
				String action = co.readLine();
				if (action.equals("R")) {

				}
				else if (action.equals("A")) {

				}
				else if (action.equals("L")) {

				}
				else if (action.equals("V")) {

				}
				else if (action.equals("S")) {

				}
				else if (action.equals("E")) {
					// No more statements to compile/execute. So, close connection.
					stmt.close();
		            cnxn.close();
					connected = false;
				}
				else {
					System.err.println("Invalid value entered, please try again\n");
				}
			}
		} catch (SQLException ex) {
			System.err.println("SQLException: " +
			ex.getMessage());
		}
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
		return m_conn;
	}
}
