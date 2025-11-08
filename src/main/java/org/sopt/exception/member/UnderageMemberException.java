package org.sopt.exception.member;

import org.sopt.exception.DomainException;
import org.sopt.exception.ErrorCode;

public class UnderageMemberException extends DomainException {
    public UnderageMemberException() {
        super(ErrorCode.UNDERAGE_MEMBER);
    }
}
