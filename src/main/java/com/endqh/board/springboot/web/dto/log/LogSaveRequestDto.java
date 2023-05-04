package com.endqh.board.springboot.web.dto.log;

import com.endqh.board.springboot.domain.log.Log;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogSaveRequestDto {

    private String boardName, ipAddress;
    private Long articleId, userId;

    @Builder
    public LogSaveRequestDto(String boardName, Long articleId, Long userId, String ipAddress) {
        this.boardName = boardName;
        this.articleId = articleId;
        this.userId = userId;
        this.ipAddress = ipAddress;
    }

    public Log toEntity() {
        return Log.builder()
                .boardName(boardName)
                .articleId(articleId)
                .userId(userId)
                .ipAddress(ipAddress)
                .build();
    }
}
