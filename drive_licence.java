import java.util.*;
import java.sql.Blob;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialException;

public class drive_licence {
	public String licence_no;
	public String sin;
	public String classType;
	public Blob photo;
	public Date issuing_date;
	public Date expiring_date;

	public drive_licence(String licence_no, String sin, String classType, Blob photo, Date issuing_date, Date expiring_date){
		this.licence_no = licence_no;
		this.sin = sin;
		this.classType = classType;
		this.photo = photo;
		this.issuing_date = issuing_date;
		this.expiring_date = expiring_date;
	}

	public drive_licence(String licence_no){
		this.licence_no = licence_no;
		this.sin = " ";
		this.classType = " ";
		try{
			this.photo = new SerialBlob(new byte[1]);
		}
		catch(SerialException e){
			System.out.print("Error with Byte array");
		}
		catch(SQLException e){
			System.out.print("Error with Serialblob");
		}
		this.issuing_date = issuing_date;
		this.expiring_date = expiring_date;
	}

	public drive_licence(){
		this.licence_no = " ";
		this.sin = " ";
		this.classType = " ";
		try{
			this.photo = new SerialBlob(new byte[1]);
		}
		catch(SerialException e){
			System.out.print("Error with Byte array");
		}
		catch(SQLException e){
			System.out.print("Error with Serialblob");
		}
		this.issuing_date = issuing_date;
		this.expiring_date = expiring_date;
	}
}
