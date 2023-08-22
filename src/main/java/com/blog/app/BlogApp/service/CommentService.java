package com.blog.app.BlogApp.service;

import com.blog.app.BlogApp.payload.CommentDto;

import java.util.List;

public interface CommentService {
 CommentDto saveComment(Long postId ,CommentDto dto);

 List<CommentDto> getAllCommentByPostId(Long postId);



 CommentDto getCommentById(Long postId, Long commentId);

 CommentDto updateComment(long postId, Long id,CommentDto dto);

 void deleteById(long postId, Long id);
}
