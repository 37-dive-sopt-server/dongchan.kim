package org.sopt.dto.member;

import org.sopt.domain.enums.Gender;

import java.time.LocalDateTime;

public class MemberSignupRequest {
    private String name;
    private String email;
    private Gender gender;
    private LocalDateTime birthDate;

    // 생성자
    public MemberSignupRequest(String name, String email, Gender gender, LocalDateTime birthDate) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    // getter
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Gender getGender() { return gender; }
    public LocalDateTime getBirthDate() { return birthDate; }
}
