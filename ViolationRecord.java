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
		Console co = System.console();
		System.out.print("violator sin\n");
		String sin = co.readLine();
		
		System.out.print("vehicle id\n");
		String vid = co.readLine();

		System.out.print("officer number\n");
		String onum = co.readLine();


		List<ticket_type> types = getTicketTypes(conn);
		boolean goodValue = false;
		String input_type = null;
		while(!goodValue) {
			System.out.print("violation type (  ");
			for (ticket_type type : types) {
				System.out.print(String.format("%s=%f  ", type.vtype.trim(), type.fine));
			}
			System.out.print(")\n");
			input_type = co.readLine();
			input_type = input_type.replace("\n", "");
			for (ticket_type type : types) {
				if (type.vtype.equals(input_type)) {
					goodValue = true;
				}
			}
			if (!goodValue) {
				System.out.print("Invalid type, does not exist, try again\n");
			}
		}

		java.util.Date vdate = getDate("violation date? (yyyy-MM-dd)\n");

		System.out.print("violation place\n");
		String place = co.readLine();

		System.out.print("violation description\n");
		String desc = co.readLine();
		

	}

	java.util.Date getDate(String prompt) {
		Console co = System.console();
		boolean goodValue = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date myDate = null;
		while (!goodValue) {
			System.out.print(prompt);
			try {
				myDate = format.parse(co.readLine());
				goodValue = true;
			} catch (Exception ex) {
				System.out.print("invalid date format, use 'yyyy-MM-dd'\n");
			}
		}
		return myDate;
	}

	Integer getInt(String prompt) {
		Console co = System.console();
		boolean goodValue = false;
		Integer value = null;
		while(!goodValue) {
			try {
				System.out.print(prompt);
				value = Integer.parseInt(co.readLine());
				goodValue = true;
			} catch (NumberFormatException ex) {
				System.out.print("Invalid Integer Format\n");
			}
		}
		return value;
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
