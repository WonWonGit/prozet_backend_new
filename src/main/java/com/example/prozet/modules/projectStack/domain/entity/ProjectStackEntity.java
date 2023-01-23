package com.example.prozet.modules.projectStack.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.projectStack.domain.dto.response.ProjectStackResDTO;
import com.example.prozet.modules.stack.domain.entity.StackEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PROJECT_STACK")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToOne
    @JoinColumn(name = "stack_idx")
    private StackEntity stackEntity;

    @ManyToOne
    @JoinColumn(name = "project_idx")
    private ProjectEntity projectEntity;

    public ProjectStackResDTO toProjectStackResDTO() {
        return ProjectStackResDTO.builder()
                .idx(idx)
                .stackResDTO(stackEntity.toStackResDTO())
                .build();

    }

}
