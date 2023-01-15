package com.example.prozet.modules.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.prozet.common.ErrorCode;
import com.example.prozet.common.ErrorResponse;
import com.example.prozet.common.ResponseDTO;
import com.example.prozet.common.ResponseEnum;
import com.example.prozet.modules.member.domain.dto.response.MemberInfoResDTO;
import com.example.prozet.modules.member.service.MemberInfoService;
import com.example.prozet.security.auth.PrincipalDetails;

@RestController
@RequestMapping("v1/api/member")
public class MemberController {

    @Autowired
    private MemberInfoService memberInfoService;

    @GetMapping("/information/{username}")
    public ResponseEntity<?> getMemberInfo(@PathVariable String username,
            Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (!principalDetails.getUsername().equals(username)) {
            return ErrorResponse.toResponseEntity(ErrorCode.FORBIDDEN);
        }

        MemberInfoResDTO memberInfoResDTO = memberInfoService.getMemberInfo(principalDetails.getUsername());

        return ResponseDTO.toResponseEntity(ResponseEnum.FIND_MEMBERINFO_SUCCESS, memberInfoResDTO);
    }

}
