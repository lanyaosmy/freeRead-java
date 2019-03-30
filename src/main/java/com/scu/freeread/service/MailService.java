package com.scu.freeread.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender; //框架自带的

    @Value("${spring.mail.username}")  //发送人的邮箱
    private String from;

//    @Async  //意思是异步调用这个方法
    public JSONObject sendMail(String title, String url, String email) {
        JSONObject json =new JSONObject();
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from); // 发送人的邮箱
            message.setSubject(title); //标题
            message.setTo(email); //发给谁  对方邮箱
            message.setText(url); //内容
            mailSender.send(message); //发送
        }catch (Exception e){
            json.put("error",e+"邮件发送失败！");
        }
        return json;
    }

}
