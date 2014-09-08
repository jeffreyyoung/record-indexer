package shared.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import shared.model.Project;

public class GetProjects_Result extends Result implements Serializable {

	private List<Project> projects = new ArrayList<Project>();

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	public String toString(){
		
		StringBuilder b = new StringBuilder();
		
		if (projects.size() == 0){
			b.append("FAILED");
		}
		
		for (Project p : projects){
			b.append(p.getId());
			b.append("\n");
			b.append(p.getTitle());
			b.append("\n");
		}
		
		return b.toString();
	}
}
