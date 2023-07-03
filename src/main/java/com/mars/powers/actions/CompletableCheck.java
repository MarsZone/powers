package com.mars.powers.actions;

import com.mars.powers.entity.Bee;

public class CompletableCheck {
    public static String checkActionsState(FutureParams futureParams){
        //通过mongo查询结果，尝试N次，每次N秒。N次后返回失败，成功返回正常
        int count = futureParams.getCount();
        while(count>0){
            try {
                futureParams.getProcessActions().sendCheckInventoryFull(futureParams.getName());
                Thread.sleep(futureParams.getSleep());
                Bee bee = futureParams.getMongoTools().getBeeByCid(futureParams.getKey());
                if(bee!=null){
                    if(bee.getIndustrialShipIsFull().equals(futureParams.getAction())){
                        return futureParams.getAction();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            count--;
        }
        return "校验失败";
    }

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
