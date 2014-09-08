package server.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import shared.model.Batch;


public class BatchDAO {

	private static Logger logger;

	static {
		logger = Logger.getLogger("recordindexer");
	}

	private Database db;

	BatchDAO(Database db) {
		this.db = db;
	}

	/**
	 * Get all batches from DB
	 * 
	 * @return All batches
	 * @throws SQLException 
	 */
	public List<Batch> getAll() throws SQLException {
		logger.entering("server.database.BatchDAO", "getAll");
		List<Batch> batches = null;;
		Connection connection = db.getConnection();

		Statement stmt = connection.createStatement();
		String sql = "Select * from batch";

		ResultSet rs = stmt.executeQuery(sql);

		batches = new ArrayList<Batch>();

		while ( rs.next() ) {

			int id = rs.getInt("id");
			String imageurl = rs.getString("imageurl");
			int projectid = rs.getInt("projectid");
			Boolean isavailable = (rs.getInt("isavailable") != 0);

			Batch b = new Batch(id, projectid, imageurl, isavailable);

			batches.add(b);	
		}

		rs.close();
		stmt.close();


		return batches;

	}

	/**
	 * Update boolean indicating whether batch is currently available
	 * 
	 * @param batchId Id of batch to be updated
	 * @param b - True if batch is available, False if batch is inuse
	 * @return - True if update successful
	 * @throws SQLException 
	 */
	public Boolean setIsAvailable(int isAvailable, int batchId) throws SQLException{
		logger.entering("server.database.BatchDAO", "updateBatchAvailability");

		Connection connection = db.getConnection();


		String sql = "update batch set isavailable = ? where id = ?";

		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, isAvailable);
		stmt.setInt(2, batchId);

		stmt.execute();


		stmt.close();
		return true;

	}

	/**
	 * Gets batch by batch id
	 * 
	 * @param batchId
	 * @return Batch with given id
	 * @throws SQLException 
	 */
	public Batch getBatch(int batchId) throws SQLException{

		Connection connection = db.getConnection();
		System.out.println("getting batch");
		Statement stmt = connection.createStatement();
		String sql = "Select * from batch where id = " + batchId;

		ResultSet rs = stmt.executeQuery(sql);

		Batch b = null;

		while ( rs.next() ) {

			int id = rs.getInt("id");
			String imageurl = rs.getString("imageurl");
			int projectid = rs.getInt("projectid");
			Boolean isavailable = (rs.getInt("isavailable") != 0);

			b = new Batch(id, projectid, imageurl, isavailable);
		}

		rs.close();
		stmt.close();

		return b;

	}

	public String getSampleImage(int projectId) throws SQLException {


		Connection connection = db.getConnection();

		Statement stmt = connection.createStatement();
		String sql = "Select imageurl from batch where projectid = " + projectId;

		ResultSet rs = stmt.executeQuery(sql);

		String url = null;

		if (rs.next()){
			url = rs.getString("imageurl");
		}

		rs.close();
		stmt.close();
		return url;

	}

	/**
	 * Gets next available batch not in use
	 * 
	 * @return Returns next batch not currently in use
	 * @throws SQLException 
	 */
	public Batch getNextAvailableBatch(int projectId) throws SQLException{		
		Connection connection = db.getConnection();

		Statement stmt = connection.createStatement();
		String sql = "Select * from batch where isavailable = 1 and projectid = " + projectId;

		ResultSet rs = stmt.executeQuery(sql);

		Batch b = null;

		if ( rs.next() ) {

			int id = rs.getInt("id");
			String imageurl = rs.getString("imageurl");
			int projectid = rs.getInt("projectid");
			Boolean isavailable = (rs.getInt("isavailable") != 0);


			b = new Batch(id, projectid, imageurl, isavailable);
		}

		rs.close();
		stmt.close();
		return b;

	}

	/**
	 * adds batch to DB
	 * 
	 * @param batch
	 * @return returns true if add is succesfull
	 */
	public Boolean add(Batch batch){
		Connection connection = db.getConnection();
		PreparedStatement stmt = null;
		try {  
			String sql = "INSERT INTO batch"
					+ "(imageurl, projectid, isavailable)"
					+ "VALUES (?, ?, ?)"; 

			stmt = connection.prepareStatement(sql); 
			stmt.setString(1, batch.getImageURL()); 
			stmt.setInt(2, batch.getProjectId()); 
			int available = (batch.getIsAvailable()) ? 1 : 0;
			stmt.setInt(3, available); 

			stmt.executeUpdate();	
			stmt.close(); 
		} 
		catch (SQLException e) { 
			e.printStackTrace();
			return null;
		} 
		return true;
	}

}
