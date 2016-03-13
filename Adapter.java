import java.sql.*;
import java.lang.*;
import java.lang.reflect.Field;

public class Adapter
{
	boolean toSql(Connection conn, Object obj) {
		try {
			String name = obj.getClass().getName();
			Field[] fields = obj.getClass().getFields();
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
				builder.append(field.get(obj).toString());
				builder.append(",");
			}
			builder.deleteCharAt(builder.length() -1);
			builder.append(")");

			String createString = builder.toString();
			System.out.println(createString);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			stmt.executeUpdate(createString);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	Object searchTablePrimaryKey(Connection conn, Object obj, String colName, Object value) {
		try {
			String table = obj.getClass().getName();
			Field[] fields = obj.getClass().getFields();

			StringBuilder builder = new StringBuilder();
			builder.append("select ");
			for (Field field : fields) {
				builder.append(field.getName());
				builder.append(",");
			}
			builder.deleteCharAt(builder.length() -1);
			builder.append(" from ");
			builder.append(table);
			builder.append(" where ");
			builder.append(colName);
			builder.append("=");
			builder.append(value);

			String query = builder.toString();
			System.out.println(query);

			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			ResultSet rs = stmt.executeQuery(query);
	      	while (rs.next())
		    {
				//Class T = obj.getClass();
				Object result = obj.getClass().newInstance();
				Field[] newFields = result.getClass().getFields();

				for (Field field : newFields) {
					field.set(result, rs.getString(field.getName()));
					System.out.println(rs.getString(field.getName()));
				}
			}
			return result;
		} catch (Exception ex) {
			return null;
		}
	}

	//T fromSql(Connection conn);
}
