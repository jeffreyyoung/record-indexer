package server.handler;

import java.io.IOException;
import java.io.OutputStream;

import server.Facade;
import server.FacadeException;
import shared.Serializer;
import shared.communication.GetSampleImage_Params;
import shared.communication.GetSampleImage_Result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class GetSampleImageHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		try {
						
			Facade f = new Facade();
			GetSampleImage_Params params = new GetSampleImage_Params();
			params = (GetSampleImage_Params)Serializer.deserialzie(exchange.getRequestBody());
			
			GetSampleImage_Result result = f.getSampleImage(params);
			
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