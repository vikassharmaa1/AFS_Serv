package com.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseFetch {

	private static Connection getDBConnection(String dbName) {

		Connection conn = null;

		try {
			if (dbName.toLowerCase().equals("sql")) {
				conn = DatabaseConnection.getConnection("sql");

			} else if (dbName.toLowerCase().equals("db2_unix")) {

				conn = DatabaseConnection.getConnection("db2_unix");

			} else if (dbName.toLowerCase().equals("db2_mainframe")) {
				conn = DatabaseConnection.getConnection("db2_mainframe");

			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return conn;
	}

	public static ResultSet runDBQuery(String query, String databaseName) {

		Statement stmt = null;
		ResultSet rSet = null;

		try {
			stmt = getDBConnection(databaseName).createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			if (query.toUpperCase().startsWith("UPDATE") || query.toUpperCase().startsWith("DELETE")
					|| query.toUpperCase().startsWith("INSERT")) {

				stmt.executeUpdate(query);

			} else {

				rSet = stmt.executeQuery(query);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return rSet;

	}
}
