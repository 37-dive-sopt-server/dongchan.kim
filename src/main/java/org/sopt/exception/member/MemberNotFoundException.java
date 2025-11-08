package org.sopt.exception.member;

import org.sopt.exception.DomainException;
import org.sopt.exception.ErrorCode;

public class MemberNotFoundException extends DomainException {
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
