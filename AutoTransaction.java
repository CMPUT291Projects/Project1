import java.util.*;
import java.sql.*;
import java.io.*;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.lang.reflect.Field;

/*
	Class the subprogram to enter a new auto sale into the database.
*/
public class AutoTransaction
{
	private Connection conn;

	public AutoTransaction(Connection conn)
	{
		this.conn = conn;
	}

	/*
		Enter a new auto transaction in the database.  Requests user input to populate
		the objects’ fields.  At each step the values entered by the user are validated
		to ensure the user has input reasonable values (i.e. of the correct data type
		and format).  The user must enter the SIN of the seller and the buyer.  If
		either one of these people do not exist in the database the user can chose
		to enter them using the insertPerson method from the VehicleRegistration
		class.  Once the appropriate values have been entered by the user, the
		previous owner is deleted from the owner table, and the new owner and auto_sale
		objects are inserted into the database using the Adapter class’ toSql function.
	*/
	public void run() {
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			Console co = System.console();

			VehicleRegistration vr = new VehicleRegistration(this.conn);

			System.out.println("Create new auto sale transaction:");
			// Create seller
			System.out.print("Seller SIN:  ");
			String sin = co.readLine().toLowerCase();

			people p = new people(sin);
			Adapter a = new Adapter();

			people seller = (people) a.searchTablePrimaryKey(conn, p, "sin", sin);
			if (seller == null) {
				System.err.println("Seller does not exist in system");
				System.out.print("Do you want to insert a new person with this SIN? (Y/N)  ");
				String response = co.readLine().toLowerCase();
				if (response.equals("y")) {
					vr.insertPerson(sin);
					seller = (people) a.searchTablePrimaryKey(conn, p, "sin", sin);
				} else {
					return;
				}
			}

			// Create buyer
			System.out.print("Buyer SIN:  ");
			sin = co.readLine().toLowerCase();

			p = new people(sin);
			people buyer = (people) a.searchTablePrimaryKey(conn, p, "sin", sin);
			if (buyer == null) {
				System.err.println("Buyer does not exist in system");
				System.out.print("Do you want to insert a new person with this SIN? (Y/N)  ");
				String response = co.readLine().toLowerCase();
				if (response.equals("y")) {
					vr.insertPerson(sin);
					buyer = (people) a.searchTablePrimaryKey(conn, p, "sin", sin);
				} else {
					return;
				}
			}
			// Create vehicle
			System.out.print("Vehicle serial number:  ");
			String sn = co.readLine().toLowerCase();
			vehicle v = new vehicle(sn);
			vehicle auto = (vehicle) a.searchTablePrimaryKey(conn, v, "serial_no", sn);
			if (auto == null) {
				System.err.println("Vehicle does not exist in system");
				System.out.println();
				return;
			}

			// Get ownership
			String queryStmt = "select owner_id,vehicle_id,is_primary_owner from owner where (owner_id=" + seller.sin +
							" and vehicle_id=" + auto.serial_no + ")";
			ResultSet rs = stmt.executeQuery(queryStmt);

			// Check that the query returned a result
			if (!rs.isBeforeFirst()) { // returns false if rs has no rows
				System.err.println("Seller is not an owner of the vehicle");
				System.out.println();
				return;
			}
			owner formerOwner = new owner();
			while (rs.next()) {
				try {
					Field[] nf = owner.class.getFields();

					for (Field field : nf) {
						field.set(formerOwner, rs.getObject(field.getName()));
					}
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}

			// Get price
			System.out.print("Price:  ");
			Float price = new Float(co.readLine());

			// Delete old ownership
			String dropStmt = "delete from owner where (owner_id=" + seller.sin +
							" and vehicle_id=" + auto.serial_no + ")";
			stmt.executeUpdate(dropStmt);

			// Create new objects - primary/secondary ownership is inherited
			owner newOwner = new owner(buyer.sin, auto.serial_no, formerOwner.is_primary_owner);
			a.toSql(conn, newOwner);

			//TODO(need to check if this is in table already or not)
			Integer trans_id = new Random().nextInt();

			// Taky, http://stackoverflow.com/questions/14558289/java-insert-into-a-table-datetime-data, 2016-03-13
			//Timestamp timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
			Date timestamp = new Date();
			auto_sale newSale = new auto_sale(trans_id, seller.sin, buyer.sin, auto.serial_no, timestamp, price);
			a.toSql(conn, newSale);

			stmt.close();
		} catch(SQLException ex) {
			System.err.println("SQLException: " +
			ex.getMessage());
		}
	}
}
