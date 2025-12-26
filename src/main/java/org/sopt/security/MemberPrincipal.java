package org.sopt.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberPrincipal {
    private Long id;
    private String email;
}
