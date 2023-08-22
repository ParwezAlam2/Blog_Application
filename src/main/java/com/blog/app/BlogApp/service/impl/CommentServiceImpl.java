package com.blog.app.BlogApp.service.impl;

import com.blog.app.BlogApp.entities.Comment;
import com.blog.app.BlogApp.entities.Post;
import com.blog.app.BlogApp.exceptions.BlogApiException;
import com.blog.app.BlogApp.exceptions.ResourceNotFoundException;
import com.blog.app.BlogApp.payload.CommentDto;
import com.blog.app.BlogApp.repository.CommentRepository;
import com.blog.app.BlogApp.repository.PostRepository;
import com.blog.app.BlogApp.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private PostRepository postRepository;   //we use userRepository here because it check user id present or not according that it will set the comment which oresent in post table.

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDto saveComment(Long postId, CommentDto dto) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found with id:" + postId)
        );
        Comment newcomment = mapToEntity(dto);
        newcomment.setPost(post); //for this post we set the comment
        Comment save = commentRepository.save(newcomment);
        CommentDto dtos = mapToDto(save);
        return dtos;
    }



    @Override
    public List<CommentDto> getAllCommentByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentDto> dtos = comments.stream().map(e -> mapToDto(e)).collect(Collectors.toList());
        return dtos;

    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {


        // this will found based on post id.
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post Not found with id" + postId)
        );

        // this will found based on comment id.
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () ->new ResourceNotFoundException ("comment not found with exception" + commentId)
        );

        //url => http://localhost:8080/api/posts/1/comments/1
        //for a particular post give me a particular comment,earlier we give post id it will give all the comment which are maped with postid but here we want just give the comment based on post id.
        // does this comment belong to this post ,
        //whenever comment id is given remeber we return only one comments not list.
        if (!comment.getPost().getId().equals(post.getId())){
          throw  new BlogApiException(" Comments not found "+commentId);
        }

        CommentDto dto = mapToDto(comment);
        return dto;
    }

    @Override
    public CommentDto updateComment(long postId, Long id,CommentDto dto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found with id" + postId)
        );

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("post not found with id" + postId)
        );

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException("post not found");
        }

        //comment.setId(dto.getId()); because we get altered table exception thats why we comment
        comment.setName(dto.getName());
        comment.setEmail(dto.getEmail());
        comment.setBody(dto.getBody());
        Comment save = commentRepository.save(comment);
       return mapToDto(save);

    }

    @Override
    public void deleteById(long postId, Long id) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found with id" + postId)
        );
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("post not found with id" + postId)
        );

        if(!comment.getPost().getId().equals(post.getId())){
            throw  new BlogApiException("post not found");
        }

        commentRepository.deleteById(id);
    }


    Comment mapToEntity(CommentDto dto){
       Comment comment =  modelMapper.map(dto,Comment.class); //this is used to map
      /*  Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setName(dto.getName());
        comment.setEmail(dto.getEmail());
        comment.setBody(dto.getBody());*/
        return comment;
    }

    CommentDto mapToDto(Comment comment){
        CommentDto dto = modelMapper.map(comment, CommentDto.class);
       /* CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setName(comment.getName());
        dto.setEmail(comment.getEmail());
        dto.setBody(comment.getBody());*/
        return dto;
    }
}
