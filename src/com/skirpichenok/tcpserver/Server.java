package com.skirpichenok.tcpserver;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * Server - simple TCP server based on NIO.
 */
public class Server {

	public static final Logger LOGGER = Logger.getAnonymousLogger();

	private ServerConfig config;
	private String name;

	private Queue<ServerHandler> handlers;
	private Thread[] workers;

	/**
	 * Server constructor.
	 * 
	 * @param config
	 *            ServerConfig
	 */
	public Server(ServerConfig config) {
		if (config == null) {
			throw new NullPointerException("Please specify config! Thx!");
		}
		this.config = config;
		name = "TcpServer on port " + config.getPort();
	}

	/**
	 * This method starts waiting incoming connections for proxy to remote host.
	 */
	public void start() {
		if (workers != null) {
			throw new UnsupportedOperationException("Please shutdown connector!");
		}
		LOGGER.info("Starting " + name + " with " + config.getWorkerCount() + " workers");

		handlers = new ConcurrentLinkedQueue<ServerHandler>();
		handlers.add(new ServerAcceptor(config, handlers));

		workers = new Thread[config.getWorkerCount()];
		for (int i = 0; i < workers.length; i++) {
			workers[i] = new ServerWorker(handlers);
			workers[i].start();
		}

		LOGGER.info(name + " started");
	}

	/**
	 * Shutdown connector.
	 */
	public void shutdown() {
		if (workers == null) {
			LOGGER.info(name + " already been shutdown");
			return;
		}

		LOGGER.info("Starting shutdown " + name);

		for (Thread worker : workers) {
			worker.interrupt();
			try {
				worker.join();
			} catch (InterruptedException exception) {
				Thread.currentThread().interrupt();
			}
		}
		workers = null;

		ServerHandler handler;
		while ((handler = handlers.poll()) != null) {
			handler.destroy();
		}
		handlers = null;

		LOGGER.info(name + " was shutdown");
	}

}
