import java.util.Date;

public class people{

	public String sin;
	public String name;
	public Float height;
	public Float weight;
	public String eyecolor;
	public String haircolor;
	public String addr;
	public String gender;
	public Date birthday;

	public people(String sin, String name, float height, float weight, String eyecolor,
		String haircolor, String addr, String gender, Date birthday){

		this.sin = sin;
		this.name = name;
		this.height = height;
		this.weight = weight;
		this.eyecolor = eyecolor;
		this.haircolor = haircolor;
		this.addr = addr;
		this.gender = gender;
		this.birthday = birthday;
	}

	public people(String sin) {
		this.sin = sin;
		this.name = " ";
		this.height = new Float(1.0);
		this.weight = new Float(1.0);
		this.eyecolor = " ";
		this.haircolor = " ";
		this.addr = "";
		this.gender = " ";
		this.birthday = null;
	}
}
