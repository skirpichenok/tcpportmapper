package com.skirpichenok.tcpproxy;

import com.skirpichenok.tcpserver.TcpServerHandler;
import com.skirpichenok.tcpserver.TcpServerHandlerFactory;

import java.nio.channels.SocketChannel;

class TcpProxyConnectorFactory implements TcpServerHandlerFactory {

    private final TcpProxyConfig config;

    public TcpProxyConnectorFactory(TcpProxyConfig config) {
        this.config = config;
    }

    @Override
    public TcpServerHandler create(final SocketChannel clientChannel) {
        return new TcpProxyConnector(clientChannel, config);
    }

}
