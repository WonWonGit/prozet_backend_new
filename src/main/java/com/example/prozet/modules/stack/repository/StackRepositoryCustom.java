package com.example.prozet.modules.stack.repository;

import java.util.List;

import com.example.prozet.modules.stack.domain.dto.response.StackFindResDTO;

public interface StackRepositoryCustom {
    List<StackFindResDTO> getStackList(String projectKey);
}
