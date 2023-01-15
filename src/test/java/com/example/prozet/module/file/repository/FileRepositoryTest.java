package com.example.prozet.module.file.repository;

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
import com.example.prozet.modules.file.domain.entity.FileEntity;
import com.example.prozet.modules.file.domain.entity.FileMasterEntity;
import com.example.prozet.modules.file.repository.FileMasterRepository;
import com.example.prozet.modules.file.repository.FileRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDSLConfigTest.class)
@ActiveProfiles("test")
public class FileRepositoryTest {

        @Autowired
        private FileMasterRepository fileMasterRepository;

        @Autowired
        private FileRepository fileRepository;

        private FileEntity getFileEntity() {

                FileMasterEntity fileMasterEntity = FileMasterEntity.builder().idx(1)
                                .category(FileType.MEMBER_PROFILE.fileType())
                                .build();

                return FileEntity.builder().extension("img/jpg")
                                .originalName("test")
                                .size(100l)
                                .url("file url")
                                .fileKey("fileKey")
                                .deleteYn("N")
                                .fileMaster(fileMasterEntity)
                                .build();
        }

        @BeforeEach
        public void fileSave() {

                FileMasterEntity fileMasterEntity = FileMasterEntity.builder()
                                .category(FileType.MEMBER_PROFILE.fileType())
                                .build();

                FileMasterEntity fileMasterEntityPS = fileMasterRepository.save(fileMasterEntity);

                FileEntity fileEntity = FileEntity.builder().extension("img/jpg")
                                .originalName("test")
                                .size(100l)
                                .url("file url")
                                .fileKey("fileKey")
                                .deleteYn("N")
                                .fileMaster(fileMasterEntityPS)
                                .build();

                FileEntity fileEntityPS = fileRepository.save(fileEntity);

                assertThat(fileEntityPS.getOriginalName()).isEqualTo("test");
                assertThat(fileEntityPS.getFileMaster().getIdx()).isEqualTo(1);
        }

        @Test
        // @Sql("classpath:db/tableInit.sql")
        public void findFileByMasterIdx() {

                FileEntity fileEntity = fileRepository.findByFileMaster_Idx(1)
                                .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_EXIST));

                assertThat(fileEntity.getOriginalName()).isEqualTo("test");

        }

}
