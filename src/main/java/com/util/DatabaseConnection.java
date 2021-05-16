package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	public static Connection getConnection(String databaseType) throws SQLException {
		Connection conn = null;
		String driverName = null;

		try {
			if (databaseType.equals("sql")) {

				driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
				Class.forName(driverName);
				conn = DriverManager.getConnection(
						"jdbc:dqlserver://" + System.getProperty("bank.sql.server") + ";databaseName"
								+ System.getProperty("bank.sql.database"),
						System.getProperty("bank.sql.username"), System.getProperty("bank.sql.password"));

			}

			else if (databaseType.equals("db2_unix")) {
				driverName = "com.ibm.db2.jcc.DB2Driver";
				Class.forName(driverName);
				String ecrdb_url = "jdbc:db2://" + System.getProperty("bank.db2.unix.server") + ":"
						+ System.getProperty("bank.db2_unix.port") + "/" + System.getProperty("bank.db2_unix.database")
						+ "";
				conn = DriverManager.getConnection(ecrdb_url, System.getProperty("bank.db2_unix.username"),
						System.getProperty("bank.db2_unix.password"));

			}

			else if (databaseType.equals("db2_mainframe")) {
				driverName = "com.ibm.db2.jcc.DB2Driver";
				Class.forName(driverName);
				String ecrdb_url = "jdbc:db2://" + System.getProperty("bank.db2.mainframe.server") + ":"
						+ System.getProperty("bank.db2_mainframe.port") + "/"
						+ System.getProperty("bank.db2_mainframe.database") + "";
				conn = DriverManager.getConnection(ecrdb_url, System.getProperty("bank.db2_mainframe.username"),
						System.getProperty("bank.db2_mainframe.password"));

			}
		} catch (ClassNotFoundException e) {
			System.out.println(e.toString());
		} catch (SQLException e) {

			MessageLogger.WriteExceptionToConsole(
					"There was an error creating a " + databaseType + "database connection.", e.toString());
		}

		return conn;

	}

}
