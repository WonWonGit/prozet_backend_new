package com.example.prozet.enum_pakage;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

import com.example.prozet.modules.member.domain.dto.request.MemberReqDTO;

public enum OauthAttribute {

    GOOGLE(Provider.GOOGLE.provider(), (attributes) -> {
        return MemberReqDTO.builder()
                .provider(Provider.GOOGLE)
                .username(Provider.GOOGLE.provider() + "_" + (String) attributes.get("sub"))
                .email((String) attributes.get("email"))
                .role(Role.USER)
                .build();
    });

    private final String oauthProvider;
    private final Function<Map<String, Object>, MemberReqDTO> of;

    private OauthAttribute(String oauthProvider, Function<Map<String, Object>, MemberReqDTO> of) {
        this.oauthProvider = oauthProvider;
        this.of = of;
    }

    public static MemberReqDTO extract(String oauthProvider, Map<String, Object> attributes) {

        MemberReqDTO memberReqSaveDto = Arrays.stream(values())
                .filter(provider -> oauthProvider.equals(provider.oauthProvider))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new).of.apply(attributes);

        return memberReqSaveDto;
    }

}
