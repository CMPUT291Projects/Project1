import java.util.*;
import java.sql.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

/*
	Class for running the subprogram to enter a new violation into the database.
*/
public class ViolationRecord
{
	private Connection conn;

	public ViolationRecord(Connection conn)
	{
		this.conn = conn;
	}

	/*
		Launch subprogram and enter a new driving violation into the database.
		It does this by creating a new ticket object and requesting user input to
		populate the object’s fields.  At each step the values entered by the user
		are validated to ensure the user has input reasonable values (i.e. of the
		correct data type and format).  Once the appropriate values have been
		entered by the user the new ticket object is inserted into the database
		using the Adapter class’ toSql function.
	*/
	public void run() {
		ConsoleParser parser = new ConsoleParser();
		Adapter a = new Adapter();
		String sin = null;
		while (true) {
			sin = parser.getString("violator sin\n");
			people p = new people();
			people registered_person = (people) a.searchTablePrimaryKey(conn, p, "sin", sin);
			if(registered_person == null) {
				System.out.print("that person does not, exist, try again with a different sin\n");
			} else {
				break;
			}
		}

		String vid = null;
		while (true) {
			vid = parser.getString("vehicle id\n");
			vehicle v = new vehicle();
			vehicle registered_vehicle = (vehicle) a.searchTablePrimaryKey(conn, v, "serial_no", vid);
			if(registered_vehicle == null) {
				System.out.print("that vehicle does not, exist, try again with a different vehicle id\n");
			} else {
				break;
			}
		}

		String onum = null;
		while (true) {
			onum = parser.getString("officer number\n");
			people p = new people();
			people registered_person = (people) a.searchTablePrimaryKey(conn, p, "sin", onum);
			if(registered_person == null) {
				System.out.print("that person does not, exist, try again with a different sin\n");
			} else {
				break;
			}
		}


		Console co = System.console();
		List<ticket_type> types = getTicketTypes(conn);
		boolean goodValue = false;
		String input_type = null;
		DecimalFormat df = new DecimalFormat("00.000");
		while(!goodValue) {
			System.out.print("violation type (  ");
			for (ticket_type type : types) {
				System.out.print(String.format("%s=%s  ", type.vtype.trim(), df.format(type.fine)));
			}
			System.out.print(")\n");
			input_type = co.readLine().toLowerCase();
			input_type = input_type.replace("\n", "");
			for (ticket_type type : types) {
				if (type.vtype.trim().toLowerCase().equals(input_type)) {
					goodValue = true;
				}
			}
			if (!goodValue) {
				System.out.print("Invalid type, does not exist, try again\n");
			}
		}

		java.util.Date vdate = parser.getDate("violation date? (yyyy-MM-dd)\n");
		String place = parser.getString("violation place\n");
		String desc = parser.getString("violation description\n");

		//genereate an Id not in the table yet
		Integer id = null;
		while (true) {
			id = new Random().nextInt();
			ticket t = new ticket();
			ticket registered_ticket = (ticket) a.searchTablePrimaryKey(conn, t, "ticket_no", id);
			if (registered_ticket == null) {
				break; //no ticket with same id in table
			}
		}

		ticket tkt = new ticket(id, sin, vid, onum, input_type, vdate, place, desc);
		a.toSql(conn, tkt);


	}

	/*
		Retrieve all types of ticket violations from the ticket_type table and
		return as a list.
	*/
	List<ticket_type> getTicketTypes(Connection conn) {
		List<ticket_type> types = new ArrayList<ticket_type>();
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String query = "select vtype, fine from ticket_type";
			ResultSet rs = stmt.executeQuery(query);

			while(rs.next()) {
				ticket_type vt = new ticket_type(rs.getString("vtype"), rs.getFloat("fine"));
				types.add(vt);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
		return types;
	}
}
