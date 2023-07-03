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

    public void addMessageBlock(List<MessageBlock> blocks,String eventName,int x1,int y1,int delay){
        blocks.add(new MessageBlock(eventName,x1,y1,delay));
    }
    public void addMessageBlock(List<MessageBlock> blocks,String eventName,int x1,int y1,int delay,String nextNode){
        blocks.add(new MessageBlock(eventName,x1,y1,delay,nextNode));
    }
    //展示信息
    public void sendMessage(String message, String user){
        Response response = new Response("命令执行");
        //检测命令完成，后续优化下
        response.setCommand(CommandEnum.C10);
        response.setParams(new CommandParams(message));

        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }
    public void sendCharViewIsOpenCheck(String user){
        Response response = new Response("命令执行");
        //检测命令完成，后续优化下
        response.setCommand(CommandEnum.CK1000);
        response.setParams(new CommandParams(134,25, 201,58,"人物面板"));
        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }
    public void sendCheckInventoryFull(String user){
        Response response = new Response("命令执行");
        //检测命令完成，后续优化下
        response.setCommand(CommandEnum.CK2000);
        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }

    public void sendGetUserName(String user){
        Response response = new Response("命令执行");
        //检测命令完成，后续优化下
        response.setCommand(CommandEnum.C1000);
        response.setParams(new CommandParams(703,123, 883,173,"用户名"));
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
        MessageBlock block2 = new MessageBlock("人物入口",61,212,1000);
        blocks.add(block);
        blocks.add(block2);
        response.setParams(new CommandParams(blocks));
        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }

    public void sendClosePanel(String user){
        Response response = new Response("命令执行");
        response.setCommand(CommandEnum.C2000);
        response.setParams(new CommandParams("关闭面板",1232,38,500,CommandParams.NoNext));
        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }

    public void sendClosePanelWithAction(String user,String action){
        Response response = new Response("命令执行");
        response.setCommand(CommandEnum.C2000);
        response.setParams(new CommandParams("关闭面板",1232,38,500,action));
        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }

    public void sendOpenInventory(String user){
        Response response = new Response("命令执行");
        response.setCommand(CommandEnum.C3000);
        List<MessageBlock> blocks = new ArrayList<>();
        MessageBlock block = new MessageBlock("库存界面",27,116,500);
        MessageBlock block2 = new MessageBlock("选择矿石舱",134,553,1000,CommandParams.NoNext);
        blocks.add(block);
        blocks.add(block2);
        response.setParams(new CommandParams(blocks));
        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }

    public void sendUnloadOre(String user){
        Response response = new Response("命令执行");
        response.setCommand(CommandEnum.C3000);
        List<MessageBlock> blocks = new ArrayList<>();
        MessageBlock block = new MessageBlock("卸货_全选",983,657,500);
        MessageBlock block2 = new MessageBlock("卸货_移动至",145,151,1000);
        MessageBlock block3 = new MessageBlock("卸货_物品机库",499,172,2500);
        MessageBlock block4 = new MessageBlock("关闭面板",1232,38,3500,"准备出站");
        blocks.add(block);
        blocks.add(block2);
        blocks.add(block3);
        blocks.add(block4);
        response.setParams(new CommandParams(blocks));
        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }
    public void sendLevelStation(String user){
        Response response = new Response("命令执行");
        response.setCommand(CommandEnum.C2000);
        response.setParams(new CommandParams("离站",1161,242,1000,"出站中"));
        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }

    public static MessageBlock firstRow = new MessageBlock("第一个小行星带",1093,95,0);

    public void sendGoToMindPos(String user){
        Response response = new Response("命令执行");
        response.setCommand(CommandEnum.C3000);
        List<MessageBlock> blocks = new ArrayList<>();
        addMessageBlock(blocks,"打开概览",1231,404,2000);
        addMessageBlock(blocks,"选项",1045,30,3500);
        addMessageBlock(blocks,"矿点",1074,486,5000);
        addMessageBlock(blocks,"小行星带",1251,83,6000);
        addMessageBlock(blocks,"第一个小行星带",firstRow.getX1(),firstRow.getY1(),7000);
        addMessageBlock(blocks,"跃迁",844,174,8000,"等待跃迁完成");
        response.setParams(new CommandParams(blocks));
        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }

    public void sendStartMiningProcess(String user){
        Response response = new Response("命令执行");
        response.setCommand(CommandEnum.C3000);
        List<MessageBlock> blocks = new ArrayList<>();
        addMessageBlock(blocks,"矿点",1252,83,1000);
        addMessageBlock(blocks,"第一个",firstRow.getX1(),firstRow.getY1(),2000);
        addMessageBlock(blocks,"环绕",845,250,3000);
        addMessageBlock(blocks,"启动矿机1",846,659,4500);
        addMessageBlock(blocks,"启动矿机2",937,660,5500);
        addMessageBlock(blocks,"启动矿机3",1005,660,6500,"作业中");
        response.setParams(new CommandParams(blocks));
        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }

    public void sendGoBack(String user){
        Response response = new Response("命令执行");
        response.setCommand(CommandEnum.C3000);
        List<MessageBlock> blocks = new ArrayList<>();
        addMessageBlock(blocks,"空间站",1249,147,1000);
        addMessageBlock(blocks,"第一个",firstRow.getX1(),firstRow.getY1(),2000);
        addMessageBlock(blocks,"停靠",845,93,3000,"停靠");
        response.setParams(new CommandParams(blocks));
        simpMessagingTemplate.convertAndSend("/user/"+user+"/msg",response);
    }

}
