package com.example.prozet.modules.stack.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.stack.domain.dto.response.StackResDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "STACKS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String name;
    private String icon;
    @Enumerated(EnumType.STRING)
    @Column(name = "stack_type")
    private StackType stackType;

    @ManyToOne
    @JoinColumn(name = "project_idx", referencedColumnName = "idx")
    private ProjectEntity projectEntity;

    @ManyToOne
    @JoinColumn(name = "stack_category_idx", referencedColumnName = "idx")
    private StackCategoryEntity stackCategory;

    public StackResDTO toStackResDTO() {
        return StackResDTO.builder()
                .idx(idx)
                .name(name)
                .icon(icon)
                .stackType(stackType)
                .stackCategory(stackCategory.toStackCategoryResDTO())
                .build();

    }

}
