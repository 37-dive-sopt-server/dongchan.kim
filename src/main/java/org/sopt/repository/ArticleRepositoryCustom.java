package org.sopt.repository;

import org.sopt.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {
    Page<Article> search(String title, String authorName, Pageable pageable);
}
