import java.sql.Blob;
import javax.sql.rowset.serial.SerialBlob;

public class drive_licence {
	String licence_no;
	String sin;
	String classType;
	Blob photo;
	String issuing_date;
	String expiring_date;

	public drive_licence(String licence_no, String sin, String classType, Blob photo, String issuing_date, String expiring_date){
		this.licence_no = licence_no;
		this.sin = sin;
		this.classType = classType;
		this.photo = photo;
		this.issuing_date = issuing_date;
		this.expiring_date = expiring_date;
	}
}
