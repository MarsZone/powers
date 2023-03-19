package com.mars.powers.net;

import com.mars.powers.entity.CommandParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务器向客户端发送此类消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor  // 避免反序列化异常
public class Response {
    private String responseMessage;
    private String command;

    private String content;

    private CommandParams params;


    public Response(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}