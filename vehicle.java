/*
	Model class representing the vehicle table in the database.
*/
public class vehicle
{
	public String serial_no;
	public String maker;
	public String model;
	public Integer year;
	public String color;
	public Integer type_id;

	public vehicle(String serial_no, String maker, String model,
			Integer year, String color, Integer type_id)
	{
		this.serial_no = serial_no;
		this.maker = maker;
		this.model = model;
		this.year = year;
		this.color = color;
		this.type_id = type_id;
	}

	public vehicle(String serial_no)
	{
		this.serial_no = serial_no;
		this.maker = "a";
		this.model = "a";
		this.year = 0;
		this.color = "a";
		this.type_id = 0;
	}

	public vehicle()
	{
		this.serial_no = " ";
		this.maker = " ";
		this.model = " ";
		this.year = 0;
		this.color = " ";
		this.type_id = 0;
	}
}
