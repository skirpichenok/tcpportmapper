package com.skirpichenok.tcpproxy;

/**
 * TCP proxy configuration.
 */
public class ProxyConfig {

    private final int localPort;
    private final String remoteHost;
    private final int remotePort;
    private int workerCount;

    public ProxyConfig(int localPort, String remoteHost, int remotePort) {
        this.localPort = localPort;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    /**
     * @return - local port which TCP proxy will be listening, should be 0..64000
     */
    public int getLocalPort() {
        return localPort;
    }

    /**
     * @return - remote port on which TCP proxy will send all traffic from incoming connections
     */
    public int getRemotePort() {
        return remotePort;
    }

    /**
     * @return - remote host on which TCP proxy will send all traffic from incoming connections
     */
    public String getRemoteHost() {
        return remoteHost;
    }

    /**
     * @return - count of worker (thread) which TCP proxy will use for processing
     *         incoming client connection, should more 0
     */
    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }

}
