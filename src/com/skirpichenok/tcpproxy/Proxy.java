package com.skirpichenok.tcpproxy;

import com.skirpichenok.tcpserver.Server;
import com.skirpichenok.tcpserver.ServerConfig;

/**
 * TCP proxy.
 * <p/>
 * After starting it listening local port and send all incoming
 * traffic on it from client to remote host and from remote host to client.
 * Doesn't have any timeout. If client or remote server closes connection it will
 * close opposite connection.
 * <p/>
 * Multi-thread and asynchronous TCP proxy server based on NIO.
 * <p/>
 * You can create any count of proxy instances and run they in together.
 *
 * @see ProxyConnectorFactory
 * @see ProxyConnector
 * @see ProxyConfig
 * @see com.skirpichenok.tcpserver.Server
 */
public class Proxy {

    private final Server server;

    public Proxy(final ProxyConfig config) {
        ProxyConnectorFactory handlerFactory = new ProxyConnectorFactory(config);

        final ServerConfig serverConfig =
                new ServerConfig(config.getLocalPort(), handlerFactory, config.getWorkerCount());

        server = new Server(serverConfig);
    }

    /**
     * Start server.
     * This method run servers worked then return control.
     * This method isn't blocking.
     * <p/>
     * If you call this method when server is started, it throw exception.
     * <p/>
     * See {@link com.skirpichenok.tcpserver.Server#start()}
     */
    public void start() {
        server.start();
    }

    /**
     * Stop server and release all resources.
     * If server already been closed this method return immediately
     * without side effects.
     * <p/>
     * See {@link com.skirpichenok.tcpserver.Server#shutdown()}
     */
    public void shutdown() {
        server.shutdown();
    }

}
