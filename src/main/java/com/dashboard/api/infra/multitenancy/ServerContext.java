package com.dashboard.api.infra.multitenancy;

import java.util.UUID;

public class ServerContext {
	
private static final ThreadLocal<UUID> currentServer = new ThreadLocal<>();
	
	public static void setServerId(UUID serverId) {
		currentServer.set(serverId);
	}
	
	public static UUID getServerId() {
		return currentServer.get();
	}
	
	public static void clear() {
		currentServer.remove();
	}

}
