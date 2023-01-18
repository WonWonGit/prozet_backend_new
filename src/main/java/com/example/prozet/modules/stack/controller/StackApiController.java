package com.example.prozet.modules.stack.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.common.ErrorCode;
import com.example.prozet.common.ErrorResponse;
import com.example.prozet.common.ResponseDTO;
import com.example.prozet.common.ResponseEnum;
import com.example.prozet.modules.stack.domain.dto.request.StackReqDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackResDTO;
import com.example.prozet.modules.stack.service.StackService;
import com.example.prozet.security.auth.PrincipalDetails;

@RestController
@RequestMapping("v1/api/stack")
public class StackApiController {

    @Autowired
    private StackService stackService;

    @PostMapping
    public ResponseEntity<?> saveStack(
            @Valid @RequestPart(required = true) StackReqDTO stackReqDTO,
            @RequestPart(required = false, name = "iconImg") MultipartFile iconImg,
            Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        StackResDTO stackResDTO = stackService.saveStack(stackReqDTO, iconImg, principalDetails.getUsername());

        if (stackReqDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.SAVE_STACK_FAIL);
        }

        return ResponseDTO.toResponseEntity(ResponseEnum.SAVE_STACK_SUCCESS, stackResDTO);
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteStack(@PathVariable Long idx, Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        StackResDTO stackResDTO = stackService.findByIdx(idx);

        if (stackResDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.STACK_NOT_EXIST);
        } else {

            if (stackResDTO.getRole().role().equals(principalDetails.getRole())) {
                stackService.deleteStackService(stackResDTO);

                return ResponseDTO.toResponseEntity(ResponseEnum.DELETE_STACK_SUCCESS, null);

            } else {

                return ErrorResponse.toResponseEntity(ErrorCode.STACK_FORBIDDEN);

            }
        }
    }

}
