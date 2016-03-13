import java.util.*;
import java.sql.*;
import java.io.*;

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

			System.out.println("Create new auto sale transaction:");
			System.out.print("Seller SIN:  ");
			Console co = System.console();
			String sin = co.readLine();

			people p = new people(sin);
			Adapter a = new Adapter();
			a.searchTablePrimaryKey(conn, p, "sin", sin);

			stmt.close();
		} catch(SQLException ex) {
			System.err.println("SQLException: " +
			ex.getMessage());
		}
	}
}
