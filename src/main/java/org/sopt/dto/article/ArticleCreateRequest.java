package org.sopt.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.sopt.domain.enums.ArticleTag;

public record ArticleCreateRequest(
        @NotNull Long authorId,
        @NotNull ArticleTag tag,
        @NotBlank @Size(max = 200) String title,
        @NotBlank String content
) {}
