package org.sopt.service;

import jakarta.persistence.EntityNotFoundException;
import org.sopt.domain.Article;
import org.sopt.domain.Member;
import org.sopt.dto.article.ArticleCreateRequest;
import org.sopt.dto.article.ArticleListResponse;
import org.sopt.dto.article.ArticleResponse;
import org.sopt.exception.MemberNotFoundException;
import org.sopt.repository.ArticleRepository;
import org.sopt.repository.MemberRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public ArticleService(ArticleRepository articleRepository, MemberRepository memberRepository) {
        this.articleRepository = articleRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public ArticleResponse create(ArticleCreateRequest request) {
        if (articleRepository.existsByTitle(request.title())) {
            throw new IllegalArgumentException("Title already exists");
        }
        Member author = memberRepository.findById(request.authorId())
                .orElseThrow(MemberNotFoundException::new);

        Article article = Article.of(author, request.tag(), request.title(), request.content());
        Article saved = safeSave(article);

        return toResponse(saved);
    }

    public ArticleResponse getOne(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("아티클을 찾을 수 없습니다. id=" + id));
        return toResponse(article);
    }

    public ArticleListResponse getAll(Pageable pageable) {
        Page<Article> page = articleRepository.findAll(pageable);
        return new ArticleListResponse(
                page.map(this::toResponse).getContent(),
                page.getTotalElements()
        );
    }

    private ArticleResponse toResponse(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getAuthor().getId(),
                article.getAuthor().getName(),
                article.getTag().name(),
                article.getTitle(),
                article.getContent(),
                article.getCreatedAt()
        );
    }


    // DB 유니크 인덱스 동시성 방어
    private Article safeSave(Article article) {
        try {
            return articleRepository.save(article);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 존재하는 제목입니다: " + article.getTitle());
        }
    }
}
