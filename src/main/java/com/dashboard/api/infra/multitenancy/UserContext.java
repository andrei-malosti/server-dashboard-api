package com.dashboard.api.infra.multitenancy;

import java.util.UUID;

public class UserContext {
	
private static final ThreadLocal<UUID> currentUser = new ThreadLocal<>();
	
	public static void setUserId(UUID userId) {
		currentUser.set(userId);
	}
	
	public static UUID getUserId() {
		return currentUser.get();
	}
	
	public static void clear() {
		currentUser.remove();
	}
}
