package org.sopt.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.Article;
import org.sopt.domain.Comment;
import org.sopt.domain.Member;
import org.sopt.dto.comment.CommentRequest;
import org.sopt.dto.comment.CommentResponse;
import org.sopt.exception.DomainException;
import org.sopt.exception.ErrorCode;
import org.sopt.repository.ArticleRepository;
import org.sopt.repository.CommentRepository;
import org.sopt.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public CommentResponse createComment(Long articleId, Long memberId, CommentRequest request) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new DomainException(ErrorCode.ARTICLE_NOT_FOUND));


        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new DomainException(ErrorCode.MEMBER_NOT_FOUND));


        Comment comment = Comment.of(article, member, request.getContent());


        article.addComment(comment);

        Comment savedComment = commentRepository.save(comment);

        return CommentResponse.from(savedComment);
    }


    public List<CommentResponse> getCommentsByArticle(Long articleId) {

        if (!articleRepository.existsById(articleId)) {
            throw new DomainException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        List<Comment> comments = commentRepository.findAllByArticleIdWithAuthor(articleId);

        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }

    public CommentResponse getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DomainException(ErrorCode.COMMENT_NOT_FOUND));

        return CommentResponse.from(comment);
    }


    @Transactional
    public CommentResponse updateComment(Long commentId, Long memberId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DomainException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getId().equals(memberId)) {
            throw new DomainException(ErrorCode.COMMENT_UNAUTHORIZED);
        }

        // 댓글 수정 (더티 체킹)
        comment.changeContent(request.getContent());

        return CommentResponse.from(comment);
    }


    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DomainException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getId().equals(memberId)) {
            throw new DomainException(ErrorCode.COMMENT_UNAUTHORIZED);
        }

        commentRepository.delete(comment);
    }


    public List<CommentResponse> getCommentsByMember(Long memberId) {
        // 회원 존재 확인
        if (!memberRepository.existsById(memberId)) {
            throw new DomainException(ErrorCode.MEMBER_NOT_FOUND);
        }

        List<Comment> comments = commentRepository.findAllByAuthorIdOrderByCreatedAtDesc(memberId);

        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }
}