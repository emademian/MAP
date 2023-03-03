package com.example.labjavafx.repository;

import com.example.labjavafx.model.User;
import com.example.labjavafx.model.validator.UserValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryDB implements Repository<User, Long> {
    private final JDBCUtils jdbcUtils = new JDBCUtils();
    private final UserValidator validator;

    public UserRepositoryDB(UserValidator validator) {
        this.validator = validator;
    }

    @Override
    public User save(User entity) {
        String query = "INSERT INTO users(id, first_name, last_name,age,email,username,password) VALUES(?, ?, ?,?,?,?,?)";
        validator.validate(entity);
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setInt(4, entity.getAge());
            statement.setString(5, entity.getEmail());
            statement.setString(6, entity.getUsername());
            statement.setString(7, entity.getPassword());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public User delete(Long aLong) {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, aLong);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return findOne(aLong);
    }

    @Override
    public User findOne(Long aLong) {
        String query = "SELECT * FROM users WHERE id=?";
        User user = null;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)

        ) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                int age = resultSet.getInt("age");
                String email = resultSet.getString("email");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                user = new User(firstName, lastName, age, email, username, password, aLong);
                user.setId(aLong);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User update(User entity) {
        String query = "UPDATE users SET password=? WHERE username = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, entity.getPassword());
            statement.setString(2, entity.getUsername());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        validator.validate(entity);
        return entity;
    }

    @Override
    public Iterable<User> findAll() {
        List<User> users = new ArrayList<>();

        String query = "SELECT * from users";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                int age = resultSet.getInt("age");
                String email = resultSet.getString("email");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                User user = new User(firstName, lastName, age, email, username, password, id);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}
