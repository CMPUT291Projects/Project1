import java.util.*;
import java.sql.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

/*
CREATE TABLE ticket (
  ticket_no     int,
  violator_no   CHAR(15),
  vehicle_id    CHAR(15),
  office_no     CHAR(15),
  vtype        char(10),
  vdate        date,
  place        varchar(20),
  descriptions varchar(1024),
  PRIMARY KEY (ticket_no),
  FOREIGN KEY (vtype) REFERENCES ticket_type,
  FOREIGN KEY (violator_no) REFERENCES people ON DELETE CASCADE,
  FOREIGN KEY (vehicle_id)  REFERENCES vehicle,
  FOREIGN KEY (office_no) REFERENCES people ON DELETE CASCADE
);*/

public class ViolationRecord
{
	private Connection conn;

	public ViolationRecord(Connection conn)
	{
		this.conn = conn;
	}

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
		while(!goodValue) {
			System.out.print("violation type (  ");
			for (ticket_type type : types) {
				System.out.print(String.format("%s=%f  ", type.vtype.trim(), type.fine));
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
