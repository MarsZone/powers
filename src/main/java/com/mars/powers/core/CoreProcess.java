package com.mars.powers.core;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mars.powers.actions.CommandEnum;
import com.mars.powers.actions.ControlProcessActions;
import com.mars.powers.actions.MongoTools;
import com.mars.powers.actions.ProcessActions;
import com.mars.powers.base.ResultGenerator;
import com.mars.powers.entity.Bee;
import com.mars.powers.entity.CommandParams;
import com.mars.powers.net.ChatMessage;
import com.mars.powers.net.Response;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RestController
public class CoreProcess {
    @Autowired
    MongoTools mongoTools;
    @Autowired
    ProcessActions processActions;
    @Autowired
    SimpUserRegistry userRegistry;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    ControlProcessActions controlProcessActions;

    private static Logger logger = LoggerFactory.getLogger("core");
    private static long heartBeat = 0;
//    @Scheduled(fixedRate=5000)
//    public void tick(){
//        //每0.5秒执行
//        heartBeat++;
////        logger.info("CoreLive:"+heartBeat+"_Time:"+System.currentTimeMillis());
//        logger.info("Connect Count "+userRegistry.getUserCount());
//    }
    @RequestMapping("/command") //测试
    public String command(){
//        userRegistry.getUsers().stream()
//                .map(u -> u.getName())
//                .forEach(System.out::println);
        for(SimpUser simpUser : userRegistry.getUsers()){
            logger.info(simpUser.getName());
//            simpMessagingTemplate.convertAndSend("/b",new Response("通知"));
            Response response = new Response("命令执行");
            //检测命令完成，后续优化下
            response.setCommand(CommandEnum.C1000);
            response.setParams(new CommandParams(120,21, 183,44,"星系"));
            //Todo 单个点击命令，序列点击命令
            response.setCommand(CommandEnum.C2000);
            response.setParams(new CommandParams("点击菜单",63,50,500,1));
            //Todo 序列点击..连续行为
            response.setCommand(CommandEnum.C3000);
            List<MessageBlock> blocks = new ArrayList<>();
            MessageBlock block = new MessageBlock("库存界面",27,116,500);
            MessageBlock block2 = new MessageBlock("选择矿石舱",125,550,1500,1);
            blocks.add(block);
            blocks.add(block2);
            response.setParams(new CommandParams(blocks));

            simpMessagingTemplate.convertAndSend("/user/"+simpUser.getName()+"/msg",response);
            //全部完成后，MongoDB状态缓存
        }
        return "User Count:";
    }

    @RequestMapping(value = "/cvConnect", method = RequestMethod.POST)
    public Object cvConnect(@RequestBody CoreParams params, HttpSession session){
        //取出session中的browser
        String key = params.getKey();
        //查一下是否存在，有就设置，没有就返回错误
        Bee bee = mongoTools.getBeeByCid(key);
        if(bee!=null){
            session.setAttribute("key",params.getKey());
            //给Bee指令，关闭当前窗口
            processActions.sendCloseCmdPanel(bee.getName());
            return ResultGenerator.getSuccessResult();
        }else{
            return ResultGenerator.getFailResult("查不到");
        }
    }
    @RequestMapping(value = "/checkConnect",method = RequestMethod.POST)
    public Object checkConnect(@RequestBody CoreParams params) throws InterruptedException {
        return controlProcessActions.checkConnect(params);
    }

    @RequestMapping(value = "/getUserName",method = RequestMethod.POST)
    public Object getUserName(@RequestBody CoreParams params) throws InterruptedException {
        String key = params.getKey();
        Bee bee = mongoTools.getBeeByCid(key);
        if(bee!=null){
            return ResultGenerator.getSuccessResult(bee.getUserName());
        }else{
            return ResultGenerator.getFailResult("校验失败");
        }
    }

    //Todo 状态机
    public void responseProcess(ChatMessage chatMessage) throws JsonProcessingException {
        //TODO 返回数据优化
//        System.out.println(chatMessage.getMessage());
        //处理Bee返回的命令
        ObjectMapper mapper = new ObjectMapper();
        Response receive = mapper.readValue(chatMessage.getMessage(),Response.class);
        //这个是，应对1000命令，查询区域内文字的
        if(receive.getCommand().equals("C1010")){
            Bee bee = mongoTools.getBeeByUname(chatMessage.getFromUserID());
            bee.setUserName(receive.getContent());
            mongoTools.updateByBee(bee);
        }
        //这个是，Bee展示他自己的密码编号
        if(receive.getCommand().equals("C666")){
            String cid = mongoTools.getCid(chatMessage.getFromUserID());
            if(!cid.equals("")){
                Response response = new Response("密码:"+cid);
                simpMessagingTemplate.convertAndSend("/user/"+chatMessage.getFromUserID()+"/msg",response);
            }
        }
    }

}
