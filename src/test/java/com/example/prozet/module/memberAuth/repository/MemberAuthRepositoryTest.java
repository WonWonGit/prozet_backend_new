package com.example.prozet.module.memberAuth.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.memberAuth.domain.entity.MemberAuthEntity;
import com.example.prozet.modules.memberAuth.repository.MemberAuthRepository;

@DataJpaTest
@Import(QueryDSLConfigTest.class)
public class MemberAuthRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberAuthRepository memberAuthRepository;

    private static String username = "test_username";

    private static String refreshToken = "test_refreshToken";

    @BeforeEach
    public void saveRefreshToken() {

        MemberEntity memberEntity = MemberEntity.builder()
                .name("test")
                .email("test@google.com")
                .username(username)
                .provider(Provider.GOOGLE)
                .role(Role.USER)
                .build();
        memberRepository.save(memberEntity);

        MemberEntity member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User is not exist"));

        MemberAuthEntity memberAuthEntity = MemberAuthEntity.builder()
                .refreshToken(refreshToken)
                .build();

        memberAuthEntity.setMemberEntity(member);
        MemberAuthEntity memberAuthEntityPS = memberAuthRepository.save(memberAuthEntity);

        assertThat(memberAuthEntityPS.getRefreshToken()).isEqualTo(refreshToken);

    }

    @Test
    public void findRefreshTokenMemberIdxTest() {
        MemberEntity memberEntity = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User is not exist"));
        MemberAuthEntity memberAuthEntity = memberAuthRepository.findByMemberEntity_idx(memberEntity.getIdx())
                .orElseThrow(() -> new RuntimeException("User idx is not exist"));

        assertThat(memberAuthEntity.getRefreshToken()).isEqualTo(refreshToken);

    }

    @Test
    public void findRefreshTokenTest() {

        MemberAuthEntity memberAuthEntity = memberAuthRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Token is not exist"));
        assertThat(memberAuthEntity.getRefreshToken()).isEqualTo(refreshToken);

    }

    @Test
    public void updateRefreshToken() {

        String newRefresh = "new_refresh_token";

        MemberEntity memberEntity = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User is not exist"));
        Optional<MemberAuthEntity> memberAuthEntity = memberAuthRepository
                .findByMemberEntity_idx(memberEntity.getIdx());

        memberAuthEntity.get().setRefreshToken(newRefresh);

        assertThat(memberAuthEntity.get().getRefreshToken()).isEqualTo(newRefresh);

    }

}
