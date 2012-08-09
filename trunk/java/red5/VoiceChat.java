package com.idb.red5.voicechat;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.stream.IBroadcastStream;

public class VoiceChat extends ApplicationAdapter {
	private VoiceChatManager manager = new VoiceChatManager();

	@Override
	public boolean appConnect(IConnection connection, Object[] params) {
		boolean appConnect = super.appConnect(connection, params);
		return manager.appConnectHandler(connection, params, appConnect);
	}

	@Override
	public void appDisconnect(IConnection connection) {
		super.appDisconnect(connection);
		manager.appDisconnectHandler(connection);
	}

	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
		super.streamBroadcastStart(stream);
		manager.streamBroadcastStartHandler(stream);
	}

	@Override
	public void streamBroadcastClose(IBroadcastStream stream) {
		super.streamBroadcastClose(stream);
		manager.streamBroadcastCloseHandler(stream);
	}

	// @Override
	// public synchronized void disconnect(IConnection conn, IScope scope) {
	// Object isPublsher = conn.getAttribute("isPublsher");
	// if (isPublsher != null && ((Boolean) isPublsher)) {
	// refreshPublishers();
	// }
	// super.disconnect(conn, scope);
	// }
	//
	// private void refreshPublishers() {
	// List<Sayer> removePublishers = new ArrayList<Sayer>();
	// for (Sayer publisher : publishers) {
	// if (publisher.isClosed()) {
	// removePublishers.add(publisher);
	// }
	// }
	// for (Sayer publisher : removePublishers) {
	// publishers.remove(publisher);
	// }
	// for (Sayer publisher : publishers) {
	// publisher.refreshPublishers();
	// }
	//
	// }
	//
	// public void startPaly(String tn, String sn, String cn) {
	// playEvery(tn, sn, cn);
	// }
	//
	// private void playEvery(String tn, String sn, String cn) { //
	// Collection<Set<IConnection>> connections = connection.getScope() //
	// .getConnections();
	// Collection<Set<IConnection>> connections = scope.getConnections();
	// IConnection connection = null;
	//
	// for (Set<IConnection> set : connections) {
	// for (IConnection connectionTemp : set) {
	// String connectionTn = (String) connectionTemp.getAttribute("tn");
	// String connectionSn = (String) connectionTemp.getAttribute("sn");
	// String connectionCn = (String) connectionTemp.getAttribute("cn");
	// if (tn.equals(connectionTn) && sn.equals(connectionSn) &&
	// cn.equals(connectionCn)) {
	// connection = connectionTemp;
	// }
	// }
	// }
	//
	// for (Set<IConnection> set : connections) {
	// for (IConnection connectionTemp : set) {
	// String connectionTn = (String) connectionTemp.getAttribute("tn");
	// String connectionSn = (String) connectionTemp.getAttribute("sn");
	// String connectionCn = (String) connectionTemp.getAttribute("cn");
	// if (tn.equals(connectionTn) && sn.equals(connectionSn) &&
	// !cn.equals(connectionCn)) {
	// playClient(connectionTemp, tn, sn, cn);
	// playClient(connection, connectionTn, connectionSn, connectionCn);
	// }
	// }
	// }
	// }

}
