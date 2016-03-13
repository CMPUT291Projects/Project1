public class people{

	String sin;
	String name;
	float height;
	float weight;
	String eyecolor;
	String haircolor;
	String addr;
	String gender;
	String birthday;
	public people(String sin, String name, float height, float weight, String eyecolor,
		String haircolor, String addr, String gender, String birthday){

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
		this.height = 1.0;
		this.weight = 1.0;
		this.eyecolor = " ";
		this.haircolor = " ";
		this.addr = "";
		this.gender = " ";
		this.birthday = " ";
	}
}
