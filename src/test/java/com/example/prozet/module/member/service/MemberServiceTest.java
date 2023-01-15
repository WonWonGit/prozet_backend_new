package com.example.prozet.module.member.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.member.domain.dto.request.MemberReqDTO;
import com.example.prozet.modules.member.domain.dto.response.MemberResDTO;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.member.service.MemberService;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Test
    @Transactional
    public void saveMemberTest() {

        MemberReqDTO memberReqSaveDto = MemberReqDTO.builder()
                .idx(1l)
                .name("test")
                .email("test@google.com")
                .username("username")
                .provider(Provider.GOOGLE)
                .role(Role.USER)
                .build();

        MemberEntity memberEntity = memberReqSaveDto.toEntity();

        when(memberRepository.save(any())).thenReturn(memberEntity);

        MemberResDTO memberResDto = memberService.saveMember(memberReqSaveDto);

        assertThat(memberResDto.getUsername()).isEqualTo("username");

    }

}
