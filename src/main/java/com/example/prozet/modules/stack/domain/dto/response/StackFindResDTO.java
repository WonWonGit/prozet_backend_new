package com.example.prozet.modules.stack.domain.dto.response;

import com.example.prozet.enum_pakage.StackType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StackFindResDTO {

    private Long idx;
    private String name;
    private String icon;
    private String category;

    public StackFindResDTO(Long idx, String name, String icon, String category) {
        this.idx = idx;
        this.name = name;
        this.icon = icon;
        this.category = category;
    }

}
