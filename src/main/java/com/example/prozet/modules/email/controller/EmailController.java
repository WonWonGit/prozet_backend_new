package com.example.prozet.modules.email.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.prozet.common.ErrorCode;
import com.example.prozet.common.ErrorResponse;
import com.example.prozet.modules.email.service.EmailService;
import com.example.prozet.modules.projectMember.service.ProjectMemberService;
import com.example.prozet.redis.RedisUtil;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/confirm-invite")
    @ResponseBody
    public ResponseEntity<?> confirmMail(@RequestParam String token, @RequestParam String projectKey)
            throws URISyntaxException, IOException {

        String username = emailService.confirmToken(token);

        if (username == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_INVITATION_EXPIRED);
        }

        Boolean result = projectMemberService.editProjectMemberState(username,
                projectKey);

        if (result == false) {
            return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_MEMBER_NOT_EXIST);
        }

        redisUtil.deleteData("pm:" + token);

        URI redirectUri = new URI("http://localhost:8080/v1/api/project");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);

    }

}
