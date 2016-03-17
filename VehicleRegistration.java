import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class VehicleRegistration
{
	private Connection conn;

	public VehicleRegistration(Connection conn)
	{
		this.conn = conn;
	}

	public void run() {
		String serialNo = insertVehicle();
		insertOwner(serialNo);
	}

	//returns the serial number of inserted vehicle
	private String insertVehicle()
	{
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
		return serial_no;

	}


	private void insertOwner(String serial_no)
	{
		while (true)
		{
			System.out.print("Register a owner for vehicle? (Y/N)\n");
			Console co = System.console();
			String response = co.readLine().toLowerCase();
			if (response.equals("y")) {

				//get owner ID, apparently not an int
				System.out.print("Owner SIN?\n");
				String ownerId = co.readLine();

				//get if primary owner
				boolean primaryOwner = false;
				boolean goodValue = false;
				while (!goodValue) {
					System.out.print("Is primary owner? (Y/N)\n");
					response = co.readLine().toLowerCase();

					if (response.equals("y")) {
						primaryOwner = true;
						goodValue = true;
					} else if (response.equals("n")) {
						primaryOwner = false;
						goodValue = true;
					} else {
						goodValue = false;
						System.out.print("Invalid option, select [Y]es or [N]o\n");
					}
				}

				if (!personExists(ownerId)) {
					System.out.print("It appears that person does not exist, we will enter that data now\n");
					insertPerson(ownerId);
				}

			} else if (response.equals("n")) {
				break;
			} else {
				System.out.print("Invalid option, select [Y]es or [N]o\n");
			}
		}
	}

	private void insertPerson(String SIN)
	{
		Console co = System.console();
		System.out.print("Name:  ");
		String name = co.readLine();

		Float height = null;
		boolean goodValue = false;
		while (!goodValue) {
			try {
				System.out.print("Height: (ex 113.42)  ");
				height = Float.parseFloat(co.readLine());
				goodValue = true;
			} catch (NumberFormatException ex) {
				System.out.print("Invalid number format, use 5 digits with 2 decimal places\n");
			}
		}

		Float weight = null;
		goodValue = false;
		while (!goodValue) {
			try {
				System.out.print("Weight: (ex 113.42)  ");
				weight = Float.parseFloat(co.readLine());
				goodValue = true;
			} catch (NumberFormatException ex) {
				System.out.print("Invalid number format, use 5 digits with 2 decimal places\n");
			}
		}

		System.out.print("Eye Color:  ");
		String eyeColor = co.readLine();

		System.out.print("Hair Color:  ");
		String hairColor = co.readLine();

		System.out.print("Address: ");
		String addr = co.readLine();

		goodValue = false;
		String gender = null;
		while (!goodValue) {
			System.out.print("Gender: (m, f)  ");
			gender = co.readLine();
			if (!gender.equals("m") && !gender.equals("f")) {
				System.out.println("Gender must be 'm' or 'f', try again");
			} else {
				goodValue = true;
			}
		}

		goodValue = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date birthday = null;
		while (!goodValue) {
			System.out.print("Birthday: (yyyy-MM-dd)  ");
			try {
				birthday = format.parse(co.readLine());
				goodValue = true;
			} catch (Exception ex) {
				System.out.println("Invalid birthday format, use 'yyyy-MM-dd'");
			}
		}

	 	people p = new people(SIN, name, height, weight, eyeColor,
					hairColor, addr, gender, birthday);
		Adapter a = new Adapter();
		a.toSql(conn, p);
	}


	private boolean personExists(String sin)
	{
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String query = String.format("select sin from people where sin=%s", sin);
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
