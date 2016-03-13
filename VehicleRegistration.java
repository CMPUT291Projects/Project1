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
		System.out.print("Vehicle serial no\n");
		String serial_no = co.readLine();

		System.out.print("Vehicle maker\n");
		String maker = co.readLine();

		System.out.print("Vehicle model\n");
		String model = co.readLine();

		System.out.print("Vehicle year\n");
		Integer year = Integer.parseInt(co.readLine());

		System.out.print("Vehicle color\n");
		String color = co.readLine();

		System.out.print("Vehicle type\n");
		Integer type = Integer.parseInt(co.readLine());

		vehicle v = new vehicle(serial_no, maker, model,
						year, color, type);
		Adapter a = new Adapter();
		a.toSql(conn, v);
	}
}
