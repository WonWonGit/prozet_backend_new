package com.example.prozet.module.projectInformation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.modules.projectInformation.domain.entity.ProjectInfoEntity;
import com.example.prozet.modules.projectInformation.repository.ProjectInfoRepository;

@DataJpaTest
@Import(QueryDSLConfigTest.class)
public class ProjectInfoRepositoryTest {

    @Autowired
    ProjectInfoRepository projectInfoRepository;

    @Test
    public void projectInfoSaveTest() {

        ProjectInfoEntity projectInfoEntity = ProjectInfoEntity.builder()
                .title("title")
                .content("content")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        ProjectInfoEntity projectInfoEntityPS = projectInfoRepository.save(projectInfoEntity);
        assertThat(projectInfoEntityPS.getTitle()).isEqualTo("title");

    }

}
