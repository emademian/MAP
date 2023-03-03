package com.example.labjavafx.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    private Long idUser1;
    private Long idUser2;
    private LocalDateTime friendsFrom;

    public Friendship(Long idFr, Long idUser1, Long idUser2, LocalDateTime friendsFrom) {
        super.setId(idFr);
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.friendsFrom = friendsFrom;
    }


    public Long getIdUser1() {
        return idUser1;
    }

    public Long getIdUser2() {
        return idUser2;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public void setIdUser1(Long idUser1) {
        this.idUser1 = idUser1;
    }

    public void setIdUser2(Long idUser2) {
        this.idUser2 = idUser2;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    @Override
    public String toString() {
        return super.getId() + ". " + this.idUser1 + " and " +
                this.idUser2 + " are friends since " + this.friendsFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(idUser1, that.idUser1) && Objects.equals(idUser2, that.idUser2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser1, idUser2);
    }
}
