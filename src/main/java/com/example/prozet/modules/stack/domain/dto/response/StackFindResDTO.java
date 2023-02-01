package com.example.prozet.modules.stack.domain.dto.response;

import com.example.prozet.enum_pakage.StackType;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class StackFindResDTO {

    private Long idx;
    private String name;
    private String icon;
    private int categoryIdx;
    private String category;

    public StackFindResDTO(Long idx, String name, String icon, int categoryIdx, String category) {
        this.idx = idx;
        this.name = name;
        this.icon = icon;
        this.categoryIdx = categoryIdx;
        this.category = category;
    }

}
