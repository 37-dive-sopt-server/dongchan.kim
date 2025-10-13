package org.sopt.service;

import org.sopt.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    Long join(org.sopt.dto.member.MemberSignupRequest request);

    Optional<Member> findOne(Long memberId);

    List<Member> findAllMembers();
}
