public class Ticket
{
	public int ticket_no;
	public String violator_no;
	public String vehicle_id;
	public String office_no;
	public String vtype;
	public Date vdate;
	public String place;
	public String descriptions;

	public Ticket(int ticket_no, String violator_no, String office_no, 
			String vtype, Data vdate, String place, String descriptions)
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
}
