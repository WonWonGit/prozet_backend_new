package com.example.prozet.modules.member.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.prozet.modules.member.domain.dto.request.MemberReqDTO;
import com.example.prozet.modules.member.domain.dto.response.MemberResDTO;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public MemberResDTO saveMember(MemberReqDTO memberReqSaveDto) {

        Optional<MemberEntity> memberEntity = memberRepository.findByUsername(memberReqSaveDto.getUsername());

        if (memberEntity.isPresent()) {
            return memberEntity.get().toMemberResDto();
        } else {
            MemberEntity memberEntityPS = memberRepository.save(memberReqSaveDto.toEntity());
            return memberEntityPS.toMemberResDto();
        }
    }

}
