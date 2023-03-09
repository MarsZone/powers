package com.mars.powers.core;

import lombok.Data;

@Data
public class MessageBlock {
    int x1;
    int y1;
    int delay;
    int hasNext;
    String eventName;
    public MessageBlock(String eventName,int x1,int y1,int delay){
        this.eventName = eventName;
        this.x1 = x1;
        this.y1 = y1;
        this.delay = 0;
        this.hasNext = hasNext;
    }
    public MessageBlock(String eventName,int x1,int y1,int delay,int hasNext){
        this.eventName = eventName;
        this.x1 = x1;
        this.y1 = y1;
        this.delay = delay;
        this.hasNext = hasNext;
    }
}
