package com.mars.powers.core;


import com.mars.powers.actions.CommandEnum;
import com.mars.powers.actions.ProcessActions;
import com.mars.powers.base.ResultGenerator;
import com.mars.powers.entity.Bee;
import com.mars.powers.entity.CommandParams;
import com.mars.powers.net.ChatMessage;
import com.mars.powers.net.Response;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RestController
public class CoreProcess {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    ProcessActions processActions;
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
            Response response = new Response("命令执行");
            //检测命令完成，后续优化下
            response.setCommand(CommandEnum.C1000);
            response.setParams(new CommandParams(120,21, 183,44));
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
        Criteria condition = Criteria.where("cid").is(key);
        Query query = new Query(condition);
        Bee bee = mongoTemplate.findOne(query,Bee.class);
        if(bee!=null){
            session.setAttribute("key",params.getKey());
            return ResultGenerator.getSuccessResult();
        }else{
            return ResultGenerator.getFailResult("查不到");
        }
    }
    @RequestMapping(value = "/checkConnect",method = RequestMethod.POST)
    public Object checkConnect(@RequestBody CoreParams params){
        String key = params.getKey();
        Bee bee = getBeeByCid(key);
        if(bee!=null){
            //执行命令，点击人物界面，查询角色名 1.执行两次点击。2.等待完成消息。 3.发出查看角色信息命令，等待返回文字。


            return ResultGenerator.getSuccessResult("");
        }else{
            return ResultGenerator.getFailResult("校验失败");
        }
    }

    private Bee getBeeByCid(String key){
        Criteria condition = Criteria.where("cid").is(key);
        Query query = new Query(condition);
        Bee bee = mongoTemplate.findOne(query,Bee.class);
        return bee;
    }


    //Todo 状态机
    public void responseProcess(ChatMessage chatMessage){
        //TODO 返回数据优化
//        System.out.println(chatMessage.getMessage());
        if(chatMessage.getMessage().equals("C666")){
            String cid = getCid(chatMessage.getFromUserID());
            if(!cid.equals("")){
                Response response = new Response("密码:"+cid);
                simpMessagingTemplate.convertAndSend("/user/"+chatMessage.getFromUserID()+"/msg",response);
            }
        }
    }

    public String getCid(String name){
        Criteria condition = Criteria.where("name").is(name);
        Query query = new Query(condition);
        Bee bee = mongoTemplate.findOne(query,Bee.class);
        if(bee!=null){
            return bee.getCid();
        }else{
            processActions.sendError("查询失败",name);
        }
        return "";
    }

}
