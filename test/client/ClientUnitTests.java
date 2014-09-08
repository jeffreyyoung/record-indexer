package client;

import org.junit.*;

import shared.communication.ValidateUser_Params;
import shared.communication.ValidateUser_Result;
import client.communication.ClientCommunicator;
import dataimporter.DataImporter;
import static org.junit.Assert.*;

public class ClientUnitTests {
	
	@Before
	public void setup() throws Exception {
		DataImporter importer = new DataImporter();
		importer.importData("/users/guest/y/youngj4/Desktop/Records/Records.xml");
	}
	
	@After
	public void teardown() {
	}
	
	@Test
	public void validateUser_test() throws ClientException {		
		ClientCommunicator c = new ClientCommunicator("localhost", "8080");


		ValidateUser_Params params = new ValidateUser_Params();
		params.setUsername("test1");
		params.setPassword("test1");



		ValidateUser_Result result = c.validateUser(params);
		
		assertEquals(result.toString(), "TRUE\nTest\nOne\n0\n");


	}

	public static void main(String[] args) {

		String[] testClasses = new String[] {
				"client.ClientUnitTests"
		};

		org.junit.runner.JUnitCore.main(testClasses);
	}
}

