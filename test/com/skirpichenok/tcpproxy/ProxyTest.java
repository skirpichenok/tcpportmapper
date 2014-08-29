package com.skirpichenok.tcpproxy;

import org.junit.Test;

public class ProxyTest {

	private ProxyConfig config = new ProxyConfig(0, "", 0);

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenCreateWithNegativeWorkersCount() {
		config.setWorkerCount(-100);

		new Proxy(config);
	}

}
