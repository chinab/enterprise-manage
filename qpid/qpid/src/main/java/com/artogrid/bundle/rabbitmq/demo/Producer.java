package com.artogrid.bundle.rabbitmq.demo;

import java.io.IOException;


/**
 * The producer endpoint that writes to the queue.
 * @author syntx
 *
 */
public class Producer extends EndPoint{
	
	public Producer(String endPointName) throws IOException{
		super(endPointName);
	}

	public void sendMessage(byte[] data) throws IOException {
	    channel.basicPublish("",endPointName, null, data);
	}	
}