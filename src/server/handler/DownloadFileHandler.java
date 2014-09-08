package server.handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class DownloadFileHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		File f = new File("Records" + File.separator + exchange.getRequestURI().getPath());
		exchange.sendResponseHeaders(200, 0);
		Files.copy(f.toPath(), exchange.getResponseBody());
		exchange.getResponseBody().close();

	}
}