package com.mars.powers.net;

import com.mars.powers.entity.Bee;
import com.mars.powers.entity.BeeHis;
import com.mars.powers.utils.UuidGen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.messaging.Message;

import java.util.Date;

@Component
@Slf4j
public class SecurityChannelInterceptor implements ChannelInterceptor {
    @Autowired
    private MongoTemplate mongoTemplate;
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
                ClientNode clientNode = new ClientNode(username,token,getCid());
                accessor.setUser(clientNode);
                setOnlineCacheData(clientNode);
                log.info("【{}】用户上线了",username);
            }else{
                accessor.setUser(new ClientNode(token,"匿名用户",""));
                log.info("【{}】匿名用户上线了",token);
            }
//            accessor.addNativeHeader("uid",username);
        }else if(StompCommand.DISCONNECT.equals(accessor.getCommand())){
            ClientNode clientNode = (ClientNode)accessor.getUser();
            if(!clientNode.getToken().equals("匿名用户")){
                setOfflineCacheData(clientNode.getName());
            }
            log.info("【{}】用户下线了",clientNode.getName());
        }
        return message;
    }

    public String getCid(){
        String u4d = UuidGen.generateShortUuid().substring(0,4);
        Criteria condition = Criteria.where("cid").is(u4d);
        Query query = new Query(condition);
        Bee bee = mongoTemplate.findOne(query,Bee.class);
        if(bee==null){
            return u4d;
        }else{
            return getCid();
        }
    }

    public void setOnlineCacheData(ClientNode clientNode){
        Bee bee = new Bee();
        bee.setName(clientNode.getName());
        bee.setToken(clientNode.getToken());
        bee.setCid(clientNode.getCid());
        bee.setStatus("online");
        bee.setCreateDate(new Date());
        Bee user1 = mongoTemplate.insert(bee);
    }
    public void setOfflineCacheData(String username){
        Criteria condition = Criteria.where("name").is(username);
        Query query = new Query(condition);
        Bee bee = mongoTemplate.findOne(query,Bee.class);
        BeeHis beeHis = new BeeHis();
        BeanUtils.copyProperties(bee,beeHis);
        mongoTemplate.remove(bee);
        beeHis.setOfflineDate(new Date());
        beeHis.setStatus("offline");
        mongoTemplate.insert(beeHis);
    }


}