package com.example.prozet.module.stack.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.domain.entity.StackEntity;
import com.example.prozet.modules.stack.repository.StackCategoryRepository;
import com.example.prozet.modules.stack.repository.StackRepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfigTest.class)
public class StackRepsitoryTest {

    @Autowired
    private StackRepository stackRepository;

    @Autowired
    private StackCategoryRepository stackCategoryRepository;

    @BeforeEach
    public void stackSave() {

        StackCategoryEntity stackCategoryEntity = StackCategoryEntity.builder()
                .category("BACK END")
                .stackType(StackType.CUSTOMSTACK)
                .projectEntity(null)
                .build();

        StackCategoryEntity stackCategoryEntityPS = stackCategoryRepository.save(stackCategoryEntity);

        StackEntity stackEntity = StackEntity.builder()
                .icon("iconUrl")
                .name("spring")
                .stackType(StackType.CUSTOMSTACK)
                .stackCategory(stackCategoryEntityPS)
                .build();

        StackEntity stackEntityPS = stackRepository.save(stackEntity);
        assertThat(stackEntityPS.getName()).isEqualTo("spring");
    }

    @Test
    public void findStackByIdx() {
        StackEntity stackEntity = stackRepository.findByIdx(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.STACK_NOT_EXIST));

        assertThat(stackEntity.getName()).isEqualTo("spring");

    }

}