package com.example.prozet.modules.member.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.common.ErrorCode;
import com.example.prozet.common.ErrorResponse;
import com.example.prozet.common.ResponseDTO;
import com.example.prozet.common.ResponseEnum;
import com.example.prozet.modules.member.domain.dto.request.MemberInfoSaveReqDTO;
import com.example.prozet.modules.member.domain.dto.request.MemberInfoUpdateDTO;
import com.example.prozet.modules.member.domain.dto.request.MemberRequireDTO;
import com.example.prozet.modules.member.domain.dto.response.MemberInfoResDTO;
import com.example.prozet.modules.member.service.MemberInfoService;
import com.example.prozet.security.auth.PrincipalDetails;

@RestController
@RequestMapping("v1/api/member")
public class MemberApiController {

    @Autowired
    private MemberInfoService memberInfoService;

    @PostMapping("/information")
    public ResponseEntity<?> save(@Valid @RequestPart(name = "memberInfo") MemberInfoSaveReqDTO memberInfoSaveReqDTO,
            @RequestPart(name = "imgProfile", required = false) MultipartFile imgFile,
            Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        MemberInfoResDTO memberInfoDTO = memberInfoService.saveMemberInfo(memberInfoSaveReqDTO,
                principalDetails.getUsername(), imgFile);

        if (memberInfoDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.SAVE_MEMBERINFO_FAIL);
        }

        return ResponseDTO.toResponseEntity(ResponseEnum.SAVE_MEMBERINFO_SUCCESS, memberInfoDTO);

    }

    @PutMapping("/information/{username}")
    public ResponseEntity<?> memberInformationUpdate(@PathVariable String username,
            Authentication authentication,
            @Valid @RequestPart(name = "memberRequireInfo") MemberRequireDTO memberRequireDTO,
            @RequestPart(name = "memberInfo") MemberInfoUpdateDTO memberUpdateDTO,
            @RequestPart(name = "imgProfile", required = false) MultipartFile imgFile) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (!principalDetails.getUsername().equals(username)) {
            return ErrorResponse.toResponseEntity(ErrorCode.FORBIDDEN);
        }

        MemberInfoResDTO memberInfoResDTO = memberInfoService.memberInfoUpdate(memberRequireDTO, memberUpdateDTO,
                principalDetails.getUsername(), imgFile);

        if (memberInfoResDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.MEMBER_INFO_UPDATE_FAIL);
        }

        return ResponseDTO.toResponseEntity(ResponseEnum.UPDATE_MEMBERINFO_SUCCESS, memberInfoResDTO);
    }

}
