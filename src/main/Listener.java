package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A Listener class is a subscriber to a service.
 * 
 * @author Manjula Amarasekara
 * @version 2.0
 *
 */
public class Listener {

	private long pollingFrequency;
	public List<Service> services = new ArrayList<Service>();

	/**
	 * If the service is up and running, this method of the listener will be called.
	 * 
	 * @param service
	 *            The service of which the status is checked
	 * @param timeStamp
	 *            Time of the notification
	 */
	public void updateServiceUP(Service service, Calendar timeStamp) {
		System.out.println(service.toString() + " " + "is UP at : " + timeStamp.getTime().toString());
	}

	/**
	 * If the service is down, this method of the listener will be called.
	 * 
	 * @param service
	 *            The service of which the status is checked
	 * @param timeStamp
	 *            Time of the notification
	 */
	public void updateServiceDown(Service service, Calendar timeStamp) {
		System.out.println(service.toString() + " " + "is DOWN at : " + timeStamp.getTime().toString());
	}

	/**
	 * If the service is not reachable, this method of the listener will be called.
	 * 
	 * @param service
	 *            The service of which the status is checked
	 * @param timeStamp
	 *            ime of the notification
	 */
	public void updateServiceNotReachable(Service service, Calendar timeStamp) {
		System.out.println(service.toString() + " " + "is NOT REACHABLE at : " + timeStamp.getTime().toString());
	}

	/**
	 * Should not poll any service more frequently than once a second.
	 * 
	 * @param pollingFrequency
	 *            The polling time defined by the listener.
	 */
	public void setPollingFrequency(long pollingFrequency) {
		if (pollingFrequency > Util.pollingUpperLimit) {
			pollingFrequency = 60;
		}
		this.pollingFrequency = pollingFrequency;
	}

	/**
	 * Returns the polling frequency requested by a listener.
	 * 
	 * @return The registered polling frequency
	 */
	public long getPollingFrequency() {
		return pollingFrequency;
	}

	/**
	 * Returns true if the listener has already subscribed for this service.
	 * 
	 * @param service
	 * @return whether the service has been subscribed
	 */
	public boolean isHasRequestedBefore(Service service) {
		return services.contains(service);

	}

	/**
	 * Returns a list of registered services of this listener.
	 * 
	 * @return List of subscribed services
	 */
	public List<Service> getservices() {
		return services;
	}

	/**
	 * The monitor detects multiple callers registering interest in the same
	 * service.
	 * 
	 * @param service
	 *            The service to be registered
	 */
	public void addService(Service service) {
		if (service != null) {
			services.add(service);
		}
	}
}
