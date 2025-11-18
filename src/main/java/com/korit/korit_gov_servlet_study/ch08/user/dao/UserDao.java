package com.korit.korit_gov_servlet_study.ch08.user.dao;

import com.korit.korit_gov_servlet_study.ch08.user.entity.User;
import com.korit.korit_gov_servlet_study.ch08.user.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {
    private static UserDao instance;

    private UserDao() {}

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    // username으로 유저 찾기
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT user_id, username, password, age, create_dt FROM user_tb WHERE username = ?";

        try (
                Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);

        ){
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()){
                return rs.next() ? Optional.of(toUser(rs)) : Optional.empty();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // user 추가
    public User addUser(User user) {
        String sql = "INSERT INTO user_tb(user_id, username, password, age, create_dt) VALUES (0, ?, ?, ?, now())";

        try (
                Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ){
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getAge());


            if (ps.executeUpdate() == 1) { // execute() => sql 실행, boolean으로 반환
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        Integer userId = rs.getInt("user_id");
                        user.setUserId(userId);
                    }
                }
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<List<User>> findByKeyword(String keyword) {
        String sql = "SELECT * FROM user_tb WHERE username LIKE ?";
        List<User> userList = new ArrayList<>();
        try (
                Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    userList.add(toUser(rs));
                }
            }
            return Optional.of(userList);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<List<User>> getUserAll() {
        String sql = "SELECT * FROM user_tb";
        List<User> userList = new ArrayList<>();
        try (
                Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    userList.add(toUser(rs));
                }
            }
            return Optional.of(userList);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    public User toUser(ResultSet resultSet) throws SQLException{
        return User.builder()
                .userId(resultSet.getInt("user_id"))
                .username(resultSet.getString("username"))
                .password(resultSet.getString("password"))
                .age(resultSet.getInt("age"))
                .createDt(resultSet.getTimestamp("create_dt").toLocalDateTime())
                .build();
    }



}
