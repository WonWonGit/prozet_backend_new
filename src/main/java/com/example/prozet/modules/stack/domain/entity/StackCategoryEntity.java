package com.example.prozet.modules.stack.domain.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.prozet.enum_pakage.Role;
import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.stack.domain.dto.response.StackCategoryResDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "STACK_CATEGORY")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StackCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;
    private String category;
    @Enumerated(EnumType.STRING)
    @Column(name = "stack_type")
    private StackType stackType;
    @OneToOne
    @JoinColumn(name = "project_idx", referencedColumnName = "idx")
    private ProjectEntity projectEntity;
    @OneToMany(mappedBy = "stackCategory")
    private List<StackEntity> stacks;

    public StackCategoryResDTO toStackCategoryResDTO() {
        return StackCategoryResDTO.builder()
                .idx(idx)
                .category(category)
                .stackType(stackType)
                .build();
    }

}
