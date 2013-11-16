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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.qpid.client.AMQConnection;

public class MessageSender {

	public static void main(String[] args) throws Exception {
		String cmd = Cmd.E_FID_QB_BOND_INFO;
		String requstQueue = "dal.qb.queue";
		String replyTo = "java.qb.queue";
		String messageId = "test";
		String connStr = "amqp://guest:guest@test/?brokerlist='tcp://192.168.1.230:5672'";

		Map<String, String> bondKey1 = new HashMap<String, String>();
		bondKey1.put("BondKey", "00882b3c58e549e0b89c5ca7af6db778");
		Map<String, String> bondKey2 = new HashMap<String, String>();
		bondKey2.put("BondKey", "02253e15364b4413bab5093a48fbc6e0");
		
		List<Object> bonds = new ArrayList<Object>();
		bonds.add(bondKey1);
		bonds.add(bondKey2);
		
		Map<String, Object> bondInfoRequest = new HashMap<String, Object>();
		bondInfoRequest.put("Bond", bonds);

		Connection connection = new AMQConnection(connStr);
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(IDBQpidUtils.createDestination(requstQueue));
		MapMessage m = IDBQpidUtils.createMessage(session, cmd, replyTo, requstQueue, messageId);


		m.setObject("xQBBondInfoReq", bondInfoRequest);

		producer.send(m);
		connection.close();
	}

}