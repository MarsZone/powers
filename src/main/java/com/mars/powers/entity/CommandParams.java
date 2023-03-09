package com.mars.powers.entity;

import com.mars.powers.core.CommandEnum;
import com.mars.powers.core.MessageBlock;
import lombok.Data;

import java.util.List;

@Data
public class CommandParams {
    int x1;
    int x2;
    int y1;
    int y2;
    int delay;
    int hasNext;
    String eventName;

    List<MessageBlock> blocks;
    public CommandParams(int x1,int y1,int x2,int y2){
        this.x1= x1;this.x2 = x2;
        this.y1 = y1;this.y2 = y2;
    }
    public CommandParams(String eventName,int x1,int y1,int delay,int hasNext){
        this.x1 = x1;
        this.y1 = y1;
        this.delay = delay;
        this.hasNext = hasNext;
        if(CommandEnum.isDebug){
            this.eventName = eventName;
        }else{
            this.eventName = "T";
        }
    }

    public CommandParams(List<MessageBlock> blocks){
        this.blocks = blocks;
    }
}
