package com.example.prozet.modules.stack.domain.dto.request;

import javax.validation.constraints.NotBlank;

import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.domain.entity.StackEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StackReqDTO {

    @NotBlank(message = "Stack name is require.")
    private String name;
    private String iconUrl;
    @NotBlank(message = "Stack Category is require.")
    private int StackCategoryIdx;

    public StackEntity toEntity(String iconUrl, StackCategoryEntity stackCategoryEntity) {
        return StackEntity.builder()
                .name(name)
                .icon(iconUrl)
                .role(Role.USER)
                .stackCategory(stackCategoryEntity)
                .build();
    }

}
