package org.sopt.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sopt.domain.common.BaseEntity;
import org.sopt.domain.enums.ArticleTag;

@Entity
@Table(
        name = "article",
        indexes = {
                @Index(name = "idx_article_author", columnList = "member_id"),
                @Index(name = "idx_article_tag_created", columnList = "tag, created_at"),
                @Index(name = "idx_article_created_id", columnList = "created_at, id")
        }
)
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ArticleTag tag;

    @Column(nullable = false, unique = true, length = 200)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    private Article(Member author, ArticleTag tag, String title, String content) {
        this.author = author;
        this.tag = tag;
        this.title = title;
        this.content = content;
    }

    public static Article of(Member author, ArticleTag tag, String title, String content) {
        return new Article(author, tag, title, content);
    }

    public void changeTitle(String title) { this.title = title; }
    public void changeContent(String content) { this.content = content; }
    public void changeTag(ArticleTag tag) { this.tag = tag; }
    public void setAuthor(Member author) { this.author = author; }
}
