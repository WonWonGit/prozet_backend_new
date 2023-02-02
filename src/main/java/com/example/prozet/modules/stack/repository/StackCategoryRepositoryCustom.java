package com.example.prozet.modules.stack.repository;

import java.util.List;

import com.example.prozet.modules.stack.domain.dto.response.StackCategoryFindResDTO;

public interface StackCategoryRepositoryCustom {

    List<StackCategoryFindResDTO> getStackCategory(String projectKey);

}
