package com.mars.powers.actions;

import com.mars.powers.entity.CommandParams;
import com.mars.powers.net.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProcessActions {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public void sendError(String message,String uname){
        Response response = new Response("命令执行");
        //检测命令完成，后续优化下
        response.setCommand(CommandEnum.C10);
        response.setParams(new CommandParams(message));

        simpMessagingTemplate.convertAndSend("/user/"+uname+"/msg",response);
    }
}
