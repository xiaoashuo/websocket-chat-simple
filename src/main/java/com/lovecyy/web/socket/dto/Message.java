package com.lovecyy.web.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ys
 * @topic
 * @date 2019/7/31 15:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    private int messageId;
    private int fromId;
    private String fromName;
    private int toId;
    /** 状态 0未读 1已读 */
    private Integer status;
    private String messageText;
    private Date messageDate;

}
