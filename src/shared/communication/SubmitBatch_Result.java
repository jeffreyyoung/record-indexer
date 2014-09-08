package shared.communication;

import java.io.Serializable;

public class SubmitBatch_Result extends Result implements Serializable{
	
	public String toString(){
		return getMessage();
	}
	
}
