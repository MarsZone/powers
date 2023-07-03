package com.mars.powers.actions;

import lombok.Data;

@Data
public class FutureParams {
    ProcessActions processActions;
    String name;
    MongoTools mongoTools;
    String key;
    String action;
    int count=5;
    int sleep=1000;

    public FutureParams(ProcessActions processActions, String name, MongoTools mongoTools, String key) {
        this.processActions = processActions;
        this.name = name;
        this.mongoTools = mongoTools;
        this.key = key;
    }

    public void updateActionAndCheckFrequency(String action,int count,int sleep){
        this.action=action;
        this.count =count;
        this.sleep=sleep;
    }
}
