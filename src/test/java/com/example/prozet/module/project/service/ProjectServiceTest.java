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

    private ProjectInfoEntity getProjectInfoEntity() {
        ProjectInfoEntity projectInfoEntity = ProjectInfoEntity.builder().title("title")
                .content("content")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now()).build();

        return projectInfoEntity;
    }

    private ProjectEntity getProjectEntity() {
        ProjectEntity projectEntity = ProjectEntity.builder().projectInformation(getProjectInfoEntity())
                .owner(getMemberEntity())
                .projectKey("projectKey").build();

        return projectEntity;
    }

    @Test
    public void createProjectTest() {

        ProjectInfoReqDTO projectInfoReqDTO = ProjectInfoReqDTO.builder().title("title")
                .startDate(LocalDateTime.now()).endDate(LocalDateTime.now()).build();

        when(projectRepository.save(any())).thenReturn(getProjectEntity());
        when(projectInfoRepository.save(any())).thenReturn(getProjectInfoEntity());

        ProjectResDTO projectResDTO = projectService.createProject(getMemberEntity(), projectInfoReqDTO, null);

        assertThat(projectResDTO.getProjectKey()).isEqualTo("projectKey");
        assertThat(projectResDTO.getProjectInfoResDTO().getTitle()).isEqualTo("title");

    }

    @Test
    public void editProjectMemberAccessTest() {
    }

    @Test
    public void deleteProjectMemberTest() {
    }

}
