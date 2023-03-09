package com.mars.powers.entity;

import com.mars.powers.core.CommandEnum;
import lombok.Data;

@Data
public class CommandParams {
    int x1;
    int x2;
    int y1;
    int y2;
    int delay;
    String eventName;
    public CommandParams(int x1,int y1,int x2,int y2){
        this.x1= x1;this.x2 = x2;
        this.y1 = y1;this.y2 = y2;
    }
    public CommandParams(int x1,int y1,int delay,String eventName){
        this.x1 = x1;
        this.y1 = y1;
        this.delay = delay;
        if(CommandEnum.isDebug){
            this.eventName = eventName;
        }else{
            this.eventName = "T";
        }
    }
}
