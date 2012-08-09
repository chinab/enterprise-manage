package com.idb.red5.voicechat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.red5.server.api.IConnection;
import org.red5.server.api.service.IServiceCapableConnection;
import org.red5.server.api.stream.IBroadcastStream;

public class VoiceChatManager {
	private static final String ME = "me";
	private static final String VOICE_ROLE_TYPE = "voiceRoleType";
	public final static int VOICE_ROLE_MANAGER = 0;
	public final static int VOICE_ROLE_SPEAKER = 1;
	public final static int VOICE_ROLE_LISTENER = 2;
	public final static Map<String, IConnection> managers = new HashMap<String, IConnection>();
	public final static Map<String, Set<String>> activeUserPublishedNameMap = new HashMap<String, Set<String>>();

	public boolean appConnectHandler(IConnection connection, Object[] params, boolean appConnect) {
		if (params.length >= 1) {
			int voiceRoleType = (Integer) params[0];
			connection.setAttribute(VOICE_ROLE_TYPE, voiceRoleType);
			if (voiceRoleType == VOICE_ROLE_MANAGER) {
				String user = (String) params[1];
				connection.setAttribute(ME, user);
				managers.put(user, connection);
				playActivedPublishedStream(connection, user);
			} else if (voiceRoleType == VOICE_ROLE_LISTENER) {
				// connection.get
			}
			return appConnect;
		}
		return false;
	}

	public void appDisconnectHandler(IConnection connection) {
		int voiceRoleType = connection.getIntAttribute(VOICE_ROLE_TYPE);
		if (voiceRoleType == VOICE_ROLE_MANAGER) {
			String user = connection.getStringAttribute(ME);
			managers.remove(user);
		}
	}

	public void streamBroadcastStartHandler(IBroadcastStream stream) {
		String publishedName = stream.getPublishedName();
		String user = getManagerUserByPublishedName(publishedName);
		playNetStream(publishedName, managers.get(user));
		addActiveUserPublishedName(user, publishedName);
	}

	public void streamBroadcastCloseHandler(IBroadcastStream stream) {
		String publishedName = stream.getPublishedName();
		String user = getManagerUserByPublishedName(publishedName);
		stopNetStream(publishedName, managers.get(user));
		removeActiveUserPublishedName(user, publishedName);
	}

	private void playActivedPublishedStream(IConnection connection, String user) {
		Set<String> activeUsers = activeUserPublishedNameMap.get(user);
		if (CollectionUtils.isNotEmpty(activeUsers)) {
			for (String publishedName : activeUsers) {
				playNetStream(publishedName, connection);
			}
		}
	}

	private void addActiveUserPublishedName(String user, String publishedName) {
		if (StringUtils.isBlank(user) || StringUtils.isBlank(publishedName)) {
			return;
		}
		if (!activeUserPublishedNameMap.containsKey(user)) {
			activeUserPublishedNameMap.put(user, new HashSet<String>());
		}
		activeUserPublishedNameMap.get(user).add(publishedName);
	}

	private void removeActiveUserPublishedName(String user, String publishedName) {
		if (StringUtils.isBlank(user) || StringUtils.isBlank(publishedName)) {
			return;
		}
		if (activeUserPublishedNameMap.containsKey(user)) {
			Set<String> publishedNames = activeUserPublishedNameMap.get(user);
			if (publishedNames.contains(publishedName)) {
				publishedNames.remove(publishedName);
			}
			if (publishedNames.size() == 0) {
				activeUserPublishedNameMap.remove(user);
			}
		}
	}

	private String getManagerUserByPublishedName(String publishedName) {
		int endMark = publishedName.lastIndexOf("_");
		int startMark = publishedName.lastIndexOf("_", endMark - 1);
		return publishedName.substring(startMark + 1, endMark);
	}

	private void playNetStream(String publishedName, IConnection managerConnection) {
		if (managerConnection != null) {
			try {
				IServiceCapableConnection sc = (IServiceCapableConnection) managerConnection;
				sc.invoke("playNetStream", new Object[] { publishedName });
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void stopNetStream(String publishedName, IConnection managerConnection) {
		if (managerConnection != null) {
			try {
				IServiceCapableConnection sc = (IServiceCapableConnection) managerConnection;
				sc.invoke("stopNetStream", new Object[] { publishedName });
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
