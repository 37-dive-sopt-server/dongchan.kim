package org.sopt.controller;

import org.sopt.domain.Member;
import org.sopt.dto.member.MemberResponse;
import org.sopt.dto.member.MemberSignupRequest;
import org.sopt.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @PostMapping
    public Long createMember(@RequestBody MemberSignupRequest request) {
        return memberService.join(request);
    }


    @GetMapping("/{id}")
    public MemberResponse findMemberById(@PathVariable Long id) {
        Member member = memberService.findOne(id);
        return MemberResponse.from(member);
    }

    @GetMapping
    public List<MemberResponse> getAllMembers() {
        return memberService.findAllMembers()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }


    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable Long id) {
        memberService.delete(id);
    }
}
