package org.sopt.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.sopt.domain.enums.Gender;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private Gender gender;
    private LocalDate birthDate;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles = new ArrayList<>();

    protected Member() {
    }

    private Member(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.gender = builder.gender;
        this.birthDate = builder.birthDate;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String email;
        private Gender gender;
        private LocalDate birthDate;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Member build() {
            return new Member(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean hasEmail(String email) {
        return this.email.equalsIgnoreCase(email);
    }

    public void addArticle(Article article) {
        articles.add(article);
        article.setAuthor(this);
    }
    //SOFT , HARD 중에 골라서 구현할 예정
    public void removeArticle(Article article) {
        articles.remove(article);
        article.setAuthor(null);
    }

}
