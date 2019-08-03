package com.lovecyy.web.socket.service;

import com.lovecyy.web.socket.dto.Message;

import java.util.List;

/**
 * @author ys
 * @topic
 * @date 2019/7/31 20:41
 */
public interface MessageService {
    /**
     * 查询点对点聊天记录
     *
     * @param fromId
     * @param toId
     * @param page
     * @param rows
     * @return
     */
    List<Message> findListByFromAndTo(Integer fromId, Integer toId, Integer page, Integer rows);
    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    Message findMessageById(String id);
    /**
     * 更新消息状态
     *
     * @param messageId
     * @param status
     * @return
     */
    int updateMessageState(String messageId, Integer status);

    /**
     * 新增消息数据
     *
     * @param message
     * @return
     */
    Message saveMessage(Message message);
    /**
     * 根据消息id删除数据
     *
     * @param id
     * @return
     */
    int deleteMessage(String id);

}
