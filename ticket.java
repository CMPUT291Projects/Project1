import java.util.Date;

public class ticket
{
	public Integer ticket_no;
	public String violator_no;
	public String vehicle_id;
	public String office_no;
	public String vtype;
	public Date vdate;
	public String place;
	public String descriptions;

	public ticket(Integer ticket_no, String violator_no, String vehicle_id, String office_no,
			String vtype, Date vdate, String place, String descriptions)
	{
		this.ticket_no = ticket_no;
		this.violator_no = violator_no;
		this.vehicle_id = vehicle_id;
		this.office_no = office_no;
		this.vtype = vtype;
		this.vdate = vdate;
		this.place = place;
		this.descriptions = descriptions;
	}

	public ticket()
	{
		this.ticket_no = 0;
		this.violator_no = " ";
		this.vehicle_id = " ";
		this.office_no =  " ";
		this.vtype = " ";
		this.vdate = null;
		this.place = " ";
		this.descriptions = " ";
	}
}
