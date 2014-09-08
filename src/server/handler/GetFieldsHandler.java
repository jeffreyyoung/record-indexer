package server.handler;

import java.io.IOException;
import java.io.OutputStream;

import server.Facade;
import server.FacadeException;
import shared.Serializer;
import shared.communication.GetFields_Params;
import shared.communication.GetFields_Result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class GetFieldsHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		try {
						
			Facade f = new Facade();
			GetFields_Params params = new GetFields_Params();
			params = (GetFields_Params)Serializer.deserialzie(exchange.getRequestBody());
			
			GetFields_Result result = f.getFields(params);
	
	        String response = Serializer.serialize(result);
	        exchange.sendResponseHeaders(200, response.length());
	        OutputStream os = exchange.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
			
		} catch (FacadeException e) {
			e.printStackTrace();
		}

	}
}