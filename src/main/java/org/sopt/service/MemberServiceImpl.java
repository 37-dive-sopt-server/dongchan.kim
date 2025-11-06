package org.sopt.service;

import org.sopt.domain.Member;
import org.sopt.dto.member.MemberSignupRequest;
import org.sopt.exception.DuplicateEmailException;
import org.sopt.exception.MemberNotFoundException;
import org.sopt.exception.UnderageMemberException;
import org.sopt.repository.MemberRepository;
import org.sopt.util.AgeCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Long join(MemberSignupRequest request) {
        if (existsByEmail(request.email())) {
            throw new DuplicateEmailException();
        }
        if (AgeCalculator.calculate(request.birthDate()) < 20) {
            throw new UnderageMemberException();
        }

        Member member = Member.builder()
                .name(request.name())
                .email(request.email())
                .gender(request.gender())
                .birthDate(request.birthDate())
                .build();

        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    private boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

}
