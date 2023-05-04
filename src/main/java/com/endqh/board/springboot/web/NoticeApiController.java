package com.endqh.board.springboot.web;

import com.endqh.board.springboot.service.posts.NoticeService;
import com.endqh.board.springboot.web.dto.notice.NoticeResponseDto;
import com.endqh.board.springboot.web.dto.notice.NoticeSaveRequestDto;
import com.endqh.board.springboot.web.dto.notice.NoticeUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class NoticeApiController {

    private final NoticeService noticeService;
    private final LogApiController logApiController;
    private final String DEFAULT_URI = "/api/v1/notice";

    @PostMapping(DEFAULT_URI)
    public Long save(@RequestBody NoticeSaveRequestDto requestDTO) {
        return noticeService.save(requestDTO);
    }

    @PutMapping(DEFAULT_URI + "/{id}")
    public Long update(@PathVariable Long id, @RequestBody NoticeUpdateRequestDto requestDTO) {
        return noticeService.update(id, requestDTO);
    }

    @GetMapping(DEFAULT_URI + "/{id}")
    public NoticeResponseDto findById(@PathVariable Long id) {
        NoticeResponseDto notice = noticeService.findById(id);
        notice.setViewCount(logApiController.getViewCount("notice", notice.getId()));
        return notice;
    }

    @DeleteMapping(DEFAULT_URI + "/{id}")
    public Long delete(@PathVariable Long id) {
        noticeService.delete(id);
        return id;
    }

}
