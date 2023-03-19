package com.mars.powers.actions;

import com.mars.powers.base.Result;
import com.mars.powers.base.ResultGenerator;
import com.mars.powers.core.CoreParams;
import com.mars.powers.entity.Bee;
import com.mars.powers.net.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class ControlProcessActions {
    @Autowired
    MongoTools mongoTools;
    @Autowired
    ProcessActions processActions;
    public Result checkConnect(CoreParams params) throws InterruptedException {
        String key = params.getKey();
        Bee bee = mongoTools.getBeeByCid(key);
        if(bee!=null){
            //执行命令，点击人物界面，查询角色名 1.执行两次点击。2.等待完成消息。 3.发出查看角色信息命令，等待返回文字。
            String name = bee.getName();
            processActions.sendOpenCharView(name);
            Thread.sleep(3500);
            processActions.sendGetUserName(name);
            Thread.sleep(1500);
            Response response = new Response();
            response.setCommand("C1020");
            response.setContent("6000");
            return ResultGenerator.getSuccessResult(response);
        }else{
            return ResultGenerator.getFailResult("校验失败");
        }
    }
}
