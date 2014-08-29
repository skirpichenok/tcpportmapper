package com.skirpichenok.tcpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ServerAcceptor class.
 */
public class ServerAcceptor implements ServerHandler {

	public static final int ACCEPT_BUFFER_SIZE = 1000;
	public static final Logger LOGGER = Logger.getAnonymousLogger();

	private ServerConfig config;
	private Queue<ServerHandler> handlers;

	/**
	 * ServerAcceptor constructor.
	 * 
	 * @param config
	 *            ServerConfig
	 * @param handlers
	 *            Queue<ServerHandler>
	 */
	public ServerAcceptor(ServerConfig config, Queue<ServerHandler> handlers) {
		this.config = config;
		this.handlers = handlers;
	}

	@Override
	public void register(final Selector selector) {
		try {
			ServerSocketChannel server = ServerSocketChannel.open();
			server.socket().bind(new InetSocketAddress(config.getPort()), ACCEPT_BUFFER_SIZE);
			server.configureBlocking(false);
			server.register(selector, SelectionKey.OP_ACCEPT, this);
		} catch (IOException exception) {
			LOGGER.log(Level.SEVERE, "Can't init server connection!", exception);
		}
	}

	@Override
	public void process(SelectionKey key) {
		if (key.isValid() && key.isAcceptable()) {
			try {
				handlers.add(config.getHandlerFactory().create(((ServerSocketChannel) key.channel()).accept()));
			} catch (final IOException exception) {
				LOGGER.log(Level.SEVERE, "Can't accept client connection!", exception);
			}
		}
	}

	@Override
	public void destroy() {

	}

}
