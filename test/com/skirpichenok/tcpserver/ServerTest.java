package com.skirpichenok.tcpserver;

import org.junit.Test;
import org.mockito.Mockito;

public class ServerTest {

	private final ServerHandlerFactory handlerFactory = Mockito.mock(ServerHandlerFactory.class);
	private ServerConfig config = new ServerConfig(0, handlerFactory, 1);

	@Test
	public void shouldSuccessStartAndShutdown() {
		Server connector = new Server(config);

		connector.start();
		connector.shutdown();
	}

	@Test
	public void shouldSuccessStartAndShutdownWithThreeWorkers() {
		config = new ServerConfig(0, handlerFactory, 3);
		Server connector = new Server(config);

		connector.start();
		connector.shutdown();
	}

	@Test
	public void shouldSuccessShutdownTwice() {
		Server connector = new Server(config);

		connector.start();
		connector.shutdown();
		connector.shutdown();
	}

	@Test
	public void shouldSuccessShutdownWithoutStart() {
		Server connector = new Server(config);
		connector.shutdown();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldFailWhenStartTwice() {
		Server connector = new Server(config);
		connector.start();

		try {
			connector.start();
		} finally {
			connector.shutdown();
		}
	}

	@Test(expected = NullPointerException.class)
	public void shouldFailWhenCreateWithNullConfig() {
		new Server(null);
	}

}
