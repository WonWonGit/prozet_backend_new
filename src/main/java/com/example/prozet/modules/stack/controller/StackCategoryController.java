package com.example.prozet.modules.stack.controller;

import java.util.List;

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
import com.example.prozet.modules.stack.domain.dto.response.StackCategoryFindResDTO;
import com.example.prozet.modules.stack.service.StackCategoryService;

@Controller
@RequestMapping("v1/api/stackCategory")
public class StackCategoryController {

    @Autowired
    private StackCategoryService stackCategoryService;

    @GetMapping("/{projectKey}")
    public ResponseEntity<?> getStackCategoryList(@PathVariable String projectKey) {

        List<StackCategoryFindResDTO> stackCategoryList = stackCategoryService.getStackCategory(projectKey);

        if (stackCategoryList.isEmpty()) {
            return ErrorResponse.toResponseEntity(ErrorCode.STACK_CATEGORY_NOT_EXIST);
        }

        return ResponseDTO.toResponseEntity(ResponseEnum.FIND_STACK_CATEGORY_SUCCESS, stackCategoryList);

    }

}
