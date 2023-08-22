package com.blog.app.BlogApp.controller;

import com.blog.app.BlogApp.entities.Post;
import com.blog.app.BlogApp.payload.PostDto;
import com.blog.app.BlogApp.payload.PostResponse;
import com.blog.app.BlogApp.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    final private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //http:localhost:8080/api/posts
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto,
                                              BindingResult result) {
        if(result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PostDto dto = postService.createPost(postDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);

    }


   // localhost:8080/api/posts?pageNo=1&pageSize=2              //if we give required as true = pageNo=1&pageSize=5  this is mandatory.
    @GetMapping
    public PostResponse/*List<PostDto>*/ getAllPost(// we change the method return type because we want to send a response to the postman.
            @RequestParam(value = "pageNo",defaultValue="0",required=false)int pageNo,
            @RequestParam(value="pageSize",defaultValue="5",required=false)int pageSize,
            @RequestParam(value="sortBy",defaultValue="id",required=false)String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false)String sortDir

    ) {
        /*List<PostDto> postDtos = */ // beause service layer return Response object so we change the return type.
        PostResponse postresponse = postService.getAllPost(pageNo, pageSize, sortBy, sortDir);
        return /*postDtos*/ postresponse;
    }


    @GetMapping("/{id}")
    public ResponseEntity<PostDto> postById(@PathVariable("id") Long id){
        PostDto dto = postService.getPostById(id);
        return new ResponseEntity<>(dto,HttpStatus.FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
   @DeleteMapping ("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id")Long id){ //if we want to return message as a String we give ResponseEntity<String>.
       PostDto dto = postService.deleteById(id);
       return new ResponseEntity<>("post are deleted",HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> UpdatePostById(@PathVariable("id") long id,@RequestBody PostDto postDto){
        PostDto dtos = postService.updatePost(id,postDto);
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }


}
