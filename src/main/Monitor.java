package main;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Monitor class does functions which are linked to all services. E.g. register
 * services and remove a service
 * 
 * @author Manjula Amarasekara
 * @version 2.0
 *
 */
public class Monitor {

	private Map<String, Service> services = new HashMap<String, Service>();
	private List<Socket> sockets = new ArrayList<Socket>();

	private static final Monitor monitor = new Monitor();

	private Monitor() {
	}

	/**
	 * 
	 * @return The singleton monitor instance
	 */
	public static Monitor getInstance() {
		return monitor;
	}

	/**
	 * Returns a service instance by a the name.
	 * 
	 * @param name
	 *            Name of the service
	 * @return Service instance
	 */
	public Service getService(String name) {
		return services.get(name);
	}

	/**
	 * Adds a service to the list of service.
	 * 
	 * @param service
	 *            The new service to be added
	 */
	public void addService(Service service) {
		services.put(service.getName(), service);
	}

	/**
	 * Removes a service by it's name
	 * 
	 * @param name
	 *            The name of the service to be removed
	 */
	public void removeService(String name) {
		services.remove(name);
	}

	public Map<String, Service> getServices() {
		return services;
	}

	public void setServices(Map<String, Service> services) {
		this.services = services;
	}

	public Iterable<String> getServiceNames() {
		return services.keySet();
	}

	public List<Socket> getSockets() {
		return sockets;
	}

	public void setSockets(List<Socket> sockets) {
		this.sockets = sockets;
	}

	public void addSocket(Socket socket) {
		sockets.add(socket);
	}

}
