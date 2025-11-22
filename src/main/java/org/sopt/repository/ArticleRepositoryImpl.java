package org.sopt.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.sopt.domain.Article;
import org.sopt.domain.QArticle;
import org.sopt.domain.QMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;


public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory query;

    public ArticleRepositoryImpl(JPAQueryFactory query) {
        this.query = query;
    }

    @Override
    public Page<Article> search(String title, String authorName, Pageable pageable) {
        QArticle a = QArticle.article;
        QMember m = QMember.member;

        Long total = query
                .select(a.count())
                .from(a)
                .join(a.author, m)
                .where(
                        titleContains(title),
                        authorNameContains(authorName)
                )
                .fetchOne();
        long totalCount = (total == null) ? 0L : total;

        List<Article> content = query
                .selectFrom(a)
                .join(a.author, m).fetchJoin()
                .where(
                        titleContains(title),
                        authorNameContains(authorName)
                )
                .orderBy(orderBy(pageable.getSort(), a))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        return new PageImpl<>(content, pageable, totalCount);
    }

    private OrderSpecifier<?>[] orderBy(Sort sort, QArticle a) {
        return new OrderSpecifier[]{ a.createdAt.desc() };
    }

    private BooleanExpression titleContains(String title) {
        return (title == null || title.isBlank()) ? null : QArticle.article.title.containsIgnoreCase(title);
    }

    private BooleanExpression authorNameContains(String authorName) {
        return (authorName == null || authorName.isBlank()) ? null : QArticle.article.author.name.containsIgnoreCase(authorName);
    }

    private BooleanExpression tagEq(String tag) {
        return (tag == null || tag.isBlank()) ? null : QArticle.article.tag.stringValue().eq(tag);
    }



}
