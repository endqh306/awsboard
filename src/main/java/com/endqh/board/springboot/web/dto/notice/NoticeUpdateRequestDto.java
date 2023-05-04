package com.endqh.board.springboot.web.dto.notice;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeUpdateRequestDto {
    private String title, content;

    @Builder
    public NoticeUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
