package server;

import java.util.List;

import org.junit.* ;

import shared.communication.GetProjects_Params;
import shared.communication.GetProjects_Result;
import shared.model.Project;
import dataimporter.DataImporter;
import static org.junit.Assert.* ;

public class ServerUnitTests {
	
	@Before
	public void setup() throws Exception {
		DataImporter importer = new DataImporter();
		importer.importData("/users/guest/y/youngj4/Desktop/Records/Records.xml");
	}
	
	@After
	public void teardown() {
	}
	
	@Test
	public void test_1() throws Exception {
		
		Facade f = new Facade();
		
		GetProjects_Params params = new GetProjects_Params();
		params.setPassword("test1");
		params.setUsername("test1");
		GetProjects_Result result = f.getProjects(params);
		
		List<Project> projects = result.getProjects();
		
	
		for (int i = 0; i < projects.size(); i++){
			
			projects.get(i).getTitle();
			
			if (i == 0){
				assertEquals(projects.get(i).getTitle(), "1890 Census");
			}
			else if (i == 1){
				assertEquals(projects.get(i).getTitle(), "1900 Census");
			}
			else if (i == 2){
				assertEquals(projects.get(i).getTitle(), "Draft Records");
			}
			
		}
	}

	public static void main(String[] args) {
		
		String[] testClasses = new String[] {
				"server.ServerUnitTests"
		};

		org.junit.runner.JUnitCore.main(testClasses);
	}
	
}

