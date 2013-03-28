package com.harpatec.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringWebsocketAdapter implements WebSocketBroadcaster {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringWebsocketAdapter.class);
	
	private WebSocketBroadcaster broadcaster;

	public WebSocketBroadcaster getBroadcaster() {
		return broadcaster;
	}

	public void setBroadcaster(WebSocketBroadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}

	@Override
	public void broadcast(String message) {
		if (broadcaster != null) {
			broadcaster.broadcast(message);
		} else {
			LOGGER.debug("WebSocketBroadcaster is not set so there is nowhere to send the message.");
		}
	}
	
}
