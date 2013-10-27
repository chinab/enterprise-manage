import org.apache.qpid.amqp_1_0.client.AcknowledgeMode;
import org.apache.qpid.amqp_1_0.client.Connection;
import org.apache.qpid.amqp_1_0.client.Message;
import org.apache.qpid.amqp_1_0.client.Receiver;
import org.apache.qpid.amqp_1_0.client.Session;
import org.apache.qpid.amqp_1_0.client.Transaction;
import org.apache.qpid.amqp_1_0.transport.Container;
import org.apache.qpid.amqp_1_0.type.UnsignedInteger;

public class TestReceiver {
	public static void main(String[] args) {
		final String queue = "java.test.helloworld";
		try {
			Container container = new Container();
			Connection conn = new Connection("192.168.1.102", 5672, null, null, 65536, container, "192.168.1.102", false, 0);
			Session session = conn.createSession();
			Receiver r = session.createReceiver(queue,AcknowledgeMode.ALO,null, false);
//			Transaction txn = session.createSessionLocalTransaction();

			int credit = 0;
			for (int i = 0; i < 100; i++) {
				if (credit == 0) {
					credit = 100 - i;
					r.setCredit(UnsignedInteger.valueOf(credit), false);
				}

				Message m = r.receive(1000L);
				credit--;
				if (m == null) {
					break;
				}

//				r.acknowledge(m.getDeliveryTag(), txn);
				r.acknowledge(m.getDeliveryTag());
				System.out.println("Received Message : " + m.getPayload());
			}
//			txn.commit();
			r.close();
			session.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
