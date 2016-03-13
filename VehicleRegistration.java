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
		System.out.print("Vehicle serial no\n");
		Console co = System.console();
		String serial_no = co.readLine();

		Vehicle v = new Vehicle(serial_no);
		Adapter a = new Adapter();
		a.toSql(conn, v);
	}
}
