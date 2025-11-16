package com.korit.korit_gov_servlet_study.practice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserServlet2 extends HttpServlet {
    private UserRepository2 userRepository2;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        userRepository2 = UserRepository2.getInstance();
        gson = new GsonBuilder().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json");

        List<User2> users = userRepository2.findAll();
        SuccessResponse2<List<User2>> successResponse2 = SuccessResponse2.<List<User2>>builder()
                .status(200)
                .massage("전체 조회 완료")
                .body(users)
                .build();
        resp.setStatus(200);
        String json = gson.toJson(successResponse2);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());

        UserDto2 userDto2 = gson.fromJson(req.getReader(), UserDto2.class);
        User2 foundUser = userRepository2.findByUsername(userDto2.getUsername());

        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json");

        if (foundUser != null) {
            ErrorResponse2 errorResponse2 = ErrorResponse2.builder()
                    .status(400)
                    .massage("이미 사용중인 username 입니다.")
                    .build();
            resp.setStatus(400);
            String json = gson.toJson(errorResponse2);
            resp.getWriter().write(json);
            return;
        }

        User2 user2 = userRepository2.addUSer(userDto2.toEntity());

        SuccessResponse2<User2> successResponse2 = SuccessResponse2.<User2> builder()
                .status(200)
                .massage("사용자 등록 완료")
                .body(user2)
                .build();
        resp.setStatus(200);
        String json = gson.toJson(successResponse2);
        resp.getWriter().write(json);

    }
}
