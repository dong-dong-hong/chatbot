package com.chatbot.project.config;

import com.chatbot.project.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Autowired
    private WebSocketAuthChannelInterceptor webSocketAuthChannelInterceptor;

    private final JwtUtil jwtUtil;

    public WebSocketConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(webSocketAuthChannelInterceptor);
//    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("Authorization");
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                        if (jwtUtil.validateToken(token)) {
                            String username = jwtUtil.getUsernameFromToken(token);
                            accessor.setUser(new StompPrincipal(username, token));
                        }
                    }
                }
                return message;
            }
        });
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic","/queue")
                .setHeartbeatValue(new long[]{10000, 10000}) // 10초마다 Heartbeat 설정
                .setTaskScheduler(heartBeatScheduler());     // Heartbeat 스케줄러 설정

        registry.setApplicationDestinationPrefixes("/app");

        registry.setUserDestinationPrefix("/user");

        logger.info("📡 WebSocket 메시지 브로커가 활성화되었습니다.");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .addInterceptors(new HttpHandshakeInterceptor())
                .setAllowedOriginPatterns("*")
//                .setHandshakeHandler(new DefaultHandshakeHandler() {
//                    @Override
//                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
//                                                      Map<String, Object> attributes) {
//                        String token = (String) attributes.get("token");
//                        return () -> token != null ? token : "anonymous";
//                    }
//                })
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                                      Map<String, Object> attributes) {
                        String token = (String) attributes.get("token");
                        // 익명함수로는 stompPrincipal 체크 false라서 구체적으로 변경
                        if (token != null && jwtUtil.validateToken(token)) {
                            String username = jwtUtil.getUsernameFromToken(token);
                            return new StompPrincipal(username, token);
                        }

                        return new StompPrincipal("anonymous", ""); // 또는 null 반환
                    }
                })
                .withSockJS();

        logger.info("✅ WebSocket 엔드포인트 '/chat'이 등록되었습니다.");
    }


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        logger.warn("🚨 WebSocket 연결 끊김: {}", event.getSessionId());
    }


    @Bean
    public TaskScheduler heartBeatScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("wss-heartbeat-thread-");
        scheduler.initialize();
        return scheduler;
    }
}
