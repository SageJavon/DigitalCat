package com.cat.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Setter
@Getter
@ServerEndpoint(value = "/websocket/{token}")
@Component
public class WebSocket {

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String token;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("token") String token) {
        this.session = session;
        this.token = token;
        WebSocketUtils.serverClients.put(token,this);
        try {
            sendMessage("服务端连接成功！");
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("token") String token) {
        System.out.println("The connection has been released.");
        WebSocketUtils.serverClients.remove(token);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println(message);
    }

    /**
     * 发生错误时调用
     @OnError
     public void onError(Session session, Throwable error) {
     System.out.println("发生错误");
     error.printStackTrace();
     }
     *
     **/


     public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
     }

}
