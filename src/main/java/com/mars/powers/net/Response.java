package com.mars.powers.net;

import com.mars.powers.entity.CommandParams;

/**
 * 服务器向客户端发送此类消息
 */
public class Response {
    private String responseMessage;
    private String command;

    private CommandParams params;


    public Response(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public CommandParams getParams() {
        return params;
    }

    public void setParams(CommandParams params) {
        this.params = params;
    }
}