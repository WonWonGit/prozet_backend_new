package com.example.prozet.module.file.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.config.AwsConfigTest;
import com.example.prozet.enum_pakage.FileType;
import com.example.prozet.modules.file.domain.dto.response.FileMasterDTO;
import com.example.prozet.modules.file.domain.entity.FileEntity;
import com.example.prozet.modules.file.domain.entity.FileMasterEntity;
import com.example.prozet.modules.file.repository.FileMasterRepository;
import com.example.prozet.modules.file.repository.FileRepository;
import com.example.prozet.modules.file.service.FileService;
import com.example.prozet.modules.file.service.S3Service;

@Import(AwsConfigTest.class)
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

        @InjectMocks
        FileService fileService;

        @Mock
        S3Service s3Service;

        @Mock
        FileRepository fileRepository;

        @Mock
        FileMasterRepository fileMasterRepository;

        String path = "src/test/resources/rocketlauncher.png";
        String fileName = "rocketlauncher";
        String contentType = "png";

        @Test
        @DisplayName("file save 테스트")
        @Transactional
        public void fileSaveTest() throws IOException {

                String testUrl = "https://prozet.s3/member/rocketlauncher.png";

                String fileKey = testUrl.substring(testUrl.lastIndexOf("/") + 1, testUrl.length());

                FileInputStream fileInputStream = new FileInputStream(new File(path));
                MultipartFile file = new MockMultipartFile(fileName, fileName + "." + contentType, contentType,
                                fileInputStream);

                FileMasterEntity fileMasterEntity = FileMasterEntity.builder()
                                .category(FileType.MEMBER_PROFILE.fileType())
                                .build();

                FileEntity fileEntity = FileEntity.builder().fileMaster(fileMasterEntity)
                                .extension(contentType)
                                .originalName(fileName)
                                .size(100l)
                                .url("test url")
                                .deleteYn("N")
                                .fileKey(FileType.MEMBER_PROFILE.bucketName() + fileKey)
                                .build();

                when(fileMasterRepository.save(any())).thenReturn(fileMasterEntity);
                when(fileRepository.save(any())).thenReturn(fileEntity);
                when(s3Service.uploadS3File(file, FileType.MEMBER_PROFILE)).thenReturn(testUrl);

                FileMasterDTO savedFileMasterDTO = fileService.fileSave(FileType.MEMBER_PROFILE, file);

                assertThat(savedFileMasterDTO.getFileList().get(0).getOriginalName()).isEqualTo(fileName);
                assertThat(savedFileMasterDTO.getFileList().get(0).getFileKey()).isEqualTo("member/rocketlauncher.png");

        }

}
