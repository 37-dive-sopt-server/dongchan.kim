package org.sopt.domain.enums;

public enum Gender {
    MALE("남성"), FEMALE("여성");

    private final String description;

    private Gender(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

}
