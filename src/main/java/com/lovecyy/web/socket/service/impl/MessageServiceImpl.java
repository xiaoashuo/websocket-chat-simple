package com.lovecyy.web.socket.service.impl;

import com.lovecyy.web.socket.dto.Message;
import com.lovecyy.web.socket.service.MessageService;
import com.lovecyy.web.socket.utils.DBUtility;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

/**
 * @author ys
 * @topic
 * @date 2019/7/31 20:43
 */
@Service
public class MessageServiceImpl implements MessageService {

    private JdbcTemplate jdbcTemplate= DBUtility.getJdbcTemplate();

    @Override
    public List<Message> findListByFromAndTo(Integer fromId, Integer toId, Integer page, Integer rows) {
        String sql="SELECT * FROM `im_message` WHERE (`from_id`=? AND to_id=?) OR (`from_id`=? AND to_id=?)  ORDER BY `message_date`   ";
        sql+=" limit "+(page-1)*rows+" , "+rows;
        System.out.println(fromId+"--"+sql);
        List<Message> query = jdbcTemplate.query(sql, new Object[]{fromId, toId, toId, fromId}, new BeanPropertyRowMapper<Message>(Message.class));
        return query;
    }

    @Override
    public Message findMessageById(String id) {
        String sql="SELECT  * FROM `im_message`    WHERE `message_id`=?";
        Message message=null;
        try {
            message= jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Message.class));
        } catch (DataAccessException e) {
          //  e.printStackTrace();
        }
        return message;
    }

    @Override
    public int updateMessageState(String messageId, Integer status) {
        String sql="UPDATE `im_message` SET `status`=? WHERE `message_id`=?";
        return  jdbcTemplate.update(sql, new Object[]{status, messageId});
    }

    @Override
    public Message saveMessage(Message message) {
        String sql="INSERT INTO `im_message` (`from_id`,`from_name`,`to_id`,`status`,`message_text`,`message_date`) VALUES(?,?,?,0,?,NOW())";
        message.setStatus(0);
        message.setMessageDate(new Date());
        KeyHolder holder=new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, message.getFromId());
                preparedStatement.setString(2, message.getFromName());
                preparedStatement.setInt(3, message.getToId());
                preparedStatement.setString(4, message.getMessageText());
                return preparedStatement;
            }
        }, holder);
        int result = holder.getKey().intValue();
        if (result!=0) {
            message.setMessageId(result);
            return message;
        };
        return null;
    }

    @Override
    public int deleteMessage(String id) {
        String sql="DELETE FROM `im_message` WHERE `message_id`=?";
        return jdbcTemplate.update(sql, new Object[]{id});
    }
}
