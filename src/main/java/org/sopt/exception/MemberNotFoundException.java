package org.sopt.exception;

public class MemberNotFoundException extends DomainException{
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND, "회원이 존재하지 않습니다.");
    }
}
