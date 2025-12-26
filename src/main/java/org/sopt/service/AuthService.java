package org.sopt.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.Member;
import org.sopt.dto.auth.LoginRequest;
import org.sopt.dto.auth.SignupRequest;
import org.sopt.dto.auth.TokenResponse;
import org.sopt.exception.DomainException;
import org.sopt.exception.ErrorCode;
import org.sopt.repository.MemberRepository;
import org.sopt.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;


    @Transactional
    public TokenResponse signup(SignupRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new DomainException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        Member member = Member.builder()
                .name(request.name())
                .email(request.email())
                .password(encodedPassword)
                .gender(request.gender())
                .birthDate(request.birthDate())
                .build();

        Member savedMember = memberRepository.save(member);

        String accessToken = tokenProvider.createAccessToken(savedMember.getId(), savedMember.getEmail());
        String refreshToken = tokenProvider.createRefreshToken(savedMember.getId());

        return TokenResponse.of(
                accessToken,
                refreshToken,
                savedMember.getId(),
                savedMember.getName(),
                savedMember.getEmail()
        );
    }


    public TokenResponse login(LoginRequest request) {

        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new DomainException(ErrorCode.INVALID_CREDENTIALS));


        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new DomainException(ErrorCode.INVALID_CREDENTIALS);
        }


        String accessToken = tokenProvider.createAccessToken(member.getId(), member.getEmail());
        String refreshToken = tokenProvider.createRefreshToken(member.getId());

        return TokenResponse.of(
                accessToken,
                refreshToken,
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }


    public TokenResponse refresh(String refreshToken) {

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new DomainException(ErrorCode.INVALID_TOKEN);
        }


        Long memberId = tokenProvider.getMemberId(refreshToken);


        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new DomainException(ErrorCode.MEMBER_NOT_FOUND));

        String newAccessToken = tokenProvider.createAccessToken(member.getId(), member.getEmail());

        return TokenResponse.of(
                newAccessToken,
                refreshToken,
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }
}