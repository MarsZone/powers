package com.mars.powers.actions;

import com.mars.powers.base.Result;
import com.mars.powers.base.ResultGenerator;
import com.mars.powers.core.CoreParams;
import com.mars.powers.entity.Bee;
import com.mars.powers.net.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.mars.powers.actions.CompletableCheck.*;

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
            processActions.sendOpenCharView(bee.getName()); //组合命令，点人物头像，点人物按钮。这个不用拆分。不需要判断是否加载完成
            //不应该用sleep。。。应该有个处理消息队列..这个命令会有3-5秒的时间打开。应该等待客户端返回通知
            //Thread.sleep(2500);
            //判断是否打开了用户页面
            bee.setCheckCache("");
            mongoTools.updateCacheByBee(bee);
            CompletableFuture<String> orgFuture = CompletableFuture.supplyAsync(()->getCheckCharView(processActions,bee.getName(),mongoTools,key));
            String res = orgFuture.join();
            if(res.equals("开启")){
                processActions.sendGetUserName(bee.getName());
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
    public void startMining(CoreParams params, ExecutorService executor) throws InterruptedException {
        String key = params.getKey();
        Bee bee = mongoTools.getBeeByCid(key);
        //初始化
        FutureParams futureParams = new FutureParams(processActions,bee.getName(),mongoTools,key);
        if(bee.getAction()==null||bee.getActionNode().equals("待机")){
            boolean isLoopRun=true;
            String nextAction = "";
            while(isLoopRun){
                //初始化
                bee.setAction("挖矿");
                bee.setActionNode("校验存储空间");
                bee.setIndustrialShipIsFull("空仓");
                mongoTools.saveActions(bee);

                //打开舰船仓库
                processActions.sendOpenInventory(bee.getName());
                //Todo 状态自检，在站内在站外。
                //校验库存情况
                Thread.sleep(2000); //太快了校验不准
                futureParams.updateActionAndCheckFrequency("满仓",5,1000);
                CompletableFuture<String> checkFullFuture = CompletableFuture.supplyAsync(()->checkActionsState(futureParams));
                String isFull = checkFullFuture.join();
                System.out.println("仓库状态:"+isFull);
                if(isFull.equals("满仓")){
                    //卸货
                    bee.setActionNode("卸货");
                    mongoTools.saveActions(bee);
                    processActions.sendUnloadOre(bee.getName());
                    Thread.sleep(4000);
                }else {
                    processActions.sendClosePanelWithAction(bee.getName(),"准备出站");
                }
                //出站
                futureParams.updateActionAndCheckFrequency("准备出站",2,1000);
                CompletableFuture<String> checkReadyForLeave = CompletableFuture.supplyAsync(()->checkActionsState(futureParams));
                if(!checkFullFuture.join().equals("准备出站")){
                    isLoopRun=false;
                }
                //等出站完成
                bee.setActionNode("出站中");
                mongoTools.saveActions(bee);
                processActions.sendLevelStation(bee.getName());
                Thread.sleep(5000);
                futureParams.updateActionAndCheckFrequency("已出站",10,1000);
                CompletableFuture<String> checkOutStation = CompletableFuture.supplyAsync(()->checkActionsState(futureParams));
                if(!checkOutStation.join().equals("已出站")){
                    isLoopRun=false;
                }
                Thread.sleep(6000); //校验成功还需要时间加载界面
                //跳矿区
                processActions.sendGoToMindPos(bee.getName());
                //等到达
                Thread.sleep(15000); //15秒后校验
                futureParams.updateActionAndCheckFrequency("跃迁完成",10,10000);
                CompletableFuture<String> checkArrivalLocation = CompletableFuture.supplyAsync(()->checkActionsState(futureParams));
                if(!checkArrivalLocation.join().equals("跃迁完成")){
                    isLoopRun=false;
                }

                //循环挖矿,等挖满
                processActions.sendStartMiningProcess(bee.getName());
                futureParams.updateActionAndCheckFrequency("跃迁完成",10,10000);
                CompletableFuture<String> checkInventoryIsFull = CompletableFuture.supplyAsync(()->checkActionsState(futureParams));
                if(!checkInventoryIsFull.join().equals("满仓")){
                    isLoopRun=false;
                }

                //回程
                processActions.sendGoBack(bee.getName());
                Thread.sleep(15000); //15秒后校验
                futureParams.updateActionAndCheckFrequency("已停靠",10,10000);
                checkArrivalLocation = CompletableFuture.supplyAsync(()->checkActionsState(futureParams));
                if(!checkArrivalLocation.join().equals("已停靠")){
                    isLoopRun=false;
                }

                //卸货
                bee.setActionNode("卸货");
                mongoTools.saveActions(bee);
                processActions.sendUnloadOre(bee.getName());
                Thread.sleep(4000);
                //重置状态流程循环,直到关闭状态。
                if(!isLoopRun){
                    bee.setActionNode("待机");
                    mongoTools.saveActions(bee);
                }
            }
            executor.shutdown();
        }else{
            executor.shutdown();
        }
    }

}
