package com.korit.korit_gov_servlet_study.ch08.user.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.korit.korit_gov_servlet_study.ch08.user.dto.ApiRespDto;
import com.korit.korit_gov_servlet_study.ch08.user.dto.SignupReqDto;
import com.korit.korit_gov_servlet_study.ch08.user.entity.User;
import com.korit.korit_gov_servlet_study.ch08.user.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/ch08/user")
public class UserServlet extends HttpServlet {
    private UserService userService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        userService = UserService.getInstance();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 파라미터가 없으면 전체조회
        // username 파라미터가 있으면 username조회
        // 키워드가 있으면 키워드 조회
        String username = req.getParameter("username");
        String keyword = req.getParameter("keyword");
        ApiRespDto<?> apiRespDto = null;
        if (username != null) {
            // username 조회
            Optional<User> foundUser = userService.findByUsername(username);
            if (foundUser.isPresent()) {
                apiRespDto = ApiRespDto.<User>builder()
                        .status("success")
                        .massage(username + ": 회원 조회 완료")
                        .body(foundUser.get())
                        .build();
            } else {
                apiRespDto = ApiRespDto.builder()
                        .status("failed")
                        .massage(": 해당하는 회원이 존재하지 않습니다.")
                        .body(null)
                        .build();
            }
        } else if (keyword != null) {
            // keyword 조회
            Optional<List<User>> foundUserList = userService.findByKeyword(keyword);
            if (foundUserList.isPresent()) {
                apiRespDto = ApiRespDto.<List<User>>builder()
                        .status("success")
                        .massage(keyword + ": 조회 완료")
                        .body(foundUserList.get())
                        .build();
            } else {
                apiRespDto = ApiRespDto.<List<User>>builder()
                        .status("failed")
                        .massage(keyword + ": 조회된 결과가 없습니다.")
                        .body(null)
                        .build();
            }
        } else {
            // 전체 조회
            Optional<List<User>> foundUserList = userService.getUserAll();
            if (foundUserList.isPresent()) {
                apiRespDto = ApiRespDto.<List<User>>builder()
                        .status("success")
                        .massage("전체 조회 성공")
                        .body(foundUserList.get())
                        .build();
            } else {
                apiRespDto = ApiRespDto.builder()
                        .status("failed")
                        .massage("조회된 결과가 없습니다.")
                        .body(null)
                        .build();
            }
        }
        objectMapper.writeValue(resp.getWriter(), apiRespDto);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // body json을 가져오면 dto로 변환해서 username 중복검사한 후 서비스에 전달 후 db에 추가
        // 1. body json 가져오기 => dto 변환(json에서 변환하려면 Gson필요)
        SignupReqDto signupReqDto = objectMapper.readValue(req.getReader(), SignupReqDto.class);
        // 2. dto의 username 중복 검사 => 서비스에서 중복인지 아닌지 판단 메소드
        ApiRespDto<?> apiRespDto = null;
        if (userService.isDuplicated(signupReqDto.getUsername())) {
            apiRespDto = ApiRespDto.<String>builder()
                    .status("failed")
                    .massage(signupReqDto.getUsername() + "은 이미 사용중인 username 입니다.")
                    .body(signupReqDto.getUsername())
                    .build();
        } else {
            // 3. 추가하기
            User user = userService.addUser(signupReqDto);
            apiRespDto = ApiRespDto.<User>builder()
                    .status("success")
                    .massage("회원가입 완료")
                    .body(user)
                    .build();
        }

        // 4. 응답보내기
        objectMapper.writeValue(resp.getWriter(), apiRespDto);
    }

}
