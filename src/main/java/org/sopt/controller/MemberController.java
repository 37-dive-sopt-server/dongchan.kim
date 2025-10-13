package org.sopt.controller;

import org.sopt.domain.Member;
import org.sopt.dto.member.MemberSignupRequest;
import org.sopt.service.MemberService;
import org.sopt.service.MemberServiceImpl;

import java.util.List;
import java.util.Optional;

public class MemberController {

    private final MemberService memberServiceImpl = new MemberServiceImpl();


    public Long createMember(MemberSignupRequest request) {
        return memberServiceImpl.join(request);
    }

    public Optional<Member> findMemberById(Long id) {
        return memberServiceImpl.findOne(id);
    }

    public List<Member> getAllMembers() {
        return memberServiceImpl.findAllMembers();
    }

    public void deleteMember(Long id) {
        memberServiceImpl.delete(id);
    }
}
