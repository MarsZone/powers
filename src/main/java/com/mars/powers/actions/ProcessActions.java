package com.mars.powers.actions;

import com.mars.powers.entity.CommandParams;
import com.mars.powers.net.Response;

public class ProcessActions {

    public void sendError(String message){
        Response response = new Response("命令执行");
        //检测命令完成，后续优化下
        response.setCommand(CommandEnum.C10);
        response.setParams(new CommandParams(message));
    }
}
