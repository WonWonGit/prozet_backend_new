package com.example.prozet.modules.member.domain.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequireDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "It is not Email format")
    private String email;
    @NotBlank(message = "Name is required")
    private String name;

}
