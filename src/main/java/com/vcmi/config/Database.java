package com.vcmi.config;

import com.vcmi.Message;
import com.vcmi.VCMI;
import java.io.File;
import java.math.BigInteger;
import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class Database {

	private Connection connection;
	private String tablePrefix;

	public boolean connect() {
		String type = Config.getDatabaseType();
		tablePrefix = Config.getDatabaseTablePrefix();
		return (type.equalsIgnoreCase("sqlite")) ? connectSQLite() : connectMySQL();
	}

	private boolean connectSQLite() {
		String directory = VCMI.pluginPath + "/storage";
		File storageDirectory = new File(directory);

		if (!storageDirectory.exists() && !storageDirectory.mkdirs()) {
			Message.error("Failed to create SQL storage directory!");
			return false;
		}

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + directory + "/" + Config.getDatabaseName() + ".db");
			Message.info("Connection to the SQL database is successful");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean connectMySQL() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			connection = DriverManager.getConnection(
					"jdbc:mariadb://" + Config.getDatabaseHost() + ":" + Config.getDatabasePort() + "/" + Config.getDatabaseName() + "?useSSL=" + Config.getSsl(),
					Config.getDatabaseUser(), Config.getDatabasePassword());
			Message.info("Connection to the MySQL database is successful");
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			Message.error("Failed to connect to MySQL!");
			e.printStackTrace();
			return false;
		}
	}

	private boolean executeUpdateSync(String query, Object... parameters) {
		return executeSync(query, parameters, PreparedStatement::executeUpdate) != null;
	}

	private ResultSet executeQuerySync(String query, Object... parameters) {
		return executeSync(query, parameters, PreparedStatement::executeQuery);
	}

	private <T> T executeSync(String query, Object[] parameters, SQLExecutor<T> executor) {
		try (PreparedStatement preparedStatement = prepareStatement(query, parameters)) {
			return executor.execute(preparedStatement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private PreparedStatement prepareStatement(String query, Object... parameters) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		for (int i = 0; i < parameters.length; i++) {
			preparedStatement.setObject(i + 1, parameters[i]);
		}
		return preparedStatement;
	}

	@FunctionalInterface
	private interface SQLExecutor<T> {
		T execute(PreparedStatement preparedStatement) throws SQLException;
	}

	// Helper methods to construct queries
	private String constructInsertQuery(String tableName, String columns, int valueCount) {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ").append(appendPrefix(tableName)).append(" (").append(columns).append(") VALUES (");
		query.append("?,".repeat(Math.max(0, valueCount)));
		query.deleteCharAt(query.length() - 1); // Remove last comma
		query.append(")");
		return query.toString();
	}

	private String constructUpdateOrDeleteQuery(String tableName, String set, String where) {
		return "UPDATE " + appendPrefix(tableName) + " SET " + set + " WHERE " + where;
	}

	private String constructSelectQuery(String tableName, String columns, String where) {
		return "SELECT " + columns + " FROM " + appendPrefix(tableName) + " WHERE " + where;
	}

	// The main methods
	public boolean createTable(String tableName, String columns) {
		return executeUpdateSync("CREATE TABLE IF NOT EXISTS " + appendPrefix(tableName) + " (" + columns + ")");
	}

	public boolean insert(String tableName, String columns, Object... values) {
		return executeUpdateSync(constructInsertQuery(tableName, columns, values.length), castValuesToLong(values));
	}

	public boolean update(String tableName, String set, String where, Object... values) {
		return executeUpdateSync(constructUpdateOrDeleteQuery(tableName, set, where), castValuesToLong(values));
	}

	public boolean delete(String tableName, String where, Object... values) {
		return executeUpdateSync("DELETE FROM " + appendPrefix(tableName) + " WHERE " + where, castValuesToLong(values));
	}

	public ResultSet select(String tableName, String columns, String where, Object... values) {
		return executeQuerySync(constructSelectQuery(tableName, columns, where), castValuesToLong(values));
	}

	public CompletableFuture<Boolean> createTableAsync(String tableName, String columns) {
		return CompletableFuture.supplyAsync(() -> createTable(tableName, columns));
	}

	public CompletableFuture<Boolean> insertAsync(String tableName, String columns, Object... values) {
		return CompletableFuture.supplyAsync(() -> insert(tableName, columns, castValuesToLong(values)));
	}

	public CompletableFuture<Boolean> updateAsync(String tableName, String set, String where, Object... values) {
		return CompletableFuture.supplyAsync(() -> update(tableName, set, where, castValuesToLong(values)));
	}

	public CompletableFuture<Boolean> deleteAsync(String tableName, String where, Object... values) {
		return CompletableFuture.supplyAsync(() -> delete(tableName, where, castValuesToLong(values)));
	}

	public CompletableFuture<ResultSet> selectAsync(String tableName, String columns, String where, Object... values) {
		return CompletableFuture.supplyAsync(() -> select(tableName, columns, where, castValuesToLong(values)));
	}

	public CompletableFuture<ResultSet> executeQueryAsync(String query, Object... parameters) {
		return CompletableFuture.supplyAsync(() -> executeQuerySync(query, castValuesToLong(parameters)));
	}

	public CompletableFuture<Boolean> executeUpdateAsync(String query, Object... parameters) {
		return CompletableFuture.supplyAsync(() -> executeUpdateSync(query, castValuesToLong(parameters)));
	}

	public boolean exists(String tableName, String where, Object... values) {
		try (ResultSet resultSet = select(tableName, "*", where, castValuesToLong(values))) {
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String appendPrefix(String tableName) {
		return tablePrefix + tableName;
	}

	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception ignored) {}
	}

	public CompletableFuture<Void> closeAsync() {
		return CompletableFuture.runAsync(this::close);
	}

	private Object[] castValuesToLong(Object... values) {
		Object[] castedValues = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			Object value = values[i];
			if (value instanceof BigInteger) {
				castedValues[i] = ((BigInteger) value).longValue();
			} else if (value instanceof String && isNumeric((String) value)) {
				castedValues[i] = new BigInteger((String) value).longValue();
			} else {
				castedValues[i] = value;
			}
		}
		return castedValues;
	}

	private boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}
}
