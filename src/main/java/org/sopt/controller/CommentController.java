package org.sopt.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.common.ApiResponse;
import org.sopt.dto.comment.CommentRequest;
import org.sopt.dto.comment.CommentResponse;
import org.sopt.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles/{articleId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long articleId,
            @RequestHeader("Member-Id") Long memberId,
            @Valid @RequestBody CommentRequest request) {

        CommentResponse response = commentService.createComment(articleId, memberId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("댓글이 작성되었습니다.", response));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @PathVariable Long articleId) {

        List<CommentResponse> responses = commentService.getCommentsByArticle(articleId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }


    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> getComment(
            @PathVariable Long commentId) {

        CommentResponse response = commentService.getComment(commentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long commentId,
            @RequestHeader("Member-Id") Long memberId,
            @Valid @RequestBody CommentRequest request) {

        CommentResponse response = commentService.updateComment(commentId, memberId, request);
        return ResponseEntity.ok(ApiResponse.success("댓글이 수정되었습니다.", response));
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("Member-Id") Long memberId) {

        commentService.deleteComment(commentId, memberId);
        return ResponseEntity.ok(ApiResponse.success("댓글이 삭제되었습니다.", null));
    }
}