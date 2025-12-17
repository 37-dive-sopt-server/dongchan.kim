package org.sopt.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.domain.common.BaseEntity;
import org.sopt.domain.enums.Gender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Member(String name, String email, String password, Gender gender, LocalDate birthDate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public boolean hasEmail(String email) {
        return this.email.equalsIgnoreCase(email);
    }

    public void addArticle(Article article) {
        articles.add(article);
        article.setAuthor(this);
    }

    public void removeArticle(Article article) {
        articles.remove(article);
        article.setAuthor(null);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setAuthor(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setAuthor(null);
    }
}