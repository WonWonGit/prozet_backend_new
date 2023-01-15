package com.example.prozet.modules.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prozet.modules.member.domain.entity.MemberInfoEntity;

public interface MemberInfoRepository extends JpaRepository<MemberInfoEntity, Long> {
    Optional<MemberInfoEntity> findByMemberEntity_Username(String username);
}
