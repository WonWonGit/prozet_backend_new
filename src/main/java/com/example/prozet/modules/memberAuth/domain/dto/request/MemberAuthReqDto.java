package com.example.prozet.modules.memberAuth.domain.dto.request;

import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.memberAuth.domain.entity.MemberAuthEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberAuthReqDto {
    private String refreshToken;
    
    public MemberAuthEntity toEntity(){
        return MemberAuthEntity.builder()
        .refreshToken(refreshToken)
        .build();
    }
}
