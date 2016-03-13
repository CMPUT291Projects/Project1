import java.sql.*;
import java.lang.*;

public class Adapter
{
	boolean toSql(Connection conn, Object obj) {
		try {
			String name = obj.getClass().getName();
			Fields[] fields = obj.getClass().getFields();
			StringBuilder builder = new StringBuilder();
			builder.append("insert into ");
			builder.append(name);
			builder.append(" (");
			for (Field field : fields) {
				builder.append(field.getName());
				builder.append(",");
			}
			builder.deleteCharAt(builder.length() -1);

			builder.append(") values (");
			for (Field field: fields) {
				builder.append(field.get().toString());
				builder.append(",");
			}
			builder.deleteCharAt(builder.length() -1);
			builder.append(")");
			
			String createString = builder.toString();
		
			Statement stmt = cnxn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	
			stmt.executeUpdate(createString);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	

	//T fromSql(Connection conn);
}
