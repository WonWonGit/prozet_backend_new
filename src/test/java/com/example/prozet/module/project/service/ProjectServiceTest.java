package com.example.prozet.module.project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.project.service.ProjectService;
import com.example.prozet.modules.projectInformation.domain.dto.request.ProjectInfoReqDTO;
import com.example.prozet.modules.projectInformation.domain.entity.ProjectInfoEntity;
import com.example.prozet.modules.projectInformation.repository.ProjectInfoRepository;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @InjectMocks
    ProjectService projectService;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    ProjectInfoRepository projectInfoRepository;

    private MemberEntity getMemberEntity() {
        return MemberEntity.builder().name("name").email("test@gmail").username("username").build();
    }

    private ProjectEntity getProjectEntity() {

        ProjectEntity projectEntity = ProjectEntity.builder()
                .createDate(LocalDateTime.now())
                .deleteYn("N")
                .deleteDate(null)
                .projectInformation(getProjectInfoEntity())
                .projectKey("projectKey").build();

        return projectEntity;
    }

    private ProjectInfoEntity getProjectInfoEntity() {

        ProjectInfoEntity projectInfoEntity = ProjectInfoEntity.builder()
                .title("title")
                .content("content")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        return projectInfoEntity;
    }

    @Test
    public void createProjectTest() {

        when(projectRepository.save(any())).thenReturn(getProjectEntity());

        ProjectResDTO projectResDTO = projectService.createProject(getProjectInfoEntity().toProjectInfoResDTO());

        assertThat(projectResDTO.getProjectKey()).isEqualTo("projectKey");

    }

    @Test
    public void editProjectMemberAccessTest() {
    }

    @Test
    public void deleteProjectMemberTest() {
    }

    @Test
    public void getProjectListTest() {

    }

}
