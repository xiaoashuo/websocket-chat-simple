import com.lovecyy.web.socket.SocketApplication;
import com.lovecyy.web.socket.dto.Message;

import com.lovecyy.web.socket.service.MessageService;
import com.lovecyy.web.socket.utils.DBUtility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ys
 * @topic
 * @date 2019/7/31 20:15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocketApplication.class)
public class testJaca {



    @Autowired
    private MessageService messageService;

    @Test
    public void testInsert(){
        Message build = Message.builder().fromId(1).fromName("消息").toId(2).messageText("你是好人").build();
      //  Message message = messageService.saveMessage(build);
        List<Message> listByFromAndTo = messageService.findListByFromAndTo(91, 1, 1, 10);
        for (Message message : listByFromAndTo) {
            System.out.println(message);
        }


    }


    @Test
    public void ts(){
        System.out.println(Math.round(System.currentTimeMillis()/1000));
    }

}
