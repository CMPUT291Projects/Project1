/*
	Model class representing the vehicle_type table in the database.
*/
public class vehicle_type
{
	public Integer type_id;
	public String type;

	public vehicle_type(int type_id, String type)
	{
		this.type_id = type_id;
		this.type = type;
	}
}
