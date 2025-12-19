package org.sopt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;


    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "commentList", key = "#articleId"),
            @CacheEvict(value = "articleDetail", key = "#articleId")
    })
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


    @Cacheable(
            value = "commentList",
            key = "#articleId",
            unless = "#result == null || #result.isEmpty()"
    )
    public List<CommentResponse> getCommentsByArticle(Long articleId) {

        if (!articleRepository.existsById(articleId)) {
            throw new DomainException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        List<Comment> comments = commentRepository.findAllByArticleIdWithAuthor(articleId);

        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }


    @Cacheable(
            value = "commentDetail",
            key = "#commentId",
            unless = "#result == null"
    )
    public CommentResponse getComment(Long commentId) {
        log.info("💾 [CACHE MISS] 댓글 상세 조회 - commentId: {}", commentId);

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

        Long articleId = comment.getArticle().getId();

        // 댓글 수정
        comment.changeContent(request.getContent());

        // 캐시 무효화
        evictCommentCache(articleId, commentId);

        return CommentResponse.from(comment);
    }


    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DomainException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getId().equals(memberId)) {
            throw new DomainException(ErrorCode.COMMENT_UNAUTHORIZED);
        }

        Long articleId = comment.getArticle().getId();

        commentRepository.delete(comment);

        // 캐시 무효화
        evictCommentCache(articleId, commentId);
    }


    @Cacheable(
            value = "memberCommentList",
            key = "#memberId",
            unless = "#result == null || #result.isEmpty()"
    )
    public List<CommentResponse> getCommentsByMember(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new DomainException(ErrorCode.MEMBER_NOT_FOUND);
        }

        List<Comment> comments = commentRepository.findAllByAuthorIdOrderByCreatedAtDesc(memberId);

        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }

    /**
     * 댓글 관련 캐시 무효화 헬퍼 메서드 => 이게 있어야한다는데 좀 더 공부좀 해봐야겠다
     */
    @Caching(evict = {
            @CacheEvict(value = "commentList", key = "#articleId"),
            @CacheEvict(value = "commentDetail", key = "#commentId"),
            @CacheEvict(value = "articleDetail", key = "#articleId")
    })
    public void evictCommentCache(Long articleId, Long commentId) {
        log.info("🗑[CACHE EVICT] 댓글 관련 캐시 삭제 - articleId: {}, commentId: {}", articleId, commentId);
    }
}