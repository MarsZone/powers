package com.mars.powers.net;

import com.mars.powers.core.CoreProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class WebSocketController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    CoreProcess coreProcess;
    // 当客户端向服务器发送请求时，通过 `@MessageMapping` 映射 /broadcast 这个地址
    @MessageMapping("/broadcast")
    // 当服务器有消息时，会对订阅了 @SendTo 中的路径的客户端发送消息
    @SendTo("/b")
    public Response say(SimpMessageHeaderAccessor headerAccessor, Message message) {
        String msg = "Welcome, " + message.getName() + "!";
        Map<String, Object> attrs = headerAccessor.getSessionAttributes();
        System.out.println(msg);
        return new Response(msg);
    }

    @MessageMapping("/group/{groupID}")
    public void group(@DestinationVariable String groupID, Message message) {
        Response response = new Response("Welcome to group " + groupID + ", " + message.getName() + "!");
        simpMessagingTemplate.convertAndSend("/g/" + groupID, response);
    }

    @MessageMapping("/chat")
    public void chat(ChatMessage chatMessage) {
        Response response = new Response("Receive message from user " + chatMessage.getFromUserID() + ": " + chatMessage.getMessage());
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(chatMessage.getUserID()), "/msg", response);
        if(chatMessage.getUserID().equals("core")){
            coreProcess.responseProcess(chatMessage);
        }
    }
}