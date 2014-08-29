package com.skirpichenok.tcpserver;

/**
 * ServerConfig class. Configuration for TCP server.
 */
public class ServerConfig {

	private int port;
	private int workerCount;
	private ServerHandlerFactory handlerFactory;

	/**
	 * ServerConfig constructor.
	 * 
	 * @param port
	 *            int
	 * @param handlerFactory
	 *            ServerHandlerFactory
	 * @param workerCount
	 *            int
	 */
	public ServerConfig(int port, ServerHandlerFactory handlerFactory, int workerCount) {
		if (workerCount < 1) {
			throw new IllegalArgumentException("Count of workers should be at least 1!");
		}
		if (port < 0) {
			throw new IllegalArgumentException("Port can't be negative!");
		}
		if (handlerFactory == null) {
			throw new NullPointerException("Please specify handler factory!");
		}
		this.port = port;
		this.workerCount = workerCount;
		this.handlerFactory = handlerFactory;
	}

	/**
	 * Method that returns port value.
	 * 
	 * @return local port which TCP server will be listening, should be 0..64000
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Method that returns handlerFactory.
	 *
	 * @return handler factory which TCP server will use for process incoming connections
	 */
	public ServerHandlerFactory getHandlerFactory() {
		return handlerFactory;
	}

	/**
	 * Method that returns workerCount value.
	 *
	 * @return count of worker (thread) which TCP proxy will use for processing incoming client connection, should be
	 *         more than 0
	 */
	public int getWorkerCount() {
		return workerCount;
	}

}
