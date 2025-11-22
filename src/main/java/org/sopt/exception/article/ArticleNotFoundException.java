package org.sopt.exception.article;

import org.sopt.exception.DomainException;
import org.sopt.exception.ErrorCode;

public class ArticleNotFoundException extends DomainException {
    public ArticleNotFoundException(ErrorCode errorCode) {
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }
}
