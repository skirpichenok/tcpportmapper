package com.skirpichenok.tcpproxy;

import com.skirpichenok.tcpserver.ServerHandler;
import com.skirpichenok.tcpserver.ServerHandlerFactory;

import java.nio.channels.SocketChannel;

class ProxyConnectorFactory implements ServerHandlerFactory {

    private final ProxyConfig config;

    public ProxyConnectorFactory(ProxyConfig config) {
        this.config = config;
    }

    @Override
    public ServerHandler create(final SocketChannel clientChannel) {
        return new ProxyConnector(clientChannel, config);
    }

}
