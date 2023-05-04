package com.endqh.board.springboot.web;

import com.endqh.board.springboot.service.posts.LogService;

import com.endqh.board.springboot.web.dto.log.LogSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LogApiController {

    private final LogService logService;

    @PostMapping("/api/v1/log")
    public Long save(@RequestBody LogSaveRequestDto logSaveRequestDTO) {
        return logService.save(logSaveRequestDTO);
    }

    @GetMapping("/api/v1/log/{boardName}/{articleId}")
    public Long getViewCount(@PathVariable("boardName") String boardName,
                             @PathVariable("articleId") Long articleId) {
        return logService.getViewCountByBoardNameAndArticleId(boardName, articleId);
    }
}
