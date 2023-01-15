package com.example.prozet.module.member.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.enum_pakage.FileType;
import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.file.domain.dto.response.FileMasterDTO;
import com.example.prozet.modules.file.domain.entity.FileEntity;
import com.example.prozet.modules.file.domain.entity.FileMasterEntity;
import com.example.prozet.modules.file.repository.FileMasterRepository;
import com.example.prozet.modules.file.repository.FileRepository;
import com.example.prozet.modules.file.service.FileService;
import com.example.prozet.modules.member.domain.dto.request.MemberInfoSaveReqDTO;
import com.example.prozet.modules.member.domain.dto.request.MemberInfoUpdateDTO;
import com.example.prozet.modules.member.domain.dto.request.MemberRequireDTO;
import com.example.prozet.modules.member.domain.dto.response.MemberInfoResDTO;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.domain.entity.MemberInfoEntity;
import com.example.prozet.modules.member.repository.MemberInfoRepository;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.member.service.MemberInfoService;

@ExtendWith(MockitoExtension.class)
public class MemberInfoServiceTest {

    @InjectMocks
    MemberInfoService memberInfoService;

    @Mock
    FileService fileService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    MemberInfoRepository memberInfoRepository;

    @Mock
    FileRepository fileRepository;

    @Mock
    FileMasterRepository fileMasterRepository;

    private static String username = "username";

    String path = "src/test/resources/rocketlauncher.png";
    String fileName = "rocketlauncher";
    String contentType = "png";

    String newPath = "src/test/resources/spaceship.png";
    String newFileName = "spaceship";

    @Test
    @Transactional
    public void saveMemberInfoTestWithoutFile() {

        MemberInfoSaveReqDTO memberInfoSaveReqDTO = getMemberInfoReqDTO();

        MemberInfoEntity memberInfoEntity = memberInfoSaveReqDTO.toEntity();

        memberInfoEntity.setMemberEntity(getMemberEntity());

        when(memberInfoRepository.save(any())).thenReturn(memberInfoEntity);
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(getMemberEntity()));

        MemberInfoResDTO memberInfoResDto = memberInfoService.saveMemberInfo(memberInfoSaveReqDTO, username, null);

