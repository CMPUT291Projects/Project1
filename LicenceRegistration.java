import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.rowset.serial.SerialBlob;
import java.text.SimpleDateFormat;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialException;
import java.text.ParseException;
import java.lang.NullPointerException;


public class LicenceRegistration
{
	private Connection conn;

	public LicenceRegistration(Connection conn)
	{
		this.conn = conn;
	}

	public void run() {
		
		System.out.print("Insert licence no.\n");
		Console co = System.console();
		String licence_no = co.readLine();
		boolean goodValue;
		String sin = null;
		String temp_sin;
		do {
			System.out.print("Insert SIN.\n");
			temp_sin = co.readLine();
			if(!idExists(temp_sin, this.conn)) {
				sin = temp_sin;
			} else {
				System.out.print("Person with this SIN already has licence, try again\n");
			}
		} while (sin == null);

		if(!personExists(sin)){
			insertPerson(sin);
		}

		System.out.print("Insert classType.\n");
		String classType = co.readLine();

		System.out.print("Reading in picture.\n");
		String file1 = "test.jpg";
		File file = new File(file1);
		FileInputStream fileInput = null;
		try{
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e){
			System.out.print("File not found.");
		}
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		//converting file to byte array to be loaded in Blob
		try{
			while ((nRead = fileInput.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}
			buffer.flush();
		}
		catch(IOException e){
			System.out.print("File error.");
		}
		catch(NullPointerException e){
			System.out.print("Null pointer. Used the wrong file.");
		}

		SerialBlob picture = null;
		try{
			picture = new SerialBlob(buffer.toByteArray());
		}
		catch(SerialException e){
			System.out.print("Error with Byte array.");
		}
		catch(SQLException e){
			System.out.print("Error with Serialblob.");
		}

		goodValue = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date issuing_date = null;
		while (!goodValue) {
			System.out.print("Insert issuing date (yyyy-MM-dd).\n");
			try {
				issuing_date = format.parse(co.readLine());
				goodValue = true;
			} catch (Exception ex) {
				System.out.print("invalid issuing date format, use 'yyyy-MM-dd'\n");
			}
		}

		goodValue = false;
		java.util.Date expiring_date = null;
		while (!goodValue) {
			System.out.print("Insert expiring date (yyyy-MM-dd).\n");
			try {
				expiring_date = format.parse(co.readLine());
				goodValue = true;
			} catch (Exception ex) {
				System.out.print("invalid expiration date format, use 'yyyy-MM-dd'\n");
			}
		}

		drive_licence dl = new drive_licence(licence_no, sin, classType, picture, issuing_date, expiring_date);
		Adapter a = new Adapter();
		a.toSql(conn, dl);
	}

	private boolean idExists(String id, Connection conn)
	{
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String query = String.format("select sin from drive_licence where sin=%s", id);
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

	private void insertPerson(String SIN)
	{
		Console co = System.console();
		System.out.print("Name:\n");
		String name = co.readLine();

		Float height = null;
		boolean goodValue = false;
		while (!goodValue) {
			try {
				System.out.print("Height: (ex 113.42)\n");
				String input = co.readLine();
				if (!input.matches("[\\d][\\d][\\d][.][\\d][\\d]")) throw new NumberFormatException();
				height = Float.parseFloat(input);
				goodValue = true;
			} catch (NumberFormatException ex) {
				System.out.print("Invalid number format, use 5 digits with 2 decimal places\n");
			}
		}

		Float weight = null;
		goodValue = false;
		while (!goodValue) {
			try {
				System.out.print("Weight: (ex 113.42)\n");
				String input = co.readLine();
				if (!input.matches("[\\d][\\d][\\d][.][\\d][\\d]")) throw new NumberFormatException();
				weight = Float.parseFloat(input);
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
}
