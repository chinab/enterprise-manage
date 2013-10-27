import org.apache.qpid.amqp_1_0.client.AcknowledgeMode;
import org.apache.qpid.amqp_1_0.client.Connection;
import org.apache.qpid.amqp_1_0.client.Message;
import org.apache.qpid.amqp_1_0.client.Sender;
import org.apache.qpid.amqp_1_0.client.Session;
import org.apache.qpid.amqp_1_0.client.Transaction;
import org.apache.qpid.amqp_1_0.transport.Container;

public class Test {
	public static void main(String[] args) {
		final String queue = "java.test.helloworld";
		String message = "你好啊";
		try {
			Container container = new Container();
			Connection conn = new Connection("192.168.1.102", 5672, null, null, 65536, container, "192.168.1.102", false, 0);
			Session session = conn.createSession();
			Sender s = session.createSender(queue, 100, AcknowledgeMode.ALO, null);
//			Transaction txn = session.createSessionLocalTransaction();

//			s.send(new Message(message), txn);

//			txn.commit();
			s.send(new Message(message));
			
			
			
			s.close();
			session.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
