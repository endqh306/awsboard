package com.endqh.board.springboot.web;

import com.endqh.board.springboot.config.auth.LoginUser;
import com.endqh.board.springboot.config.auth.dto.SessionUser;
import com.endqh.board.springboot.service.posts.LogService;
import com.endqh.board.springboot.service.posts.NoticeService;
import com.endqh.board.springboot.service.posts.PostsService;
import com.endqh.board.springboot.util.Paginator;
import com.endqh.board.springboot.web.dto.PostsResponseDto;
import com.endqh.board.springboot.web.dto.log.LogSaveRequestDto;
import com.endqh.board.springboot.web.dto.notice.NoticeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final NoticeService noticeService;
    private final LogService logService;

    private static final Integer POSTS_PER_PAGE = 10;
    private static final Integer PAGES_PER_BLOCK = 5;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user,
                        @RequestParam(value = "page", defaultValue = "1") Integer page) {


        // 글 목록 전송
        model.addAttribute("boardTitle", "자유게시판");
        model.addAttribute("requestFrom", "posts");

        // 페이지네이션
        try {
            Paginator paginator = new Paginator(PAGES_PER_BLOCK, POSTS_PER_PAGE, postsService.count());
            Map<String, Object> pageInfo = paginator.getFixedBlock(page);

            model.addAttribute("pageInfo", pageInfo);
        } catch(IllegalStateException e) {
            model.addAttribute("pageInfo", null);
            System.err.println(e);
        }

        model.addAttribute("posts", postsService.findAllByOrderByIdDesc(page, POSTS_PER_PAGE));

        // 사용자 정보: 위의 @LoginUser 어노테이션으로 대체
        // SessionUser user = (SessionUser) httpSession.getAttribute("user");


        if(user != null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userImg", user.getPicture());
            model.addAttribute("userEmail", user.getEmail());
            model.addAttribute("isAllowWrite", true);
        }

        return "index";
    }

    @GetMapping("/posts/save")
    public String postSave(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userImg", user.getPicture());
            model.addAttribute("userEmail", user.getEmail());
            model.addAttribute("userId", user.getId());
            model.addAttribute("requestFrom", "posts");

            return "posts-save";
        } else {
            return "redirect:/";
        }

    }

    @GetMapping("/posts/update/{id}")
    public String postUpdate(@PathVariable Long id, Model model, @LoginUser SessionUser loginUser) {

        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        model.addAttribute("requestFrom", "posts");

        if(!dto.getAuthorId().equals(loginUser.getId())) {
            return "redirect:/";
        }

        return "posts-update";
    }

    @GetMapping("/posts/view/{id}")
    public String postView(@PathVariable Long id,
                           Integer page,
                           Model model,
                           @LoginUser SessionUser loginUser,
                           HttpServletRequest request) {

        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("requestFrom", "posts");
        model.addAttribute("page", page);

        Long userId = -99l;
        String remoteIp = request.getRemoteAddr();
        if(loginUser != null) {
            System.out.println(">> DTO: " + dto.getAuthorId());
            System.out.println(">> LoginUser: " + loginUser.getId());

            userId = loginUser.getId();
        }

        System.out.println(">> RemoteIP: " + remoteIp);
        System.out.println(">> currentPageNum: " + page);

        return "posts-view";
    }

    @GetMapping("/notice")
    public String noticeIndex(@RequestParam(value = "page", defaultValue = "1") Integer page, Model model, @LoginUser SessionUser user) {
        // 글 목록 전송
        model.addAttribute("boardTitle", "공지사항");
        model.addAttribute("requestFrom", "notice");
        // 사용자 정보: 위의 @LoginUser 어노테이션으로 대체
        // SessionUser user = (SessionUser) httpSession.getAttribute("user");

        // 페이지네이션
        try {
            Paginator paginator = new Paginator(PAGES_PER_BLOCK, POSTS_PER_PAGE, noticeService.count());
            Map<String, Object> pageInfo = paginator.getFixedBlock(page);

            model.addAttribute("pageInfo", pageInfo);
        } catch(IllegalStateException e) {
            model.addAttribute("pageInfo", null);
            System.err.println(e);
        }

        model.addAttribute("posts", noticeService.findAllByOrderByIdDesc(page, POSTS_PER_PAGE));

        if(user != null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userImg", user.getPicture());
            model.addAttribute("userEmail", user.getEmail());
            model.addAttribute("isAllowWrite", false);

            String userRole = user.getRole();
            System.out.println(">>>>>>>>>>>>" + userRole);

            if(user.getRole().equalsIgnoreCase("ADMIN")) {
                model.addAttribute("isAllowWrite", true);
            }
        }

        return "index";
    }

    @GetMapping("/notice/save")
    public String noticeSave(Model model, @LoginUser SessionUser user) {
        if(user != null && user.getRole().equalsIgnoreCase("ADMIN")) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userImg", user.getPicture());
            model.addAttribute("userEmail", user.getEmail());
            model.addAttribute("userId", user.getId());
            model.addAttribute("requestFrom", "notice");

            return "posts-save";
        } else {
            return "redirect:/";
        }

    }

    @GetMapping("/notice/update/{id}")
    public String noticeUpdate(@PathVariable Long id, Model model, @LoginUser SessionUser loginUser) {

        NoticeResponseDto dto = noticeService.findById(id);
        model.addAttribute("post", dto);
        model.addAttribute("requestFrom", "notice");

        if(!dto.getAuthorId().equals(loginUser.getId())) {
            return "redirect:/";
        }

        return "posts-update";
    }

    @GetMapping("/notice/view/{id}")
    public String noticeView(Integer page, @PathVariable Long id, Model model, @LoginUser SessionUser loginUser, HttpServletRequest request) {

        NoticeResponseDto dto = noticeService.findById(id);
        dto.setViewCount(logService.getViewCountByBoardNameAndArticleId("notice", dto.getId()));
        model.addAttribute("post", dto);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("requestFrom", "notice");
        model.addAttribute("page", page);

        Long userId = -99l;
        String remoteIp = request.getRemoteAddr();
        if(loginUser != null) {
            System.out.println(">> DTO: " + dto.getAuthorId());
            System.out.println(">> LoginUser: " + loginUser.getId());

            userId = loginUser.getId();
        }

        logService.save(LogSaveRequestDto.builder().articleId(id).boardName("notice").userId(userId).ipAddress(remoteIp).build());

        return "posts-view";
    }

}
