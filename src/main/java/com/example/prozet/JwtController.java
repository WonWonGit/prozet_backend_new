package com.example.prozet;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.prozet.common.ResponseDTO;
import com.example.prozet.common.ResponseEnum;
import com.example.prozet.enum_pakage.OauthAttribute;
import com.example.prozet.modules.member.domain.dto.request.MemberReqDTO;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.member.service.MemberService;
import com.example.prozet.modules.memberAuth.domain.dto.request.MemberAuthReqDto;
import com.example.prozet.modules.memberAuth.domain.entity.MemberAuthEntity;
import com.example.prozet.modules.memberAuth.repository.MemberAuthRepository;
import com.example.prozet.modules.memberAuth.service.MemberAuthService;
import com.example.prozet.security.auth.PrincipalDetails;
import com.example.prozet.security.jwt.JwtProvider;

@RestController
public class JwtController {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberAuthRepository memberAuthRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberAuthService memberAuthService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/login/{provider}")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> data,
            @PathVariable String provider,
            HttpServletResponse response) {

        MemberReqDTO memberEntity = OauthAttribute.extract(provider, data);

        MemberEntity memberEntityPs = memberService.saveMember(memberEntity).toEntity();

        PrincipalDetails principalDetails = new PrincipalDetails(memberEntityPs);

        String refreshToken = jwtProvider.createRefreshToken(principalDetails);
        String accessToken = jwtProvider.createAccessToken(principalDetails);

        MemberAuthReqDto memberAuthReqDto = MemberAuthReqDto.builder().refreshToken(refreshToken).build();

        Optional<MemberAuthEntity> memberAuthEntity = memberAuthRepository
                .findByMemberEntity_idx(memberEntityPs.getIdx());

        if (memberAuthEntity.isEmpty()) {

            memberAuthService.saveRefreshToken(memberAuthReqDto, memberEntityPs);

        } else {

            memberAuthService.updateRefreshToken(memberAuthReqDto, memberEntityPs);

        }

        jwtProvider.setHeaderAccessToken(response, accessToken);
        jwtProvider.setHeaderRefreshToken(response, refreshToken);

        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<?> successResponse = new ResponseEntity<>(
                ResponseDTO.toResponseEntity(ResponseEnum.CREATE_TOKEN_SUCCESS, null), headers, HttpStatus.OK);

        return successResponse;
    }

}
