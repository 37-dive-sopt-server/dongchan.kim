package org.sopt.controller;

import jakarta.validation.Valid;
import org.sopt.dto.article.ArticleCreateRequest;
import org.sopt.dto.article.ArticleListResponse;
import org.sopt.dto.article.ArticleResponse;
import org.sopt.service.ArticleService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ArticleResponse create(@RequestBody @Valid ArticleCreateRequest request) {
        return articleService.create(request);
    }


    @GetMapping("/{id}")
    public ArticleResponse getOne(@PathVariable Long id) {
        return articleService.getOne(id);
    }

    @GetMapping
    public ArticleListResponse getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return articleService.getAll(pageable);
    }
}
