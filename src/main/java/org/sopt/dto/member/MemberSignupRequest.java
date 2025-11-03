package org.sopt.dto.member;

import org.sopt.domain.enums.Gender;

import java.time.LocalDate;

public record MemberSignupRequest(
        String name,
        String email,
        Gender gender,
        LocalDate birthDate
) {

}
