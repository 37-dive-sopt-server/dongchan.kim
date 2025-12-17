package org.sopt.dto.auth;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        Long memberId,
        String name,
        String email
) {
    public static TokenResponse of(String accessToken, String refreshToken,
                                   Long memberId, String name, String email) {
        return new TokenResponse(accessToken, refreshToken, memberId, name, email);
    }
}