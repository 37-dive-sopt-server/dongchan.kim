package org.sopt.dto.article;

import java.util.List;

public record ArticleListResponse(
        List<ArticleResponse>  articles,
        long totalCount
) {
}
