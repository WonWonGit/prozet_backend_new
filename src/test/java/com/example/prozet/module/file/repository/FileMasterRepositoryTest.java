package com.example.prozet.module.file.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.FileType;
import com.example.prozet.modules.file.domain.entity.FileMasterEntity;
import com.example.prozet.modules.file.repository.FileMasterRepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@DataJpaTest
@Import(QueryDSLConfigTest.class)
public class FileMasterRepositoryTest {

    @Autowired
    private FileMasterRepository fileMasterRepository;

    @BeforeEach
    public void FileMasterSaveTest() {

        FileMasterEntity fileMasterEntity = FileMasterEntity.builder().category(FileType.MEMBER_PROFILE.fileType())
                .build();
        fileMasterRepository.save(fileMasterEntity);

    }

    @Test
    // @Sql("classpath:db/tableInit.sql")
    public void FileMasterFindByIdxTest() {

        FileMasterEntity fileMasterEntity = fileMasterRepository.findByIdx(1)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_MASTER_NOT_EXIST));

        assertThat(fileMasterEntity.getCategory()).isEqualTo(FileType.MEMBER_PROFILE.fileType());

    }

}
