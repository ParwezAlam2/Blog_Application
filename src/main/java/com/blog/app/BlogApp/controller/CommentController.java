package com.blog.app.BlogApp.controller;
import com.blog.app.BlogApp.payload.CommentDto;
import com.blog.app.BlogApp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/")
public class CommentController {


   @Autowired
    private CommentService commentService;

   //http://localhost:8080/api/posts/1/comments
   @PostMapping("/posts/{postId}/comments")
   public ResponseEntity<CommentDto> createComment(@PathVariable("postId")long postId, @RequestBody CommentDto dto){
       CommentDto dtos = commentService.saveComment(postId, dto);
       return  new ResponseEntity<>(dtos, HttpStatus.CREATED);
   }

   @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getAllComments(@PathVariable("postId")Long postId){
     List<CommentDto> dto = commentService.getAllCommentByPostId(postId);
     return dto;
   }

   @GetMapping("posts/{postId}/comments/{commentId}")
 public ResponseEntity<CommentDto> getCommentById(@PathVariable ("postId") Long postId,@PathVariable("commentId") Long commentId){
      CommentDto dtos = commentService.getCommentById(postId, commentId);
      return new ResponseEntity<>(dtos,HttpStatus.OK);
   }

   @PutMapping("/posts/{postId}/comments/{id}")
   public ResponseEntity<CommentDto> updateComment(
           @PathVariable("postId")long postId,
           @PathVariable("id") Long id,
           @RequestBody CommentDto dto){

       CommentDto dtos = commentService.updateComment(postId,id,dto);
       return new ResponseEntity<>(dtos,HttpStatus.OK);
   }

   @DeleteMapping("/posts/{postId}/comments/{id}")
   public ResponseEntity<String> deleteCommentById(@PathVariable("postId")long postId,@PathVariable("id") Long id){
       commentService.deleteById(postId,id);
       return new ResponseEntity<>("comment delete successfully",HttpStatus.OK);
   }

}
