import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.Console;

/*
	Class for taking input entered by the user in the console, and ensuring it is
	in the correct type and format.
*/
public class ConsoleParser
{
	Console co;

	public ConsoleParser() {
		co = System.console();
	}

	public Integer getInt(String prompt) {
		boolean goodValue = false;
		Integer value = null;
		while(!goodValue) {
			try {
				System.out.print(prompt);
				value = Integer.parseInt(co.readLine());
				goodValue = true;
			} catch (NumberFormatException ex) {
				System.out.print("Invalid Integer Format\n");
			}
		}
		return value;
	}

	/*
		Get date values from the user.  Ensure they are in the correct format for
		entering into the database.
	*/
	public Date getDate(String prompt) {
		boolean goodValue = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date myDate = null;
		while (!goodValue) {
			System.out.print(prompt);
			try {
				myDate = format.parse(co.readLine());
				goodValue = true;
			} catch (Exception ex) {
				System.out.print("invalid date format, use 'yyyy-MM-dd'\n");
			}
		}
		return myDate;
	}

	public String getString(String prompt) {
		System.out.print(prompt);
		return co.readLine().toLowerCase();
	}
}
