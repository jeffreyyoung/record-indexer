package server.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import shared.model.User;


public class UserDAO {

	private Database db;

	public UserDAO(Database db) {
		this.db = db;
	}

	/**
	 * Get user with entered Username and Password
	 * 
	 * 
	 * @param username
	 * @param password
	 * @return User with given attributes
	 * @throws SQLException 
	 */
	public User getUserByUsernameAndPassword(String username, String password) throws SQLException{
		Connection connection = db.getConnection();
		System.out.println("getting user by username and password");

		String sql = "Select * from user where username = ? and password = ?";

		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, username);
		stmt.setString(2, password);

		ResultSet rs = stmt.executeQuery();

		User user = null;

		if (rs.next()){
			int id = rs.getInt("id");
			//String username = rs.getString("username");
			//String password = rs.getString("username");
			String firstname = rs.getString("firstname");
			String lastname = rs.getString("lastname");
			String email = rs.getString("email");
			int recordsindexed = rs.getInt("recordsindexed");
			int currentBatchId = rs.getInt("batchid");

			user = new User(id, username, password, firstname, lastname, email, recordsindexed, currentBatchId);  
		}


		rs.close();
		stmt.close();
		return user;


	}

	public void incrementNumRecordsIndexed(int userId , int numRecordsIndexed) throws SQLException{
		System.out.println("incrementing num records indexed");
		
		Connection connection = db.getConnection();
		
		String sql = "update user set recordsindexed = recordsindexed + ? where id = " + userId; //TODO figure this out...

		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, numRecordsIndexed);
		stmt.executeUpdate();
		stmt.close();

	}

	public void updateCurrentBatchId(String username, int currentBatchId) throws SQLException{
		System.out.println("updating user's current batch Id ");
		
		Connection connection = db.getConnection();
		PreparedStatement stmt = null;

		String sql = "UPDATE user SET batchid = ? where username = ?";
		stmt = connection.prepareStatement(sql); 
		stmt.setInt(1, currentBatchId); 
		stmt.setString(2, username); 
		stmt.executeUpdate();	
		stmt.close();

	}

	/**
	 * Add user to database
	 * 
	 * @param user
	 * @return Returns true if add succesfull
	 */
	public Boolean addUser( User user){
		Connection connection = db.getConnection();
		PreparedStatement stmt = null;
		try {  
			String sql = "INSERT INTO USER" +
					"(username, password, firstname, lastname, email, recordsindexed, batchid)" + 
					"						VALUES (?, ?, ?, ?, ?, ?, ?)";
			stmt = connection.prepareStatement(sql); 
			stmt.setString(1, user.getUsername()); 
			stmt.setString(2, user.getPassword()); 
			stmt.setString(3, user.getFirstname()); 
			stmt.setString(4, user.getLastname()); 
			stmt.setString(5, user.getEmail());
			stmt.setInt(6, user.getNumIndexedRecords());
			stmt.setInt(7, user.getCurBatchId());

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
