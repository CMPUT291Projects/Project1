public class ticket_type
{
	public String vtype;
	public Float fine;

	public ticket_type(String vtype, Float fine)
	{
		this.vtype = vtype;
		this.fine = fine;
	}

	public ticket_type()
	{
		this.vtype = " ";
		this.fine = new Float(1.0);
	}
}
