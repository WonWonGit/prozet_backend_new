package com.example.prozet.module.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.FileType;
import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.file.domain.entity.FileEntity;
import com.example.prozet.modules.file.domain.entity.FileMasterEntity;
import com.example.prozet.modules.file.repository.FileMasterRepository;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.domain.entity.MemberInfoEntity;
import com.example.prozet.modules.member.repository.MemberInfoRepository;
import com.example.prozet.modules.member.repository.MemberRepository;

@DataJpaTest
@Import(QueryDSLConfigTest.class)
@ActiveProfiles("test")
public class MemberInfoRepositoryTest {

    @Autowired
    MemberInfoRepository memberInfoRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FileMasterRepository fileMasterRepository;

    private static String username = "username";

    @BeforeEach
    public void saveMember() {
        MemberEntity memberEntity = MemberEntity.builder()
                .email("email@google.com")
                .name("name")
                .provider(Provider.GOOGLE)
                .role(Role.USER)
                .username(username)
                .build();

        memberRepository.save(memberEntity);
    }

    @BeforeEach
    public void fileMasterSave() {
        FileMasterEntity fileMasterEntity = FileMasterEntity.builder()
                .idx(1)
                .category(FileType.MEMBER_PROFILE.fileType())
                .build();

        fileMasterRepository.save(fileMasterEntity);
    }

    public MemberEntity getMemberEntity() {
        return memberRepository.findByUsername("username")
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
    }

    public FileMasterEntity getFileMasterEntity() {
        return fileMasterRepository.findByIdx(1)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_MASTER_NOT_EXIST));
    }

    public FileEntity getFileEntity() {
        return FileEntity.builder()
                .fileMaster(getFileMasterEntity())
                .extension("img")
                .originalName("filename")
                .size(200l)
                .url("url")
                .build();
    }

    @Test
    public void MemberInfoSaveWithOutFileTest() {

        MemberInfoEntity memberInfoEntity = MemberInfoEntity.builder()
                .blog("blog")
                .github("github")
                .job("job")
                .memberEntity(getMemberEntity())
                .fileMasterEntity(null)
                .build();

        MemberInfoEntity memberInfoEntityPS = memberInfoRepository.save(memberInfoEntity);
        assertThat(memberInfoEntityPS.getBlog()).isEqualTo("blog");
    }

    @Test
    public void MemberInfoSaveWithFileTest() {

        MemberInfoEntity memberInfoEntity = MemberInfoEntity.builder()
                .blog("blog")
                .github("github")
                .job("job")
                .memberEntity(getMemberEntity())
                .fileMasterEntity(getFileMasterEntity())
                .build();

        MemberInfoEntity memberInfoEntityPS = memberInfoRepository.save(memberInfoEntity);
        assertThat(memberInfoEntityPS.getFileMasterEntity().getCategory())
                .isEqualTo(FileType.MEMBER_PROFILE.fileType());
    }

    @Test
    public void FindMemberInfoByUsername() {

        MemberInfoEntity memberInfoEntity = MemberInfoEntity.builder()
                .blog("blog")
                .github("github")
                .job("job")
                .memberEntity(getMemberEntity())
                .fileMasterEntity(null)
                .build();

        memberInfoRepository.save(memberInfoEntity);

        Optional<MemberInfoEntity> memberInfoEntityPS = memberInfoRepository.findByMemberEntity_Username(username);
        assertThat(memberInfoEntityPS.get().getMemberEntity().getUsername()).isEqualTo(username);

    }

}
