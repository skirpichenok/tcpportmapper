package com.skirpichenok.tcpproxy;

import com.skirpichenok.tcpserver.ServerHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ProxyConnector class.
 */
class ProxyConnector implements ServerHandler {

	public static final Logger LOGGER = Logger.getAnonymousLogger();

	private ProxyBuffer clientBuffer = new ProxyBuffer();
	private ProxyBuffer serverBuffer = new ProxyBuffer();
	private SocketChannel clientChannel;

	private Selector selector;
	private SocketChannel serverChannel;
	private ProxyConfig config;

	/**
	 * ProxyConnector constructor.
	 * 
	 * @param clientChannel
	 *            SocketChannel
	 * @param config
	 *            ProxyConfig
	 */
	public ProxyConnector(SocketChannel clientChannel, ProxyConfig config) {
		this.clientChannel = clientChannel;
		this.config = config;
	}

	/**
	 * Method that reads from client.
	 * 
	 * @throws IOException
	 */
	public void readFromClient() throws IOException {
		serverBuffer.writeFrom(clientChannel);
		if (serverBuffer.isReadyToRead()) {
			register();
		}
	}

	/**
	 * Method that reads from server.
	 * 
	 * @throws IOException
	 */
	public void readFromServer() throws IOException {
		clientBuffer.writeFrom(serverChannel);
		if (clientBuffer.isReadyToRead()) {
			register();
		}
	}

	/**
	 * Method that writes to client.
	 * 
	 * @throws IOException
	 */
	public void writeToClient() throws IOException {
		clientBuffer.writeTo(clientChannel);
		if (clientBuffer.isReadyToWrite()) {
			register();
		}
	}

	/**
	 * Method that writes to server.
	 * 
	 * @throws IOException
	 */
	public void writeToServer() throws IOException {
		serverBuffer.writeTo(serverChannel);
		if (serverBuffer.isReadyToWrite()) {
			register();
		}
	}

	/**
	 * Method that registers channels.
	 * 
	 * @throws ClosedChannelException
	 */
	public void register() throws ClosedChannelException {
		int clientOps = 0;
		if (serverBuffer.isReadyToWrite()) {
			clientOps |= SelectionKey.OP_READ;
		}
		if (clientBuffer.isReadyToRead()) {
			clientOps |= SelectionKey.OP_WRITE;
		}
		clientChannel.register(selector, clientOps, this);

		int serverOps = 0;
		if (clientBuffer.isReadyToWrite()) {
			serverOps |= SelectionKey.OP_READ;
		}
		if (serverBuffer.isReadyToRead()) {
			serverOps |= SelectionKey.OP_WRITE;
		}
		serverChannel.register(selector, serverOps, this);
	}

	private static void closeQuietly(SocketChannel channel) {
		if (channel != null) {
			try {
				channel.close();
			} catch (IOException exception) {
				LOGGER.log(Level.WARNING, "Could not close channel properly.", exception);
			}
		}
	}

	@Override
	public void register(Selector selector) {
		this.selector = selector;

		try {
			clientChannel.configureBlocking(false);

			InetSocketAddress socketAddress = new InetSocketAddress(config.getRemoteHost(), config.getRemotePort());
			serverChannel = SocketChannel.open();
			serverChannel.connect(socketAddress);
			serverChannel.configureBlocking(false);

			register();
		} catch (IOException exception) {
			destroy();
			LOGGER.log(Level.WARNING, "Could not connect to " + config.getRemoteHost() + ":" + config.getRemotePort(),
					exception);
		}
	}

	@Override
	public void process(SelectionKey key) {
		try {
			if (key.channel() == clientChannel) {
				if (key.isValid() && key.isReadable()) {
					readFromClient();
				}
				if (key.isValid() && key.isWritable()) {
					writeToClient();
				}
			}
			if (key.channel() == serverChannel) {
				if (key.isValid() && key.isReadable()) {
					readFromServer();
				}
				if (key.isValid() && key.isWritable()) {
					writeToServer();
				}
			}
		} catch (ClosedChannelException exception) {
			destroy();
			LOGGER.log(Level.INFO, "Channel was closed by client or server.", exception);
		} catch (IOException exception) {
			destroy();
			LOGGER.log(Level.WARNING, "Could not process.", exception);
		}
	}

	@Override
	public void destroy() {
		closeQuietly(clientChannel);
		closeQuietly(serverChannel);
	}

}