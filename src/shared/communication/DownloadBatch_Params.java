package shared.communication;

import java.io.Serializable;

public class DownloadBatch_Params extends Params implements Serializable {

	private Integer projectId;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public boolean validateParams() {
		if( getUsername() == null || getPassword() == null || getProjectId() == null)
			return false;
		
		
		
		
		
		return true;
	}
}
