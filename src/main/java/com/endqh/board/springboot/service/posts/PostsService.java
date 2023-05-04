package com.endqh.board.springboot.service.posts;

import com.endqh.board.springboot.domain.posts.Posts;
import com.endqh.board.springboot.domain.posts.PostsRepository;
import com.endqh.board.springboot.web.dto.PostsListResponseDto;
import com.endqh.board.springboot.web.dto.PostsResponseDto;
import com.endqh.board.springboot.web.dto.PostsSaveRequestDto;
import com.endqh.board.springboot.web.dto.PostsUpdateRequestDto;
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
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDTO) {
        return postsRepository.save(requestDTO.toEntity())
                .getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDTO) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + id));
        posts.update(requestDTO.getTitle(), requestDTO.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream().map(PostsListResponseDto::new).collect(Collectors.toList());
    }

    // 페이지로 가져오기
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllByOrderByIdDesc(Integer pageNum, Integer postsPerPage) {
        Page<Posts> page = postsRepository.findAll(
                // PageRequest의 page는 0부터 시작
                PageRequest.of(pageNum - 1, postsPerPage,
                        Sort.by(Sort.Direction.DESC, "id")
                ));
        return page.stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    public Long count() {
        return postsRepository.count();
    }

    @Transactional
    public void delete(Long id) {
        Posts post = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + id));

        postsRepository.delete(post);

    }




}
