package com.example.prozet.modules.file.domain.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.prozet.enum_pakage.FileType;
import com.example.prozet.modules.file.domain.entity.FileMasterEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileMasterDTO {
    private int idx;
    
    private String category;

    private List<FileDTO> fileList;

    public FileMasterEntity toEntity(){
        return FileMasterEntity.builder()
                                .idx(idx)
                                .category(category)
                                .fileList(
                                // !fileList.isEmpty() ?
                                fileList.stream()
                                .map(FileDTO::toEntity)
                                .collect(Collectors.toList())
                                // :
                                // null
                                )
                                .build();
    }


}
