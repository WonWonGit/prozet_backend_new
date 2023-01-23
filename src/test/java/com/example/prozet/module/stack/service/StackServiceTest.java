package com.example.prozet.module.stack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.utils.ProjectUtil;
import com.example.prozet.modules.stack.domain.dto.request.StackReqDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackResDTO;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.domain.entity.StackEntity;
import com.example.prozet.modules.stack.repository.StackCategoryRepository;
import com.example.prozet.modules.stack.repository.StackRepository;
import com.example.prozet.modules.stack.service.StackService;

@ExtendWith(MockitoExtension.class)

public class StackServiceTest {

    @InjectMocks
    private StackService stackService;

    @Mock
    private StackRepository stackRepository;

    @Mock
    private StackCategoryRepository stackCategoryRepository;

    @Test
    public void stackSaveTest() {

        MemberEntity owner = MemberEntity.builder().username("username").build();

        ProjectEntity projectEntity = ProjectEntity.builder().idx(1L).projectKey("projectKey").owner(owner).build();

        StackCategoryEntity stackCategoryEntity = StackCategoryEntity.builder()
                .category("backend")
                .projectEntity(null)
                .stackType(StackType.DEFAULTSTACK)
                .build();

        StackEntity stackEntity = StackEntity.builder().icon("iconUrl").name("stack")
                .stackCategory(stackCategoryEntity).projectEntity(projectEntity)
                .stackType(StackType.CUSTOMSTACK)
                .build();

        when(stackCategoryRepository.findByIdx(anyInt())).thenReturn(Optional.of(stackCategoryEntity));
        when(stackRepository.save(any())).thenReturn(stackEntity);

        StackReqDTO stackReqDTO = StackReqDTO.builder().StackCategoryIdx(1).iconUrl("iconUrl").name("stack").build();

        StackResDTO stackResDTO = stackService.saveStack(stackReqDTO, null);

        assertThat(stackResDTO.getIcon()).isEqualTo("iconUrl");
    }

}
