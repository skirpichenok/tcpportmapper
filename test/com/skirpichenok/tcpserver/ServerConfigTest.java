package com.skirpichenok.tcpserver;

import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ServerConfigTest {

	private final ServerHandlerFactory handlerFactory = Mockito.mock(ServerHandlerFactory.class);

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenCreateWithWorkerCountsZero() {
		new ServerConfig(0, handlerFactory, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenCreateWithNegativeWorkerCount() {
		new ServerConfig(0, handlerFactory, -10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenCreateWithNegativePort() {
		new ServerConfig(-90, handlerFactory, 1);
	}

	@Test(expected = NullPointerException.class)
	public void shouldFailWhenCreateWithNullHandlerFactory() {
		new ServerConfig(0, null, 3);
	}

	@Test
	public void shouldSuccess() {
		final ServerConfig config = new ServerConfig(5600, handlerFactory, 3);

		Assert.assertEquals(5600, config.getPort());
		Assert.assertSame(handlerFactory, config.getHandlerFactory());
		Assert.assertEquals(3, config.getWorkerCount());
	}

}
