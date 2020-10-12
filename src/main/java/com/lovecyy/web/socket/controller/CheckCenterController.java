package com.lovecyy.web.socket.controller;

import com.lovecyy.web.socket.MyWebSocket;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/checkcenter")
public class CheckCenterController {

    //页面请求
    @GetMapping("/socket/{id}/{name}")
    public String socket(@PathVariable("id") String cid,@PathVariable("name") String name, Model model) {
        model.addAttribute("id", cid);
        return "index11";
    }
    //推送数据接口
    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public String pushToWeb(@PathVariable String cid,String message) {
        try {
            MyWebSocket.sendInfo("可笑","1");
        } catch (Exception e) {
            e.printStackTrace();
            return "{cid:#"+e.getMessage()+"}";
        }
        return "{cid:"+cid+"}";
    }



}
