package server.handler;

import java.io.IOException;
import java.io.OutputStream;

import server.Facade;
import server.FacadeException;
import shared.Serializer;
import shared.communication.GetProjects_Params;
import shared.communication.GetProjects_Result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class GetProjectsHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {

		try {
						
			GetProjects_Params params = new GetProjects_Params();
			params = (GetProjects_Params)Serializer.deserialzie(exchange.getRequestBody());
			
			Facade f = new Facade();
			GetProjects_Result result = f.getProjects(params);

	        String response = Serializer.serialize(result);
	        exchange.sendResponseHeaders(200, response.length());//
	        OutputStream os = exchange.getResponseBody();//
	        os.write(response.getBytes());//
	        os.close();
			
		} catch (FacadeException e) {
			e.printStackTrace();
		}
	}
}