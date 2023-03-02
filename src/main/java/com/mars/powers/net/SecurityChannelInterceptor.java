package com.mars.powers.net;

import com.mars.powers.utils.UuidGen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.messaging.Message;

@Component
@Slf4j
public class SecurityChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())){
            String token = null;
            String username = null;
            try {
//                String token = accessor.getNativeHeader("Authorization").get(0);
                //校验token
//                JwtBean jwtBean = ApplicationContextUtils.getBean(JwtBean.class);
//                username = jwtBean.getUsername(token);
//                username = UuidGen.generateShortUuid();
                if(accessor.getNativeHeader("uuid")!=null){
                    username = accessor.getNativeHeader("uuid").get(0);
                }
                token = (String) accessor.getSessionAttributes().get("sessionId");
            } catch (Exception e) {
                e.printStackTrace();
                log.error("token is error");
                throw new IllegalStateException("The token is illegal");
            }
//            if(StringUtils.isEmpty(username)){
//                log.error("token is overtime");
//                throw new IllegalStateException("The token is illegal");
//            }
            if(username!=null){
                accessor.setUser(new ClientNode(username,token));
                log.info("【{}】用户上线了",username);
            }else{
                accessor.setUser(new ClientNode(token,"匿名用户"));
                log.info("【{}】用户上线了",token);
            }
//            accessor.addNativeHeader("uid",username);
        }else if(StompCommand.DISCONNECT.equals(accessor.getCommand())){
            log.info("【{}】用户下线了",accessor.getUser().getName());
        }
        return message;
    }


}