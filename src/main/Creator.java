package main;

import java.net.Socket;
import java.util.Calendar;

import org.apache.log4j.Logger;

/**
 * This class checks for the availability of a service.
 * 
 * @author Manjula Amarasekara
 * @version 2.0
 *
 */
public class Creator implements Runnable {

	private Calendar currentTime;
	protected Logger logger;
	protected Service service;

	/**
	 * This constructs a creator with a specified service and a list of sockets.
	 * 
	 * @param service
	 *            A service to be converted to a socket
	 * @param sockets
	 *            List of sockets of the monitor
	 */
	public Creator(Service service) {
		this.logger = Logger.getLogger(this.getClass());
		this.service = service;
		run();
	}

	public void run() {

		Socket socket = null;

		try {

			long timeStamp = System.currentTimeMillis();
			currentTime = Calendar.getInstance();
			currentTime.setTimeInMillis(timeStamp);

			socket = new Socket(service.getHost(), service.getPort());

			if (socket != null) {

				// When the service is NOT within the outage time
				if (!service.isInOutageInterval(currentTime)) {

					// If the service is UP
					if (socket.isConnected()) {
						Thread.sleep(service.getPolingFrequency());
						service.notifyListeners(service, currentTime, Util.SERVICE_UP);
					}

					/*
					 * If a service is not responding, the monitor will wait for the grace time to
					 * expire before notifying any clients.
					 */
					else {

						/*
						 * When the grace time is greater than the polling frequency wait for that time
						 * before notifying the clients.
						 */
						if (service.getGraceTime() >= service.getPolingFrequency()) {
							Thread.sleep(service.getGraceTime());
						}
						/*
						 * If the grace time is less than the polling frequency
						 */
						else {
							Thread.sleep(service.getPolingFrequency());
						}

						/*
						 * If the service goes back online during this grace time, no notification will
						 * be sent.
						 */
						if (!socket.isConnected()) {

							// Notification is being sent as the service did not connect again.
							service.notifyListeners(service, currentTime, Util.SERVICE_DOWN);
						}
					}
				}
				// When the service is within the outage time.
				else {
					Thread.sleep(service.getPolingFrequency());
					service.notifyListeners(service, currentTime, Util.SERVICE_DOWN);
				}

			}

		} catch (Exception e) {
			logger.debug("Unexpected exception on socket connection level", e);
			service.notifyListeners(service, currentTime, Util.CANNOT_CONNECT);
		}

	}
}
