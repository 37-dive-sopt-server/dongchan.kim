package org.sopt.dto.comment;

import org.sopt.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        AuthorInfo author,
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

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                AuthorInfo.from(comment.getAuthor()),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
