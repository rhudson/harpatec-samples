package com.harpatec.examples;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class TomcatWebSocketServlet extends WebSocketServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatWebSocketServlet.class);
	
	private final WebApplicationContext webContext;
	
	private final Set<WebsocketMessageInbound> connections =
            new CopyOnWriteArraySet<WebsocketMessageInbound>();
	
	public TomcatWebSocketServlet() {
		super();
		webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

		WebSocketBroadcaster broadcaster = new WebSocketBroadcaster() {

			@Override
			public void broadcast(String message) {
				LOGGER.debug("Broadcasting message [{}].", message);
		        for (WebsocketMessageInbound connection : connections) {
		            try {
		                CharBuffer buffer = CharBuffer.wrap(message);
		                connection.getWsOutbound().writeTextMessage(buffer);
		            } catch (IOException ignore) {
		                // Ignore
		            }
		        }
			}
			
		};

		LOGGER.debug("Setting the WebSocketBroadcaster callback in the SpringWebsocketAdapter");
		SpringWebsocketAdapter springWebsocketAdapter = webContext.getBean(SpringWebsocketAdapter.class);
		springWebsocketAdapter.setBroadcaster(broadcaster);
	}

    @Override
	protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
		return new WebsocketMessageInbound();
	}

	private final class WebsocketMessageInbound extends MessageInbound {

        @Override
        protected void onOpen(WsOutbound outbound) {
            connections.add(this);
			LOGGER.debug("WebSocket connection added [{}].", connections.size());
        }
        
        @Override
		protected void onBinaryMessage(ByteBuffer message) throws IOException {
			throw new UnsupportedOperationException("Binary message not supported.");
		}

		@Override
		protected void onTextMessage(CharBuffer message) throws IOException {
			LOGGER.debug("Echoing message received from web socket connection [{}].", message);
			getWsOutbound().writeTextMessage(message);
		}
	}

}
