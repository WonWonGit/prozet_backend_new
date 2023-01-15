package com.example.prozet.modules.memberAuth.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.memberAuth.domain.dto.response.MemberAuthResDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="MEMBER_AUTH")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MemberAuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToOne
    @JoinColumn(name="member_idx", referencedColumnName = "idx")
    private MemberEntity memberEntity; 

    @Column(name ="refresh_token")
    private String refreshToken;


    public MemberAuthResDto toDto(){
        return MemberAuthResDto.builder()
                    .idx(idx)
                    .refreshToken(refreshToken)
                    .memberIdx(memberEntity.getIdx())
                    .build();
    }

    public void memberAuthUpdate(String refreshToken){
        this.refreshToken = refreshToken;
    }

   
}
