package shared.communication;

import java.io.Serializable;

import shared.model.User;

public class ValidateUser_Result extends Result implements Serializable{

	private User user;

	public User getUser(){
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String toString(){
		StringBuilder b = new StringBuilder();
		if (user == null){
			return getMessage();
		}
		else {
			b.append("TRUE\n");
			b.append(user.getFirstname());
			b.append("\n");
			b.append(user.getLastname());
			b.append("\n");
			b.append(user.getNumIndexedRecords());
			b.append("\n");
		}
		return b.toString();
	}
}
