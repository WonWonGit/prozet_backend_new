package com.example.prozet.modules.memberAuth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.memberAuth.domain.dto.request.MemberAuthReqDto;
import com.example.prozet.modules.memberAuth.domain.dto.response.MemberAuthResDto;
import com.example.prozet.modules.memberAuth.domain.entity.MemberAuthEntity;
import com.example.prozet.modules.memberAuth.repository.MemberAuthRepository;

@Service
@Transactional(readOnly = true)
public class MemberAuthService {

    @Autowired
    private MemberAuthRepository memberauthRepository;

    @Transactional
    public MemberAuthResDto saveRefreshToken(MemberAuthReqDto memberAuthReqDto, MemberEntity memberEntity) {

        MemberAuthEntity memberAuthEntity = memberAuthReqDto.toEntity();
        memberAuthEntity.setMemberEntity(memberEntity);

        MemberAuthResDto memberAuthResDto = memberauthRepository.save(memberAuthEntity).toDto();

        return memberAuthResDto;
    }

    @Transactional
    public MemberAuthResDto updateRefreshToken(MemberAuthReqDto memberAuthReqDto, MemberEntity memberEntity) {

        Optional<MemberAuthEntity> memberAuthEntity = memberauthRepository
                .findByMemberEntity_idx(memberEntity.getIdx());

        if (memberAuthEntity.isPresent()) {
            MemberAuthEntity memberAuthEntityPS = memberAuthEntity.get();
            memberAuthEntityPS.memberAuthUpdate(memberAuthReqDto.getRefreshToken());
            return memberAuthEntityPS.toDto();
        } else {
            return null;
        }

    }
}
