package org.sopt.exception;

public class UnderageMemberException extends DomainException {
    public UnderageMemberException() {
        super(ErrorCode.UNDERAGE_MEMBER, "만 20세 미만은 회원가입이 불가능합니다.");
    }
}
