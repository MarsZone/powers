package com.mars.powers.net;
import java.security.Principal;

public class ClientNode implements Principal {
    private String name;
    private String token;
    public ClientNode(String name,String token){
        this.name=name;
        this.token = token;
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
}