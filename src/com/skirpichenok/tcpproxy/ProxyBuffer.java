package com.skirpichenok.tcpproxy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

/**
 * ProxyBuffer class.
 */
public class ProxyBuffer {

	private static enum BufferState {
		READY_TO_WRITE, READY_TO_READ
	}

	public static final int BUFFER_SIZE = 1000;

	private ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

	private BufferState state = BufferState.READY_TO_WRITE;

	/**
	 * Method checks state to be ready to read.
	 * 
	 * @return boolean
	 */
	public boolean isReadyToRead() {
		return state == BufferState.READY_TO_READ;
	}

	/**
	 * Method checks state to be ready to write.
	 * 
	 * @return boolean
	 */
	public boolean isReadyToWrite() {
		return state == BufferState.READY_TO_WRITE;
	}

	/**
	 * Mehod reads data from channel to buffer.
	 * 
	 * @param channel
	 *            SocketChannel
	 * @throws IOException
	 *             the IOException
	 */
	public void writeFrom(SocketChannel channel) throws IOException {
		int read = channel.read(buffer);
		if (read == -1) {
			throw new ClosedChannelException();
		}
		if (read > 0) {
			buffer.flip();
			state = BufferState.READY_TO_READ;
		}
	}

	/**
	 * This method try to write data from buffer to channel. Buffer changes state to READY_TO_READ only if all data were
	 * wrote to channel, in other case you should call this method again
	 *
	 * @param channel
	 *            SocketChannel
	 * @throws IOException
	 *             the IOException
	 */
	public void writeTo(SocketChannel channel) throws IOException {
		channel.write(buffer);
		// only if buffer is empty
		if (buffer.remaining() == 0) {
			buffer.clear();
			state = BufferState.READY_TO_WRITE;
		}
	}

}
