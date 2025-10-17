package org.sopt.service;

import org.sopt.domain.Member;
import org.sopt.dto.member.MemberSignupRequest;
import org.sopt.exception.DuplicateEmailException;
import org.sopt.exception.MemberNotFoundException;
import org.sopt.exception.UnderageMemberException;
import org.sopt.repository.MemberRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private static long sequence = 1L;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Long join(MemberSignupRequest request) {

        if(existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException();
        }

        if(calculateAge(request.getBirthDate()) < 20) {
            throw new UnderageMemberException();
        }

        Member member = new Member(
                sequence++,
                request.getName(),
                request.getEmail(),
                request.getGender(),
                request.getBirthDate()
        );
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public void delete(Long memberId) {
        if(!memberRepository.deleteById(memberId)){
            throw new MemberNotFoundException();
        }
    }

    private Boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    private int calculateAge(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        return Period.between(LocalDate.from(birthDate),today).getYears();
    }
}
