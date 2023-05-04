package com.endqh.board.springboot.web.dto;

import com.endqh.board.springboot.domain.posts.Posts;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PostsResponseDto {

    private Long id, authorId;
    private String title;
    private String content;
    private String author;

    @Setter
    private Long viewCount;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.authorId = entity.getAuthorId();
    }
}
