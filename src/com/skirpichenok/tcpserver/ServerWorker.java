package com.skirpichenok.tcpserver;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ServerWorker class.
 */
public class ServerWorker extends Thread {

	public static final long SELECTOR_TIMEOUT = 100L;
	public static final Logger LOGGER = Logger.getAnonymousLogger();

	private final Queue<ServerHandler> handlers;

	/**
	 * ServerWorker constructor.
	 * 
	 * @param handlers
	 *            Queue<ServerHandler>
	 */
	public ServerWorker(Queue<ServerHandler> handlers) {
		super("TcpServerWorker");
		this.handlers = handlers;
	}

	@Override
	public void run() {
		Selector selector = null;
		try {
			selector = Selector.open();
			while (!Thread.interrupted()) {
				ServerHandler newHandler = handlers.poll();
				if (newHandler != null) {
					newHandler.register(selector);
				}

				selector.select(SELECTOR_TIMEOUT);

				Set<SelectionKey> keys = selector.selectedKeys();
				for (SelectionKey key : keys) {
					((ServerHandler) key.attachment()).process(key);
				}
				keys.clear();
			}
		} catch (IOException exception) {
			LOGGER.log(Level.SEVERE, "Problem with selector, worker will be stopped!", exception);
		} finally {
			if (selector != null) {
				try {
					selector.close();
				} catch (IOException exception) {
					LOGGER.log(Level.WARNING, "Could not close selector properly.", exception);
				}
			}
		}
	}

}
