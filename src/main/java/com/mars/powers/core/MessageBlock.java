package com.mars.powers.core;

import com.mars.powers.actions.CommandEnum;
import com.mars.powers.entity.CommandParams;
import lombok.Data;

@Data
public class MessageBlock {
    int x1;
    int y1;
    int delay;
    int hasNext;

    String nextNode;
    String eventName;

    public MessageBlock(String eventName,int x1,int y1,int delay){
        if(CommandEnum.isDebug){
            this.eventName = eventName;
        }else{
            this.eventName = "T";
        }
        this.x1 = x1;
        this.y1 = y1;
        this.delay = delay;
        this.hasNext = CommandParams.NoNext;
    }
    public MessageBlock(String eventName,int x1,int y1,int delay,String nextNode){
        if(CommandEnum.isDebug){
            this.eventName = eventName;
        }else{
            this.eventName = "T";
        }
        this.x1 = x1;
        this.y1 = y1;
        this.delay = delay;
        this.nextNode = nextNode;
        this.hasNext = CommandParams.hasNextCmd;
    }
    public MessageBlock(String eventName,int x1,int y1,int delay,int hasNext){
        if(CommandEnum.isDebug){
            this.eventName = eventName;
        }else{
            this.eventName = "T";
        }
        this.x1 = x1;
        this.y1 = y1;
        this.delay = delay;
        this.hasNext = hasNext;
    }
}
