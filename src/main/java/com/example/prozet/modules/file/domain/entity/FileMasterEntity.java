package com.example.prozet.modules.file.domain.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.prozet.modules.file.domain.dto.response.FileMasterDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FILE_MASTER")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "fileMaster")
    private List<FileEntity> fileList = new ArrayList<>();

    private String category;

    @Column(name = "create_date")
    private Timestamp createDate;

    public FileMasterDTO toFileMasterDTO() {
        return FileMasterDTO.builder()
                .idx(idx)
                .category(category)
                .fileList(
                        fileList.stream().filter(
                                file -> file.getDeleteYn().equals("N")).map(FileEntity::toFileDTO)
                                .collect(Collectors.toList()))
                .build();
    }

}
