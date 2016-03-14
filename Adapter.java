import java.sql.*;
import java.lang.*;
import java.lang.reflect.Field;

public class Adapter
{
	void toSql(Connection conn, Object obj) {
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
				boolean isString = field.getType() == String.class;
				if (isString) {
					builder.append("'");
				}
				builder.append(field.get(obj).toString());
				if (isString) {
					builder.append("'");
				}
				builder.append(",");
			}
			builder.deleteCharAt(builder.length() -1);
			builder.append(")");

			String createString = builder.toString();
			System.out.println(createString);
			System.out.println(conn == null);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			stmt.executeUpdate(createString);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
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

			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			ResultSet rs = stmt.executeQuery(query);
			// Check that the query returned a result
			if (!rs.isBeforeFirst()) { // returns false if rs has no rows
				return null;
			}
			// If there was a result, use the returned data to create an object
	      	while (rs.next()){
				Field[] newFields = obj.getClass().getFields();

				for (Field field : newFields) {
					if (field.getType() == Float.class) {
						field.set(obj, rs.getFloat(field.getName()));
					} else if (field.getType() == Integer.class) {
						field.set(obj, rs.getInt(field.getName()));
					} else {
						field.set(obj, rs.getObject(field.getName()));
						System.out.println(rs.getObject(field.getName()));
					}
				}
			}
			return obj;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	//T fromSql(Connection conn);
}
