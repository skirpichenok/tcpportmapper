package com.skirpichenok.tcpproxy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * ProxyConfigParser class.
 */
public final class ProxyConfigParser {

	private ProxyConfigParser() {
	}

	/**
	 * Method that parses properties.
	 * 
	 * @param properties
	 *            Properties
	 * @return List<ProxyConfig>
	 */
	public static List<ProxyConfig> parse(Properties properties) {
		Set<String> proxyNames = new HashSet<String>();
		for (String propertyName : properties.stringPropertyNames()) {
			int dotIndex = propertyName.lastIndexOf('.');
			if (dotIndex == -1) {
				throw new IllegalArgumentException("Invalid property " + propertyName
						+ " should be <proxy name>.localPort|remotePort|remoteHost");
			}
			proxyNames.add(propertyName.substring(0, dotIndex));
		}
		if (proxyNames.isEmpty()) {
			throw new IllegalArgumentException("Please specify at least one proxy.");
		}
		List<ProxyConfig> tcpProxyConfigs = new ArrayList<ProxyConfig>();
		for (String proxyName : proxyNames) {
			tcpProxyConfigs.add(new ProxyConfig(findIntegerProperty(properties, proxyName + ".localPort"),
					findProperty(properties, proxyName + ".remoteHost"), findIntegerProperty(properties, proxyName
							+ ".remotePort")));
		}
		return tcpProxyConfigs;
	}

	private static int findIntegerProperty(Properties properties, String key) {
		String value = findProperty(properties, key);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException exception) {
			throw new IllegalArgumentException("Invalid integer " + key + " = " + value, exception);
		}
	}

	private static String findProperty(Properties properties, String key) {
		String value = properties.getProperty(key);
		if (value == null) {
			throw new IllegalArgumentException("Please specify " + key);
		}
		return value;
	}

}
