package shared.model;

import java.io.Serializable;

public class User implements Serializable{
	
	public static final String FAILED = "FAILED-VALIDATE-USER";
	
	public User(String username, String password, String firstname,
			String lastname, String email, int numIndexedRecords, int curBatchId) {
		super();
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.numIndexedRecords = numIndexedRecords;
		this.curBatchId = curBatchId;
	}
	
	public User(int id, String username, String password, String firstname,
			String lastname, String email, int numIndexedRecords, int curBatchId) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.numIndexedRecords = numIndexedRecords;
		this.curBatchId = curBatchId;
	}
	
	public User(){
		
	}
	private int id;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private int numIndexedRecords;
	private int curBatchId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getNumIndexedRecords() {
		return numIndexedRecords;
	}
	public void setNumIndexedRecords(int numIndexedRecords) {
		this.numIndexedRecords = numIndexedRecords;
	}
	public int getCurBatchId() {
		return curBatchId;
	}
	public void setCurBatchId(int curBatchId) {
		this.curBatchId = curBatchId;
	}

}
