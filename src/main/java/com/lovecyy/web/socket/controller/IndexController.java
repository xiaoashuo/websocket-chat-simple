package com.lovecyy.web.socket.controller;

import com.lovecyy.web.socket.dto.Message;
import com.lovecyy.web.socket.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author ys
 * @topic
 * @date 2019/7/31 16:12
 */
@Controller
@RequestMapping("/a")
public class IndexController {

    @Autowired
    private MessageService messageService;

    @GetMapping("index")
    public String index(){
        return "index11";
    }

    @GetMapping("list")
    public String list(){
        return "chat_list";
    }
    @GetMapping("box")
    public String box(Integer fromId, Model model){
        List<Message> messages = messageService.findListByFromAndTo(fromId, 1, 1, 100);
        System.out.println(messages);
        model.addAttribute("messages",messages);
        return "chat_box";
    }

    @PostMapping("box")
    @ResponseBody
    public List<Message> boxPost(Integer fromId, Model model){
        List<Message> messages = messageService.findListByFromAndTo(fromId, 1, 1, 100);
        System.out.println(messages);
        model.addAttribute("messages",messages);
        return messages;
    }
    @GetMapping("admin")
    public String admin(){
        return "admin";
    }
}
