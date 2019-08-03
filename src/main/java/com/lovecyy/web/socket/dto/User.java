package com.lovecyy.web.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ys
 * @topic
 * @date 2019/7/31 15:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;//唯一标识属性

    private String nickname;
}
