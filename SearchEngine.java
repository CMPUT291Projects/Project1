import java.util.*;
import java.sql.*;
import java.io.*;

public class SearchEngine
{
	private Connection conn;
	private Statement stmt;

	public SearchEngine(Connection conn)
	{
		this.conn = conn;
		try {
			this.stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch(SQLException ex) {
			System.err.println("SQLException: " +
			ex.getMessage());
		}
	}

	public void run() {
		while (true) {
			System.out.print("To search for a person's name enter 'N'\n" +
							"To search for a licence number enter 'L'\n" +
							"To search for a SIN number enter 'S'\n" +
							"To search for a vehicle's serial number enter 'V'\n" +
							"To return to the main menu press 'M'\n");
			Console co = System.console();
			String action = co.readLine();
			System.out.println();
			if (action.equals("N")) {
				searchForName();
			}
			else if (action.equals("L")) {
				searchforLicenceNo();
			}
			else if (action.equals("S")) {
				searchForSIN();
			}
			else if (action.equals("V")) {
				searchForSerialNo();
			}
			else if (action.equals("M")) {
				return;
			}
			else {
				System.err.println("Invalid value entered, please try again\n");
			}
		}
	}

	private void searchForName() {
		Console co = System.console();
		Adapter a = new Adapter();

		System.out.print("Enter person's full name:  ");
		String name = co.readLine();
		people p = new people();
		ArrayList<Object> nameResults = new ArrayList<Object>();
		nameResults = a.searchTableAnyKey(conn, p, "name", name);

		if (nameResults == null) {
			System.err.println("No one with that name exists in system");
			System.out.println();
			return;
		}

		for (Object nr : nameResults) {
			people r = (people) nr;
			drive_licence l = (drive_licence) a.searchTablePrimaryKey(conn, new drive_licence(), "sin", r.sin);
			restriction e = (restriction) a.searchTablePrimaryKey(conn, new restriction(), "licence_no", l.licence_no);
			System.out.print("Name:  "); System.out.println(r.name);
			System.out.print("Licence no:  "); System.out.println(l.licence_no);
			System.out.print("Address:  "); System.out.println(r.addr);
			System.out.print("Birthday:  "); System.out.println(r.birthday);
			System.out.print("Driving class:  "); System.out.println(l.classType);
			System.out.print("Diving condition:  ");
			if (e != null) {
				driving_condition d = (driving_condition) a.searchTablePrimaryKey(conn, new driving_condition(), "c_id", e.r_id);
			 	System.out.println(d.description);
			} else {
				System.out.println("None");
			}
			System.out.print("Licence expiry:  "); System.out.println(l.expiring_date);
			System.out.println();
		}
	}

	private void searchforLicenceNo() {
		Console co = System.console();
		Adapter a = new Adapter();

		System.out.print("Enter licence number:  ");
		String ln = co.readLine();
		drive_licence c = new drive_licence();
		c = (drive_licence) a.searchTablePrimaryKey(conn, c, "licence_no", ln);

		if (c == null) {
			System.err.println("That licence number does not exist in the system");
			System.out.println();
			return;
		}

		people r = (people) a.searchTablePrimaryKey(conn, new people(), "sin", c.sin);
		restriction e = (restriction) a.searchTablePrimaryKey(conn, new restriction(), "licence_no", c.licence_no);
		System.out.print("Name:  "); System.out.println(r.name);
		System.out.print("Licence no:  "); System.out.println(c.licence_no);
		System.out.print("Address:  "); System.out.println(r.addr);
		System.out.print("Birthday:  "); System.out.println(r.birthday);
		System.out.print("Driving class:  "); System.out.println(c.classType);
		System.out.print("Diving condition:  ");
		if (e != null) {
			driving_condition d = (driving_condition) a.searchTablePrimaryKey(conn, new driving_condition(), "c_id", e.r_id);
		 	System.out.println(d.description);
		} else {
			System.out.println("None");
		}
		System.out.print("Licence expiry:  "); System.out.println(c.expiring_date);
		System.out.println();

		ticket k = new ticket();
		ArrayList<Object> ticResults = new ArrayList<Object>();
		ticResults = a.searchTableAnyKey(conn, k, "violator_no", r.sin);

		if (ticResults == null) {
			System.out.println("No recorded violations");
			System.out.println();
			return;
		}

		for (Object tr : ticResults) {
			ticket t = (ticket) tr;
			System.out.print("Ticket number:  "); System.out.println(t.ticket_no);
			System.out.print("Violator SIN:  "); System.out.println(t.violator_no);
			System.out.print("Vehicle number:  "); System.out.println(t.vehicle_id);
			System.out.print("Officer number:  "); System.out.println(t.office_no);
			System.out.print("Violation type:  "); System.out.println(t.vtype);
			System.out.print("Date:  "); System.out.println(t.vdate);
			System.out.print("Place:  "); System.out.println(t.place);
			System.out.print("Description:  "); System.out.println(t.descriptions);
			System.out.println();
		}

	}

	private void searchForSIN(){
		Console co = System.console();
		Adapter a = new Adapter();

		System.out.print("Enter person's SIN:  ");
		String psin = co.readLine();
		people p = new people();
		p = (people) a.searchTablePrimaryKey(conn, p, "sin", psin);

		if (p == null) {
			System.err.println("No one with that SIN exists in system");
			System.out.println();
			return;
		}

		ticket k = new ticket();
		ArrayList<Object> ticResults = new ArrayList<Object>();
		ticResults = a.searchTableAnyKey(conn, k, "violator_no", p.sin);

		if (ticResults == null) {
			System.out.println("No recorded violations");
			System.out.println();
			return;
		}

		for (Object tr : ticResults) {
			ticket t = (ticket) tr;
			System.out.print("Ticket number:  "); System.out.println(t.ticket_no);
			System.out.print("Violator SIN:  "); System.out.println(t.violator_no);
			System.out.print("Vehicle number:  "); System.out.println(t.vehicle_id);
			System.out.print("Officer number:  "); System.out.println(t.office_no);
			System.out.print("Violation type:  "); System.out.println(t.vtype);
			System.out.print("Date:  "); System.out.println(t.vdate);
			System.out.print("Place:  "); System.out.println(t.place);
			System.out.print("Description:  "); System.out.println(t.descriptions);
			System.out.println();
		}
	}

	private void searchForSerialNo() {

	}
}
