package com.mars.powers.net;
import java.security.Principal;

public class ClientNode implements Principal {
    private String name;
    private String cid;
    private String token;
    public ClientNode(String name,String token,String cid){
        this.name=name;
        this.token = token;
        this.cid = cid;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}