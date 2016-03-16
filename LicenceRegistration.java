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
			if(!idExists(temp_sin, conn)) {
				sin = temp_sin;
			} else {
				System.out.print("SIN already exists, try again\n");
			}
		} while (sin == null);

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
}
