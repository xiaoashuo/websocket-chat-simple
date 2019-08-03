package com.lovecyy.web.socket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovecyy.web.socket.dto.Message;
import com.lovecyy.web.socket.dto.User;
import com.lovecyy.web.socket.utils.MapperUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ys
 * @topic
 * @date 2019/7/31 16:01
 */
@Component
public class MyHandler implements WebSocketHandler {



    public static final ConcurrentHashMap<Integer, WebSocketSession> userSocketSessionMap;

    @Autowired
    private MessageService messageService;

    static {
        userSocketSessionMap = new ConcurrentHashMap<>();
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        //webSocketSession.sendMessage(new TextMessage("欢迎连接到ws服务"));

        int uid = (Integer) webSocketSession.getAttributes().get("uid");
        System.out.println("欢迎连接本服务"+uid);
        if (userSocketSessionMap.get(uid) == null) {
            userSocketSessionMap.put(uid, webSocketSession);
            //给管理员发送信息 增加一个人列表
            if (uid!=1)
            sendMessageToUser(1,new TextMessage(uid+","+1));
        }
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        System.out.println("获取到消息 >> " + webSocketMessage.getPayload());

        if(webSocketMessage.getPayloadLength()==0)return;

        Integer uid = (Integer) webSocketSession.getAttributes().get("uid");
        ObjectMapper mapper = MapperUtils.getInstance();
        JsonNode jsonNode = mapper.readTree(webSocketMessage.getPayload().toString());
        Integer toId = jsonNode.get("toId").asInt();
        String msg = jsonNode.get("msg").asText();
        Message message = Message.builder().fromId(uid).toId(toId).messageText(msg)
                .messageDate(new Date()).fromName("客户端").build();
        //存储成功返回消息 带主键的
        Message message1 = messageService.saveMessage(message);
        //判断对方是否在线
        WebSocketSession toSession = userSocketSessionMap.get(toId);
        if (toSession!=null&&toSession.isOpen()){
            //具体格式需和前端对接
            TextMessage textMessage = new TextMessage(MapperUtils.obj2json(message1));
            toSession.sendMessage(textMessage);
            messageService.updateMessageState(String.valueOf(message1.getMessageId()),1);
        }

        //发送Socket信息
        // sendMessageToUser(msg.getToId(), new TextMessage(MapperUtils.obj2json(msg)));

    }

    /**
     * 消息传输过程中出现的异常处理函数
     * 处理传输错误：处理由底层WebSocket消息传输过程中发生的异常
     */
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        // 记录日志，准备关闭连接
        System.out.println("Websocket异常断开:" + webSocketSession.getId() + "已经关闭");
        //群发消息告知大家
        Message msg = new Message();
        msg.setMessageDate(new Date());

        //获取异常的用户的会话中的用户编号
        User loginUser=(User)webSocketSession.getAttributes().get("uid");
        Set<Map.Entry<Integer, WebSocketSession>> entries = userSocketSessionMap.entrySet();
        //并查找出在线用户的WebSocketSession（会话），将其移除（不再对其发消息了。。）
        for (Map.Entry<Integer, WebSocketSession> entry : entries) {
            if(entry.getKey().equals(loginUser.getId())){
                //群发消息告知大家
                msg.setMessageText("万众瞩目的【"+loginUser.getNickname()+"】已经有事先走了，大家继续聊...");
                //清除在线会话
                userSocketSessionMap.remove(entry.getKey());
                //记录日志：
                System.out.println("Socket会话已经移除:用户ID" + entry.getKey());
                break;
           }
        }
    }

    /**
     * 在此刷新页面就相当于断开WebSocket连接,原本在静态变量userSocketSessionMap中的
     * WebSocketSession会变成关闭状态(close)，但是刷新后的第二次连接服务器创建的
     * 新WebSocketSession(open状态)又不会加入到userSocketSessionMap中,所以这样就无法发送消息
     * 因此应当在关闭连接这个切面增加去除userSocketSessionMap中当前处于close状态的WebSocketSession，
     * 让新创建的WebSocketSession(open状态)可以加入到userSocketSessionMap中
     * @param webSocketSession
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        if ((Integer) webSocketSession.getAttributes().get("uid")!=1)
        sendMessageToUser(1,new TextMessage(webSocketSession.getAttributes().get("uid")+","+0));

        System.out.println("WebSocket:"+webSocketSession.getAttributes().get("uid")+"close connection");
        Iterator<Map.Entry<Integer,WebSocketSession>> iterator = userSocketSessionMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Integer,WebSocketSession> entry = iterator.next();
            //判断 内存map中是否存在和当前webSocketSession中uid相等的 若存在 删除 否则不做处理
            if(entry.getValue().getAttributes().get("uid")==webSocketSession.getAttributes().get("uid")){
                userSocketSessionMap.remove(webSocketSession.getAttributes().get("uid"));
                System.out.println("WebSocket in staticMap:" + webSocketSession.getAttributes().get("uid") + "removed");
            }
        }


    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    //发送信息的实现
    public void sendMessageToUser(int uid, TextMessage message)
            throws IOException {
        WebSocketSession session = userSocketSessionMap.get(uid);
        if (session != null && session.isOpen()) {
            session.sendMessage(message);
        }
    }

}
