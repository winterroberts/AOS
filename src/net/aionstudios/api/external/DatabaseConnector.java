package net.aionstudios.api.external;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.aionstudios.api.util.DatabaseUtils;

/**
 * Interfaces with the JDBC driver.
 * 
 * @author Winter Roberts
 *
 */
public class DatabaseConnector {
	
	private static String host = "";
	private static String user = "";
	private static String password = "";
	private static String database = "";
	
	private static boolean loadedJDBC = false;
	
	private static Connection db;
	
	/**
	 * Establishes a connection the the database.
	 * @param hostname				The database hostname.
	 * @param databaseName			The default transacting database name.
	 * @param databasePort			The connection port (3306 is default).
	 * @param databaseUser			The user for this database connection.
	 * @param databasePassword		The password for this database connection.
	 * @return	True if the connection was successful, false otherwise.
	 */
	public static boolean setupDatabase(String hostname, String databaseName, String databasePort, String databaseUser, String databasePassword) {
		if(host=="") {
			host = "jdbc:mysql://"+hostname+":"+databasePort;
			database = databaseName;
			user = databaseUser;
			password = databasePassword;
			try {
				loadJDBC();
				db = DriverManager.getConnection(host, user, password);
			} catch (SQLException e1) {
				System.err.println("DatabaseConnector failed while connecting to database '"+database+"'!");
				e1.printStackTrace();
			}
			try {
				DatabaseUtils.prepareAndExecute("CREATE DATABASE IF NOT EXISTS `"+database+"` CHARACTER SET latin1 COLLATE latin1_general_cs;", false);
				getDatabase().setCatalog(database);
			} catch (SQLException e) {
				System.err.println("DatabaseConnector failed while switching to database '"+database+"'!");
				e.printStackTrace();
			}
			return true;
		}
		System.err.println("Only one database can be connected per instance.");
		return false;
	}
	
	/**
	 * Establishes a connection the the database.
	 * @param hostname				The database hostname.
	 * @param databaseName			The default transacting database name.
	 * @param databaseUser			The user for this database connection.
	 * @param databasePassword		The password for this database connection.
	 * @return	True if the connection was successful, false otherwise.
	 */
	public static boolean setupDatabase(String hostname, String databaseName, String databaseUser, String databasePassword) {
		if(host=="") {
			host = "jdbc:mysql://"+hostname;
			database = databaseName;
			user = databaseUser;
			password = databasePassword;
			try {
				loadJDBC();
				db = DriverManager.getConnection(host, user, password);
			} catch (SQLException e1) {
				System.err.println("DatabaseConnector failed while connecting to database '"+database+"'!");
				e1.printStackTrace();
			}
			try {
				DatabaseUtils.prepareAndExecute("CREATE DATABASE IF NOT EXISTS `"+database+"` CHARACTER SET latin1 COLLATE latin1_general_cs;", false);
				getDatabase().setCatalog(database);
			} catch (SQLException e) {
				System.err.println("DatabaseConnector failed while switching to database '"+database+"'!");
				e.printStackTrace();
			}
			return true;
		}
		System.err.println("Only one database can be connected per instance.");
		return false;
	}
	
	/**
	 * Loads the database drive for JDBC if it hasn't been already
	 */
	public static void loadJDBC() {
		if(loadedJDBC==false) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e1) {
				System.out.println("Failed loading JDBC");
				e1.printStackTrace();
			}
		}
		loadedJDBC = true;
	}
	
	/**
	 * Gets an instance of the database to query through
	 * @return	A database connection
	 */
	public static Connection getDatabase() {
		return db;
	}

}
