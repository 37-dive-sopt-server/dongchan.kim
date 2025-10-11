package org.sopt.controller;

import org.sopt.domain.Member;
import org.sopt.service.MemberServiceImpl;

import java.util.List;
import java.util.Optional;

public class MemberController {

    private MemberServiceImpl memberServiceImpl = new MemberServiceImpl();

    public Long createMember(String name) {

        return memberServiceImpl.join(name);
    }

    public Optional<Member> findMemberById(Long id) {
        return memberServiceImpl.findOne(id);
    }

    public List<Member> getAllMembers() {
        return memberServiceImpl.findAllMembers();
    }
}
