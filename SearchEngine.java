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
		this.stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
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

	}

	private void searchforLicenceNo() {

	}

	private void searchForSIN(){

	}

	private void searchForSerialNo() {

	}
}
