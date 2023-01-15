package com.example.prozet.modules.memberAuth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.prozet.modules.memberAuth.domain.entity.MemberAuthEntity;

public interface MemberAuthRepository extends JpaRepository<MemberAuthEntity, Long>{
    // MemberAuthEntity findBymember_idx(long member_idx);
    Optional<MemberAuthEntity> findByRefreshToken(String refresToken);
    Optional<MemberAuthEntity> findByMemberEntity_idx(long memberIdx);

}
