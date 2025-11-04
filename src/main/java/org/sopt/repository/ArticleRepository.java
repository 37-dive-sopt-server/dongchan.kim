package org.sopt.repository;

import org.sopt.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    boolean existsByTitle(String title);
}
