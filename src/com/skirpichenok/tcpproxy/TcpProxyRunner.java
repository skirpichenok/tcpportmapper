package com.skirpichenok.tcpproxy;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpProxyRunner {

    private final static Logger LOGGER = Logger.getAnonymousLogger();

    public static void main(final String[] args) {
        if (args.length != 1) {
            System.err.println("Please specify path to config file!");
            System.exit(1);
        }

        final Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(args[0]));
        } catch (IOException exception) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, "Can't load properties from " + args[0], exception);
            System.exit(1);
        }

        final List<TcpProxyConfig> configs = TcpProxyConfigParser.parse(properties);
        if (LOGGER.isLoggable(Level.INFO))
            LOGGER.info("Starting TcpProxy with " + configs.size() + " connectors");

        final int cores = Runtime.getRuntime().availableProcessors();
        if (LOGGER.isLoggable(Level.INFO))
            LOGGER.info("TcpProxy detected " + cores + " core" + (cores > 1 ? "s" : ""));

        final int workerCount = Math.max(cores / configs.size(), 1);
        if (LOGGER.isLoggable(Level.INFO))
            LOGGER.info("TcpProxy will use " + workerCount + " workers per connector");

        for (final TcpProxyConfig config : configs) {
            config.setWorkerCount(workerCount);

            new TcpProxy(config).start();
        }

        if (LOGGER.isLoggable(Level.INFO))
            LOGGER.info("TcpProxy started");
    }

}
