package com.example.prozet.module.project.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.project.domain.dto.response.ProjectListDTO;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

@DataJpaTest
@Import(QueryDSLConfigTest.class)
public class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    JPAQueryFactory query;

    private MemberEntity getMemberEntity() {
        return MemberEntity.builder().name("name").email("email@google.com")
                .provider(Provider.GOOGLE)
                .role(Role.USER).username("username").build();
    }

    @Test
    public void projectSaveTest() {
        ProjectEntity projectEntity = ProjectEntity.builder().owner(getMemberEntity())
                .deleteYn("N")
                .projectKey("projectKey").build();
        ProjectEntity projectEntityPS = projectRepository.save(projectEntity);
        assertThat(projectEntityPS.getProjectKey()).isEqualTo("projectKey");
    }

    @Test
    public void findProjectListTest() {

        List<ProjectListDTO> results = projectRepository.getProjectList("username");
        assertThat(results.size()).isEqualTo(0);

    }

}
