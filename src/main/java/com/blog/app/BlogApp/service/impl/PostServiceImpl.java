package com.blog.app.BlogApp.service.impl;

import com.blog.app.BlogApp.entities.Post;
import com.blog.app.BlogApp.exceptions.ResourceNotFoundException;
import com.blog.app.BlogApp.payload.PostDto;
import com.blog.app.BlogApp.payload.PostResponse;
import com.blog.app.BlogApp.repository.PostRepository;
import com.blog.app.BlogApp.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository) { //this is used for dependency injection using constructor
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) { //copy Dto data to entity.

        Post post = mapToEntity(postDto);
        Post savedPost = postRepository.save(post); //after save we store in local variable
        PostDto dto = mapToDto(savedPost);
        return dto;

    }

    @Override
    public PostResponse/*List<PostDto>*/ getAllPost(int pageNo,int pageSize,String sortBy,String sortDir) { //sort by here is string and builtin method return type is sort so we have to convert into sort using Sort.by() method.

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? /*Condition 1 =>*/ Sort.by(sortBy).ascending() : /*Condition 2 =>*/ Sort.by(sortBy).descending();//Sort.Direction.ASC.name() = it is used for Ascending order,(sortBy).descending() it is used for descending order.
        PageRequest pageble = PageRequest.of(pageNo, pageSize, sort); //Page.Request method is used For pagable,the return type of this method is page so we give the return type page instead of list
        Page<Post> content = postRepository.findAll(pageble); //the return type of this method is page, so we change the return type of list ang give page.,this will return collections of page,
        List<Post> posts = content.getContent(); // it again convert back to List.

        List<PostDto> postDtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse response = new PostResponse();
        response.setContent(postDtos);
        response.setPageNo(content.getNumber());
        response.setPageSize(content.getSize());
        response.setTotalPages(content.getTotalPages());
        response.setTotalElements((long) content.getNumberOfElements());
        response.setLast(content.isLast());
        return response;
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("post not found wit id: " + id)
        );


        PostDto postDto = mapToDto(post);
        return postDto;

    }


    @Override
    public PostDto updatePost(long id, PostDto postDto) {
       Post post = postRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("record not found with id "+id)
        );

       post.setTitle(postDto.getTitle());
       post.setDescription(postDto.getDescription());
       post.setContent(postDto.getContent());
       postRepository.save(post);
        PostDto dtos = mapToDto(post);
        return dtos;
    }

    @Override
    public PostDto deleteById(Long id) {
        Post posts = postRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Record not found by this id:"+id)
        );

       postRepository.deleteById(id);
        PostDto dto = mapToDto(posts);
        return dto;
    }

    Post mapToEntity(PostDto postDto){ //here we convert Dto object to entity
        Post post = modelMapper.map(postDto, Post.class);
       /* Post post = new Post();
        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());*/
        return post;
    }

    PostDto mapToDto(Post post){ //here we convert again  entity object to dto because we never expose our entity class.
         //here convert the entity object to postDto because the return type is PostDto
        PostDto dto = modelMapper.map(post, PostDto.class);
        /*PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setContent(post.getContent());*/
        return dto;  //savedPost(entity) =  convert the data back again to dto because return type is PostDto

    }






}
