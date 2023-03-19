package com.mars.powers.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document("Bee")
public class Bee {
    @Id
    String id;
    String name;
    String userName;
    String cid;
    String token;
    Date createDate;
    Date offlineDate;
    String status;
}