package org.sopt.dto.article;

import java.time.LocalDateTime;

public record ArticleResponse(
        Long id,
        Long authorId,
        String authorName,
        String tag,
        String title,
        String content,
        LocalDateTime createdAt
) {
}
