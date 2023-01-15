package com.example.prozet.modules.email.service;

import java.util.StringTokenizer;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.prozet.redis.RedisUtil;
import com.example.prozet.utils.UtilsClass;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private RedisUtil redisUtil;

    @Async
    public void sendMail(String to, String username, String from, String projecetKey) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("프로젝트 멤버 초대");
        message.setText("http://localhost:8080/confirmInvite?token=" +
                createConfirmToken(username) + "&projectKey=" + projecetKey);
        emailSender.send(message);

    }

    public String createConfirmToken(String username) {

        String token = UUID.randomUUID().toString().concat(UtilsClass.getCurrentDate());
        redisUtil.setDataExpire("pm:" + token, username, 1800000);

        return token;
    }

    public String confirmToken(String token) {

        String username = redisUtil.getData("pm:" + token);

        if (username == null) {
            return null;
        }

        return username;

    }

}
