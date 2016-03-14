import java.util.*;
import java.sql.*;
import java.io.*;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

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
			sin = co.readLine();

			p = new people(sin);
			people buyer = (people) a.searchTablePrimaryKey(conn, p, "sin", sin);
			if (buyer == null) {
				System.err.println("Buyer does not exist in system");
			}
			// Create vehicle
			System.out.print("Vehicle serial number:  ");
			String serial_no = co.readLine();

			vehicle v = new vehicle(serial_no);
			vehicle auto = (vehicle) a.searchTablePrimaryKey(conn, v, "serial_no", serial_no);
			if (auto == null) {
				System.err.println("Vehicle does not exist in system");
			}
			// Get price
			System.out.print("Price:  ");
			Float price = new Float(co.readLine());

			// Delete old ownership
			String dropStmt = "delete from owner where (owner_id=" + seller.sin +
							" and vehicle_id=" + auto.serial_no + ")";
			stmt.executeUpdate(dropStmt);

			// Create new objects
			owner newOwner = new owner(buyer.sin, auto.serial_no, true);
			Integer trans_id = new Random().nextInt();
			try {
				//Taky, http://stackoverflow.com/questions/14558289/java-insert-into-a-table-datetime-data, 2016-03-13
				Timestamp timestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
				auto_sale newSale = new auto_sale(trans_id, seller.sin, buyer.sin, auto.serial_no, timestamp, price);
				a.toSql(conn, newSale);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			stmt.close();
		} catch(SQLException ex) {
			System.err.println("SQLException: " +
			ex.getMessage());
		}
	}
}
