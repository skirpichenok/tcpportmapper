package com.skirpichenok.tcpserver;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

class ServerWorker extends Thread {

    private final static long SELECTOR_TIMEOUT = 100L;
    private final static Logger LOGGER = Logger.getAnonymousLogger();

    private final Queue<ServerHandler> handlers;

    public ServerWorker(final Queue<ServerHandler> handlers) {
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

                final Set<SelectionKey> keys = selector.selectedKeys();
                for (final SelectionKey key : keys) {
                    final ServerHandler handler = (ServerHandler) key.attachment();
                    handler.process(key);
                }
                keys.clear();
            }
        } catch (final IOException exception) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, "Problem with selector, worker will be stopped!", exception);
        } finally {
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException exception) {
                    if (LOGGER.isLoggable(Level.WARNING))
                        LOGGER.log(Level.WARNING, "Could not close selector properly.", exception);
                }
            }
        }
    }

}
