package org.sopt.service;

import org.sopt.domain.Member;
import org.sopt.dto.member.MemberSignupRequest;
import org.sopt.respository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberServiceImpl implements MemberService {

    private final MemoryMemberRepository memberRepository = new MemoryMemberRepository();
    private static long sequence = 1L;

    @Override
    public Long join(MemberSignupRequest request) {
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
}
