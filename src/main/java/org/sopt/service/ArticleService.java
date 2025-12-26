package org.sopt.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.sopt.domain.Article;
import org.sopt.domain.Member;
import org.sopt.dto.article.ArticleCreateRequest;
import org.sopt.dto.article.ArticleListResponse;
import org.sopt.dto.article.ArticleResponse;
import org.sopt.exception.member.MemberNotFoundException;
import org.sopt.repository.ArticleRepository;
import org.sopt.repository.MemberRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
    @CacheEvict(value = "articleList", allEntries = true)
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


    @Cacheable(
            value = "articleList",
            key = "'search:' + #title + ':' + #authorName + ':page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize",
            unless = "#result == null || #result.articles().isEmpty()"
    )
    public ArticleListResponse search(String title, String authorName, Pageable pageable) {
        log.info("💾 [CACHE MISS] 게시글 검색 조회 - title: {}, authorName: {}", title, authorName);

        Page<Article> page = articleRepository.search(title, authorName, pageable);
        return new ArticleListResponse(
                page.map(this::toResponse).getContent(),
                page.getTotalElements()
        );
    }


    @Cacheable(
            value = "articleDetail",
            key = "#id",
            unless = "#result == null"
    )
    public ArticleResponse getOne(Long id) {

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("아티클을 찾을 수 없습니다. id=" + id));
        return toResponse(article);
    }


    @Cacheable(
            value = "articleList",
            key = "'all:page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize + ':sort:' + #pageable.sort.toString()",
            unless = "#result == null || #result.articles().isEmpty()"
    )
    public ArticleListResponse getAll(Pageable pageable) {
        log.info("💾 [CACHE MISS] 게시글 전체 목록 조회 - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Article> page = articleRepository.findAll(pageable);
        return new ArticleListResponse(
                page.map(this::toResponse).getContent(),
                page.getTotalElements()
        );
    }


    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "articleDetail", key = "#id"),
            @CacheEvict(value = "articleList", allEntries = true),
            @CacheEvict(value = "commentList", key = "#id")
    })
    public ArticleResponse update(Long id, ArticleCreateRequest request) {
        log.info("🗑️ [CACHE EVICT] 게시글 관련 캐시 삭제 - articleId: {}", id);

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("아티클을 찾을 수 없습니다. id=" + id));

        article.changeTitle(request.title());
        article.changeContent(request.content());
        article.changeTag(request.tag());

        return toResponse(article);
    }


    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "articleDetail", key = "#id"),
            @CacheEvict(value = "articleList", allEntries = true),
            @CacheEvict(value = "commentList", key = "#id")
    })
    public void delete(Long id) {
        log.info("🗑️ [CACHE EVICT] 게시글 관련 모든 캐시 삭제 - articleId: {}", id);

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("아티클을 찾을 수 없습니다. id=" + id));

        articleRepository.delete(article);
    }

    private ArticleResponse toResponse(Article article) {
        return ArticleResponse.fromWithComments(article);
    }

    private Article safeSave(Article article) {
        try {
            return articleRepository.save(article);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 존재하는 제목입니다: " + article.getTitle());
        }
    }
}