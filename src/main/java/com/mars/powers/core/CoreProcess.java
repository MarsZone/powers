package com.mars.powers.core;


import com.mars.powers.net.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
public class CoreProcess {
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
    @RequestMapping("/hello")
    public String hello(){
//        userRegistry.getUsers().stream()
//                .map(u -> u.getName())
//                .forEach(System.out::println);
        for(SimpUser simpUser : userRegistry.getUsers()){
            logger.info(simpUser.getName());
//            simpMessagingTemplate.convertAndSend("/b",new Response("通知"));

            simpMessagingTemplate.convertAndSend("/user/"+simpUser.getName()+"/msg",new Response("上线提醒"));
        }

        return "User Count:";
    }

}
