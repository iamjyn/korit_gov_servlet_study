package com.korit.korit_gov_servlet_study.ch06;

import java.util.ArrayList;
import java.util.List;

public class BoardRepository {
    private static BoardRepository instance;
    private List<Board> boards;

    private BoardRepository() {
        boards = new ArrayList<>();
    }

    public static BoardRepository getInstance() {
        if (instance == null) {
            instance = new BoardRepository();
        }
        return instance;
    }

    public Board findByTitle(String title) {
        return boards.stream().filter(t -> t.equals(title))
                .findFirst()
                .orElse(null);
    }

    public Board addTitle(Board title) {
        boards.add(title);
        return title;
    }

    public List<Board> getAllTitle() {
        return boards;
    }
}
