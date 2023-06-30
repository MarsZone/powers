package com.mars.powers.actions;

import com.mars.powers.entity.Bee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class MongoTools {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ProcessActions processActions;

    public Bee getBeeByUname(String uname){
        Criteria condition = Criteria.where("name").is(uname);
        Query query = new Query(condition);
        Bee bee = mongoTemplate.findOne(query,Bee.class);
        return bee;
    }
    public Bee getBeeByCid(String key){
        Criteria condition = Criteria.where("cid").is(key);
        Query query = new Query(condition);
        Bee bee = mongoTemplate.findOne(query,Bee.class);
        return bee;
    }
    public String getCid(String name){
        Criteria condition = Criteria.where("name").is(name);
        Query query = new Query(condition);
        Bee bee = mongoTemplate.findOne(query,Bee.class);
        if(bee!=null){
            return bee.getCid();
        }else{
            processActions.sendMessage("查询失败",name);
        }
        return "";
    }

    public void updateByBee(Bee bee){
        Query query = new Query(Criteria.where("_id").is(bee.getId()));
        Update update = new Update().set("userName", bee.getUserName());
        mongoTemplate.updateFirst(query, update, Bee.class);
    }
    public void updateCacheByBee(Bee bee){
        Query query = new Query(Criteria.where("_id").is(bee.getId()));
        Update update = new Update().set("checkCache", bee.getCheckCache());
        mongoTemplate.updateFirst(query, update, Bee.class);
    }
}
