package com.example.labjavafx.repository;

import com.example.labjavafx.model.Message;
import com.example.labjavafx.model.validator.MessageValidator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageRepositoryDB implements Repository<Message, Long> {
    private final JDBCUtils jdbcUtils = new JDBCUtils();

    private final MessageValidator validator;

    public MessageRepositoryDB(MessageValidator validator) {
        this.validator = validator;
    }

    @Override
    public Message save(Message entity) {
        String query = "INSERT INTO messages(id_msg, user_from, user_to, text, msg_time, msg_status) VALUES(?,?,?,?,?,?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, entity.getId());
            statement.setLong(2, entity.getUserFrom());
            statement.setLong(3, entity.getUserTo());
            statement.setString(4, entity.getText());
            statement.setTimestamp(5, Timestamp.valueOf(entity.getMsgTime()));
            statement.setString(6, entity.getMsgStatus());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        validator.validate(entity);
        return entity;
    }

    @Override
    public Message delete(Long aLong) {
        String query = "DELETE FROM messages WHERE id_fr = ?";

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
    public Message findOne(Long aLong) {
        String query = "SELECT * FROM messages WHERE id_fr=?";
        Message message = null;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long userFrom = resultSet.getLong("user_from");
                Long userTo = resultSet.getLong("user_to");
                String text = resultSet.getString("text");
                LocalDateTime msgTime = resultSet.getTimestamp("msg_time").toLocalDateTime();
                String msgStatus = resultSet.getString("msg_status");
                message = new Message(aLong, userFrom, userTo, text, msgTime, msgStatus);
                message.setId(aLong);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public Message update(Message entity) {
        String query = "UPDATE messages SET msg_status=? WHERE id_msg = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1,entity.getMsgStatus());
            statement.setLong(2, entity.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        validator.validate(entity);
        return entity;
    }

    @Override
    public Iterable<Message> findAll() {
        List<Message> messages = new ArrayList<>();

        String query = "SELECT * from messages";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Long idMsg= resultSet.getLong("id_msg");
                Long userFrom = resultSet.getLong("user_from");
                Long userTo = resultSet.getLong("user_to");
                String text = resultSet.getString("text");
                LocalDateTime msgTime = resultSet.getTimestamp("msg_time").toLocalDateTime();
                String msgStatus = resultSet.getString("msg_status");
                Message message = new Message(idMsg,userFrom,userTo,text,msgTime,msgStatus);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }
}
