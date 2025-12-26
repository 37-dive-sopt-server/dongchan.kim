package org.sopt.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sopt.domain.common.BaseEntity;

@Entity
@Table(
        name = "comment",
        indexes = {
                @Index(name = "idx_comment_article", columnList = "article_id"),
                @Index(name = "idx_comment_author", columnList = "member_id"),
                @Index(name = "idx_comment_article_created", columnList = "article_id, created_at")
        }
)
@Getter
@ToString(exclude = {"article", "author"})
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;

    @Column(nullable = false, length = 300)
    private String content;

    private Comment(Article article, Member author, String content) {
        this.article = article;
        this.author = author;
        this.content = content;
    }

    public static Comment of(Article article, Member author, String content) {
        return new Comment(article, author, content);
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public void setAuthor(Member author) {
        this.author = author;
    }
}