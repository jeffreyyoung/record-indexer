package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import shared.model.Project;

public class ProjectDAO {


	private Database db;

	public ProjectDAO(Database db) {
		this.db = db;
	}

	/**
	 * Get all projects in DB
	 * 
	 * @return - List of all projects
	 */
	public List<Project> getAll() {
		Connection connection = db.getConnection();
		try {
			Statement stmt = connection.createStatement();
			String sql = "Select * from project";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			List<Project> projects = new ArrayList<Project>();
			
			while ( rs.next() ) {
				
				int id = rs.getInt("id");
				String title = rs.getString("title");
				int fieldsperimage = rs.getInt("fieldsperimage");
				int recordsperimage = rs.getInt("recordsperimage");
				int firstycoord = rs.getInt("firstycoord");
				int recordheight = rs.getInt("recordheight");
				
				Project p = new Project(id, title, recordsperimage, fieldsperimage, recordheight, firstycoord);
				
				projects.add(p);
				
			}
			
			rs.close();
			stmt.close();
			return projects;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Get project with given id
	 * 
	 * @param id
	 *            id of project to be returned
	 * @return Returns project with parameter id;
	 */
	public Project getProject(int id) {
		Connection connection = db.getConnection();
		System.out.println("getting project");
		try {
			Statement stmt = connection.createStatement();
			String sql = "Select * from project where id = " + id;
			
			ResultSet rs = stmt.executeQuery(sql);
			
			Project p = null;
			
			if ( rs.next() ) {
				
				int projectId = rs.getInt("id");
				String title = rs.getString("title");
				int fieldsperimage = rs.getInt("fieldsperimage");
				int recordsperimage = rs.getInt("recordsperimage");
				int firstycoord = rs.getInt("firstycoord");
				int recordheight = rs.getInt("recordheight");
				
				p = new Project(projectId, title, recordsperimage, fieldsperimage, recordheight, firstycoord);
				
			}
			rs.close();
			stmt.close();
			return p;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Add project to database
	 * 
	 * @param project
	 *            - Project to be added to db
	 * @return - Returns true if successfull add
	 */
	public Boolean add(Project project) {
		Connection connection = db.getConnection();
		PreparedStatement stmt = null;
		try {
			String sql = "INSERT INTO project"
					+ "(title, fieldsperimage, recordsperimage, firstycoord, recordheight)"
					+ "VALUES (?, ?, ?, ?, ?)";
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, project.getTitle());
			stmt.setInt(2, project.getFieldsPerImage());
			stmt.setInt(3, project.getRecordsPerImage());
			stmt.setInt(4, project.getFirstYCoord());
			stmt.setInt(5, project.getRecordHeight());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return true;
	}

}
