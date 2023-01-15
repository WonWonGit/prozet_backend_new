package com.example.prozet.modules.member.domain.dto.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.member.domain.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberReqDTO {

    private Long idx;
    private String username;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private Provider provider;
    @Enumerated(EnumType.STRING)
    private Role role;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .idx(idx)
                .email(email)
                .name(name)
                .username(username)
                .provider(provider)
                .role(role)
                .build();
    }

}
