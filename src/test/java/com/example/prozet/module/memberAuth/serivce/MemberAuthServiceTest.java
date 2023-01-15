package com.example.prozet.module.memberAuth.serivce;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.memberAuth.domain.dto.request.MemberAuthReqDto;
import com.example.prozet.modules.memberAuth.domain.dto.response.MemberAuthResDto;
import com.example.prozet.modules.memberAuth.domain.entity.MemberAuthEntity;
import com.example.prozet.modules.memberAuth.repository.MemberAuthRepository;
import com.example.prozet.modules.memberAuth.service.MemberAuthService;


@ExtendWith(MockitoExtension.class)
public class MemberAuthServiceTest {

    @InjectMocks
    private MemberAuthService memberAuthService;

    @Mock
    MemberAuthRepository memberAuthRepository;

    @Mock
    MemberRepository memberRepository;


    private MemberEntity getMemberEntity(){
        MemberEntity memberEntity = MemberEntity.builder()
                                                .name("test")
                                                .email("test@google.com")
                                                .username("username")
                                                .provider(Provider.GOOGLE)
                                                .role(Role.ADMIN)
                                                .build();
        return memberEntity;                                        
    }


    @Test
    public void saveRefreshToken(){
        
        MemberAuthReqDto memberAuthReqDto = MemberAuthReqDto.builder().refreshToken("refresh").build();

        MemberEntity memberEntity = getMemberEntity();
        memberEntity.setIdx(1l);

        MemberAuthEntity memberAuthEntity = memberAuthReqDto.toEntity();
        memberAuthEntity.setIdx(1l);
        memberAuthEntity.setMemberEntity(memberEntity);

        //stub
        when(memberAuthRepository.save(any())).thenReturn(memberAuthEntity);
        
        MemberAuthResDto memberAuthResDto = memberAuthService.saveRefreshToken(memberAuthReqDto, memberEntity);

        assertThat(memberAuthResDto.getRefreshToken()).isEqualTo(memberAuthReqDto.getRefreshToken());
        assertThat(memberAuthResDto.getMemberIdx()).isEqualTo(1l);
        
    } 

    @Test
    public void updateRefreshToken(){

        Long idx = 1L;

        MemberEntity memberEntity = getMemberEntity();
        memberEntity.setIdx(idx);

        MemberAuthReqDto memberAuthReqDto = MemberAuthReqDto.builder().refreshToken("refresh_new").build();

        //stub
        MemberAuthEntity memberAuthEntity = MemberAuthEntity.builder().idx(idx).refreshToken("refresh").memberEntity(memberEntity).build();
        Optional<MemberAuthEntity> memberAuthOP = Optional.of(memberAuthEntity);
        when(memberAuthRepository.findByMemberEntity_idx(idx)).thenReturn(memberAuthOP);

        MemberAuthResDto memberAuthResDto = memberAuthService.updateRefreshToken(memberAuthReqDto, memberAuthOP.get().getMemberEntity());

        assertThat(memberAuthResDto.getRefreshToken()).isEqualTo("refresh_new");

        
    }
    
}
