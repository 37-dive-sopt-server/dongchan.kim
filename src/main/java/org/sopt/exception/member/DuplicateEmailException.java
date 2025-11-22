package org.sopt.exception.member;

import org.sopt.exception.DomainException;
import org.sopt.exception.ErrorCode;

public class DuplicateEmailException extends DomainException {
    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}
