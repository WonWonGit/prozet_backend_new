package com.example.prozet.modules.stack.domain.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotNull(message = "Stack Category is require.")
    private int StackCategoryIdx;

}
