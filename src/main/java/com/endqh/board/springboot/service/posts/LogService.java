package com.endqh.board.springboot.service.posts;

import com.endqh.board.springboot.domain.log.Log;
import com.endqh.board.springboot.domain.log.LogRepository;
import com.endqh.board.springboot.web.dto.log.LogSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class LogService {
    private final LogRepository logRepository;

    @Transactional
    public Long save(LogSaveRequestDto logSaveRequestDTO) {
        return logRepository.save(logSaveRequestDTO.toEntity()).getId();
    }

    // 업데이트 하지 않고 쌓기만 함

    @Transactional
    public Long getViewCountByBoardNameAndArticleId(String boardName, Long articleId) {
        Map<Long, List<Log>> filteredMap = logRepository.getListGroupByBoardNameAndArticleId(boardName.toLowerCase(), articleId).stream()
                .collect(Collectors.groupingBy(Log::getUserId, HashMap::new, toList()));
        Map<String, List<Log>> filteredMapOfGuest = logRepository.getListGroupByBoardNameAndArticleIdOfGuest(boardName.toLowerCase(), articleId).stream()
                .collect(Collectors.groupingBy(Log::getIpAddress, HashMap::new, toList()));

        // Log::getUserId
        // x -> x.getUserId()

        System.out.println("filteredMap: " + filteredMap);
        System.out.println("filteredMapOfGuest: " + filteredMapOfGuest);
        return Long.valueOf(filteredMap.size() + filteredMapOfGuest.size());

    }
}
