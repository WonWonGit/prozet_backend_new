package com.example.prozet.security.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.prozet.modules.member.domain.entity.MemberEntity;

public class PrincipalDetails implements UserDetails {

    private MemberEntity memberEntity;

    
    public PrincipalDetails(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }

    public String getRole(){
        return memberEntity.getRole().role();
    }

    public Long getIdx(){
        return memberEntity.getIdx();
    }

    public MemberEntity getMember(){
        return memberEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return memberEntity.getRole().role();
            }
            
        });
        return collection;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return memberEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

    
}
