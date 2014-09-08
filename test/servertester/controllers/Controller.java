package servertester.controllers;

import java.io.File;
import java.util.*;

import shared.communication.*;
import client.ClientException;
import client.communication.ClientCommunicator;
import servertester.views.*;

public class Controller implements IController {

	private IView _view;

	public Controller() {
		return;
	}

	public IView getView() {
		return _view;
	}

	public void setView(IView value) {
		_view = value;
	}

	// IController methods
	//

	@Override
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("8080");
		operationSelected();
	}

	@Override
	public void operationSelected() {
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");

		switch (getView().getOperation()) {
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}

		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public void executeOperation() {
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			validateUser();
			break;
		case GET_PROJECTS:
			getProjects();
			break;
		case GET_SAMPLE_IMAGE:
			getSampleImage();
			break;
		case DOWNLOAD_BATCH:
			downloadBatch();
			break;
		case GET_FIELDS:
			getFields();
			break;
		case SUBMIT_BATCH:
			submitBatch();
			break;
		case SEARCH:
			search();
			break;
		default:
			assert false;
			break;
		}
	}

	private boolean validateArgs(String[] args){

		for (int i = 0; i < args.length; i++){
			if (args[i].trim().length() == 0){
				return false;
			}
		}

		return true;
	}

	private void validateUser() {

		ClientCommunicator c = new ClientCommunicator(_view.getHost(), _view.getPort());

		String[] args = _view.getParameterValues();

		if (!validateArgs(args)){
			_view.setResponse("FALSE\n");
			return;
		}

		ValidateUser_Params params = new ValidateUser_Params();
		params.setUsername(args[0]);
		params.setPassword(args[1]);

		try {

			ValidateUser_Result result = c.validateUser(params);
			if (result != null){
				_view.setResponse(result.toString());
			}
			else {
				_view.setResponse("FAILED\n");
			}

		} catch (ClientException e) {

			e.printStackTrace();
		}

	}

	private void getProjects() {

		ClientCommunicator c = new ClientCommunicator(_view.getHost(), _view.getPort()); 

		String[] args = _view.getParameterValues();

		if (!validateArgs(args)){
			_view.setResponse("FAILED\n");
			return;
		}

		GetProjects_Params params = new GetProjects_Params();
		params.setUsername(args[0]);
		params.setPassword(args[1]);

		try {

			GetProjects_Result result = c.getProjects(params);
			if (result != null)
				_view.setResponse(result.toString());
			else
				_view.setResponse("FAILED\n");


		} catch (ClientException e) {

			e.printStackTrace();
		}
	}

	private void getSampleImage() {
		ClientCommunicator c = new ClientCommunicator(_view.getHost(), _view.getPort()); 

		String[] args = _view.getParameterValues();

		if (!validateArgs(args)){
			_view.setResponse("FAILED\n");
			return;
		}

		GetSampleImage_Params params = new GetSampleImage_Params();
		params.setUsername(args[0]);
		params.setPassword(args[1]);
		params.setProjectId(Integer.valueOf(args[2]));

		try {

			GetSampleImage_Result result = c.getSampleImage(params);
			if (result != null && result.getImageUrl() != null){
				String imageUrl = "http://" +_view.getHost() + ":" + _view.getPort() + File.separator + result.getImageUrl();
				result.setImageUrl(imageUrl);
				_view.setResponse(result.toString());
			}
			else {
				_view.setResponse("FAILED\n");
			}

		} catch (ClientException e) {

			e.printStackTrace();
		}
	}

	private void downloadBatch() {
		ClientCommunicator c = new ClientCommunicator(_view.getHost(), _view.getPort());

		String[] args = _view.getParameterValues();

		if (!validateArgs(args)){
			_view.setResponse("FAILED\n");
			return;
		}

		DownloadBatch_Params params = new DownloadBatch_Params();
		params.setUsername(args[0]);
		params.setPassword(args[1]);
		params.setProjectId(Integer.parseInt(args[2]));

		try {

			DownloadBatch_Result result = c.downloadBatch(params);
			if (result != null && !result.getMessage().equals("FAILED\n")){
				result.getBatch().appendHostToUrl(_view.getHost() + ":" + _view.getPort());
				result.updateFieldsHelpUrl(_view.getHost() + ":" + _view.getPort());
				_view.setResponse(result.toString());
			}
			else {
				_view.setResponse("FAILED\n");
			}


		} catch (ClientException e) {

			e.printStackTrace();
		}
	}

	private void getFields() {
		ClientCommunicator c = new ClientCommunicator(_view.getHost(), _view.getPort()); 

		String[] args = _view.getParameterValues();

		if (args[0].isEmpty() && args[1].isEmpty()){
			_view.setResponse("FAILED\n");
			return;
		}
		

		
		GetFields_Params params = new GetFields_Params();
		params.setUsername(args[0]);
		params.setPassword(args[1]);
		
		if(args[2].isEmpty()){
			params.setProjectId(-1);
		}
		else {
			params.setProjectId(Integer.parseInt(args[2]));
		}

		try {

			GetFields_Result result = c.getFields(params);
			if (result != null){
				result.updateFieldsHelpUrl(_view.getHost() + ":" + _view.getPort());
				_view.setResponse(result.toString());
			}
			else {
				_view.setResponse("FAILED\n");
			}

		} catch (ClientException e) {

			e.printStackTrace();
		}
	}

	private void submitBatch() {

		ClientCommunicator c = new ClientCommunicator(_view.getHost(), _view.getPort()); 

		String[] args = _view.getParameterValues();

		if (!validateArgs(args)){
			_view.setResponse("FAILED\n");
			return;
		}

		SubmitBatch_Params params = new SubmitBatch_Params();
		params.setUsername(args[0]);
		params.setPassword(args[1]);
		params.setBatchId(Integer.parseInt(args[2]));
		params.setFieldValues(args[3]);


		try {

			SubmitBatch_Result result = c.submitBatch(params);
			if (result != null){
				_view.setResponse(result.toString());
			}
			else {
				_view.setResponse("FAILED\n");
			}

		} catch (ClientException e) {

			e.printStackTrace();
		}

	}

	private void search() {
		ClientCommunicator c = new ClientCommunicator(_view.getHost(), _view.getPort()); 

		String[] args = _view.getParameterValues();

		if (!validateArgs(args)){
			_view.setResponse("FAILED\n");
			return;
		}

		Search_Params params = new Search_Params();
		params.setUsername(args[0]);
		params.setPassword(args[1]);
		params.addFieldIds(args[2].toLowerCase());
		params.addCellValues(args[3].toLowerCase());

		try {

			Search_Result result = c.search(params);
			if (result != null){
				result.prependHostToUrl(_view.getHost() + ":" + _view.getPort());
				_view.setResponse(result.toString());
			}
			else {
				_view.setResponse("FAILED\n");
			}

		} catch (ClientException e) {

			e.printStackTrace();
		}
	}

}

