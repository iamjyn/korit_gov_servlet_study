package com.korit.korit_gov_servlet_study.ch03;

import java.util.ArrayList;
import java.util.List;

public class TodoRepository {
    private static TodoRepository instance;
    private List<Todo> todos;
    private Integer todoId = 1;

    private TodoRepository() {
        List<Todo> todos = new ArrayList<>();
    }

    public static TodoRepository getInstance() {
        if (instance == null) {
            instance = new TodoRepository();
        }
        return instance;
    }

    // 투두 추가
    public Todo addTodo(Todo todo) {
        todo.setTodoId(todoId++);
        todos.add(todo);
        return todo;
    }

    // 투두 중복확인
    public Todo findTodoByTitle(String title) {
        return todos.stream().filter(f -> f.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    // 투두 전체 조회
    public List<Todo> findAll() {
        return todos;
    }
}
