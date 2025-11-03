package org.sopt.controller.config;

import org.sopt.controller.MemberController;
import org.sopt.repository.MemberRepository;
import org.sopt.service.MemberService;
import org.sopt.service.MemberServiceImpl;

public class AppConfig {
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    public MemberController memberController() {
        return new MemberController(memberService());
    }
}

