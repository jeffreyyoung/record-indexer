package shared.communication;

public class Params {

	private String username;
	private String password;
	
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
	
	public ValidateUser_Params getValidateUser_Params() {
		ValidateUser_Params params = new ValidateUser_Params();
		params.setPassword(getPassword());
		params.setUsername(getUsername());
		
		return params;
	}

}
