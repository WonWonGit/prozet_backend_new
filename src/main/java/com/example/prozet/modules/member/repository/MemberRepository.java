package com.example.prozet.modules.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prozet.modules.member.domain.entity.MemberEntity;


public interface MemberRepository extends JpaRepository<MemberEntity, Long>{

    Optional<MemberEntity> findByUsername(String username);
    
}
