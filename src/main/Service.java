package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The Service class that has a name, host,port etc.
 * 
 * @author Manjula Amarasekara
 * @version 2.0
 *
 */
public class Service {

	private String name;
	private String host;
	private int port;
	private boolean isUp;
	private long graceTime;
	private long polingFrequency;
	private long outage;
	private Calendar outageStart;
	private Calendar outageEnd;

	private List<Listener> listeners = new ArrayList<Listener>();

	/**
	 * This constructs a service instance.
	 * 
	 * @param name
	 *            Name of the service
	 * @param host
	 *            Host of the service
	 * @param port
	 *            Port of the service
	 * @param graceTime
	 *            Time to wait before notifying the service is down
	 * @param polingFrequency
	 *            Polling frequency of the service
	 * @param outageStart
	 *            Starting time of the service outage
	 * @param outageEnd
	 *            Ending time of the service outage
	 * 
	 */
	public Service(String name, String host, int port, long graceTime, long polingFrequency, Calendar outageStart,
			Calendar outageEnd) {
		super();
		this.name = name;
		this.host = host;
		this.port = port;

		this.graceTime = graceTime;
		this.outageStart = outageStart;
		this.outageEnd = outageEnd;

		if (polingFrequency < 1000) {
			this.polingFrequency = Util.pollingUpperLimit;
		} else {
			this.polingFrequency = polingFrequency;
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isUp() {
		return isUp;
	}

	public void setUp(boolean isUp) {
		this.isUp = isUp;
	}

	public long getOutage() {
		return outage;
	}

	public long getGraceTime() {
		return graceTime;
	}

	public void setGraceTime(long graceTime) {
		this.graceTime = graceTime;
	}

	public long getPolingFrequency() {
		return polingFrequency;
	}

	public void setPolingFrequency(long polingFrequency) {
		this.polingFrequency = polingFrequency;
	}

	public void setOutage(long outage) {
		this.outage = outage;
	}

	public synchronized void addListener(Listener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	public synchronized void removeListener(Listener listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
	}

	public Calendar getOutageStart() {
		return outageStart;
	}

	public void setOutageStart(Calendar outageStart) {
		this.outageStart = outageStart;
	}

	public Calendar getOutageEnd() {
		return outageEnd;
	}

	public void setOutageEnd(Calendar outageEnd) {
		this.outageEnd = outageEnd;
	}

	public List<Listener> getListeners() {
		return listeners;
	}

	public void setListeners(List<Listener> listeners) {
		this.listeners = listeners;
	}

	/**
	 * Return true if the current-time is within the outage interval
	 * 
	 * @param currentTime
	 *            The time to check for the availability
	 * @return Is the current-time is within the outage interval
	 */
	public synchronized boolean isInOutageInterval(Calendar currentTime) {
		if (currentTime.after(outageStart) && currentTime.before(outageEnd)) {
			isUp = false;
			return true;
		}

		isUp = true;
		return false;
	}

	/**
	 * Notify the registered listeners of the service.
	 * 
	 * @param service
	 *            This service
	 * @param timeStamp
	 *            Time of the notification
	 * @param status
	 *            Status of the service
	 */
	public synchronized void notifyListeners(Service service, Calendar timeStamp, String status) {
		for (Listener listener : listeners) {
			if (status.equals(Util.SERVICE_UP)) {
				listener.updateServiceUP(service, timeStamp);
			}
			if (status.equals(Util.SERVICE_DOWN)) {
				listener.updateServiceDown(service, timeStamp);
			}
			if (status.equals(Util.CANNOT_CONNECT)) {
				listener.updateServiceNotReachable(service, timeStamp);
			}
		}
	}

	@Override
	public String toString() {
		return "Service [name=" + name + ", host=" + host + ", port=" + port + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Service other = (Service) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

}
