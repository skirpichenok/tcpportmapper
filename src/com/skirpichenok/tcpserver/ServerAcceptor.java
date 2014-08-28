package com.skirpichenok.tcpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

class ServerAcceptor implements ServerHandler {

    private final static int ACCEPT_BUFFER_SIZE = 1000;
    private final static Logger LOGGER = Logger.getAnonymousLogger();

    private final ServerConfig config;
    private final Queue<ServerHandler> handlers;

    public ServerAcceptor(final ServerConfig config, final Queue<ServerHandler> handlers) {
        this.config = config;
        this.handlers = handlers;
    }

    @Override
    public void register(final Selector selector) {
        try {
            final ServerSocketChannel server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress(config.getPort()), ACCEPT_BUFFER_SIZE);
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT, this);
        } catch (final IOException exception) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, "Can't init server connection!", exception);
        }
    }

    @Override
    public void process(SelectionKey key) {
        if (key.isValid() && key.isAcceptable()) {
            try {
                final ServerSocketChannel server = (ServerSocketChannel) key.channel();

                SocketChannel clientChannel;
                clientChannel = server.accept();

                handlers.add(config.getHandlerFactory().create(clientChannel));
            } catch (final IOException exception) {
                if (LOGGER.isLoggable(Level.SEVERE))
                    LOGGER.log(Level.SEVERE, "Can't accept client connection!", exception);
            }
        }
    }

    @Override
    public void destroy() {
        // nothing
    }

}
