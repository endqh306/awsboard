package com.endqh.board.springboot.service.posts;

import com.endqh.board.springboot.domain.notice.Notice;
import com.endqh.board.springboot.domain.notice.NoticeRepository;
import com.endqh.board.springboot.web.dto.notice.NoticeResponseDto;
import com.endqh.board.springboot.web.dto.notice.NoticeSaveRequestDto;
import com.endqh.board.springboot.web.dto.notice.NoticeUpdateRequestDto;
import com.endqh.board.springboot.web.dto.notice.NoticeListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional
    public Long save(NoticeSaveRequestDto requestDTO) {
        return noticeRepository.save(requestDTO.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, NoticeUpdateRequestDto requestDTO) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + id));
        notice.update(requestDTO.getTitle(), requestDTO.getContent());

        return id;
    }

    public NoticeResponseDto findById(Long id) {
        Notice entity = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + id));

        return new NoticeResponseDto(entity);

    }

    @Transactional(readOnly = true)
    public List<NoticeListResponseDto> findAllDesc() {
        return noticeRepository.findAllDesc().stream()
                .map(NoticeListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + id));

        noticeRepository.delete(notice);
    }

    public Long count() {
        return noticeRepository.count();
    }

    // 페이지로 가져오기
    @Transactional(readOnly = true)
    public List<NoticeListResponseDto> findAllByOrderByIdDesc(Integer pageNum, Integer postsPerPage) {
        Page<Notice> page = noticeRepository.findAll(
                // PageRequest의 page는 0부터 시작
                PageRequest.of(pageNum - 1, postsPerPage,
                        Sort.by(Sort.Direction.DESC, "id")
                ));
        return page.stream()
                .map(NoticeListResponseDto::new)
                .collect(Collectors.toList());
    }
}
