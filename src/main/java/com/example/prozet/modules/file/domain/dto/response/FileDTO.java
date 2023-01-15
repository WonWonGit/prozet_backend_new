package com.example.prozet.modules.file.domain.dto.response;

import com.example.prozet.modules.file.domain.entity.FileEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {

    private int idx;
    private String originalName;
    private String extension;
    private String url;
    private String deleteYn;
    private String fileKey;

    public FileEntity toEntity() {
        return FileEntity.builder()
                .idx(idx)
                .originalName(originalName)
                .extension(extension)
                .url(url)
                .fileKey(fileKey)
                .deleteYn(deleteYn)
                .build();
    }

}
