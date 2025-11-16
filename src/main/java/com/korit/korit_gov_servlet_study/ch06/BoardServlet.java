package com.korit.korit_gov_servlet_study.ch06;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ch06/boards")
public class BoardServlet extends HttpServlet {
    private BoardRepository boardRepository;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        boardRepository = BoardRepository.getInstance();
        gson = new GsonBuilder().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Board> boards = boardRepository.getAllTitle();
        String json = gson.toJson(boards);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Board gsonBoard = gson.fromJson(req.getReader(), Board.class);

        Board foundTitle = boardRepository.findByTitle(gsonBoard.getTitle());

        if (foundTitle != null) {
            resp.getWriter().write("이미 등록된 title 입니다.");
            return;
        }

        Board board = boardRepository.addTitle(gsonBoard);
        Response response = Response.builder()
                .message("게시글 작성 완료")
                .build();
        String json = gson.toJson(response);
        resp.getWriter().write(json);
    }
}
