package com.example.prozet.modules.stack.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.prozet.common.ErrorCode;
import com.example.prozet.common.ErrorResponse;
import com.example.prozet.common.ResponseDTO;
import com.example.prozet.common.ResponseEnum;
import com.example.prozet.modules.stack.domain.dto.response.StackFindResDTO;
import com.example.prozet.modules.stack.service.StackService;

@Controller
@RequestMapping("v1/api/stack")
public class StackController {

    @Autowired
    private StackService stackService;

    @GetMapping("/{projectKey}")
    public ResponseEntity<?> getStackList(@PathVariable String projectKey) {

        Map<String, List<StackFindResDTO>> stackList = stackService.getStackList(projectKey);

        if (stackList == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.STACK_NOT_EXIST);
        }

        return ResponseDTO.toResponseEntity(ResponseEnum.FIND_STACK_SUCCESS, stackList);

    }

}
