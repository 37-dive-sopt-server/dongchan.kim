package org.sopt.exception;

public class DuplicateEmailException extends DomainException {
    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL,"이미 가입된 이메일 입니다");
    }
}
