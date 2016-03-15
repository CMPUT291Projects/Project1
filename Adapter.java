import java.sql.*;
import java.lang.*;
import java.lang.reflect.Field;
import java.util.Date;
import java.text.SimpleDateFormat;

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
				//System.out.println(field.getType());
				boolean isString = field.getType() == String.class;
				boolean isDate = field.getType() == Date.class;
				if (isString || isDate) {
					builder.append("'");
				}
				//if (isDate) {
					// Justin Cave, http://stackoverflow.com/questions/9180014/using-oracle-to-date-function-for-date-string-with-milliseconds, 2016-03-14
					//builder.append("TO_DATE(substr('");
				//}
				if (!isDate) {
					builder.append(field.get(obj).toString());
				} else {
					SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
					Date date = (Date) field.get(obj);
					builder.append(format.format(date));
				}
				if (isString || isDate) {
					builder.append("'");
				}
				//if (isDate) {
					//builder.append("', 1, 19), 'YYYY-MM-DD HH24:MI:SS')");
				//}
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
	      		while (rs.next()) {
				Field[] newFields = obj.getClass().getFields();

				for (Field field : newFields) {
					if (field.getType() == Float.class) {
						field.set(obj, rs.getFloat(field.getName()));
					} else if (field.getType() == Integer.class) {
						field.set(obj, rs.getInt(field.getName()));
					} else {
						field.set(obj, rs.getObject(field.getName()));
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
