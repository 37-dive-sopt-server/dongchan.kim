package org.sopt.domain;

import org.sopt.domain.enums.Gender;

import java.time.LocalDate;

public class Member {

    private Long id;
    private String name;
    private String email;
    private Gender gender;
    private LocalDate birthDate;

    public Member(Long id, String name, String email, Gender gender, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public boolean hasEmail(String email) {
        return this.email.equals(email);
    }

    // getter
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Gender getGender() { return gender; }
    public LocalDate getBirthDate() { return birthDate; }
}
