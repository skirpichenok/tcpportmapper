package com.skirpichenok.tcpproxy;

import com.skirpichenok.tcpserver.Server;
import com.skirpichenok.tcpserver.ServerConfig;

/**
 * Proxy class.
 */
public class Proxy {

	private Server server;

	/**
	 * Proxy constructor.
	 * 
	 * @param config
	 *            ProxyConfig
	 */
	public Proxy(ProxyConfig config) {
		server = new Server(new ServerConfig(config.getLocalPort(), new ProxyConnectorFactory(config),
				config.getWorkerCount()));
	}

	/**
	 * Method that starts server.
	 */
	public void start() {
		server.start();
	}

	/**
	 * Method that stop server.
	 */
	public void shutdown() {
		server.shutdown();
	}

}
