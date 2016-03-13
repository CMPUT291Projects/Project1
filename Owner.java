public class Owner
{
	public String owner_id;
	public String vehicle_id;
	public boolean is_primary_owner;

	public Owner(String owner_id, String vehicle_id, boolean is_primary_owner)
	{
		this.owner_id = owner_id;
		this.vehicle_id = vehicle_id;
		this.is_primary_owner = is_primary_owner;
	}
}
