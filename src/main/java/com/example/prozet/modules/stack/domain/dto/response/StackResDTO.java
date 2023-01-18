package com.example.prozet.modules.stack.domain.dto.response;

import javax.persistence.Access;

import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.stack.domain.entity.StackEntity;

import antlr.collections.Stack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StackResDTO {

    private Long idx;
    private String name;
    private String icon;
    private Role role;
    private StackCategoryResDTO stackCategory;

    public StackEntity toEntity() {
        return StackEntity.builder()
                .idx(idx)
                .name(name)
                .icon(icon)
                .role(role)
                .stackCategory(stackCategory.toEntity())
                .build();
    }

}
