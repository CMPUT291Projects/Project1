import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.rowset.serial.SerialBlob;
import java.text.SimpleDateFormat;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialException;
import java.text.ParseException;


public class LicenceRegistration
{
	private Connection conn;

	public LicenceRegistration(Connection conn)
	{
		this.conn = conn;
	}

	public void run() {
		System.out.print("licence no\n");
		Console co = System.console();
		String licence_no = co.readLine();

		System.out.print("sin\n");
		String sin = co.readLine();

		System.out.print("classType\n");
		String classType = co.readLine();

		String file1 = "window-sm.jpg";
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

		SerialBlob picture = null;
		try{
			picture = new SerialBlob(buffer.toByteArray());
		}
		catch(SerialException e){
			System.out.print("Error with Byte array");
		}
		catch(SQLException e){
			System.out.print("Error with Serialblob");
		}

		System.out.print("issuing_date\n");
		String issuing_String = co.readLine();
		java.util.Date issuing_date = null;
		try{
			issuing_date = new SimpleDateFormat().parse(issuing_String);
		}
		catch(ParseException e){
			System.out.print("Error reading issuing date.");
		}

		System.out.print("expiring_date\n");
		String expiring_String = co.readLine();
		java.util.Date expiring_date = null;
		try{
			expiring_date = new SimpleDateFormat().parse(expiring_String);
		}
		catch(ParseException e3){
			System.out.print("Error reading expiring date.");
		}

		drive_licence dl = new drive_licence(licence_no, sin, classType, picture, issuing_date, expiring_date);
		Adapter a = new Adapter();
		a.toSql(conn, dl);
	}
}
