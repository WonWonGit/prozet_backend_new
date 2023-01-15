package com.example.prozet.modules.memberAuth.domain.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberAuthResDto {

    private long idx;
    private String refreshToken;
    private long memberIdx;
    
}
