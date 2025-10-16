package org.sopt.controller;

import org.sopt.domain.Member;
import org.sopt.dto.member.MemberSignupRequest;
import org.sopt.service.MemberService;

import java.util.List;
import java.util.Optional;

public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    public Long createMember(MemberSignupRequest request) {
        return memberService.join(request);
    }

    public Optional<Member> findMemberById(Long id) {
        return memberService.findOne(id);
    }

    public List<Member> getAllMembers() {
        return memberService.findAllMembers();
    }

    public void deleteMember(Long id) {
        memberService.delete(id);
    }
}
