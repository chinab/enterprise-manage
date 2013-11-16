/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package com.artogrid.bundle.qpid.demo;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.qpid.client.AMQConnection;

public class MessageReceiver {

	public static void main(String[] args) throws Exception {
		String responseQueue = "java.qb.queue";
		
		Connection connection = new AMQConnection("amqp://guest:guest@test/?brokerlist='tcp://192.168.1.230:5672'");
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination queue = IDBQpidUtils.createDestination(responseQueue);
		MessageConsumer consumer = session.createConsumer(queue);
		try {
			while (true) {
				MapMessage m = (MapMessage) consumer.receive();
				System.out.println(m);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		connection.close();
	}

}