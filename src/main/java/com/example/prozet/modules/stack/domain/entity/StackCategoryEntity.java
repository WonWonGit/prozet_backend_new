package com.example.prozet.modules.stack.domain.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.prozet.enum_pakage.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "STACK_CATEGORY")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StackCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;
    private String category;
    @Enumerated(EnumType.STRING)
    private Role role;

}
