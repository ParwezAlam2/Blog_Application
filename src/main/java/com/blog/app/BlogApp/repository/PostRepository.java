package com.blog.app.BlogApp.repository;

import com.blog.app.BlogApp.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {

}
