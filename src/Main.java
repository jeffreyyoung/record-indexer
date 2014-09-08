import gui.indexer.IndexerFrame;
import gui.login.LoginFrame;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;

import client.ClientException;
import client.communication.ClientCommunicator;
import server.Facade;
import server.FacadeException;
import shared.communication.*;
import shared.model.Field;

public class Main
{
	public static void main( String args[] ) throws FacadeException
	{
	
		ClientCommunicator communicator = new ClientCommunicator();
		
		ValidateUser_Params params = new ValidateUser_Params();
		params.setPassword("test1");
		params.setUsername("test1");
		
		try {
			ValidateUser_Result result = communicator.validateUser(params);
			System.out.println(result.getUser().getEmail());
			
		} catch (ClientException e) {
			e.printStackTrace();
		}
		
	}
}