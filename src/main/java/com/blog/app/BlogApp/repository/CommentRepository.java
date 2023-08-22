package com.blog.app.BlogApp.repository;

import com.blog.app.BlogApp.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

 //Custom method or jpql method
//Comment findByEmail(String email);
// Comment findByName(String name);

 List<Comment> findByPostId(long postId);
}
