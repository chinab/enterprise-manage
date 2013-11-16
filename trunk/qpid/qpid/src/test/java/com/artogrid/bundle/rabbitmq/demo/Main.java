package com.artogrid.bundle.rabbitmq.demo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import com.artogrid.bundle.rabbitmq.demo.UserProto.User;
import com.artogrid.bundle.rabbitmq.demo.UserProto.User.Builder;

public class Main {
	public Main() throws Exception {

//		 QueueConsumer consumer = new QueueConsumer("xvxv-queue");
//		 Thread consumerThread = new Thread(consumer);
//		 consumerThread.start();

		Producer producer = new Producer("xvxv-queue");

		for (int i = 0; i < 100000000; i++) {
			Builder builder = User.newBuilder();
			builder.setUsername("xvxv"+i);
			builder.setPassword(i+"");
			producer.sendMessage(builder.build().toByteArray());
			System.out.println("Message Number " + i + " sent.");
		}
	}

	/**
	 * @param args
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {
		new Main();
	}
}
