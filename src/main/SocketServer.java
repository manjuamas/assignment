package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * This is a Server socket running on a customized TCP port. Set the TCP port to
 * some other int,in the range to open a service on that port.
 * 
 * @author Manjula Amarasekara
 * @version 2.0
 *
 */
public class SocketServer {

	protected static Logger logger;

	@SuppressWarnings("resource")
	public static void main(String argv[]) throws Exception {

		logger = Logger.getLogger("SocketServer");
		try {

			int tcpPort = 5685;
			System.out.println("Staring a TCP service : " + tcpPort);
			ServerSocket serverserver = new ServerSocket(tcpPort);
			// Make server socket listen
			Socket socket = serverserver.accept();

		} catch (IOException e) {
			logger.error("Error hosting the service socket", e);
		}

	}
}
