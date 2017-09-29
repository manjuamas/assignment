package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * This class acts as the server that initiates monitoring.
 * 
 * @author Manjula Amarasekara
 * @version 2.0
 *
 */
public class Server {

	protected Logger logger;
	private static Monitor monitor = Monitor.getInstance();
	private static boolean isStarted;

	public static void main(String argv[]) throws Exception {

		final String TEST_SERVICE_1 = "TEST_SERVICE_1";
		final String TEST_SERVICE_2 = "TEST_SERVICE_2";
		final String TEST_SERVICE_3 = "TEST_SERVICE_3";
		final String TEST_SERVICE_4 = "TEST_SERVICE_4";

		Calendar start = Calendar.getInstance();

		/*
		 * Month is set to 8 for September as the month indexes in Calendar start with
		 * 0. Parameter order for the set method is ->(int year, int month, int date,
		 * int hourOfDay, int minute, int second)
		 * 
		 */
		start.set(2017, 8, 28, 13, 5, 1);

		Calendar end = Calendar.getInstance();
		end.set(2017, 8, 28, 20, 25, 1);

		/**
		 * Set the start and the end times for the outage durations appropriately, so
		 * the current time when testing this test-case is within the range.
		 */
		Server.monitor
				.addService(new Service(TEST_SERVICE_1, "127.0.0.1", 5685, 20, Util.pollingUpperLimit, start, end));

		Server.monitor
				.addService(new Service(TEST_SERVICE_2, "127.0.0.1", 135, 20, Util.pollingUpperLimit, start, end));
		Server.monitor
				.addService(new Service(TEST_SERVICE_3, "127.0.0.1", 51148, 20, Util.pollingUpperLimit, null, null));
		Server.monitor
				.addService(new Service(TEST_SERVICE_4, "127.0.0.1", 135, 20, Util.pollingUpperLimit, null, null));

		for (String name : Server.monitor.getServices().keySet()) {

			Service service = monitor.getService(name);

			// Registering the listeners to the service.

			Listener listner = new Listener();

			if (!listner.isHasRequestedBefore(service)) {

				System.out.println("Adding a listener to the service " + service.getName());
				service.addListener(listner);
				listner.addService(service);
			}
		}
		setStarted(true);
		startServer();
	}

	public Server() {

		this.logger = Logger.getLogger(this.getClass());
		logger.info("Monitoring started at : " + System.currentTimeMillis());

	}

	public static void startServer() throws InterruptedException {

		List<Creator> serviceCreators = new ArrayList<Creator>();
		for (String serviceName : monitor.getServiceNames()) {
			Service service = monitor.getService(serviceName);

			Creator c = new Creator(service);
			serviceCreators.add(c);
		}

		isStarted = true;

		int threads = Runtime.getRuntime().availableProcessors();
		ExecutorService service = Executors.newFixedThreadPool(threads);

		while (isStarted) {

			for (Creator c : serviceCreators) {
				service.execute(c);
			}
		}
	}

	public static Monitor getMonitor() {
		return monitor;
	}

	public static void setMonitor(Monitor monitor) {
		Server.monitor = monitor;
	}

	public static boolean isStarted() {
		return isStarted;
	}

	public static void setStarted(boolean isStarted) {
		Server.isStarted = isStarted;
	}

}
