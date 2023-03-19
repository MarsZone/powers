package com.mars.powers.actions;

import com.mars.powers.core.MessageBlock;
import com.mars.powers.entity.CommandParams;
import com.mars.powers.net.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProcessActions {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    //展示信息
    public void sendMessage(String message, String uname){
        Response response = new Response("命令执行");
        //检测命令完成，后续优化下
        response.setCommand(CommandEnum.C10);
        response.setParams(new CommandParams(message));

        simpMessagingTemplate.convertAndSend("/user/"+uname+"/msg",response);
    }
    public void sendGetUserName(String user){
        Response response = new Response("命令执行");
        //检测命令完成，后续优化下
        response.setCommand(CommandEnum.C1000);
        response.setParams(new CommandParams(702,124, 886,170,"用户名"));
        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }
    public void sendCloseCmdPanel(String user){
        Response response = new Response("命令执行");
        response.setCommand(CommandEnum.C2000);
        response.setParams(new CommandParams("关闭菜单",691,152,500,CommandParams.NoNext));
        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }

    public void sendOpenCharView(String user){
        Response response = new Response("命令执行");
        response.setCommand(CommandEnum.C3000);
        List<MessageBlock> blocks = new ArrayList<>();
        MessageBlock block = new MessageBlock("点击头像",62,55,500);
        MessageBlock block2 = new MessageBlock("人物入口",61,212,1000,CommandParams.NoNext);
        blocks.add(block);
        blocks.add(block2);
        response.setParams(new CommandParams(blocks));
        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }

}