        assertThat(memberInfoResDto.getFileMaster()).isEqualTo(null);
        assertThat(memberInfoResDto.getGithub()).isEqualTo("git");

    }

    /*
     * 1. member save without file
     * 2. update member info without file
     */
    @Test
    @Transactional
    public void memberInfoUpdateWithoutFile() {

        MemberRequireDTO requireDTO = getRequireDTO();
        MemberInfoUpdateDTO updateDTO = MemberInfoUpdateDTO.builder().blog("blog2").build();

        MemberInfoEntity memberInfoEntity = getMemberInfoEntityWithoutFile();

        MemberEntity memberEntity = getMemberEntity();
        memberEntity.updateMember(requireDTO.getEmail(), requireDTO.getName());

        memberInfoEntity.saveJoinEntity(null, memberEntity);

        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(memberEntity));
        when(memberInfoRepository.findByMemberEntity_Username(username)).thenReturn(Optional.of(memberInfoEntity));

        MemberInfoResDTO result = memberInfoService.memberInfoUpdate(requireDTO, updateDTO, username, null);

        assertThat(result.getBlog()).isEqualTo("blog2");
        assertThat(result.getMember().getName()).isEqualTo("name2");

    }

    /*
     * 1. member save without file -> file master is not exist
     * 2. update member info with file
     */
    @Test
    @Transactional
    public void memberInfoUpdateWithFile() throws FileNotFoundException, IOException {

        MemberRequireDTO requireDTO = getRequireDTO();
        MemberInfoUpdateDTO updateDTO = MemberInfoUpdateDTO.builder().blog("blog2").build();

        MemberInfoEntity memberInfoEntity = getMemberInfoEntityWithoutFile();

        MemberEntity memberEntity = getMemberEntity();
        memberEntity.updateMember(requireDTO.getEmail(), requireDTO.getName());

        FileMasterEntity fileMasterEntity = getFileMasterEntity();
        fileMasterEntity.setFileList(List.of(getFileEntity()));
        FileMasterDTO fileMasterDTO = fileMasterEntity.toFileMasterDTO();

        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(memberEntity));
        when(memberInfoRepository.findByMemberEntity_Username(username)).thenReturn(Optional.of(memberInfoEntity));
        when(fileService.fileSave(any(), any())).thenReturn(fileMasterDTO);

        MemberInfoResDTO result = memberInfoService.memberInfoUpdate(requireDTO, updateDTO, username, getFile());

        assertThat(result.getFileMaster().getFileList().get(0).getDeleteYn()).isEqualTo("N");
        assertThat(result.getFileMaster().getFileList().get(0).getUrl()).isEqualTo("urltest");

    }

    /*
     * 1. member save with file
     * 2. update member info without file
     */
    @Test
    @Transactional
    public void memberInfoUpdateOnlyInfo() {

        MemberRequireDTO requireDTO = getRequireDTO();
        MemberInfoUpdateDTO updateDTO = MemberInfoUpdateDTO.builder().fileIdx(0).blog("blog3").build();
        FileMasterDTO fileMasterDTO = getFileMasterEntity().toFileMasterDTO();
        fileMasterDTO.setFileList(List.of(getFileEntity().toFileDTO()));

        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(getMemberEntity()));
        when(memberInfoRepository.findByMemberEntity_Username(username))
                .thenReturn(Optional.of(getMemberInfoEntityWithFile()));

        MemberInfoResDTO result = memberInfoService.memberInfoUpdate(requireDTO, updateDTO, username, null);

        assertThat(result.getBlog()).isEqualTo("blog3");
        assertThat(result.getFileMaster().getFileList().get(0).getUrl()).isEqualTo("urltest");
    }

    /*
     * 1. member save with file -> file matser is exist
     * 2. update member info with file
     */
    @Test
    @Transactional
    public void memberInfoUpdateWithProfile() throws IOException {

        MemberRequireDTO requireDTO = getRequireDTO();
        MemberInfoUpdateDTO updateDTO = MemberInfoUpdateDTO.builder().fileIdx(1).blog("blog3").build();
        MemberInfoEntity memberInfoEntity = getMemberInfoEntityWithFile();

        FileMasterDTO fileMaster = memberInfoEntity.getFileMasterEntity().toFileMasterDTO();
        fileMaster.setFileList(List.of(getNewFileEntity().toFileDTO()));

        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(getMemberEntity()));
        when(memberInfoRepository.findByMemberEntity_Username(username)).thenReturn(Optional.of(memberInfoEntity));
        when(fileService.fileUpdate(any(), any(), anyInt())).thenReturn(fileMaster);

        MemberInfoResDTO memberInfoResDTO = memberInfoService.memberInfoUpdate(requireDTO, updateDTO, username,
                getNewFile());

        assertThat(memberInfoResDTO.getFileMaster().getFileList().get(0).getOriginalName()).isEqualTo(newFileName);

    }

    private MemberEntity getMemberEntity() {
        return MemberEntity.builder()
                .idx(1l)
                .name("test")
                .provider(Provider.GOOGLE)
                .role(Role.USER)
                .email("test@gmail.com")
                .username(username)
                .build();
    }

    private MemberInfoEntity getMemberInfoEntityWithoutFile() {
        return MemberInfoEntity.builder().blog("blog")
                .fileMasterEntity(null)
                .build();
    }

    private MemberInfoEntity getMemberInfoEntityWithFile() {

        FileMasterEntity fileMasterEntity = getFileMasterEntity();
        fileMasterEntity.setFileList(List.of(getFileEntity()));

        return MemberInfoEntity.builder()
                .blog("blog")
                .fileMasterEntity(fileMasterEntity)
                .memberEntity(getMemberEntity())
                .build();
    }

    private MemberInfoSaveReqDTO getMemberInfoReqDTO() {
        return MemberInfoSaveReqDTO.builder()
                .blog("blog")
                .github("git")
                .job("job")
                .build();
    }

    private FileMasterEntity getFileMasterEntity() {
        return FileMasterEntity.builder().idx(1)
                .category(FileType.MEMBER_PROFILE.fileType())
                .build();
    }

    private FileEntity getFileEntity() {
        return FileEntity.builder().deleteYn("N")
                .fileMaster(getFileMasterEntity())
                .extension(contentType)
                .originalName(fileName)
                .idx(1)
                .url("urltest")
                .build();
    }

    private FileEntity getNewFileEntity() {
        return FileEntity.builder().deleteYn("N")
                .fileMaster(getFileMasterEntity())
                .extension(contentType)
                .originalName("spaceship")
                .idx(2)
                .url("urltest2")
                .build();
    }

    private MemberRequireDTO getRequireDTO() {
        return MemberRequireDTO.builder().email("test@gmail.com").name("name2").build();
    }

    private MultipartFile getFile() throws IOException {

        FileInputStream fileInputStream = new FileInputStream(new File(path));
        MultipartFile file = new MockMultipartFile(fileName, fileName + "." + contentType, contentType,
                fileInputStream);

        return file;
    }

    private MultipartFile getNewFile() throws IOException {

        FileInputStream fileInputStream = new FileInputStream(new File("src/test/resources/spaceship.png"));
        MultipartFile file = new MockMultipartFile(newFileName, newFileName + "." + contentType, contentType,
                fileInputStream);

        return file;
    }

}
