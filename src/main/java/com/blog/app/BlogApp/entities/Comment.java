package com.blog.app.BlogApp.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ("post_id"),nullable = false)
    private Post post;


/*

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id",nullable = false)
private Post post;*/
}

//foriegn key mapped with primary key automatically
