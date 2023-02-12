package com.example.prozet.module.stack.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.stack.domain.dto.response.StackFindResDTO;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.domain.entity.StackEntity;
import com.example.prozet.modules.stack.repository.StackCategoryRepository;
import com.example.prozet.modules.stack.repository.StackRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfigTest.class)
@TestMethodOrder(OrderAnnotation.class)
public class StackRepsitoryTest {

    @Autowired
    private StackRepository stackRepository;

    @Autowired
    private StackCategoryRepository stackCategoryRepository;

    @BeforeEach
    public void stackSave() {

        StackCategoryEntity stackCategoryEntity = StackCategoryEntity.builder()
                .category("BACK END")
                .stackType(StackType.DEFAULTSTACK)
                .projectEntity(null)
                .build();

        StackCategoryEntity stackCategoryEntityPS = stackCategoryRepository.save(stackCategoryEntity);

        StackEntity stackEntity = StackEntity.builder()
                .icon("iconUrl")
                .name("spring")
                .stackType(StackType.DEFAULTSTACK)
                .projectEntity(null)
                .stackCategory(stackCategoryEntityPS)
                .build();

        StackEntity stackEntityPS = stackRepository.save(stackEntity);
        assertThat(stackEntityPS.getName()).isEqualTo("spring");
    }

    @Test
    @Order(1)
    public void findStackByIdxTest() {
        StackEntity stackEntity = stackRepository.findByIdx(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.STACK_NOT_EXIST));

        assertThat(stackEntity.getName()).isEqualTo("spring");

    }

    @Test
    public void getStackList() {

        List<StackFindResDTO> stackList = stackRepository.getStackList("projectKey");

        assertThat(stackList.get(0).getName()).isEqualTo("spring");

    }

}
