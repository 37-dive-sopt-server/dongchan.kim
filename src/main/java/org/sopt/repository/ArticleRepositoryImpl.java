package org.sopt.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.sopt.domain.Article;
import org.sopt.domain.QArticle;
import org.sopt.domain.QMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;


public class ArticleRepositoryImpl implements ArticleRepositoryCustom{

    private final JPAQueryFactory query;

    public ArticleRepositoryImpl(JPAQueryFactory query) {
        this.query = query;
    }

    @Override
    public Page<Article> search(String title, String authorName, Pageable pageable) {
        QArticle a = QArticle.article;
        QMember m = QMember.member;

        BooleanBuilder where = new BooleanBuilder();
        if (title != null && !title.isBlank()) {
            where.and(a.title.containsIgnoreCase(title));
        }
        if (authorName != null && !authorName.isBlank()) {
            where.and(a.author.name.containsIgnoreCase(authorName));
        }

        // total count (fetch join 금지, 별도 카운트)
        Long total = query
                .select(a.count())
                .from(a)
                .join(a.author, m)
                .where(where)
                .fetchOne();
        long totalCount = (total == null) ? 0L : total;


        List<Article> content = query
                .selectFrom(a)
                .join(a.author, m).fetchJoin()
                .where(where)
                .orderBy(orderBy(pageable.getSort(), a))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        return new PageImpl<>(content, pageable, totalCount);
    }

    private OrderSpecifier[] orderBy(Sort sort, QArticle a) {
            return new OrderSpecifier[]{ a.createdAt.desc() };
    }

}
