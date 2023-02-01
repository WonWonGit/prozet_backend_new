package com.example.prozet.modules.stack.domain.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ManyToAny;

import com.example.prozet.enum_pakage.Role;
import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StackCategoryResDTO {

    private int idx;
    private String category;
    private StackType stackType;
    private ProjectResDTO projectResDTO;

    public StackCategoryEntity toEntity() {
        return StackCategoryEntity.builder()
                .idx(idx)
                .category(category)
                .projectEntity(projectResDTO != null ? projectResDTO.toEntity() : null)
                .stackType(stackType)
                .build();
    }

}
