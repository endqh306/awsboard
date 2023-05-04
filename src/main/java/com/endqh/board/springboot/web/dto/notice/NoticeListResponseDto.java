package com.endqh.board.springboot.web.dto.notice;

import com.endqh.board.springboot.domain.notice.Notice;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeListResponseDto {

    private Long id;
    private String title, author;
    private LocalDateTime modifiedDate;

    public NoticeListResponseDto(Notice entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();
    }
}
