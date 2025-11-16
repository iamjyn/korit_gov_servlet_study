package com.korit.korit_gov_servlet_study.ch07;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        String username = req.getParameter("username");
        if (userService.isDuplicatedUsername(username)) {
            ResponseDto responseDto = ResponseDto.builder()
                    .status(200)
                    .message("username 조회 완료")
                    .body(userService.getUsername(username))
                    .build();
            resp.setStatus(200);
            String json = gson.toJson(responseDto);
            resp.getWriter().write(json);
            return;
        }

        // 전체 조회
        ResponseDto responseDto = ResponseDto.builder()
                .status(200)
                .message("전체 조회 완료")
                .body(userService.getAllUsers())
                .build();
        resp.setStatus(200);
        String json = gson.toJson(responseDto);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SignupReqDto signupReqDto = gson.fromJson(req.getReader(), SignupReqDto.class);

        // 중복확인
        if (userService.isDuplicatedUsername(signupReqDto.getUsername())) {
            ResponseDto<User> responseDto = ResponseDto.<User>builder()
                    .status(400)
                    .message("username이 중복되었습니다.")
                    .body(null)
                    .build();
            resp.setStatus(400);
            String json = gson.toJson(responseDto);
            resp.getWriter().write(json);
        }

        // 추가
        User user = userService.addUser(signupReqDto.toEntity());
        ResponseDto<User> responseDto = ResponseDto.<User>builder()
                .status(200)
                .message("회원가입 완료")
                .body(user)
                .build();
        resp.setStatus(200);
        String json = gson.toJson(responseDto);
        resp.getWriter().write(json);
    }
}
