package com.skirpichenok.tcpproxy;

/**
 * ProxyConfig class that holds config properties.
 */
public class ProxyConfig {

	private int localPort;
	private String remoteHost;
	private int remotePort;
	private int workerCount;

	/**
	 * ProxyConfig constructor.
	 * 
	 * @param localPort
	 *            int
	 * @param remoteHost
	 *            String
	 * @param remotePort
	 *            int
	 */
	public ProxyConfig(int localPort, String remoteHost, int remotePort) {
		this.localPort = localPort;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
	}

	/**
	 * Method returns localPort value.
	 * 
	 * @return local port which TCP proxy will be listening, should be 0..64000
	 */
	public int getLocalPort() {
		return localPort;
	}

	/**
	 * Method returns remotePort value.
	 *
	 * @return remote port on which TCP proxy will send all traffic from incoming connections
	 */
	public int getRemotePort() {
		return remotePort;
	}

	/**
	 * Method returns remoteHost value.
	 *
	 * @return remote host on which TCP proxy will send all traffic from incoming connections
	 */
	public String getRemoteHost() {
		return remoteHost;
	}

	/**
	 * Method returns workerCount value.
	 *
	 * @return count of worker (thread) which TCP proxy will use for processing incoming client connection, should be
	 *         more than 0
	 */
	public int getWorkerCount() {
		return workerCount;
	}

	/**
	 * Method sets workerCount value.
	 *
	 * @param workerCount
	 *            int
	 */
	public void setWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}

}
