package com.mars.powers.core;


import com.mars.powers.entity.Bee;
import com.mars.powers.entity.CommandParams;
import com.mars.powers.net.ChatMessage;
import com.mars.powers.net.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Service
@RestController
public class CoreProcess {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    SimpUserRegistry userRegistry;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private static Logger logger = LoggerFactory.getLogger("core");
    private static long heartBeat = 0;
//    @Scheduled(fixedRate=5000)
//    public void tick(){
//        //每0.5秒执行
//        heartBeat++;
////        logger.info("CoreLive:"+heartBeat+"_Time:"+System.currentTimeMillis());
//        logger.info("Connect Count "+userRegistry.getUserCount());
//    }
    @RequestMapping("/command")
    public String command(){
//        userRegistry.getUsers().stream()
//                .map(u -> u.getName())
//                .forEach(System.out::println);
        for(SimpUser simpUser : userRegistry.getUsers()){
            logger.info(simpUser.getName());
//            simpMessagingTemplate.convertAndSend("/b",new Response("通知"));
            Response response = new Response("上线检测");
            response.setCommand(CommandEnum.C1000);
            response.setParams(new CommandParams(120,21, 183,44));

            simpMessagingTemplate.convertAndSend("/user/"+simpUser.getName()+"/msg",response);
        }
        return "User Count:";
    }

    public void responseProcess(ChatMessage chatMessage){
        System.out.println(chatMessage.getMessage());
    }

}
