package com.example.prozet.modules.stack.repository;

import java.util.List;

import com.example.prozet.modules.stack.domain.dto.response.StackCategoryResDTO;

public interface StackCategoryRepositoryCustom {

    List<StackCategoryResDTO> getStackCategory(String projectKey);

}
