package com.example.prozet.modules.member.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.member.domain.dto.response.MemberResDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MEMBER")
@Builder
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String username;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private Provider provider;
    @Enumerated(EnumType.STRING)
    private Role role;
    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(columnDefinition = "varchar(255) default 'Y'", name = "delete_yn")
    private String deleteYn;
    @Column(name = "delete_date")
    private LocalDateTime deleteDate;
    private String displayName;

    public MemberResDTO toMemberResDto() {
        return MemberResDTO.builder()
                .idx(idx)
                .username(username)
                .email(email)
                .provider(provider)
                .role(role)
                .name(name)
                .build();
    }

    public void updateMember(String email, String name) {
        this.email = email;
        this.name = name;
    }

}
