package com.blog.app.BlogApp.payload;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
public class CommentDto {
    private Long id;
    private String name;
    private String email;
    private String body;

}
