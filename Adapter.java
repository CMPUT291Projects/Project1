import java.sql.*;
import java.lang.*;
import java.lang.reflect.Field;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
				boolean isFloat = field.getType() == Float.class;
				if (isString || isDate) {
					builder.append("'");
				}
				if (isDate) {
					SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
					Date date = (Date) field.get(obj);
					builder.append(format.format(date));
				} else if (isFloat) {
					DecimalFormat df = new DecimalFormat("00.000");
					Float flt = (Float) field.get(obj);
					builder.append(df.format(flt));
				}  else {
					builder.append(field.get(obj).toString());
				}
				if (isString || isDate) {
					builder.append("'");
				}
				builder.append(",");
			}
			builder.deleteCharAt(builder.length() -1);
			builder.append(")");

			String createString = builder.toString();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			stmt.executeUpdate(createString);
		} catch (SQLException ex) {
			System.out.println(
				String.format("Unable to process the transaction because of a SQL exception: %s\n", ex.getMessage()));
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
	}

	/*
	   Searches database for the given value of primary key.  The column name of the
	   primary key should be given too.  This function assumes a primary key column
	   is being used, and so only returns only one result (if a non-unique value is
	   givne to this funciton it will return the last result found).
	*/
	Object searchTablePrimaryKey(Connection conn, Object obj, String colName, Object value) {
		try {
			String table = obj.getClass().getName();
			Field[] fields = obj.getClass().getFields();

			StringBuilder builder = new StringBuilder();
			builder.append("select ");
			for (Field field : fields) {
				if (field.getName().equals("classType")) {
					builder.append("class");
				} else {
					builder.append(field.getName());
				}
				builder.append(",");
			}

			builder.deleteCharAt(builder.length() -1);
			builder.append(" from ");
			builder.append(table);
			builder.append(" where lower(");
			builder.append(colName);
			builder.append(")=");
			if (value instanceof String) {
				builder.append("'");
			}
			builder.append(value);
			if (value instanceof String) {
				builder.append("'");
			}

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
					} else if (field.getName().equals("classType")) {
						field.set(obj, rs.getObject("class"));
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

	ArrayList<Object> searchTableAnyKey(Connection conn, Object obj, String colName, Object value) {
		try {
			String table = obj.getClass().getName();
			Field[] fields = obj.getClass().getFields();

			StringBuilder builder = new StringBuilder();
			builder.append("select ");
			for (Field field : fields) {
				if (field.getName().equals("classType")) {
					builder.append("class");
				} else {
					builder.append(field.getName());
				}
				builder.append(",");
			}
			builder.deleteCharAt(builder.length() -1);
			builder.append(" from ");
			builder.append(table);
			builder.append(" where lower(");
			builder.append(colName);
			builder.append(")='");
			builder.append(value);
			builder.append("'");

			String query = builder.toString();

			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			ResultSet rs = stmt.executeQuery(query);
			ArrayList<Object> results = new ArrayList<Object>();
			// Check that the query returned a result
			if (!rs.isBeforeFirst()) { // returns false if rs has no rows
				return null;
			}
			Field[] newFields = obj.getClass().getFields();
			// If there was a result, use the returned data to create an object
			while (rs.next()) {
				Object newobj = obj.getClass().newInstance();

				for (Field field : newFields) {
					if (field.getType() == Float.class) {
						field.set(newobj, rs.getFloat(field.getName()));
					} else if (field.getType() == Integer.class) {
						field.set(newobj, rs.getInt(field.getName()));
					} else if (field.getName().equals("classType")) {
						field.set(newobj, rs.getObject("class"));
					} else {
						field.set(newobj, rs.getObject(field.getName()));
					}
				}
				results.add(newobj);
			}
			return results;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	//T fromSql(Connection conn);
}
