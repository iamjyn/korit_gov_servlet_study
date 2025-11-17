package com.korit.korit_gov_servlet_study.ch07;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ch07/users")
public class UserServlet extends HttpServlet {
    private UserService userService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        userService = UserService.getInstance();
        gson = new GsonBuilder().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String username = req.getParameter("username");
        if (username != null) {
            User user = userService.findByUsername(username);
            String json;
            ResponseDto<User> responseDto;
            if (user != null) { // 해당 username이 있으면 단건 조회
                responseDto = ResponseDto.<User>builder()
                        .status(200)
                        .message(username + " 조회 완료")
                        .body(user)
                        .build();
            } else {
                responseDto = ResponseDto.<User>builder()
                        .status(200)
                        .message(username + " 해당 username의 회원이 없습니다.")
                        .body(null)
                        .build();
            }
            json = gson.toJson(responseDto);
            resp.getWriter().write(json);
            return;
        }

        // 전체 조회
        List<User> users = userService.getUserListAll();
        ResponseDto<List<User>> responseDto = ResponseDto.<List<User>>builder()
                .status(200)
                .message("전체 조회 완료")
                .body(users)
                .build();
        String json = gson.toJson(responseDto);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SignupReqDto signupReqDto = gson.fromJson(req.getReader(), SignupReqDto.class);
        resp.setContentType("application/json");

        // 중복확인
        if (userService.isDuplicatedUsername(signupReqDto.getUsername())) {
            ResponseDto<String> responseDto = ResponseDto.<String>builder()
                    .status(200) // 요청 자체는 정상적으로 이루어짐
                    .message("username이 중복되었습니다.")
                    .body(signupReqDto.getUsername()) // <String> => 중복된 username 반환
                    .build();
//        resp.setStatus(200); // 생략가능
            String json = gson.toJson(responseDto);
            resp.getWriter().write(json);
            return;
        }

        // 추가
        User user = userService.addUser(signupReqDto);
        ResponseDto<User> responseDto = ResponseDto.<User>builder()
                .status(200)
                .message("회원가입 완료")
                .body(user)
                .build();
        String json = gson.toJson(responseDto);
        resp.getWriter().write(json);
    }
}
