public class restriction
{
	public String licence_no;
	public Integer r_id;

	public restriction(String licence_no, int r_id)
	{
		this.licence_no = licence_no;
		this.r_id = r_id;
	}

	public restriction()
	{
		this.licence_no = " ";
		this.r_id = 0;
	}
}
