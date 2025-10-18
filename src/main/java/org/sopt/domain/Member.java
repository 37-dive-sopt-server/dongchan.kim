package org.sopt.domain;

import org.sopt.domain.enums.Gender;
import java.time.LocalDate;

public class Member {

    private Long id;
    private final String name;
    private final String email;
    private final Gender gender;
    private final LocalDate birthDate;

    private Member(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.gender = builder.gender;
        this.birthDate = builder.birthDate;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String email;
        private Gender gender;
        private LocalDate birthDate;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Member build() {
            return new Member(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean hasEmail(String email) {
        return this.email.equalsIgnoreCase(email);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Gender getGender() { return gender; }
    public LocalDate getBirthDate() { return birthDate; }
}
