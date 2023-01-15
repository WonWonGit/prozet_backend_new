package com.example.prozet.modules.stack.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.modules.stack.domain.dto.request.StackReqDTO;
import com.example.prozet.modules.stack.service.StackService;

@RestController
@RequestMapping("v1/api/stack")
public class StackApiController {

    @Autowired
    private StackService stackService;

    @PostMapping("/{projectKey}")
    public ResponseEntity<?> saveStack(
            @PathVariable String projectKey,
            @Valid @RequestPart(required = true) StackReqDTO stackReqDTO,
            @RequestPart(required = false, name = "iconImg") MultipartFile iconImg,
            Authentication authentication) {

        return null;
    }

}
