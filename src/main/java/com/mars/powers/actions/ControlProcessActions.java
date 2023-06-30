package com.mars.powers.actions;

import com.mars.powers.base.Result;
import com.mars.powers.base.ResultGenerator;
import com.mars.powers.core.CoreParams;
import com.mars.powers.entity.Bee;
import com.mars.powers.net.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ControlProcessActions {
    @Autowired
    MongoTools mongoTools;
    @Autowired
    ProcessActions processActions;

    public void closePanel(String  key){
        Bee bee = mongoTools.getBeeByCid(key);
        if(bee!=null){
            processActions.sendClosePanel(bee.getName());
        }
    }
    public static String WC1020 = "WC1020"; // 返回前端，等待通知。
    public Result checkConnect(CoreParams params) throws InterruptedException {
        String key = params.getKey();
        Bee bee = mongoTools.getBeeByCid(key);
        if(bee!=null){
            //执行命令，点击人物界面，查询角色名 1.执行两次点击。2.等待完成消息。 3.发出查看角色信息命令，等待返回文字。
            String name = bee.getName();
            processActions.sendOpenCharView(name); //组合命令，点人物头像，点人物按钮。这个不用拆分。不需要判断是否加载完成
            //不应该用sleep。。。应该有个处理消息队列..这个命令会有3-5秒的时间打开。应该等待客户端返回通知
            //Thread.sleep(2500);
            //判断是否打开了用户页面
            bee.setCheckCache("");
            mongoTools.updateCacheByBee(bee);
            CompletableFuture<String> orgFuture = CompletableFuture.supplyAsync(()->getCheckCharView(processActions,name,mongoTools,key));
            String res = orgFuture.join();
            if(res.equals("开启")){
                processActions.sendGetUserName(name);
                Response response = new Response();
                response.setCommand(WC1020);
                response.setContent("2000");
                return ResultGenerator.getSuccessResult(response);
            }
            return ResultGenerator.getFailResult("校验失败");
        }else{
            return ResultGenerator.getFailResult("校验失败");
        }
    }

    //自动挖矿


    public static String getCheckCharView(ProcessActions processActions,String name,MongoTools mongoTools,String key){
        //发送检测请求
        int count = 5;
        //通过mongo查询结果，尝试5次，每次一秒。3次后返回失败，成功返回正常
        while(count>0){
            try {
                processActions.sendCharViewIsOpenCheck(name);
                Thread.sleep(1000);
                Bee bee = mongoTools.getBeeByCid(key);
                if(bee!=null){
                    if(bee.getCheckCache().equals("人物面板开启")){
                        return "开启";
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            count--;
        }
        return "校验失败";
    }
}
