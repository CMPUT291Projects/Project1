import java.util.*;
import java.sql.*;
import java.io.*;

public class AutoTransaction
{
	private Connection conn;

	public AutoTransaction(Connection conn)
	{
		this.conn = conn;
	}
}
