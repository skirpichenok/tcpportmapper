package com.skirpichenok.tcpserver;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * Handler for all incoming client connection.
 */
public interface ServerHandler {

	/**
	 * Called when worker get handler from queue.
	 *
	 * @param selector
	 *            Selector.
	 */
	void register(Selector selector);

	/**
	 * Called when selector receive IO event it try to get attached handler from key and call this method.
	 *
	 * @param key
	 *            SelectionKey
	 */
	void process(SelectionKey key);

	/**
	 * Called when workers were stopped and server should close all not processed channels.
	 */
	void destroy();

}
