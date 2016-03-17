public class owner
{
	public String owner_id;
	public String vehicle_id;
	public String is_primary_owner;

	public owner(String owner_id, String vehicle_id, String is_primary_owner)
	{
		this.owner_id = owner_id;
		this.vehicle_id = vehicle_id;
		this.is_primary_owner = is_primary_owner;
	}

	public owner()
	{
		this.owner_id = " ";
		this.vehicle_id = " ";
		this.is_primary_owner = " ";
	}
}
