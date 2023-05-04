package com.endqh.board.springboot.domain.posts;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    // Entity로 만든 테이블은 대소문자를 구분해야 함 (Posts)
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();

    List<Posts> findAllByOrderByIdDesc(Pageable pageable);

}
