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
			return;
		}

		for (Object nr : nameResults) {
			people r = (people) nr;
			drive_licence l = (drive_licence) a.searchTablePrimaryKey(conn, new drive_licence(), "sin", r.sin);
			System.out.print("Name:  "); System.out.println(r.name);
			System.out.print("Address:  "); System.out.println(r.addr);
		}
	}

	private void searchforLicenceNo() {

	}

	private void searchForSIN(){

	}

	private void searchForSerialNo() {

	}
}
