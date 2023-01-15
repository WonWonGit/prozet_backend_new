package com.example.prozet.modules.file.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.example.prozet.modules.file.domain.dto.response.FileDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FILE")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @ManyToOne
    @JoinColumn(name = "file_master_idx")
    private FileMasterEntity fileMaster;

    @Column(name = "original_name")
    private String originalName;
    private String extension;
    private Long size;
    private String url;

    @Column(name = "file_key")
    private String fileKey;

    @CreationTimestamp
    private LocalDateTime createDate;

    private LocalDateTime deleteDate;

    @Column(columnDefinition = "varchar(255) default 'N'", name = "delete_yn")
    private String deleteYn;

    public FileDTO toFileDTO() {
        return FileDTO.builder().idx(idx)
                .originalName(originalName)
                .extension(extension)
                .url(url)
                .fileKey(fileKey)
                .deleteYn(deleteYn)
                .build();
    }

}
