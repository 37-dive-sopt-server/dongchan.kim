package org.sopt.exception.article;

import org.sopt.exception.DomainException;
import org.sopt.exception.ErrorCode;

public class DuplicateArticleTitleException extends DomainException {
    public DuplicateArticleTitleException(ErrorCode errorCode) {
        super(ErrorCode.DUPLICATE_ARTICLE_TITLE);
    }
}
