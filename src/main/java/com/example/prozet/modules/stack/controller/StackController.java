package com.example.prozet.modules.stack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.prozet.common.ResponseDTO;
import com.example.prozet.common.ResponseEnum;
import com.example.prozet.modules.stack.domain.dto.response.StackFindResDTO;
import com.example.prozet.modules.stack.service.StackService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Controller
@RequestMapping("v1/api/stack")
public class StackController {

    @Autowired
    private StackService stackService;

    @GetMapping
    public ResponseEntity<?> getStackList(@RequestBody String projectKey) {

        List<StackFindResDTO> stackList = stackService.getStackList(projectKey);

        return ResponseDTO.toResponseEntity(ResponseEnum.FIND_STACK_SUCCESS, stackList);

    }

}
