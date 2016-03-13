import java.util.*;
import java.sql.*;
import java.io.*;

public class ViolationRecord 
{
	private Connection conn;

	public ViolationRecord(Connection conn)
	{
		this.conn = conn;
	}
}
