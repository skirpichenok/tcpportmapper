package com.skirpichenok.tcpproxy;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ProxyRunner class.
 */
public final class ProxyRunner {

	public static final Logger LOGGER = Logger.getAnonymousLogger();

	private ProxyRunner() {
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            String[]
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Please specify path to config file!");
			System.exit(1);
		}

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(args[0]));
		} catch (IOException exception) {
			LOGGER.log(Level.SEVERE, "Can't load properties from " + args[0], exception);
			System.exit(1);
		}

		List<ProxyConfig> configs = ProxyConfigParser.parse(properties);
		LOGGER.info("Starting TcpProxy with " + configs.size() + " connectors");

		int cores = Runtime.getRuntime().availableProcessors();
		LOGGER.info("TcpProxy detected " + cores + " core" + (cores > 1 ? "s" : ""));

		int workerCount = Math.max(cores / configs.size(), 1);
		LOGGER.info("TcpProxy will use " + workerCount + " workers per connector");

		for (ProxyConfig config : configs) {
			config.setWorkerCount(workerCount);
			new Proxy(config).start();
		}

		LOGGER.info("TcpProxy started");
	}

}
