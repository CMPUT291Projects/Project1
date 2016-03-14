import java.util.*;
import java.sql.*;
import java.io.*;

public class AutoTransaction
{
	private Connection conn;

	public AutoTransaction(Connection conn)
	{
		this.conn = conn;
	}

	public void run() {
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			Console co = System.console();

			System.out.println("Create new auto sale transaction:");
			// Create seller
			System.out.print("Seller SIN:  ");
			String sin = co.readLine();

			people p = new people(sin);
			Adapter a = new Adapter();
			people seller = (people) a.searchTablePrimaryKey(conn, p, "sin", sin);
			if (seller == null) {
				System.err.println("Seller does not exist in system");
			}
			// Create buyer
			System.out.print("Buyer SIN:  ");
			co = System.console();
			sin = co.readLine();

			p = new people(sin);
			people buyer = (people) a.searchTablePrimaryKey(conn, p, "sin", sin);
			if (buyer == null) {
				System.err.println("Buyer does not exist in system");
			}
			// Create vehicle
			System.out.print("Vehicle serial number:  ");
			co = System.console();
			String serial_no = co.readLine();

			vehicle v = new vehicle(serial_no);
			vehicle auto = (vehicle) a.searchTablePrimaryKey(conn, v, "serial_no", serial_no);
			if (auto == null) {
				System.err.println("Vehicle does not exist in system");
			}
			stmt.close();
		} catch(SQLException ex) {
			System.err.println("SQLException: " +
			ex.getMessage());
		}
	}
}
