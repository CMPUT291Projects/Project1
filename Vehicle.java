public class Vehicle
{
	public String serial_no;
	public String maker;
	public String model;
	public int year;
	public String color;
	public int type_id;

	public Vehicle(String serial_no, String maker, String model,
			int year, String color, int type_id)
	{

	}

	public Vehicle(String serial_no)
	{
		this.serial_no = serial_no;
		this.maker = "a";
		this.model = "a";
		this.year = 0;
		this.color = "a";
		this.type_id = 1;
	}
}
