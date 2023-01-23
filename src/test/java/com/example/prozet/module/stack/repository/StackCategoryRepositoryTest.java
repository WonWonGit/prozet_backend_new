package com.example.prozet.module.stack.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.repository.StackCategoryRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfigTest.class)
public class StackCategoryRepositoryTest {

    @Autowired
    private StackCategoryRepository stackCategoryRepository;

    @BeforeEach
    public void stackCategorySave() {

        StackCategoryEntity stackCategoryEntity = StackCategoryEntity.builder()
                .category("BACK END")
                .stackType(StackType.CUSTOMSTACK)
                .projectEntity(null)
                .build();

        stackCategoryRepository.save(stackCategoryEntity);

    }

    @Test
    public void findStackCategoryByIdxTest() {
        Optional<StackCategoryEntity> stackCategoryEntity = stackCategoryRepository.findByIdx(1);

        if (stackCategoryEntity.isPresent()) {
            assertThat(stackCategoryEntity.get().getCategory()).isEqualTo("BACK END");
        } else {
            assertThat(stackCategoryEntity).isEmpty();
        }

    }

}
