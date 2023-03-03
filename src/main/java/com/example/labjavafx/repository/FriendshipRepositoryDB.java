package com.example.labjavafx.repository;

import com.example.labjavafx.model.Friendship;
import com.example.labjavafx.model.validator.FriendshipValidator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FriendshipRepositoryDB implements Repository<Friendship, Long> {
    private final JDBCUtils jdbcUtils = new JDBCUtils();
    private final FriendshipValidator validator;

    public FriendshipRepositoryDB(FriendshipValidator validator) {
        this.validator = validator;
    }

    @Override
    public Friendship save(Friendship entity) {
        String query = "INSERT INTO friendships(id_fr, id_u1, id_u2, fr_from) VALUES(?, ?, ?,?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, entity.getId());
            statement.setLong(2, entity.getIdUser1());
            statement.setLong(3, entity.getIdUser2());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getFriendsFrom()));
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        validator.validate(entity);
        return entity;
    }

    @Override
    public Friendship delete(Long aLong) {
        String query = "DELETE FROM friendships WHERE id_fr = ?";

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
    public Friendship findOne(Long aLong) {
        String query = "SELECT * FROM friendships WHERE id_fr=?";
        Friendship friendship = null;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long idUser1 = resultSet.getLong("id_u1");
                Long idUser2 = resultSet.getLong("id_u2");
                LocalDateTime frFrom = resultSet.getTimestamp("fr_from").toLocalDateTime();
                friendship = new Friendship(aLong, idUser1, idUser2, frFrom);
                friendship.setId(aLong);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendship;
    }

    @Override
    public Friendship update(Friendship entity) {
        String query = "UPDATE friendships SET id_u1=?, id_u2=? WHERE id_fr = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, entity.getIdUser1());
            statement.setLong(2, entity.getIdUser2());
            statement.setLong(3, entity.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        validator.validate(entity);
        return entity;
    }

    @Override
    public Iterable<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();

        String query = "SELECT * from friendships";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Long idFr = resultSet.getLong("id_fr");
                Long idUser1 = resultSet.getLong("id_u1");
                Long idUser2 = resultSet.getLong("id_u2");
                LocalDateTime frFrom = resultSet.getTimestamp("fr_from").toLocalDateTime();

                Friendship friendship = new Friendship(idFr, idUser1, idUser2, frFrom);
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return friendships;
    }
}
