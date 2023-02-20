package com.example.prozet.module.projectStack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.projectStack.domain.dto.response.ProjectStackResDTO;
import com.example.prozet.modules.projectStack.domain.dto.response.ProjectStackUnmappedResDTO;
import com.example.prozet.modules.projectStack.domain.entity.ProjectStackEntity;
import com.example.prozet.modules.projectStack.repository.ProjectStackRepository;
import com.example.prozet.modules.projectStack.service.ProjectStackService;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.domain.entity.StackEntity;
import com.example.prozet.modules.stack.repository.StackRepository;

@ExtendWith(MockitoExtension.class)
public class ProjectStackServiceTest {

        @InjectMocks
        private ProjectStackService projectStackService;

        @Mock
        private ProjectStackRepository projectStackRepository;

        @Mock
        private StackRepository stackRepository;

        @Test
        public void saveProjectStackTest() {

                StackEntity stackEntity = getStackEntity();
                ProjectStackEntity projectStackEntity = getProjectStackEntity();

                when(stackRepository.findByIdx(any())).thenReturn(Optional.of(stackEntity));
                when(projectStackRepository.save(any())).thenReturn(projectStackEntity);

                ProjectStackUnmappedResDTO projectStackResDTO = projectStackService.saveProjectStack(
                                1L,
                                getProjectEntity().toProjectResDTO());

                assertThat(projectStackResDTO.getCheckedYn()).isEqualTo("Y");
                assertThat(projectStackResDTO.getStackUnmappedResDTO().getName()).isEqualTo("spring");

        }

        @Test
        public void editProjectStackTest() {

                ProjectStackEntity projectStackEntity = getProjectStackEntity();

                when(projectStackRepository.findByIdx(anyLong())).thenReturn(Optional.of(projectStackEntity));

                ProjectStackUnmappedResDTO projectStackUnmappedResDTO = projectStackService.editProjectStack(1L);

                assertThat(projectStackUnmappedResDTO.getCheckedYn()).isEqualTo("N");

        }

        @Test
        public void findProjectStackTest() {

                ProjectStackEntity projectStackEntity = getProjectStackEntity();

                when(projectStackRepository.findByStackEntity_IdxAndProjectEntity_ProjectKey(any(), any()))
                                .thenReturn(Optional.of(projectStackEntity));

                ProjectStackResDTO projectStackResDTO = projectStackService.findProjectStack(1L, "projectKey");

                assertThat(projectStackResDTO.getProjctResDTO().getProjectKey()).isEqualTo("projectKey");

        }

        // ********** Create Model ***********/
        public ProjectEntity getProjectEntity() {

                ProjectEntity projectEntity = ProjectEntity.builder()
                                .projectKey("projectKey")
                                .projectInformation(null)
                                .build();

                return projectEntity;
        }

        public StackEntity getStackEntity() {
                StackCategoryEntity stackCategoryEntity = StackCategoryEntity.builder().category("back end")
                                .stackType(StackType.DEFAULTSTACK).build();

                StackEntity stackEntity = StackEntity.builder().name("spring")
                                .stackCategory(stackCategoryEntity)
                                .stackType(StackType.DEFAULTSTACK).build();

                return stackEntity;
        }

        public ProjectStackEntity getProjectStackEntity() {
                ProjectStackEntity projectStackEntity = ProjectStackEntity.builder()
                                .projectEntity(getProjectEntity())
                                .stackEntity(getStackEntity())
                                .checkedYn("Y")
                                .build();

                return projectStackEntity;
        }

}
