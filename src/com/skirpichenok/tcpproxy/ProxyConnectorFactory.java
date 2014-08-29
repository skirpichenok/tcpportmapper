package com.skirpichenok.tcpproxy;

import com.skirpichenok.tcpserver.ServerHandler;
import com.skirpichenok.tcpserver.ServerHandlerFactory;

import java.nio.channels.SocketChannel;

/**
 * ProxyConnectorFactory class.
 */
public class ProxyConnectorFactory implements ServerHandlerFactory {

	private final ProxyConfig config;

	/**
	 * ProxyConnectorFactory constructor.
	 * 
	 * @param config
	 *            ProxyConfig
	 */
	public ProxyConnectorFactory(ProxyConfig config) {
		this.config = config;
	}

	@Override
	public ServerHandler create(SocketChannel clientChannel) {
		return new ProxyConnector(clientChannel, config);
	}

}
