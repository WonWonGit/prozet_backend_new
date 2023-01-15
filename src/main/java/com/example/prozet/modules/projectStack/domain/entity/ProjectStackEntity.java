package com.example.prozet.modules.projectStack.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.prozet.modules.file.domain.entity.FileMasterEntity;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.stack.domain.entity.StackEntity;

@Entity
@Table(name = "PROJECT_STACK")
public class ProjectStackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToOne
    @JoinColumn(name = "file_master_idx")
    private FileMasterEntity fileMasterEntity;

    @OneToOne
    @JoinColumn(name = "stack_idx")
    private StackEntity stack;

    @ManyToOne
    @JoinColumn(name = "project_idx")
    private ProjectEntity projectEntity;

}
