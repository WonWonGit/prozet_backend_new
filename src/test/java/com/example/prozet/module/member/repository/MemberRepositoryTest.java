package com.example.prozet.module.member.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@DataJpaTest
@Import(QueryDSLConfigTest.class)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    private static String username = "username_test";

    @BeforeEach
    public void saveMember() {
        MemberEntity memberEntity = MemberEntity.builder()
                .name("test")
                .email("test@google.com")
                .username(username)
                .provider(Provider.GOOGLE)
                .role(Role.USER)
                .build();

        MemberEntity memberEntityPS = memberRepository.save(memberEntity);

        assertThat(memberEntityPS.getUsername()).isEqualTo(username);

    }

    @Test
    public void findMember() {

        MemberEntity memberEntity = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        assertThat(memberEntity.getUsername()).isEqualTo(username);
    }

    @Test
    public void updateMember() {

        String updateName = "updateName";

        MemberEntity memberEntity = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User is not exist"));
        memberEntity.setName(updateName);

        assertThat(memberEntity.getName()).isEqualTo(updateName);

    }

}
