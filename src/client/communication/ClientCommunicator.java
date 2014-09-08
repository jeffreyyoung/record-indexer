package client.communication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import shared.Serializer;
import shared.communication.DownloadBatch_Params;
import shared.communication.DownloadBatch_Result;
import shared.communication.DownloadFile_Params;
import shared.communication.DownloadFile_Result;
import shared.communication.GetFields_Params;
import shared.communication.GetFields_Result;
import shared.communication.GetProjects_Params;
import shared.communication.GetProjects_Result;
import shared.communication.GetSampleImage_Params;
import shared.communication.GetSampleImage_Result;
import shared.communication.Search_Params;
import shared.communication.Search_Result;
import shared.communication.SubmitBatch_Params;
import shared.communication.SubmitBatch_Result;
import shared.communication.ValidateUser_Params;
import shared.communication.ValidateUser_Result;
import client.ClientException;

public class ClientCommunicator {
	
	public static String SERVER_HOST;
	public static Integer SERVER_PORT;
	public static String URL_PREFIX;
	
	private static ClientCommunicator singleton = null;
	
	public ClientCommunicator(){
		ClientCommunicator.SERVER_HOST = "localhost";
		ClientCommunicator.SERVER_PORT = 8080;
		ClientCommunicator.URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	
	
	public ClientCommunicator(String host, String port){
		ClientCommunicator.SERVER_HOST = host;
		ClientCommunicator.SERVER_PORT = Integer.parseInt(port);
		ClientCommunicator.URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	
	public static ClientCommunicator getSingleton(){
		
		if (singleton == null){
			singleton = new ClientCommunicator(SERVER_HOST, SERVER_PORT.toString());
		}
		
		return singleton;
	}
	
	/**
	 * 
	 * @param params - UserName and password
	 * @return Returns null if invalid params, else returns a User object contained in a ValidateUser_Result object
	 * @throws ClientException
	 */
	public ValidateUser_Result validateUser(ValidateUser_Params params) throws ClientException{
		
		return (ValidateUser_Result)doPost("/ValidateUser", params);
	}
	
	/**
	 * 
	 * @param params 
	 * @return A list of projects
	 * @throws ClientException
	 */
	public GetProjects_Result getProjects(GetProjects_Params params) throws ClientException{
		
		return (GetProjects_Result)doPost("/GetProjects", params);
	}
	
	/**
	 * 
	 * @param params, project id
	 * @return Url of sample image for project
	 * @throws ClientException
	 */
	public GetSampleImage_Result getSampleImage(GetSampleImage_Params params) throws ClientException{
		return (GetSampleImage_Result)doPost("/GetSampleImage", params);	
	}
	
	/**
	 * 
	 * @param params Project id
	 * @return A an available batch to be indexed
	 * @throws ClientException
	 */
	public DownloadBatch_Result downloadBatch(DownloadBatch_Params params) throws ClientException{
		return (DownloadBatch_Result)doPost("/DownloadBatch", params);	
	}
	
	/**
	 * 
	 * @param params batchId
	 * @return
	 * @throws ClientException
	 */
	public SubmitBatch_Result submitBatch(SubmitBatch_Params params) throws ClientException{
		return (SubmitBatch_Result)doPost("/SubmitBatch", params);	
	}
	
	/**
	 * 
	 * @param params project id
	 * @return List of fields for project
	 * @throws ClientException
	 */
	public GetFields_Result getFields(GetFields_Params params) throws ClientException{
		return (GetFields_Result)doPost("/GetFields", params);
	}
	
	/**
	 * 
	 * @param params List of fields to be search and in addition to a list of strings to be searched for
	 * @return
	 * @throws ClientException
	 */
	public Search_Result search(Search_Params params) throws ClientException{
		return (Search_Result)doPost("/Search", params);
	}
	
	/**
	 * 
	 * @param params
	 * @return Downloads file to bin
	 * @throws ClientException
	 */
	public DownloadFile_Result downloadFile(DownloadFile_Params params) throws ClientException{
		return (DownloadFile_Result)doPost("/DownloadFile", params);
	}
	
	private Object doGet(String urlPath) throws ClientException {

		try {
			URL url;
			url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

				
		return null;
	}
	
	private Object doPost(String urlPath, Object params ) throws ClientException {
		try {
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.connect();
			Serializer.serializeToOutputStream(params, connection.getOutputStream());
			connection.getOutputStream().close();
			
			//if
			Object o = Serializer.deserialzie(connection.getInputStream());

			return o;
			
			
		} catch (IOException e) {
			
		}
		return null;
	}
}
