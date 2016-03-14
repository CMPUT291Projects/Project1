import java.util.*;
import java.sql.*;
import java.io.*;

public class VehicleRegistration
{
	private Connection conn;

	public VehicleRegistration(Connection conn)
	{
		this.conn = conn;
	}

	public void run() {
		Console co = System.console();
		String serial_no = null;
		do {
			System.out.print("Vehicle serial no\n");
			String temp_serial_no = co.readLine();
			if(!idExists(temp_serial_no, conn)) {
				serial_no = temp_serial_no;
			} else {
				System.out.print("Serial number already exists, try again\n");
			}
		} while (serial_no == null);

		System.out.print("Vehicle maker\n");
		String maker = co.readLine();

		System.out.print("Vehicle model\n");
		String model = co.readLine();

		boolean goodValue = false;
		Integer year = null;
		while(!goodValue) {
			try {
				System.out.print("Vehicle year\n");
				year = Integer.parseInt(co.readLine());
				goodValue = true;
			} catch (NumberFormatException ex) {
				System.out.print("Invalid year format, try again\n");
			}
		}

		System.out.print("Vehicle color\n");
		String color = co.readLine();

		List<vehicle_type> types = getVehicleTypes(conn);
		goodValue = false;
		Integer input_type = null;
		while(!goodValue) {
			System.out.print("Vehicle type id (  ");
			for (vehicle_type type : types) {
				System.out.print(String.format("%s=%d  ", type.type.trim(), type.type_id));
			}
			System.out.print(")\n");
			try {
				input_type = Integer.parseInt(co.readLine());
				for (vehicle_type type : types) {
					if (type.type_id == input_type) {
						goodValue = true;
					}
				}
				if (!goodValue) {
					System.out.print("Invalid id, does not exist, try again\n");
				}
			} catch (NumberFormatException ex) {
				System.out.print("Invalid type id format, try again with an integer value\n");
			}
		}

		vehicle v = new vehicle(serial_no, maker, model,
						year, color, input_type);
		Adapter a = new Adapter();
		a.toSql(conn, v);
	}

	//TODO(crashes if I pass 'asdf' as the id)
	private boolean idExists(String id, Connection conn)
	{
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String query = String.format("select serial_no from vehicle where serial_no=%s", id);
			ResultSet rs = stmt.executeQuery(query);
			if(!rs.next()) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	private List<vehicle_type> getVehicleTypes(Connection conn) {
		List<vehicle_type> types = new ArrayList<vehicle_type>();
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String query = "select type_id, type from vehicle_type";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				vehicle_type vt = new vehicle_type(rs.getInt("type_id"), rs.getString("type"));
				types.add(vt);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
		return types;
	}	
}
