package server.handler;

import java.io.IOException;
import java.io.OutputStream;

import server.Facade;
import server.FacadeException;
import shared.Serializer;
import shared.communication.ValidateUser_Params;
import shared.communication.ValidateUser_Result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class ValidateUserHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		try {			
			ValidateUser_Params params = new ValidateUser_Params();
			params = (ValidateUser_Params)Serializer.deserialzie(exchange.getRequestBody());

			Facade f = new Facade();
			ValidateUser_Result result = f.validateUser(params);

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