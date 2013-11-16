package com.artogrid.bundle.qpid.demo;

import java.net.URISyntaxException;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Session;

import org.apache.qpid.client.AMQAnyDestination;

public class IDBQpidUtils {
	public static Destination createDestination(String queue) throws URISyntaxException {
		return new AMQAnyDestination("ADDR:" + queue + "; {create: always}");
	}

	public static MapMessage createMessage(Session session, String cmd, String replyTo, String subject, String messageId) throws JMSException, URISyntaxException {
		MapMessage m = session.createMapMessage();

		m.setJMSCorrelationID(cmd);
		m.setJMSReplyTo(createDestination(replyTo));
		m.setStringProperty("qpid.subject", subject);
		m.setStringProperty("x-amqp-0-10.app-id", messageId);

		return m;
	}
}
