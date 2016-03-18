/*
	Model class representing the driving_condition table in the database.
*/
public class driving_condition {
	public Integer c_id;
	public String description;

	public driving_condition(int c_id, String description){
		this.c_id = c_id;
		this.description = description;
	}

	public driving_condition(){
		this.c_id = 0;
		this.description = " ";
	}
}
