package org.sopt.dto.article;

import java.time.LocalDateTime;

import org.sopt.domain.Article;
import org.sopt.dto.comment.CommentResponse;

import java.util.List;

public record ArticleResponse(
        Long id,
        String title,
        String content,
        String tag,
        AuthorInfo author,
        List<CommentResponse> comments,
        Integer commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record AuthorInfo(
            Long id,
            String name,
            String email
    ) {
        public static AuthorInfo from(org.sopt.domain.Member member) {
            return new AuthorInfo(
                    member.getId(),
                    member.getName(),
                    member.getEmail()
            );
        }
    }

    public static ArticleResponse fromWithComments(Article article) {
        List<CommentResponse> commentResponses = article.getComments().stream()
                .map(CommentResponse::from)
                .toList();

        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getTag().name(),
                AuthorInfo.from(article.getAuthor()),
                commentResponses,
                commentResponses.size(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }

    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getTag().name(),
                AuthorInfo.from(article.getAuthor()),
                null,  // 댓글 목록 제외
                article.getComments().size(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }
}
