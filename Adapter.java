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
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			stmt.executeUpdate(createString);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
