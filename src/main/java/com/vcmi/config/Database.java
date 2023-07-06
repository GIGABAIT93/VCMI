package com.vcmi.config;

import com.vcmi.VCMI;

import java.io.File;
import java.sql.*;
public class Database {

	private Connection connection;
	private String tablePrefix;

	public boolean connect() {
		String type = Config.getDatabaseType();
		tablePrefix = Config.getDatabaseTablePrefix();

		try {
			if (type.equalsIgnoreCase("sqlite")) {
				String directory = VCMI.rootPath + "/storage";
				File storageDirectory = new File(directory);

				if (!storageDirectory.exists()) {
					if (!storageDirectory.mkdirs()) {
						System.out.println("Failed to create storage directory!");
						return false;
					}
				}

				connection = DriverManager
						.getConnection("jdbc:sqlite:" + directory + "/" + Config.getDatabaseName() + ".db");
			} else if (type.equalsIgnoreCase("mysql")) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
				} catch (ClassNotFoundException ex) {
					System.out.println(ex);
				}
				connection = DriverManager.getConnection(
						"jdbc:mysql://" + Config.getDatabaseHost() + ":" + Config.getDatabasePort() + "/"
								+ Config.getDatabaseName() + "?useSSL=false",
						Config.getDatabaseUser(), Config.getDatabasePassword());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public Connection getConnection() {
		return connection;
	}

	public boolean createTable(String tableName, String columns) {
		return executeUpdate("CREATE TABLE IF NOT EXISTS " + getTableWithPrefix(tableName) + " (" + columns + ")");
	}

	public boolean insert(String tableName, String columns, Object... values) {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ").append(getTableWithPrefix(tableName)).append(" (").append(columns)
				.append(") VALUES (");

		for (int i = 0; i < values.length; i++) {
			query.append("?");
			if (i < values.length - 1) {
				query.append(", ");
			}
		}
		query.append(")");

		return executeUpdate(query.toString(), values);
	}

	public boolean update(String tableName, String set, String where, Object... values) {
		String query = "UPDATE " + getTableWithPrefix(tableName) + " SET " + set + " WHERE " + where;
		return executeUpdate(query, values);
	}

	public boolean delete(String tableName, String where, Object... values) {
		String query = "DELETE FROM " + getTableWithPrefix(tableName) + " WHERE " + where;
		return executeUpdate(query, values);
	}

	public ResultSet select(String tableName, String columns, String where, Object... values) {
		String query = "SELECT " + columns + " FROM " + getTableWithPrefix(tableName) + " WHERE " + where;
		return executeQuery(query, values);
	}

	public ResultSet executeQuery(String query, Object... parameters) {
		try {
			PreparedStatement preparedStatement = prepareStatement(query, parameters);
			return preparedStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean executeUpdate(String query, Object... parameters) {
		try {
			PreparedStatement preparedStatement = prepareStatement(query, parameters);
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private PreparedStatement prepareStatement(String query, Object... parameters) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(query);

		for (int i = 0; i < parameters.length; i++) {
			preparedStatement.setObject(i + 1, parameters[i]);
		}

		return preparedStatement;
	}

	public String getTableWithPrefix(String tableName) {
		return tablePrefix + tableName;
	}

	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}


//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException ex) {
//            System.out.println(ex);
//        }
//
//        Database db = new Database();
//        db.connect();
//        db.createTable("users", "id INT PRIMARY KEY, name VARCHAR(255), age INT");
//        db.insert("users", "id, name, age", 1, "John Doe", 25);
//        db.insert("users", "id, name, age", 2, "GIGABAIT", 30);
//        db.update("users", "name = ?, age = ?", "id = ?", "Jane Doe", 26, 1);
////        db.delete("users", "id = ?", 1);
//        ResultSet rs = db.select("users", "*", "age > ?", 18);
//        try {
//            while(rs.next()){
//                System.out.println("ID: " + rs.getInt("id"));
//                System.out.println("Name: " + rs.getString("name"));
//                System.out.println("Age: " + rs.getInt("age"));
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }