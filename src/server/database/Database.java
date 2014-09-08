package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;


public class Database {

	private static Logger logger;

	static {
		logger = Logger.getLogger("recordindexer");
	}

	public static void initialize() throws DatabaseException {

		logger.entering("server.database.Database", "initialize");

		// TODO: Load the SQLite database driver
		try { 
			final String driver = "org.sqlite.JDBC"; 
			Class.forName(driver); 
		} 
		catch(ClassNotFoundException e) { 
			// ERROR! Could not load database driver 
		} 

		logger.exiting("server.database.Database", "initialize");
	}

	private BatchDAO batchDAO;
	private CellDAO cellDAO;
	private FieldDAO fieldDAO;
	private ProjectDAO projectDAO;
	private UserDAO userDAO;
	private Connection connection;

	public Database() {
		this.batchDAO = new BatchDAO(this);
		this.cellDAO  = new CellDAO(this);
		this.fieldDAO = new FieldDAO(this);
		this.projectDAO = new ProjectDAO(this);
		this.userDAO = new UserDAO(this);
		connection = null;
	}

	public BatchDAO getBatchDAO() {
		return batchDAO;
	}

	public CellDAO getCellDAO() {
		return cellDAO;
	}

	public FieldDAO getFieldDAO() {
		return fieldDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public Connection getConnection() {
		return connection;
	}

	public void startTransaction() throws DatabaseException {
		System.out.println("\nentering start transaction");

		//Open a connection to the database and start a transaction
		//String dbName = "db" + File.separator + "recordindexer.sqlite"; //is this right???
		String dbName = "recordindexer.sqlite";
		String connectionURL = "jdbc:sqlite:" + dbName; 

		try { 
			// Open a database connection 
			connection = DriverManager.getConnection(connectionURL); 

			// Start a transaction 
			connection.setAutoCommit(false); 
		} 
		catch (SQLException e) { 
			// ERROR 
			e.printStackTrace();
		} 
		System.out.println("exiting start transaction\n");

	}

	public void endTransaction(boolean commit) {

		System.out.println("\nenterting end transaction");

		//Commit or rollback the transaction and close the connection
		try { 
			if (commit) { //IF ALL DATABASE OPERATIONS SUCCEEDED
				connection.commit(); 
			} 
			else { 
				connection.rollback(); 
			} 
			
			connection.close();
		} 
		catch (SQLException e) { 
			System.out.println(e.toString());
			// ERROR 
		} 

		connection = null;

		System.out.println("exiting end transaction\n");
	}
	
}
