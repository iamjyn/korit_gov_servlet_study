package com.korit.korit_gov_servlet_study.ch03;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TodoServlet extends HttpServlet {
    private TodoRepository todoRepository;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        todoRepository = TodoRepository.getInstance();
        gson = new GsonBuilder().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 투두 전체 조회
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        List<Todo> todos = todoRepository.findAll();

        SuccessResponse <List<Todo>> successResponse = SuccessResponse.<List<Todo>>builder()
                .status(200)
                .message("조회 완료")
                .body(todoRepository.findAll())
                .build();
        String json = gson.toJson(successResponse);
        resp.setContentType("application/json");
        resp.setStatus(200);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        TodoDto todoDto = gson.fromJson(req.getReader(), TodoDto.class); // json -> gson-todoDto.class로 변환
        Todo foundTodo = todoRepository.findTodoByTitle(todoDto.getTitle());

        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json");

        if (foundTodo != null) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(400)
                    .message("제목이 중복되었습니다.")
                    .build();
            resp.setStatus(400);
            String json = gson.toJson(errorResponse);
            resp.getWriter().write(json);
            return;
        }
        Todo todo = todoRepository.addTodo(todoDto.toEntity());

        SuccessResponse<Todo> successResponse = SuccessResponse.<Todo>builder()
                .status(200)
                .message("Todo 등록 완료")
                .body(todo)
                .build();
        String json = gson.toJson(successResponse);
        resp.setStatus(200);
        resp.getWriter().write(json);
    }
}
