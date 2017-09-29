package test;

import java.util.Calendar;

import org.apache.log4j.Logger;

import junit.framework.TestCase;
import main.Listener;
import main.Monitor;
import main.Server;
import main.Service;
import main.Util;

/**
 * The Test case. This creates some services and registers listeners to the
 * services.
 * 
 * @author Manjula Amarasekara
 * @version 2.0
 *
 */
public class Tester extends TestCase {

	public static final Logger logger = Logger.getLogger("Yukon Assignmet unit tester");

	public static void main(String[] args) throws Exception {

		Tester tester = new Tester();

		tester.testConnections();
	}

	public void testConnections() throws Exception {

		Monitor monitor = Monitor.getInstance();

		/*
		 * The same start and end outage times have been set for all the test
		 * services.Can create many Calender objects and set the times appropriately.
		 */

		final String TEST_SERVICE_1 = "TEST_SERVICE_1";
		final String TEST_SERVICE_2 = "TEST_SERVICE_2";
		final String TEST_SERVICE_3 = "TEST_SERVICE_3";
		final String TEST_SERVICE_4 = "TEST_SERVICE_4";

		Calendar timeNow = Calendar.getInstance();
		timeNow.setTimeInMillis(System.currentTimeMillis());

		Calendar start = Calendar.getInstance();
		start.set(2017, 8, 28, 13, 5, 1);

		Calendar end = Calendar.getInstance();
		end.set(2017, 8, 28, 14, 25, 1);

		/*
		 * Set the start and the end times for the outage durations appropriately,so the
		 * current time when testing this test-case is within the range.
		 */
		Server.getMonitor().addService(new Service(TEST_SERVICE_1, "127.0.0.1", 135, 20, Util.pollingUpperLimit, start, end));
		Server.getMonitor().addService(new Service(TEST_SERVICE_2, "127.0.0.1", 135, 20, Util.pollingUpperLimit, start, end));
		Server.getMonitor().addService(new Service(TEST_SERVICE_3, "127.0.0.1", 51148, 20, Util.pollingUpperLimit, null, null));
		Server.getMonitor().addService(new Service(TEST_SERVICE_4, "127.0.0.1", 51207, 20, Util.pollingUpperLimit, null, null));

		for (String name : Server.getMonitor().getServices().keySet()) {

			Service service = monitor.getService(name);

			// Registering the listeners for the service.
			Listener listner = new Listener();

			if (!listner.isHasRequestedBefore(service)) {

				System.out.println("Adding a listener to the service " + service.getName());
				service.addListener(listner);
				listner.addService(service);

			}
		}
		Server.setStarted(true);
		Server.startServer();
	}

}